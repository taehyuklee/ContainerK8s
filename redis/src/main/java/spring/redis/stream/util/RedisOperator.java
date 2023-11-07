package spring.redis.stream.util;

import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.core.protocol.CommandKeyword;
import io.lettuce.core.protocol.CommandType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RedisOperator {
    private final RedisTemplate redisTemplate;

    //key - value (value -> Hash일때는 <field, value> 이렇게 된다)
    public Object getRedisValue(String key, String field){
        return this.redisTemplate.opsForHash().get(key, field);
    }

    public long increaseRedisValue(String key, String field){
        return this.redisTemplate.opsForHash().increment(key, field, 1);
    }

    //Redis-stream에서 잘 처리되었다는 말을 해주기 위해 ack를 해준다.
    public void ackStream(String consumerGroupName, MapRecord<String, Object, Object> message){
        this.redisTemplate.opsForStream().acknowledge(consumerGroupName, message);
    }

    //Pending된거에 대해서 Consumer는 다시 재 요청을 할수 있는데 이것을 claim이라 한다.
    public void claimStream(PendingMessage pendingMessage, String consumerName){
        RedisAsyncCommands commands = (RedisAsyncCommands) this.redisTemplate
                .getConnectionFactory().getConnection().getNativeConnection();

        CommandArgs<String, String> args = new CommandArgs<>(StringCodec.UTF8)
                .add(pendingMessage.getIdAsString())
                .add(pendingMessage.getGroupName())
                .add(consumerName)
                .add("20")
                .add(pendingMessage.getIdAsString());
        commands.dispatch(CommandType.XCLAIM, new StatusOutput(StringCodec.UTF8), args);
    }

    public MapRecord<String, Object, Object> findStreamMessageById(String streamKey, String id){
        List<MapRecord<String, Object, Object>> mapRecordList = this.findStreamMessageByRange(streamKey, id, id);
        if(mapRecordList.isEmpty()) return null;
        return mapRecordList.get(0);
    }

    public List<MapRecord<String, Object, Object>> findStreamMessageByRange(String streamKey, String startId, String endId){
        return this.redisTemplate.opsForStream().range(streamKey, Range.closed(startId, endId));
    }

    public void createStreamConsumerGroup(String streamKey, String consumerGroupName){
        // if stream is not exist, create stream and consumer group of it
        if (Boolean.FALSE.equals(this.redisTemplate.hasKey(streamKey))){
            RedisAsyncCommands commands = (RedisAsyncCommands) this.redisTemplate
                    .getConnectionFactory()
                    .getConnection()
                    .getNativeConnection();

            CommandArgs<String, String> args = new CommandArgs<>(StringCodec.UTF8)
                    .add(CommandKeyword.CREATE)
                    .add(streamKey)
                    .add(consumerGroupName)
                    .add("0")
                    .add("MKSTREAM");

            commands.dispatch(CommandType.XGROUP, new StatusOutput(StringCodec.UTF8), args);
        }
        // stream is exist, create consumerGroup if is not exist
        else{
            if(!isStreamConsumerGroupExist(streamKey, consumerGroupName)){
                this.redisTemplate.opsForStream().createGroup(streamKey, ReadOffset.from("0"), consumerGroupName);
            }
        }
    }

    public PendingMessages findStreamPendingMessages(String streamKey, String consumerGroupName, String consumerName){
        return this.redisTemplate.opsForStream()
                .pending(streamKey, Consumer.from(consumerGroupName, consumerName), Range.unbounded(), 100L);
    }

    public boolean isStreamConsumerGroupExist(String streamKey, String consumerGroupName){
        Iterator<StreamInfo.XInfoGroup> iterator = this.redisTemplate
                .opsForStream().groups(streamKey).stream().iterator();

        while(iterator.hasNext()){
            StreamInfo.XInfoGroup xInfoGroup = iterator.next();
            if(xInfoGroup.groupName().equals(consumerGroupName)){
                return true;
            }
        }
        return false;
    }

    ///Once you’ve implemented your StreamListener, it’s time to create a message listener container and register a subscription:
    public StreamMessageListenerContainer createStreamMessageListenerContainer(){
        return StreamMessageListenerContainer.create(
                this.redisTemplate.getConnectionFactory(),
                StreamMessageListenerContainer
                        .StreamMessageListenerContainerOptions.builder()
                        .hashKeySerializer(new StringRedisSerializer())
                        .hashValueSerializer(new StringRedisSerializer())
                        .pollTimeout(Duration.ofMillis(20))
                        .build()
        ); //https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#redis.streams.send 공식문서도 참고해보고
    }

}
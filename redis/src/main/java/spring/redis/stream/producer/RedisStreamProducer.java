package spring.redis.stream.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.connection.stream.StringRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class RedisStreamProducer {

    private final RedisTemplate redisTemplate;

    public void producing(){
        // append message through RedisTemplate
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("first", "taehyuk");

        StringRecord record = StreamRecords.string(hashMap).withStreamKey("my-stream");
        redisTemplate.opsForStream().add(record);
    }

}

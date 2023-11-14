package spring.redis.lock.lettuce;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisLockRepository {
    private final RedisTemplate redisTemplate;

    //Locking에 관련된 key는 redis에 저장하는 다른 key와 겹치지 않아야 한다. 예를 들어 CRUD로 redis에 key를 1로 저장한 데이터가 있다면, lock은
    //전제조건 DB, 다른 memory에 들어가 있는 index가 redis의 key가 될 가능성이 높다. 다만 조심할건 그 내용저장을 redis에 겹쳐서 하면 안된다는거.
    public Boolean lock(final Long key){
        return redisTemplate
                .opsForValue()
                .setIfAbsent(String.valueOf(key), "lock", Duration.ofMillis(10000L));
    }

    public Boolean unlock(final Long key){
        return redisTemplate.delete(String.valueOf(key));
    }

}

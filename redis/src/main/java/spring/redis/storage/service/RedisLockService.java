package spring.redis.storage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.redis.lock.lettuce.RedisLockRepository;
import spring.redis.storage.domain.entity.Person;

@Service
@RequiredArgsConstructor
public class RedisLockService {

    private final RedisLockRepository redisLockRepository;

    //DB 동시 작업 방지를 위해
    private final RedisCRUDService redisCRUDService;

    //Cache 동시 작업 방지를 위해
    private final RedisCacheService redisCacheService;

    public void lockCRUDSvc(String name, Long id) throws Exception {

        Person p1 = new Person(name, 23);
        p1.setId(1L); //id 1로 일단 세팅

        while(!redisLockRepository.lock(id)){
            System.out.println("I'm locked");
            Thread.sleep(300); //redis에 요청 보내는 횟수를 줄이고자 sleep으로 잠시 대기를 걸어준다. (임시방편)
        }

        try{
            //redisCRUDService.create(p1);
            System.out.println("저장");
            Thread.sleep(100000);
        }finally {
            redisLockRepository.unlock(id);
        }



    }



}

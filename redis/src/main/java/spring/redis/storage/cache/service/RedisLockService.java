package spring.redis.storage.cache.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.redis.lock.lettuce.RedisLockRepository;
import spring.redis.domain.entity.Person;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class RedisLockService {

    private final RedisLockRepository redisLockRepository;

    //DB 동시 작업 방지를 위해
    private final RedisCRUDService redisCRUDService;

    //Cache 동시 작업 방지를 위해
    private final RedisCacheService redisCacheService;

    public void lockCRUDSvc(String name, Long id) throws Exception {

        Person p1 = makePerson(name);

        while(!redisLockRepository.lock(id)){
            System.out.println("I'm locked");
            Thread.sleep(300); //redis에 요청 보내는 횟수를 줄이고자 sleep으로 잠시 대기를 걸어준다. (임시방편)
        }

        try{
            redisCRUDService.create(p1);
            System.out.println("저장");
            sleep(100000L);
        }finally {
            redisLockRepository.unlock(id);
        }

    }

    public void lockCacheSvc(String name, Long id) throws Exception {

        Person p1 = makePerson(name);

        while(!redisLockRepository.lock(id)){
            System.out.println("I'm locked");
            Thread.sleep(300); //redis에 요청 보내는 횟수를 줄이고자 sleep으로 잠시 대기를 걸어준다. (임시방편)
        }

        try{
            redisCacheService.save(p1);
            System.out.println("저장");
            sleep(100000L);
        }finally {
            redisLockRepository.unlock(id);
        }

    }

    public void lockFile(Long id) throws Exception {

        while(!redisLockRepository.lock(id)){
            System.out.println("I'm locked");
            Thread.sleep(300); //redis에 요청 보내는 횟수를 줄이고자 sleep으로 잠시 대기를 걸어준다. (임시방편)
        }

        try{
            System.out.println("무언가를 쓰는중");
            writeSomething();
            sleep(100000L);
        }finally {
            redisLockRepository.unlock(id);
        }

    }


    private Person makePerson(String name){
        Person p1 = new Person(name, 23);
        p1.setId(1L); //id 1로 일단 세팅
        return p1;
    }

    private void writeSomething() throws IOException {

        String filePath = "/Users/thlee/Desktop/writeFile.txt";
        String contentToAppend = "무언가를 추가하려는 내용\n";

        try {
            // 파일 열기 (파일이 없으면 새로 생성)
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));

            // 파일에 내용 추가
            writer.write(contentToAppend);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sleep(long sleep_time) throws InterruptedException {
        Thread.sleep(sleep_time);
    }



}

package spring.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.redis.domain.entity.Person;
import spring.redis.domain.repository.PersonRedisRepository;

@Service
@RequiredArgsConstructor

public class redisService {

    private final PersonRedisRepository personRedisRepository;


    //Create
    public void create(){
        Person p1 = new Person("lee", 33);
        Person p2 = new Person("no",30);
        Person p3 = new Person("lee", 34);

        personRedisRepository.save(p1);


    }


    //Read
    public void read(){
        Person p1 = new Person("lee", 33);
        Person p2 = new Person("no",30);
        Person p3 = new Person("lee", 34);

//        personRedisRepository.save(p1);


    }



    //Update

    //Delete


}

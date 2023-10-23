package spring.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.redis.domain.entity.Person;
import spring.redis.domain.repository.PersonRedisRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class RedisService {

    private final PersonRedisRepository personRedisRepository;


    //Create
    public void create(Person person) throws Exception {

        Optional<Person> personOpt = Optional.ofNullable(personRedisRepository.findPersonByName(person.getName()))
                .orElseThrow(() -> new Exception("[" + person.getName()  + "]  이미 등록된 사용자 입니다."));

        personRedisRepository.save(personOpt.get());
    }


    //Read
    public Person read(String personNm) throws Exception {

        Optional<Person> entity = Optional.ofNullable(personRedisRepository.findPersonByName(personNm))
                .orElseThrow(() -> new Exception(" 등록되지 않은 사용자 입니다."));

        Person returnPerson = null;
        if(entity.isPresent()){
            returnPerson = personRedisRepository.findPersonByName(personNm).get();
        }
        return returnPerson;
    }

    //Update
    public void update(Person person) throws Exception {

        Optional<Person> oldEntity = Optional.ofNullable(personRedisRepository.findPersonByName(person.getName()))
                .orElseThrow(() -> new Exception("사용자 정보가 없다."));

        if(oldEntity.isPresent()) {
            personRedisRepository.save(person);
        }
        return;
    }

    //Delete
    public void delete(String personNm) throws Exception {

        Optional<Person> entiy = Optional.ofNullable(personRedisRepository.findPersonByName(personNm))
                .orElseThrow(() -> new Exception("삭제할 대상이 존재하지 않습니다"));

        if(entiy.isPresent()) {
            personRedisRepository.delete(entiy.get());
        }
    }


}

package spring.redis.storage.cache.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.redis.domain.entity.Person;
import spring.redis.domain.repository.PersonRedisRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisCRUDService {

    private final PersonRedisRepository personRedisRepository;


    //Create
    public void create(Person person) throws Exception {

        Optional<Person> personOpt = personRedisRepository.findPersonByName(person.getName());

        if (personOpt.isPresent()) {
            throw new Exception("[" + person.getName() + "] 이미 등록된 사용자 입니다.");
        }

        personRedisRepository.save(person);
    }


    //Read
    public Person read(String personNm) throws Exception {

        Person result = personRedisRepository.findPersonByName(personNm)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));
        //Optional에서 orElseThrow를 하게 되면 해당 객체로 반환하게 된다.

        return result;
    }

    //Update
    public void update(Person person) throws Exception {

        Person result = personRedisRepository.findPersonByName(person.getName())
                .orElseThrow(() -> new RuntimeException("수정할 사용자가 존재하지 않습니다."));

        person.setId(result.getId());
        personRedisRepository.save(person);
    }

    //Delete
    public void delete(String personNm) throws Exception {

        Person result = personRedisRepository.findPersonByName(personNm)
                .orElseThrow(() -> new RuntimeException("삭제할 사용자가 존재하지 않습니다."));

        personRedisRepository.deleteById(result.getId());

    }

    public long count(){
        return personRedisRepository.count();
    }



}

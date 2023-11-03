package spring.redis.domain.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import spring.redis.domain.entity.Person;

import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
public class PersonMemoryRepository {

    private String name;
    private Long id = 1L;
    private final Map<String, Person> personsNameRepo = new HashMap<>();
    private final Map<Long, Person> personsIdRepo = new HashMap<>();

    public void save(Person person){

        //personIdRepo 업데이트
        person.setId(id);
        personsIdRepo.put(id, person);
        id++;

        //personNameRepo 업데이트
        personsNameRepo.put(person.getName(), person);
        log.info("name: {} 이 생성되었습니다.", person.getName());

    }


    public Person getByName(String name){
        log.info("DB에서 name: {}을 조회합니다", name);
        return personsNameRepo.get(name);
    }

    public Person getById(Long id){
        log.info("DB에서 id: {}을 조회합니다", id);
        return personsIdRepo.get(id);
    }


    public void deleteByName(String name){

        Person target_person = personsNameRepo.get(name);
        Long id = target_person.getId();

        //person Id부터 조회해서 지운다.
        personsIdRepo.remove(id);

        //person Name에서 조회해서 지운다.
        personsNameRepo.remove(name);
        log.info("name: {} 이 삭제되었습니다.", name);
    }

    public void updateByName(Person person){

        Person target_person = personsNameRepo.get(person.getName());
        Long id = person.getId();

        //person Id부터 조회해서 업데이트 한다.
        personsIdRepo.put(id, person);

        //person Name에서 조회해서 업데이트 한다.
        personsNameRepo.remove(target_person.getName(), person);

        log.info("name: {} 이 수정되었습니다.", person.getName());

    }

    public void checkAll(){
        System.out.println("personsIdRepo: " + personsIdRepo);
        System.out.println("personsNameRepo: " + personsNameRepo);
    }



}

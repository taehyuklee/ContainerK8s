package spring.redis.domain.repository;

import org.springframework.data.repository.CrudRepository;
import spring.redis.domain.entity.Person;

import java.util.Optional;

public interface PersonRedisRepository extends CrudRepository<Person, Long> {
    Optional<Person> findPersonByName(String name);

}
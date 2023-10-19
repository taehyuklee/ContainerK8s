package spring.redis.domain.repository;

import org.springframework.data.repository.CrudRepository;
import spring.redis.domain.entity.Person;

public interface PersonRedisRepository extends CrudRepository<Person, String> {
}

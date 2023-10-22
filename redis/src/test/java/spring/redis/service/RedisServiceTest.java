package spring.redis.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.redis.domain.entity.Person;
import spring.redis.domain.repository.PersonRedisRepository;

import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.only;

@ExtendWith(MockitoExtension.class)
class RedisServiceTest {

    @InjectMocks
    RedisService testTarget;

    @Mock
    PersonRedisRepository personRedisRepository;

    @Test
    void create() throws Exception {

        /************************  Given ************************/
        Person person = new Person("lee", 33);
        Optional<Person> optPerson = Optional.of(person);

        /************************  When ************************/
        Mockito.when(personRedisRepository.findPersonByName(person.getName())).thenReturn(optPerson);

        /************************  Then ************************/
        testTarget.create(person);
        Mockito.verify(personRedisRepository, Mockito.times(1)).save(person); //앞에 create하고 뒤에 verify해야 함.
    }

    @Test
    void read() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}
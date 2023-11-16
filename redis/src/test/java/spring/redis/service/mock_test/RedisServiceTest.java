package spring.redis.service.mock_test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.redis.domain.entity.Person;
import spring.redis.domain.repository.PersonRedisRepository;
import spring.redis.storage.cache.service.RedisCRUDService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RedisServiceTest {

    @InjectMocks
    RedisCRUDService testTarget;

    @Mock
    PersonRedisRepository personRedisRepository;

    @Test
    @Order(1)
    @DisplayName("Redis 생성")
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
    @Order(2)
    @DisplayName("Redis 조회")
    void read() throws Exception {

        /************************  Given ************************/
        String personNm = "lee";
        Person person = new Person("lee", 33);
        Optional<Person> optPerson = Optional.of(person);

        /************************  When ************************/
        Mockito.when(personRedisRepository.findPersonByName(personNm)).thenReturn(optPerson);

        /************************  Then ************************/
        assertEquals(person.getName(), testTarget.read(personNm).getName());

    }

    @Test
    @Order(3)
    @DisplayName("Redis 수정")
    void update() throws Exception {

        /************************  Given ************************/
        Person person = new Person("lee", 23);
        Optional<Person> optPerson = Optional.of(person);

        /************************  When ************************/
        Mockito.when(personRedisRepository.findPersonByName(person.getName())).thenReturn(optPerson);

        /************************  Then ************************/
        testTarget.create(person);
        Mockito.verify(personRedisRepository, Mockito.times(1)).save(person);
    }

    @Test
    @Order(4)
    @DisplayName("Redis 삭제")
    void delete() throws Exception {

        /************************  Given ************************/
        String personNm = "lee";
        Person person = new Person("lee", 23);
        Optional<Person> optPerson = Optional.of(person);

        /************************  When ************************/
        Mockito.when(personRedisRepository.findPersonByName(personNm)).thenReturn(optPerson);

        /************************  Then ************************/
        testTarget.delete(personNm);
        Mockito.verify(personRedisRepository, Mockito.times(1)).delete(person);

    }


}
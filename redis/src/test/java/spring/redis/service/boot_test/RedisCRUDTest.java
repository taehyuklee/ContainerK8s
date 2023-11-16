package spring.redis.service.boot_test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.redis.domain.entity.Person;
import spring.redis.storage.cache.service.RedisCRUDService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RedisCRUDTest {

    @Autowired
    private RedisCRUDService redisService;

    @Test
    @Order(1)
    @DisplayName("Redis 생성")
    void create() throws Exception {
        /**** Given ****/
        Person p1 = new Person("lee", 33);

        /**** When ****/
        redisService.create(p1);

        /**** Then ****/

    }

    @Test
    @Order(2)
    @DisplayName("Redis 조회")
    void read() throws Exception {

        /**** Given ****/
        String name = "lee";

        /**** When ****/
        Person result = redisService.read(name);

        /**** Then ****/
        assertEquals(result.getName(), "lee");
    }


    @Test
    @Order(3)
    @DisplayName("Redis 수정")
    void update() throws Exception {
        /**** Given ****/
        Person p2 = new Person("lee", 23);

        /**** When ****/
        redisService.update(p2);

        /**** Then ****/
        Person result = redisService.read("lee");
        assertEquals(result.getAge(), 23);

    }

    @Test
    @Order(4)
    @DisplayName("Redis 삭제")
    void delete() throws Exception {

        /**** Given ****/
        String name = "lee";

        /**** When ****/
        redisService.delete("lee");

        /**** Then ****/

    }


}

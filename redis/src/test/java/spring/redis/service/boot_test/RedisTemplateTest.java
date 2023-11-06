package spring.redis.service.boot_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import spring.redis.domain.entity.Person;
import spring.redis.service.RedisCRUDService;

import java.util.Arrays;
import java.util.List;

/***
 * https://sabarada.tistory.com/105 (reference blog)
 */

@SpringBootTest
public class RedisTemplateTest {

    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    public void testStrings(){
        String key = "string_key";

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        valueOperations.set(key, "1"); //redis set 명령어
        String result_1 = valueOperations.get(key); //redis get 명령어
        System.out.println(result_1);

        /*
        * 127.0.0.1:6379> keys *
            1) "first"
        * */

        valueOperations.increment(key); //value가 숫자면 -> 증가시켜준다
        String result_2 = valueOperations.get(key);
        System.out.println(result_2);

    }

    @Test
    public void testList(){
        String key = "list_key";

        ListOperations<String, String> listOperations = redisTemplate.opsForList();

        listOperations.rightPush(key,"H");
        listOperations.rightPush(key,"e");
        listOperations.rightPush(key,"l");
        listOperations.rightPush(key,"l");
        listOperations.rightPush(key,"o");

        listOperations.leftPush(key, " ");
        listOperations.leftPush(key, "i");
        listOperations.leftPush(key, "H");

        listOperations.rightPushAll(key," ", "G", "o", "o", "d", "!");
        //Hi Hello GOOD! 출력 예상

        //Indexing
        String character_1 = listOperations.index(key,1); //두번째 글자가 나와야한다 -> i가 나올 것이다.
        System.out.println("expected: i" + " actual: " + character_1);


        //getSize
        Long size = listOperations.size(key);
        System.out.println("size: " + size); //총 사이즈 14가 된다.

        //resultRange
        List<String> resultString = listOperations.range(key,0,15);

        System.out.println("ResultRange = " + Arrays.toString(resultString.toArray()));

        System.out.println(resultString);

    }

    //Integer같은건 직접 넣지 못한다 (String화 해서 넣어줘야 한다)
    public void testIntegerList(){
        String key = "list_key";

        ListOperations<String, Integer> listOperations = redisTemplate.opsForList();

        listOperations.rightPush(key,1);
        listOperations.rightPush(key,2);
        listOperations.rightPush(key,3);
        listOperations.rightPush(key,4);
        listOperations.rightPush(key,5);

        //resultRange
        List<Integer> resultString = listOperations.range(key,0,6);

        System.out.println("ResultRange = " + Arrays.toString(resultString.toArray()));

        System.out.println(resultString);

    }

    @Test
    public void testObjectList(){
        String key = "list_key";

        Person p1 = new Person("lee", 33);
        Person p2 = new Person("song", 30);
        Person p3 = new Person("jin", 30);

        ListOperations<String, String> listOperations = redisTemplate.opsForList();


        listOperations.rightPush(key,p1.toString());
        listOperations.rightPush(key,p2.toString());
        listOperations.rightPush(key,p3.toString());


        //resultRange
        List<String> resultString = listOperations.range(key,0,6);

        System.out.println("ResultRange = " + Arrays.toString(resultString.toArray()));

        System.out.println(resultString);

        /* 확인 결과 잘 나오는 것을 확인할수 있다
        * 127.0.0.1:6379> LRANGE list_key 0 -1
        1) "Person(id=null, name=lee, age=33, cretDt=2023-11-06T17:14:04.864749, expiroation=1800)"
        2) "Person(id=null, name=song, age=30, cretDt=2023-11-06T17:14:04.864856, expiroation=1800)"
        3) "Person(id=null, name=jin, age=30, cretDt=2023-11-06T17:14:04.864861, expiroation=1800)"
        *
        *
        * */

    }

}

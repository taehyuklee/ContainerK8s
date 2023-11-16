package spring.redis.service.boot_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import spring.redis.domain.entity.Person;

import java.util.*;
import java.util.concurrent.TimeUnit;

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

    @Test
    public void testForSet(){
        String key = "set_key";

        SetOperations<String, String> setOperations = redisTemplate.opsForSet();

        setOperations.add(key, "G");
        setOperations.add(key, "i");
        setOperations.add(key, " ");
        setOperations.add(key, "H");
        setOperations.add(key, "e");
        setOperations.add(key, "l");
        setOperations.add(key, "l");
        setOperations.add(key, "o");

        //resultRange (https://docs.spring.io/spring-data/redis/docs/current/api/org/springframework/data/redis/core/SetOperations.html 공식 문서)
        Set<String> getAllMembers = setOperations.members(key);
        System.out.println("members = " + Arrays.toString(getAllMembers.toArray()));

        //getSize
        Long size = setOperations.size(key);
        System.out.println("size: " + size);

        //Cursor (pattern에 맞춰서 제한 수만큼 출력)
        Cursor<String> cursor = setOperations.scan(key, ScanOptions.scanOptions().match("*").count(3).build());
        /*
        * ScanOptions.scanOptions()를 호출하여 스캔 옵션을 설정합니다. 여기서 다음 옵션을 설정합니다.
        *   match("*"): 이 옵션은 스캔된 항목의 이름 패턴을 지정합니다. "*"는 모든 항목을 선택하는 와일드카드 패턴입니다. 즉, 모든 항목을 스캔하려고 합니다.
        *   count(3): 스캔 중에 반환할 항목 수를 제한합니다. 여기서는 3개의 항목을 반환하도록 설정되어 있습니다.
        * */

        //출력
        while(cursor.hasNext()){
            System.out.println("cursor= " + cursor.next());
        }

    }

    //SortedSet
    @Test
    public void testSortedSet(){
        //CLI 조회 명령어 - zrange myzset 0, -1 withscores (https://ryu-e.tistory.com/9)
        String key = "sorted_set_key";

        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();

        //score를 기준으로 오름차순으로 정렬되는듯하다.
        zSetOperations.add(key, "G", 1);
        zSetOperations.add(key, "i", 2);
        zSetOperations.add(key, " ", 3);
        zSetOperations.add(key, "H", 4);
        zSetOperations.add(key, "e", 5);
        zSetOperations.add(key, "l", 8);
        zSetOperations.add(key, "l", 9);
        zSetOperations.add(key, "o", 111);

        //print (from to end)
        Set<String> range = zSetOperations.range(key, 0,5);
        System.out.println("range= " + Arrays.toString(range.toArray()));

        //getSize
        Long size = zSetOperations.size(key);
        System.out.println("size: " + size);

        //scoreRange (score에 대해 제한을 걸어 출력이 가능하다)
        Set<String> scoreRange = zSetOperations.rangeByScore(key,0,5); //5이하인 score들만 출력하게 된다.
        System.out.println("scoreRange = " + Arrays.toString(scoreRange.toArray()));

    }

    //Hash
    @Test
    public void testHash(){
        //CLI - 검색 명령어 hgetall myHash, HGET myHash field1
        String key = "hash_key";

        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();

        hashOperations.put(key, "Hello", "hi");
        hashOperations.put(key, "Hello2", "hi2");
        hashOperations.put(key, "Hello3", "hi3");

        String hello = hashOperations.get(key, "Hello"); //맨 앞의 key는 redis의 key이며 그 뒤의 "Hello"는 Map의 key이다.
        System.out.println("hello =? : " + hello);

        /* Hash의 경우 HashOperations<String, Object, Object>형태로 쓰이기도 한다 -> 의문, 아니 Integer나, Person과 같은 구현체는 안되더니;;
        Object는 들어가지나? -> List에서도 Object가 잘 들어가지는지 확인해보기*/

        //Entries를 통해서 하나의 Map - key-value만 뽑아서 출력하기
        Map<String, String > entries = hashOperations.entries(key); //Java에서도 Hashmap Entries뽑잖아
        System.out.println("entires= " + entries.get("Hello2"));

        //getSize
        Long size = hashOperations.size(key);
        System.out.println("size= " + size);

    }

    /**
     * https://gogo-jjm.tistory.com/35 참고 deleteKey 부터 ~ rename까지
     * @throws InterruptedException
     */
    @Test
    public void deleteKey() throws InterruptedException {

        makeSample();

        //redis-cli에서 확인하는 시간
        Thread.sleep(3000);

        //delete method 수행
        redisTemplate.delete("list_key");
    }

    @Test
    public void isExists() {

        makeSample();

        //delete method 수행
        boolean exists = redisTemplate.hasKey("list_key");
        System.out.println(exists);
    }

    @Test
    public void setExpireTime() {

        makeSample();
        long expirationTIme = 10L;

        //delete method 수행
        redisTemplate.expire("list_key", expirationTIme, TimeUnit.SECONDS);

        while(redisTemplate.hasKey("list_key")){

        }
        System.out.println("list_key가 Expiration 되었습니다");
    }

    @Test
    public void findKeys(){
        makeSample();

        //모든 Pattern에 대해서 조회하기
        Set<String> keys = redisTemplate.keys("*");
        System.out.println(keys);
        //결과 [list_key]
    }

    @Test
    public void renameKey(){

        makeSample();

        redisTemplate.rename("list_key", "new_list_key");

        //바뀐 이름 조회해보기
        Set<String> keys = redisTemplate.keys("*");
        System.out.println(keys);

        //결과 [new_list_key]

        /**
         * Error in execution; nested exception is io.lettuce.core.RedisCommandExecutionException: ERR no such key
         * Key가 없다면 위와 같은 에러가 표출된다. 즉, Error처리를 잘 해놔야 한다.
         */
    }




    private void  makeSample(){
        String key = "list_key";

        Person p1 = new Person("lee", 33);
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        listOperations.rightPush(key,p1.toString());
    }

}

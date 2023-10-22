package spring.redis.domain.entity;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@Getter
@ToString
@RedisHash(value= "people", timeToLive = 30)
public class Person {

    @Id
    private String id;
    @Indexed
    private String name;
    private Integer age;
    private LocalDateTime cretDt;

    public Person(String name, Integer age){
        this.name = name;
        this.age = age;
        this.cretDt = LocalDateTime.now();
    }


}

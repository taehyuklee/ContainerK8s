package spring.redis.domain.entity;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@Getter
@ToString
@RedisHash(value= "people", timeToLive = 1800)
public class Person {

    public static final Long DEFUALT_TTL = 1800L;
    @Id
    private String id;
    @Indexed
    private String name;
    private Integer age;
    private LocalDateTime cretDt;

    @TimeToLive
    private Long expiroation = DEFUALT_TTL;

    public Person(String name, Integer age){
        this.name = name;
        this.age = age;
        this.cretDt = LocalDateTime.now();
        this.expiroation = DEFUALT_TTL;
    }

    public void setId(String newId){
        this.id = newId;
    }

}

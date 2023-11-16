package spring.redis.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@ToString
@RedisHash(value= "people", timeToLive = 1800)
@NoArgsConstructor
public class Person implements Serializable {

    public static final Long DEFUALT_TTL = 1800L;
    @Id
    private Long id;
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

    public void setId(Long newId){
        this.id = newId;
    }

    public void setCretDt(LocalDateTime localDateTime){
        this.cretDt = localDateTime;
    }
}

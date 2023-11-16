package spring.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;


    //Redis- 서버 연결 설정 (redisConnectionFactory의 역할)
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
        redisConfiguration.setHostName(host);
        redisConfiguration.setPort(port);
        redisConfiguration.setPassword(password);

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration);
        return lettuceConnectionFactory;
    }

    @Bean //이거 하지 않으면 룬문자가 생김.
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    /**
     *  RedisTemplate의 경우 redisTemplate을 사용할수도 있지만, StringRedisTemplate를 사용할수 있다.
     * 이 두 설정의 핵심 차이는 serialization 방법의 차이다.
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        /****** Set Serializable options (Key-value, hash, set) *******/
//        redisTemplate.setKeySerializer(new StringRedisSerializer());    //key serializer
//        redisTemplate.setValueSerializer(new StringRedisSerializer());  //value serializer
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(new StringRedisSerializer()); //이걸 사용하면, StringRedisTemplate을 사용하게 된다.

        return redisTemplate;
    }

    //@Bean //StringRedisTemplate을 이용하고자 한다면 Bean등록을 하도록 하자.
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();

        stringRedisTemplate.setConnectionFactory(redisConnectionFactory());

        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        stringRedisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        stringRedisTemplate.setDefaultSerializer(new StringRedisSerializer());
        stringRedisTemplate.afterPropertiesSet();
        return stringRedisTemplate;
    }

}
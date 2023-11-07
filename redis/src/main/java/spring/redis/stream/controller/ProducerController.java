package spring.redis.stream.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.redis.stream.producer.RedisStreamProducer;

@RestController
@RequiredArgsConstructor
@RequestMapping("/redis/stream")
public class ProducerController {

    private final RedisStreamProducer redisStreamProducer;

    @PostMapping("/produce")
    public void producing(){
        for(int i =0; i<10; i++) {
            redisStreamProducer.producing();
        }
    }

}

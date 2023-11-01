package spring.redis.controller;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.redis.service.RedisService;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/redis")
public class RedisController {

    private final RedisService redisService;

    @GetMapping("/session")
    public Object redisGetSession(HttpSession httpSession) throws Exception {
        return redisService.getSession(httpSession);
    }

    @PostMapping("/session")
    public void redisSession(HttpSession httpSession) throws Exception {
        redisService.session(httpSession);
    }

}

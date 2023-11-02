package spring.redis.controller;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.redis.service.RedisService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/redis")
public class RedisController {

    private final RedisService redisService;

    @GetMapping("/session")
    public String redisGetSession(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {
        HttpSession session = servletRequest.getSession();
        System.out.println("get session Id 확인 " + session.getId());
        return redisService.getSession(session);
    }

    @PostMapping("/session")
    public void redisSetSession(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {
        HttpSession session = servletRequest.getSession();
        System.out.println("set session Id 확인 " + session.getId());
        redisService.setSession(session);
    }

}

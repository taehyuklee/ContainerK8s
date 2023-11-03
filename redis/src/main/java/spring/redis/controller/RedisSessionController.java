package spring.redis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.redis.service.RedisCRUDService;
import spring.redis.service.RedisCacheService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/redis")
public class RedisSessionController {

    private final RedisCRUDService redisService;

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

    @GetMapping("/immediate-test")
    public String immediate(HttpServletRequest servletRequest) throws Exception {
        System.out.println("flush-on test중입니다.");
        HttpSession session = servletRequest.getSession();
        System.out.println("get session Id 확인 " + session.getId());
        redisService.setSession(session);
        Thread.sleep(10000000);
        return "test에 성공하셨습니다";
    }

}

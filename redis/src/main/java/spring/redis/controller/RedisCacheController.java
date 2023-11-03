package spring.redis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import spring.redis.domain.entity.Person;
import spring.redis.service.RedisCacheService;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/cache")
public class RedisCacheController {

    private final RedisCacheService redisCacheService;

    @PostMapping("/person")
    public void redisCacheCreate(Person person, HttpServletRequest servletRequest) throws Exception {
        redisCacheService.save(person);
        log.info("Person이 저장되었습니다");
    }

    @GetMapping("/person")
    public Person redisCacheRead(String name, HttpServletRequest servletRequest) throws Exception {
        return redisCacheService.findPersonById(name);
    }

    @PutMapping("/person")
    public void redisCacheUpdate(Person person, HttpServletRequest servletRequest) throws Exception {
        redisCacheService.update(person);
    }

    @DeleteMapping("/person")
    public void redisCacheDelete(String name, HttpServletRequest servletRequest) throws Exception {
        redisCacheService.delete(name);
    }

}

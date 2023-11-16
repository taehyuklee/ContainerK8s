package spring.redis.storage.cache.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import spring.redis.domain.entity.Person;
import spring.redis.storage.cache.service.RedisCRUDService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/redis/crud")
public class RedisCRUDController {

    private final RedisCRUDService redisCRUDService;

    @PostMapping("/person")
    public void create(@RequestBody Person person) throws Exception {
        redisCRUDService.create(person);
    }

    @GetMapping("/person")
    public void read(@RequestParam String name) throws Exception {
        redisCRUDService.read(name);
    }

    @PutMapping("/person")
    public void update(@RequestBody Person person) throws Exception {
        redisCRUDService.update(person);
    }

    @DeleteMapping("/person")
    public void delete(@RequestParam String name) throws Exception {
        redisCRUDService.delete(name);
    }

}

package spring.redis.storage.cache.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import spring.redis.storage.cache.service.RedisCacheService;
import spring.redis.domain.entity.Person;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/redis/cache")
public class RedisCacheController {

    private final RedisCacheService redisCacheService;

    @PostMapping("/person")
    public String redisCacheCreate(@RequestBody Person person, HttpServletRequest servletRequest) throws Exception {
        redisCacheService.save(person);
        return "success";
    }

    @GetMapping("/person")
    public Person redisCacheRead(@RequestParam(value = "name") String name, HttpServletRequest servletRequest) throws Exception {
        return redisCacheService.findPersonById(name);
    }

    @PutMapping("/person")
    public Person redisCacheUpdate(@RequestBody Person person, HttpServletRequest servletRequest) throws Exception {
        return redisCacheService.update(person);
    }

    @DeleteMapping("/person")
    public String redisCacheDelete(@RequestParam String name, HttpServletRequest servletRequest) throws Exception {
        redisCacheService.delete(name);
        return "success";
    }

    @GetMapping("/check_all")
    public String cehck() throws Exception {
        return redisCacheService.checkAll();
    }

    /*********** chaining **************/

    @GetMapping("/composite/person")
    public Person compositeCaching(@RequestParam(value = "name") String name) throws Exception {
        return redisCacheService.findCompositeCaching(name);
    }

    @GetMapping("/chain/person")
    public Person cahinCaching(@RequestParam(value = "name") String name) throws Exception {
        return redisCacheService.findChainCaching(name);
    }

    @PutMapping("/chain/person")
    public Person cahinUpdateCaching(@RequestBody Person person) throws Exception {
        return redisCacheService.updateChainCaching(person);
    }

    @DeleteMapping("/chain/person")
    public void deleteUpdateCaching(@RequestParam String name) throws Exception {
        redisCacheService.deleteChainCaching(name);
    }

    @DeleteMapping("/local/clear")
    public void clearLocalCache(){
        redisCacheService.clearLocalCache();
    }
}

package spring.redis.storage.cache.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.redis.storage.cache.service.RedisLockService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/redis/lock")
public class RedisLockController {

    private final RedisLockService redisLockService;

    @GetMapping("/crud/person")
    public void create(@RequestParam String name) throws Exception {
        redisLockService.lockCRUDSvc(name, 1L);
    }

    @GetMapping("/cache/person")
    public void cache(@RequestParam String name) throws Exception {
        redisLockService.lockCacheSvc(name, 1L);
    }

    @GetMapping("/file/person")
    public void file() throws Exception {
        redisLockService.lockFile(1L);
    }

}

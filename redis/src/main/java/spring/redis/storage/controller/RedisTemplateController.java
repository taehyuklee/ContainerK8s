package spring.redis.storage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.redis.storage.service.RedisTemplateService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/redis/template")
public class RedisTemplateController {

    private final RedisTemplateService templateService;



}

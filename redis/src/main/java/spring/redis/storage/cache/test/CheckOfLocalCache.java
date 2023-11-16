package spring.redis.storage.cache.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequestMapping("/check/cache")
public class CheckOfLocalCache {

    @Resource
    private CacheManager cacheManager;

    @GetMapping("/get")
    public void getBean(){

        Cache cache = cacheManager.getCache("localCache");


    }



}

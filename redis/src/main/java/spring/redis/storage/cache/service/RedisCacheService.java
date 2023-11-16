package spring.redis.storage.cache.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import spring.redis.domain.entity.Person;
import spring.redis.domain.repository.PersonMemoryRepository;

import javax.annotation.Resource;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private final PersonMemoryRepository personMemoryRepository;

    private final ApplicationContext applicationContext;

    public void save(Person person) throws Exception {
        personMemoryRepository.save(person);
    }

    @Cacheable(key = "#name", value = "personCache")
    public Person findPersonById(String name) throws Exception {
        return personMemoryRepository.getByName(name); //생각해보면 Caching할수 있는 건 return부분밖에 없다. (무슨 객체인줄 알고 하는지 어떻게 아나 궁금했었음)
    }

    @CachePut(key = "#person.name", value = "personCache")
    public Person update(Person person) throws Exception {
        return personMemoryRepository.updateByName(person);
    }

    @CacheEvict(key = "#name", value = "personCache")
    public void delete(String name) throws Exception {
        personMemoryRepository.deleteByName(name);
    }

    public String checkAll(){
        return personMemoryRepository.checkAll();
    }

    //Spring data에 있는 Local cache로도 Spring Data와 연동이 되는지
    //local cache, redis cache 동기화

    /************************** 단계별 Caching ****************************/
    @Caching(cacheable ={
        @Cacheable(value = "localCache", cacheManager = "localCacheManager",unless="#result==null"),
        @Cacheable(key = "#name", value = "personCache", cacheManager = "redisCacheManager", condition = "#result != null")
    })
    public Person findChainCaching(String name){
        return personMemoryRepository.getByName(name);
    }

    @Caching(put ={
            @CachePut(value = "localCache", cacheManager = "localCacheManager",unless="#result==null"),
            @CachePut(key = "#name", value = "personCache", cacheManager = "redisCacheManager")
    })
    public Person updateChainCaching(Person person){
        return personMemoryRepository.updateByName(person);
    }

    @Caching(evict ={
            @CacheEvict(value = "localCache", cacheManager = "localCacheManager"),
            @CacheEvict(key = "#name", value = "personCache", cacheManager = "redisCacheManager")
    })
    public void deleteChainCaching(String name){
        personMemoryRepository.deleteByName(name);
    }


    /************************** Composite Caching 이용 ****************************/
    @Cacheable(key = "#name", value = "personCache", cacheManager = "compositeCacheManager")
    public Person findCompositeCaching(String name){
        return personMemoryRepository.getByName(name);
    }


    /************************** Clear Local Cache 이용 ****************************/
    public void clearLocalCache(){

        CacheManager cacheManager = (CacheManager) applicationContext.getBean("localCacheManager");
        Cache localCache = cacheManager.getCache("localCache");

        if(localCache != null){
            localCache.clear();
            log.info("local Cache가 지워졌습니다");
        }
    }

    /************************** Cache 구현 ****************************/

    public void findPersonImple(String name){
        CacheManager localCacheManager = (CacheManager) applicationContext.getBean("localCacheManager");
        Cache localCache = localCacheManager.getCache("localCache");

        CacheManager redisCacheManager = (CacheManager) applicationContext.getBean("localCacheManager");
        Cache redisCache = redisCacheManager.getCache("localCache");
    }

}

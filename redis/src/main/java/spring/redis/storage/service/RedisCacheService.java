package spring.redis.storage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import spring.redis.storage.domain.entity.Person;
import spring.redis.storage.domain.repository.PersonMemoryRepository;

@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private final PersonMemoryRepository personMemoryRepository;

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

    @Caching(cacheable ={
        @Cacheable(value = "localCache", cacheManager = "localCacheManager",unless="#result==null"),
        @Cacheable(key = "#name", value = "personCache", cacheManager = "redisCacheManager")
    })
    public Person findChainCaching(String name){
        return personMemoryRepository.getByName(name);
    }

    @Cacheable(key = "#name", value = "personCache", cacheManager = "compositeCacheManager")
    public Person findCompositeCaching(String name){
        return personMemoryRepository.getByName(name);
    }

}

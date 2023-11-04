package spring.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import spring.redis.domain.entity.Person;
import spring.redis.domain.repository.PersonMemoryRepository;

@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private final PersonMemoryRepository personMemoryRepository;

    public void save(Person person) throws Exception {
        personMemoryRepository.save(person);
    }

    @Cacheable(key = "#name", value = "personCache")
    public Person findPersonById(String name) throws Exception {
        return personMemoryRepository.getByName(name);
    }

    @CachePut(key = "#name", value = "personCache")
    public void update(Person person) throws Exception {
        personMemoryRepository.updateByName(person);
    }

    @CacheEvict(key = "#name", value = "personCache")
    public void delete(String name) throws Exception {
        personMemoryRepository.deleteByName(name);
    }

    public void checkAll(){
        personMemoryRepository.checkAll();
    }

}

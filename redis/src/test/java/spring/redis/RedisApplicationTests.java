package spring.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.redis.domain.entity.Person;
import spring.redis.domain.repository.PersonRedisRepository;
import spring.redis.service.RedisService;

import java.util.Optional;

@SpringBootTest
class RedisApplicationTests {

	@Autowired
	private PersonRedisRepository repo;

	@Autowired
	private RedisService redisService;

	@Test
	void test() {
		Person person = new Person("Park", 20);

		// 저장
		repo.save(person);

		// `keyspace:id` 값을 가져옴
		Optional<Person> pOpt = repo.findById(person.getId());
		System.out.println(pOpt.get().getName());


		// Person Entity 의 @RedisHash 에 정의되어 있는 keyspace (people) 에 속한 키의 갯수를 구함
		repo.count();

		// 삭제
		repo.delete(person);
	}

	@Test
	void realTest(){

		Person p1 = new Person("lee", 33);

		repo.save(p1);

		Person person = repo.findPersonByName("lee").get();

//		redisService.create();
//
//		Person person = redisService.read();

		System.out.println(person);

	}

}

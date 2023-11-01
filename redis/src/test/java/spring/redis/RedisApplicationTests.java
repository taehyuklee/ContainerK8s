package spring.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
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
	void realTest() throws Exception {

		//Create
		Person p1 = new Person("lee", 33);
		redisService.create(p1);

		//Read
		System.out.println(redisService.read("lee"));

		//Update
		Person p2 = new Person("lee", 23);
		redisService.update(p2);

		//Read
		System.out.println(redisService.read("lee"));

		//Delete
		redisService.delete("lee");

		//Read (확인차원)
		//System.out.println(redisService.read("lee"));


	}

}

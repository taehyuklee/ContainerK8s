package spring.redis.service.boot_test;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedisSessionTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static String jsessionId;

    @Test
    @Order(1)
    @DisplayName("Session Set 테스트")
    void sessionSetTest() throws Exception {
        // 세션을 설정하고 JSESSIONID 쿠키를 얻는다.
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/redis/session", null, Void.class);
        jsessionId = responseEntity.getHeaders().getFirst("Set-Cookie");

    }

    @Test
    @Order(2)
    @DisplayName("Session Get 테스트")
    void sessionGetTest() throws Exception {
        // JSESSIONID 쿠키를 설정하여 동일한 세션을 유지한다.
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", jsessionId);
        HttpEntity requestEntity = new HttpEntity<>(requestHeaders);

        // 동일한 세션 ID를 사용하여 GET 요청을 보낸다.
        ResponseEntity<String> response = restTemplate.exchange("/redis/session", HttpMethod.GET, requestEntity, String.class);
        Assertions.assertEquals("test-user", response.getBody());

    }

    @Test
    @Order(3)
    @DisplayName("Session Get Set 통합 테스트")
    void sessionGetSetTest() {
        // 세션을 설정하고 JSESSIONID 쿠키를 얻는다.
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/redis/session", null, Void.class);
        String jsessionId = responseEntity.getHeaders().getFirst("Set-Cookie");

        // JSESSIONID 쿠키를 설정하여 동일한 세션을 유지한다.
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", jsessionId);
        HttpEntity requestEntity = new HttpEntity<>(requestHeaders);

        // 동일한 세션 ID를 사용하여 GET 요청을 보낸다.
        ResponseEntity<String> response = restTemplate.exchange("/redis/session", HttpMethod.GET, requestEntity, String.class);
        Assertions.assertEquals("test-user", response.getBody());
    }


}

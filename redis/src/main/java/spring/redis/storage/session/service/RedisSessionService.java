package spring.redis.storage.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class RedisSessionService {

    /****************** Redis Session set & get Service ******************/
    public void setSession(HttpSession httpSession) throws Exception {
        String number = "test-user";
        httpSession.setAttribute("test",number);
    }

    public String getSession(HttpSession httpSession) throws Exception {
        return (String) httpSession.getAttribute("test");
    }
}

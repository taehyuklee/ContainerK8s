package container.was.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class GoodService {
    
    @GetMapping("/say")
    public String sayHello(){

        return "hello, this is container service";
    }

}

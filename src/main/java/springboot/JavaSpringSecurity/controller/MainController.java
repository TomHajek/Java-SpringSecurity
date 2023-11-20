package springboot.JavaSpringSecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    /**
     * INDEX PAGE - Showing index page
     * @return index.html
     */
    @GetMapping("/")
    public String viewHomePage() {
        return "index";
    }

}

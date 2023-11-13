package springboot.JavaSpringSecurity.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Creating MVC configuration to register customized login/logout processing
 * This is only for demo purpose as an alternative for CustomSuccessHandler
 */
@Deprecated
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login_success").setViewName("login_success");
        registry.addViewController("login_error").setViewName("login_error");
        registry.addViewController("/logout_success").setViewName("logout_success");
    }

}

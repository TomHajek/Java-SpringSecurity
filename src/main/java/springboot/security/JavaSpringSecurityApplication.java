package springboot.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springboot.security.dto.RegisterRequest;
import springboot.security.service.AuthenticationService;

import static springboot.security.enumerated.Role.ADMIN;
import static springboot.security.enumerated.Role.MANAGER;

@SpringBootApplication
public class JavaSpringSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaSpringSecurityApplication.class, args);
	}

	/**
	 * 	Inserting users at application startup
 	 */
	@Bean
	public CommandLineRunner commandLineRunner(AuthenticationService service) {
		return args -> {
			var admin = RegisterRequest.builder()
					.firstname("Admin")
					.lastname("Admin")
					.email("admin@mail.com")
					.password("password")
					.role(ADMIN)
					.build();
			System.out.println("Admin token: " + service.register(admin).getAccessToken());

			var manager = RegisterRequest.builder()
					.firstname("Manager")
					.lastname("Manager")
					.email("manager@mail.com")
					.password("password")
					.role(MANAGER)
					.build();
			System.out.println("Manager token: " + service.register(manager).getAccessToken());
		};
	}

}

package springboot.JavaSpringSecurity;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import springboot.JavaSpringSecurity.entity.AppUser;
import springboot.JavaSpringSecurity.entity.Role;
import springboot.JavaSpringSecurity.repository.AppUserRepository;
import springboot.JavaSpringSecurity.repository.RoleRepository;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class JavaSpringSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaSpringSecurityApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, AppUserRepository userRepository,
						  PasswordEncoder passwordEncode) {

		return args -> {
			// Checking if the role already exists, with "return" we exit out of this method
			if (roleRepository.findByAuthority("ADMIN").isPresent()) return;

			// If db is empty, create new roles and admin user
			Role adminRole = roleRepository.save(new Role("ADMIN"));
			Role userRole = roleRepository.save(new Role("USER"));

			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);

			AppUser admin = new AppUser(1L, "admin", passwordEncode.encode("password"), roles);
			userRepository.save(admin);
		};
	}

}

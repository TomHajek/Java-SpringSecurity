package spring.security.exampleJwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import spring.security.exampleDao.entity.User;
import spring.security.exampleDao.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SpringBootApplication
public class JwtApplication {

	// injecting user repository to add dummy data
	private UserRepository userRepository;

	// work as an init method, it is run with application start
	@PostConstruct
	public void initUsers() {
		List<User> users = Stream.of(
				new User(101L, "javatechie", "javatechie@gmail.com", "password", "user"),
				new User(102L, "user1", "user1@gmail.com", "pwd1", "user"),
				new User(103L, "user2", "user2@gmail.com", "pwd2", "user"),
				new User(104L, "user3", "user3@gmail.com", "pwd3", "user")
		).collect(Collectors.toList());
		userRepository.saveAll(users);
	}

	// main method
	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}

}

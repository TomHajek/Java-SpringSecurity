package spring.security.examplePersistentToken.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.security.examplePersistentToken.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

}

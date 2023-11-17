package springboot.JavaSpringSecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboot.JavaSpringSecurity.entity.ForgotPasswordToken;

@Repository
public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordToken, Long> {

    ForgotPasswordToken findByToken(String token);

}

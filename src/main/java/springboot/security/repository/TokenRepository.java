package springboot.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import springboot.security.entity.Token;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(value = """
      SELECT t FROM Token t 
      INNER JOIN User u\s
      ON t.user.id = u.id\s
      WHERE u.id = :id AND (t.expired = false OR t.revoked = false)\s
      """)
    List<Token> findAllValidTokensByUser(Integer id);

    Optional<Token> findByToken(String token);

}

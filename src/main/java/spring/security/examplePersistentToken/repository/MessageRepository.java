package spring.security.examplePersistentToken.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.security.examplePersistentToken.entity.Message;


@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

}

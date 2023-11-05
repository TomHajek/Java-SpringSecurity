package springboot.JavaSpringSecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboot.JavaSpringSecurity.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}

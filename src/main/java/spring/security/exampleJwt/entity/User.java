package spring.security.exampleJwt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity @Table(name = "users")
@Data  @NoArgsConstructor @AllArgsConstructor
public class User {

    @Id
    private Long id;

    private String username;
    private String email;
    private String password;

}

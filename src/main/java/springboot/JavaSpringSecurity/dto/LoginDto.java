package springboot.JavaSpringSecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.JavaSpringSecurity.entity.AppUser;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    private AppUser user;
    private String jwt;

}

package springboot.JavaSpringSecurity.event;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class CustomAuthenticationHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        var authorities = authentication.getAuthorities();
        String highestRole = getHighestPriorityRole(authorities);

        if (highestRole.equals("ROLE_ADMIN")) {
            response.sendRedirect("/admin-page");
        } else if (highestRole.equals("ROLE_MANAGER")) {
            response.sendRedirect("/");  //TODO: tbd
        } else if (highestRole.equals("ROLE_USER")) {
            response.sendRedirect("/user-page");
        } else {
            response.sendRedirect("/error-page");
        }
    }

    private String getHighestPriorityRole(Collection<? extends GrantedAuthority> authorities) {
        if (authorities.isEmpty()) {
            return "";
        }

        // Defining the role priorities
        List<String> rolePriorities = Arrays.asList("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER");

        /* Taking each role (from highest) and iterating through the authorities to check,
         * if user has the authority
         */
        for (String rolePriority : rolePriorities) {
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(rolePriority)) {
                    return rolePriority;
                }
            }
        }

        // Default to the first role if none match
        return authorities.iterator().next().getAuthority();
    }

}

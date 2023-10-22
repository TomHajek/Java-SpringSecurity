package springboot.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    @JsonProperty("access_token")
    private String accessToken;

    /**
     * Refresh token improve the security and UX of the application by allowing clients to request new
     * access tokens without requiring users to re-authenticate every time their access token expires.
     */
    @JsonProperty("refresh_token")
    private String refreshToken;

}

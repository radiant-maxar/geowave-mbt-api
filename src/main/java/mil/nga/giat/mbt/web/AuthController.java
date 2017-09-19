package mil.nga.giat.mbt.web;

import mil.nga.giat.mbt.domain.UserProfile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AuthController {

    @GetMapping("/auth/login")
    public ResponseEntity startLogin() {
        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK

        // Stub implementation for Gx OAuth 2 until custom provider is finished
        return ResponseEntity
                .status(301)
                .header("Location", "http://localhost:5001/ms_oauth/oauth2/endpoints/oauthservice/authorize?redirect_uri=http://localhost:3001/auth/login/callback")
                .build();

        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
    }

    @GetMapping("/auth/login/callback")
    public ResponseEntity callback() {
        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK

        // Stub implementation for Gx OAuth 2 until custom provider is finished
        return ResponseEntity
                .status(301)
                .header("Location", "/?logged_in=true")
                .build();

        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
    }

    @GetMapping("/auth/logout")
    public ResponseEntity logout() {
        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK

        // Stub implementation for Gx OAuth 2 until custom provider is finished
        return ResponseEntity
                .status(301)
                .header("Location", "/")
                .build();

        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
    }

    @GetMapping("/auth/whoami")
    public UserProfile whoami() {
        UserProfile profile;

        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK

        // Stub implementation for Gx OAuth 2 until custom provider is finished
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "Bearer Q0FST0wgQ0FSVE9HUkFQSEVS");

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        profile = rt.exchange(
                "http://localhost:5001/ms_oauth/resources/userprofile/me",
                HttpMethod.GET,
                entity,
                UserProfile.class).getBody();

        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK
        // HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK HACK

        return profile;
    }
}

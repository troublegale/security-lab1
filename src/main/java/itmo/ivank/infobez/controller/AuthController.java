package itmo.ivank.infobez.controller;

import itmo.ivank.infobez.dto.LoginRequest;
import itmo.ivank.infobez.dto.RegisterRequest;
import itmo.ivank.infobez.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.owasp.encoder.Encode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest req) {
        var u = authService.register(req);
        return ResponseEntity.ok(Map.of(
                "id", u.getId(),
                "username", Encode.forHtmlContent(u.getUsername())
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest req) {
        try {
            String token = authService.login(req);
            return ResponseEntity.ok(Map.of(
                    "tokenType", "Bearer",
                    "token", token
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}

package itmo.ivank.infobez.controller;

import itmo.ivank.infobez.dto.LoginRequest;
import itmo.ivank.infobez.dto.RegisterRequest;
import itmo.ivank.infobez.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.owasp.encoder.Encode;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest req) {
        try {
            var u = authService.register(req);
            return ResponseEntity.ok(new java.util.LinkedHashMap<String, Object>() {{
                put("id", u.getId());
                put("username", Encode.forHtmlContent(u.getUsername()));
            }});
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest req) {
        try {
            String token = authService.login(req);
            return ResponseEntity.ok(new java.util.LinkedHashMap<String, Object>() {{
                put("tokenType", "Bearer");
                put("token", token);
            }});
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(e.getMessage());
        }
    }

}

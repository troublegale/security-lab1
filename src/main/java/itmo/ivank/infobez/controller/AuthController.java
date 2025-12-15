package itmo.ivank.infobez.controller;

import itmo.ivank.infobez.dto.LoginRequest;
import itmo.ivank.infobez.dto.RegisterRequest;
import itmo.ivank.infobez.entity.User;
import itmo.ivank.infobez.repository.UserRepository;
import itmo.ivank.infobez.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.owasp.encoder.Encode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest req) {
        if (userRepo.existsByUsername(req.username())) {
            return ResponseEntity.badRequest().body("Username already taken");
        }
        User u = new User();
        u.setUsername(req.username());
        u.setPasswordHash(encoder.encode(req.password()));
        u.setRole("USER");
        userRepo.save(u);

        return ResponseEntity.ok(new java.util.LinkedHashMap<String, Object>() {{
            put("id", u.getId());
            put("username", Encode.forHtmlContent(u.getUsername()));
        }});
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );
        String token = jwtService.generate(auth.getName());
        return ResponseEntity.ok(new java.util.LinkedHashMap<String, Object>() {{
            put("tokenType", "Bearer");
            put("token", token);
        }});
    }

}

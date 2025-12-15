package itmo.ivank.infobez.service;

import itmo.ivank.infobez.dto.LoginRequest;
import itmo.ivank.infobez.dto.RegisterRequest;
import itmo.ivank.infobez.entity.User;
import itmo.ivank.infobez.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public User register(RegisterRequest req) {
        if (userRepo.existsByUsername(req.username())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already taken");
        }
        User u = new User();
        u.setUsername(req.username());
        u.setPasswordHash(encoder.encode(req.password()));
        u.setRole("USER");
        return userRepo.save(u);
    }

    public String login(LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );
        return jwtService.generate(auth.getName());
    }

}

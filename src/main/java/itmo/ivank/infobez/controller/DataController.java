package itmo.ivank.infobez.controller;

import itmo.ivank.infobez.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.owasp.encoder.Encode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataController {

    private final UserRepository userRepo;

    @GetMapping("/data")
    public List<?> data() {
        return userRepo.findAll().stream().map(u -> new LinkedHashMap<String, Object>() {{
            put("id", u.getId());
            put("username", Encode.forHtmlContent(u.getUsername()));
            put("role", Encode.forHtmlContent(u.getRole()));
        }}).toList();
    }

}

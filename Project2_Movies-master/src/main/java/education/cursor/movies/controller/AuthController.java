package education.cursor.movies.controller;

import education.cursor.movies.config.JwtProvider;
import education.cursor.movies.dto.UserDto;
import education.cursor.movies.model.User;
import education.cursor.movies.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/auth")
    public ResponseEntity auth(@RequestBody UserDto userDto) {
        try {
            String username = userDto.getUsername();
            String password = userDto.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User userFromDB = (User) userService.loadUserByUsername(username);

            String token = jwtProvider.generateToken(userFromDB);
            Map<Object, Object> response = new HashMap<>();

            response.put("username", username);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

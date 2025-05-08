package utc.k62.cntt5.elearning.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import utc.k62.cntt5.elearning.dto.ActiveAccountRequest;
import utc.k62.cntt5.elearning.dto.ApiResponse;
import utc.k62.cntt5.elearning.dto.ResetPasswordRequest;
import utc.k62.cntt5.elearning.dto.security.JwtAuthenticationResponse;
import utc.k62.cntt5.elearning.dto.security.LoginRequest;
import utc.k62.cntt5.elearning.dto.security.SignUpRequest;
import utc.k62.cntt5.elearning.security.JwtTokenProvider;
import utc.k62.cntt5.elearning.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        userService.createNewUser(signUpRequest);
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentContextPath().path("/users/{username}")
//                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.ok(new ApiResponse(true, "Created new user. Please check your email!"));
    }

    @PutMapping("/active-account")
    public ResponseEntity<?> verifyEmail(@RequestBody ActiveAccountRequest request) {
        return ResponseEntity.ok(userService.activeAccount(request));
    }

    @GetMapping("/send-mail-forgot-password")
    public ResponseEntity<?> sendMailForgotPassword(@RequestParam String email) {
        return ResponseEntity.ok(userService.sendMailForgotPassword(email));
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(userService.resetPassword(request));
    }

}

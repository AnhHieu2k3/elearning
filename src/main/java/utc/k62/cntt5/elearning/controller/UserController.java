package utc.k62.cntt5.elearning.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utc.k62.cntt5.elearning.dto.UserDetailDto;
import utc.k62.cntt5.elearning.service.UserService;

@RestController
@RequestMapping("/api/user")
@Log4j2
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getUserDetail() {
        return ResponseEntity.ok(userService.getUserInfo());
    }

    @PutMapping
    public ResponseEntity<?> getUserDetail(@RequestBody UserDetailDto userDetailDto) {
        return ResponseEntity.ok(userService.updateUserInfo(userDetailDto));
    }
}

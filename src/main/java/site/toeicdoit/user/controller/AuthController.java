package site.toeicdoit.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.toeicdoit.user.domain.dto.UserDto;
import site.toeicdoit.user.domain.vo.MessengerVo;
// import site.toeicdoit.user.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    // private final UserService userService;

    @PostMapping("/login")
    public MessengerVo login(@RequestBody UserDto userDto) {
        return MessengerVo.builder().build();
    }

    @PostMapping("/signup")
    public MessengerVo signup(@RequestBody UserDto userDto) {
        return MessengerVo.builder().build();
    }

    @PostMapping("/logout")
    public MessengerVo logout() {
        return MessengerVo.builder().build();
    }

    @GetMapping("/refresh")
    public MessengerVo refresh() {
        return MessengerVo.builder().build();
    }
}
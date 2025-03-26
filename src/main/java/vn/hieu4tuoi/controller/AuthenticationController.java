//package vn.hieu4tuoi.controller;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import vn.hieu4tuoi.dto.request.SignInRequest;
//import vn.hieu4tuoi.dto.respone.TokenResponse;
//import vn.hieu4tuoi.service.AuthenticationService;
//
//@RestController
//@RequestMapping("/auth")
//@Tag(name = "Authentication Controller")
//@Slf4j(topic = "AUTHENTICATION-CONTROLLER")
//@RequiredArgsConstructor
//public class AuthenticationController {
//    private final AuthenticationService authenticationService;
//
//    @Operation(summary = "Get Access Token", description = "API get access token for user by username and password")
//    @PostMapping("access-token")
//    public TokenResponse getAccessToken(@RequestBody SignInRequest request) {
//        log.info("Get access token for user: {}", request.getUsername());
//        return authenticationService.getAccessToken(request);
//    }
//
//    @Operation(summary = "Refresh Token", description = "API get new access token by refresh token")
//    @PostMapping("refresh-token")
//    public TokenResponse RefreshToken(@RequestBody String refreshToken) {
//        log.info("Get new access token ");
//        return TokenResponse.builder().accessToken("DUMMY-NEW-ACCESS-TOKEN").refreshToken("DUMMY-REFRESH-TOKEN").build();
//    }
//}

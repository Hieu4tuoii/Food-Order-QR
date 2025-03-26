//package vn.hieu4tuoi.controller;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.Min;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import vn.hieu4tuoi.dto.request.UserCreationRequest;
//import vn.hieu4tuoi.dto.request.UserPasswordRequest;
//import vn.hieu4tuoi.dto.request.UserUpdateRequest;
//import vn.hieu4tuoi.service.UserService;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/user")
//@Tag(name = "User Controller")
//@Slf4j(topic = "USER-CONTROLLER")
//@RequiredArgsConstructor
//@Validated
//public class UserController {
//    private final UserService userService;
//
//    @Operation(summary = "Create User", description = "API add new user to database")
//    @PostMapping("/add")
//    public ResponseEntity<Object> createUser(@RequestBody @Valid UserCreationRequest request) {
//        log.info("Creating user: {}", request);
//        Map<String, Object> result = new LinkedHashMap<>();
//        result.put("status", HttpStatus.CREATED.value());
//        result.put("message", "User created successfully");
//        result.put("data", userService.saveUser(request));
//        return new ResponseEntity<>(result, HttpStatus.CREATED);
//    }
//
//    @Operation(summary = "Update User", description = "API update user to database")
//    @PutMapping("/update")
//    public Map<String, Object> updateUser(@RequestBody UserUpdateRequest request) {
//        log.info("Updating user: {}", request);
//        Map<String, Object> result = new LinkedHashMap<>();
//        userService.updateUser(request);
//        result.put("status", HttpStatus.ACCEPTED.value());
//        result.put("message", "User updated successfully");
//        result.put("data", "");
//        return result;
//    }
//
//    @Operation(summary = "Delete User", description = "API delete user to database")
//    @DeleteMapping("/delete/{id}")
//    public Map<String, Object> deleteUser(@PathVariable Long id) {
//        log.info("Deleting user: {}", id);
//        Map<String, Object> result = new LinkedHashMap<>();
//        userService.deleteUser(id);
//        result.put("status", HttpStatus.ACCEPTED.value());
//        result.put("message", "User deleted successfully");
//        result.put("data", "");
//        return result;
//    }
//
//    @Operation(summary = "Delete User", description = "API delete user to database")
//    @PatchMapping("/change-pwd")
//    public Map<String, Object> changePassword(@RequestBody UserPasswordRequest request) {
//        log.info("Changing password: {}", request);
//        Map<String, Object> result = new LinkedHashMap<>();
//        userService.changePassword(request);
//        result.put("status", HttpStatus.NO_CONTENT.value());
//        result.put("message", "Password changed successfully");
//        result.put("data", "");
//        return result;
//    }
//
//    @Operation(summary = "Find User by id", description = "API Find User by id")
//    @GetMapping("/{id})")
//    public Map<String, Object> getUserDetail(@PathVariable @Min(value = 1, message = "userId must be equals or gretter than 1") Long id) {
//        log.info("Finding user by id: {}", id);
//        Map<String, Object> result = new LinkedHashMap<>();
//        result.put("status", HttpStatus.OK.value());
//        result.put("message", "User found successfully");
//        result.put("data", userService.findById(id));
//        return result;
//    }
//
//    @Operation(summary = "Get user list", description = "API retrieve user from database")
//    @GetMapping("/list")
//    public Map<String, Object> getUserList(@RequestParam(required = false) String keyword,
//                                           @RequestParam(required = false) String sort,
//                                           @RequestParam(defaultValue = "0") int page,
//                                           @RequestParam(defaultValue = "20") int size) {
//        log.info("Finding user by keyword: {}, sort: {}, page: {}, size: {}", keyword, sort, page, size);
//        Map<String, Object> result = new LinkedHashMap<>();
//        result.put("status", HttpStatus.OK.value());
//        result.put("message", "user list");
//        result.put("data", userService.findAll(keyword, sort, page, size));
//        return result;
//    }
//
//
//
//
//
//}

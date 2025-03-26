package vn.hieu4tuoi.service;

import vn.hieu4tuoi.dto.request.UserCreationRequest;
import vn.hieu4tuoi.dto.request.UserPasswordRequest;
import vn.hieu4tuoi.dto.request.UserUpdateRequest;
import vn.hieu4tuoi.dto.respone.PageResponse;
import vn.hieu4tuoi.dto.respone.UserResponse;

public interface UserService {
    Long saveUser(UserCreationRequest req);
    void updateUser(UserUpdateRequest req);
    void deleteUser(Long id);
    void changePassword(UserPasswordRequest req);
    UserResponse findById(Long id);
    PageResponse<UserResponse> findAll(String keyword, String sort, int page, int size);
    UserResponse findByUsername(String username);
}

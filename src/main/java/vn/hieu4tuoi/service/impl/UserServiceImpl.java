package vn.hieu4tuoi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.hieu4tuoi.common.UserStatus;
import vn.hieu4tuoi.dto.request.UserCreationRequest;
import vn.hieu4tuoi.dto.request.UserPasswordRequest;
import vn.hieu4tuoi.dto.request.UserUpdateRequest;
import vn.hieu4tuoi.dto.respone.PageResponse;
import vn.hieu4tuoi.dto.respone.UserResponse;
import vn.hieu4tuoi.exception.InvalidDataException;
import vn.hieu4tuoi.exception.ResourceNotFoundException;
import vn.hieu4tuoi.model.AddressEntity;
import vn.hieu4tuoi.model.UserEntity;
import vn.hieu4tuoi.repository.AddressRepository;
import vn.hieu4tuoi.repository.UserRepository;
import vn.hieu4tuoi.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j(topic = "USER_SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository  userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long saveUser(UserCreationRequest req) {
        log.info("Saving user: {}", req);
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(req.getFirstName());
        userEntity.setLastName(req.getLastName());
        userEntity.setGender(req.getGender());
        userEntity.setBirthday(req.getBirthday());
        userEntity.setEmail(req.getEmail());
        userEntity.setPhone(req.getPhone());
        userEntity.setUsername(req.getUsername());
        userEntity.setType(req.getType());
        userEntity.setStatus(UserStatus.ACTIVE);
        userRepository.save(userEntity);
        log.info("Saved user: {}", userEntity);

        //save address
        if(userEntity.getId()!=null){
            log.info("Saving address for user: {}", userEntity.getId());
            List<AddressEntity> addressList = new ArrayList<>();
            req.getAddresses().forEach(address -> {
                AddressEntity addressEntity = new AddressEntity();
                addressEntity.setApartmentNumber(address.getApartmentNumber());
                addressEntity.setFloor(address.getFloor());
                addressEntity.setBuilding(address.getBuilding());
                addressEntity.setStreetNumber(address.getStreetNumber());
                addressEntity.setStreet(address.getStreet());
                addressEntity.setCity(address.getCity());
                addressEntity.setCountry(address.getCountry());
                addressEntity.setAddressType(address.getAddressType());
                addressEntity.setUserId(userEntity.getId());
                addressList.add(addressEntity);
            });
            addressRepository.saveAll(addressList);
            log.info("Saved address for user: {}", userEntity.getId());
        }
        return userEntity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateRequest req) {
        log.info("Updating user: {}", req);
        UserEntity userEntity = getUserEntityById(req.getId());
        userEntity.setFirstName(req.getFirstName());
        userEntity.setLastName(req.getLastName());
        userEntity.setGender(req.getGender());
        userEntity.setBirthday(req.getBirthday());
        userEntity.setEmail(req.getEmail());
        userEntity.setPhone(req.getPhone());
        userEntity.setUsername(req.getUsername());
        userRepository.save(userEntity);
        log.info("Updated user: {}", userEntity);

        //save address
        List<AddressEntity> addressList = new ArrayList<>();
        req.getAddresses().forEach(address -> {
            AddressEntity addressEntity = addressRepository.findByUserIdAndAddressType(req.getId(), address.getAddressType()); //0 là địa chỉ nhà, 1 là cty, ...
            if(addressEntity == null){
                addressEntity = new AddressEntity();
            }
            addressEntity.setApartmentNumber(address.getApartmentNumber());
            addressEntity.setFloor(address.getFloor());
            addressEntity.setBuilding(address.getBuilding());
            addressEntity.setStreetNumber(address.getStreetNumber());
            addressEntity.setStreet(address.getStreet());
            addressEntity.setCity(address.getCity());
            addressEntity.setCountry(address.getCountry());
            addressEntity.setAddressType(address.getAddressType());
            addressEntity.setUserId(userEntity.getId());

            addressList.add(addressEntity);
        });
        addressRepository.saveAll(addressList);
        log.info("Updated address for user: {}", userEntity.getId());
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user: {}", id);
        UserEntity userEntity = getUserEntityById(id);
        userEntity.setStatus(UserStatus.INACTIVE);
        userRepository.save(userEntity);
        log.info("Deleted user: {}", userEntity);
    }

    @Override
    public void changePassword(UserPasswordRequest req) {
        log.info("Changing password for user: {}", req);

        UserEntity userEntity = getUserEntityById(req.getId());
        if(req.getPassword().equals(req.getConfirmPassword())){
            userEntity.setPassword(passwordEncoder.encode(req.getPassword()));
            userRepository.save(userEntity);
        }
        else {
            throw new ResourceNotFoundException("Password and confirm password not match");
        }
        log.info("Changed password for user: {}", userEntity);
    }

    @Override
    public UserResponse findById(Long id) {
        log.info("Finding user by id: {}", id);
        UserEntity userEntity = getUserEntityById(id);
        log.info("Found user by id: {}", userEntity);
        return UserResponse.builder()
                .id(id)
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .gender(userEntity.getGender())
                .birthday(userEntity.getBirthday())
                .username(userEntity.getUsername())
                .phone(userEntity.getPhone())
                .email(userEntity.getEmail())
                .build();
    }

    @Override
    public PageResponse findAll(String keyword, String sort, int page, int size) {
        log.info("Finding all users by keyword: {}, sort: {}, page: {}, size: {}", keyword, sort, page, size);
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "id");//mac dinh sap xep theo id tang dan
        if(StringUtils.hasLength(sort)){
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if(matcher.find()){
                String columnName = matcher.group(1);
               order = matcher.group(3).equalsIgnoreCase("asc")
                       ? new Sort.Order(Sort.Direction.ASC, columnName)
                       : new Sort.Order(Sort.Direction.DESC, columnName);
            }
        }

        //xu ly page = 0
        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        Page<UserEntity> entityPage;

        if(StringUtils.hasLength(keyword)){
            keyword = "%" + keyword.toLowerCase() + "%";
            entityPage = userRepository.searchByKeyword(keyword, pageable);
        }
        else {
            entityPage = userRepository.findAll(pageable);
        }

        List<UserResponse> userList = entityPage.stream().map(entity -> UserResponse.builder()
                        .id(entity.getId())
                        .firstName(entity.getFirstName())
                        .lastName(entity.getLastName())
                        .gender(entity.getGender())
                        .birthday(entity.getBirthday())
                        .username(entity.getUsername())
                        .phone(entity.getPhone())
                        .email(entity.getEmail())
                        .build()
                ).toList();
        return PageResponse.builder()
                .pageNo(page)
                .pageSize(size)
                .totalPage(entityPage.getTotalPages())
                .items(userList)
                .build();
    }

    @Override
    public UserResponse findByUsername(String username) {
        return null;
    }

    private UserEntity getUserEntityById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}

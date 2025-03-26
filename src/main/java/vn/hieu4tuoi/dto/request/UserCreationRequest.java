package vn.hieu4tuoi.dto.request;


import lombok.Getter;
import lombok.ToString;
import vn.hieu4tuoi.common.UserType;

@Getter
@ToString(callSuper = true)
public class UserCreationRequest extends UserBaseRequest {
    private UserType type;
}


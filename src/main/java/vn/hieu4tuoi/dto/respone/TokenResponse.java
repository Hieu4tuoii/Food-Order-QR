package vn.hieu4tuoi.dto.respone;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class TokenResponse implements Serializable {
    private String accessToken;
    private String refreshToken;
}

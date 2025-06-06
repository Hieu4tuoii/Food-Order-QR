package vn.hieu4tuoi.dto.request.invoice;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import vn.hieu4tuoi.common.PaymentMethod;
import vn.hieu4tuoi.common.PaymentStatus;

@Getter
@ToString
public class PaymentStatusChangeRequest {
    @NotBlank(message = "id must not be blank")
    private String id;

    @NotNull(message = "paymentMethod must not be null")
    private PaymentMethod paymentMethod;

}

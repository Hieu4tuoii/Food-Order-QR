package vn.hieu4tuoi.dto.respone.bank;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class Transaction {
    private String id;
    private String bankBrandName;
    private String accountNumber;
    private String transactionDate;
    private String amountOut;
    private String amountIn;
    private String accumulated;
    private String transactionContent;
    private String referenceNumber;
    private String code;
    private String subAccount;
    private String bankAccountId;
}

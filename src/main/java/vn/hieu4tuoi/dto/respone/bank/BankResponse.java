package vn.hieu4tuoi.dto.respone.bank;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class BankResponse {
    private int status;
    private String error;
    private Map<String, Boolean> messages;
    //private List<Transaction> transactions;
}

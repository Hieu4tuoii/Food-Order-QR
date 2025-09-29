package vn.hieu4tuoi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hieu4tuoi.dto.respone.ResponseData;
import vn.hieu4tuoi.service.BankService;

@RestController
@RequestMapping("/bank")
@Tag(name = "Bank Controller")
@Slf4j(topic = "BANK-CONTROLLER")
@RequiredArgsConstructor
@Validated
public class BankController {
     private final BankService bankService;

     @Operation(summary = "Check if card number is valid")
     @GetMapping("/check/{invoiceId}")
     public ResponseData<Boolean> checkCardNumber(@PathVariable String invoiceId) {
         log.info("Checking card number: {}", invoiceId);
         boolean isValid = bankService.isValidBank(invoiceId);
         return new ResponseData<>(HttpStatus.OK.value(), "bank isValid", isValid);
     }
}

package vn.hieu4tuoi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import vn.hieu4tuoi.common.PaymentMethod;
import vn.hieu4tuoi.dto.request.invoice.PaymentStatusChangeRequest;
import vn.hieu4tuoi.dto.respone.bank.BankResponse;
import vn.hieu4tuoi.dto.respone.bank.Transaction;
import vn.hieu4tuoi.dto.respone.invoice.InvoiceResponse;
import vn.hieu4tuoi.model.DiningTable;
import vn.hieu4tuoi.repository.DiningTableRepository;
import vn.hieu4tuoi.service.BankService;
import vn.hieu4tuoi.service.InvoiceService;

import java.lang.invoke.MethodType;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
    @Value("${bank.api.url}")
    private String apiUrl;
    @Value("${bank.api.account-number}")
    private String accountNumber;
    private final RestTemplate restTemplate;
    private final DiningTableRepository diningTableRepository;
    private final InvoiceService invoiceService;

    @Override
    @Transactional
    public boolean isValidBank(String diningTableId) {
        //get dining table
        DiningTable diningTable = diningTableRepository.findById(diningTableId).orElseThrow(
                () -> new RuntimeException("Dining table not found")
        );

        BankResponse response = fetchBankTransactions();
        if (response == null || response.getTransactions() == null) {
            log.warn("No transactions found for dining table: {}", diningTableId);
            return false;
        }

       // Extract transaction list from response
        List<Transaction> transactions = response.getTransactions();

        // Regex để lấy nội dung giữa dấu chấm thứ 3 và thứ 4 của transactionContent
        String regex = "(?:[^.]*\\.){3}([^.]*)\\.";
        Pattern pattern = Pattern.compile(regex);

        //get invoice from dining table
        InvoiceResponse invoice = invoiceService.getCurrentTableInvoice(diningTableId);
        if (invoice == null) {
            log.warn("No invoice found for dining table: {}", diningTableId);
            throw new ResourceAccessException(" No invoice found for dining table: " + diningTableId);
        }
        String invoiceId= invoice.getId();
        for(Transaction transaction : transactions) {
            Matcher matcher = pattern.matcher(transaction.getTransaction_content());
            if (matcher.find()) {
                String extractedContent = matcher.group(1).trim();
                if(extractedContent.equals(invoice.getId()) && transaction.getAmount_in().equals(invoice.getTotalPrice()+"0") ) {
                    //thuc hien confirm thanh toan
                    invoiceService.confirmPayment(new PaymentStatusChangeRequest(invoice.getId(), PaymentMethod.BANK_TRANSFER));
                    log.info("Payment confirmed for invoice: {}", invoice.getId());
                    return true;
                }
            } else {
                log.warn("No match found in transaction content: {}", transaction.getTransaction_content());
            }
        }
        return false;
    }

    private BankResponse fetchBankTransactions() {
        log.info("Fetching bank transactions for account: {}", accountNumber);

        // Set up HTTP headers with Bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("LF2YDB0US6SGDHRXPHALRJYJ8FH3S7DPXTOTKSWBZKQEJIDLJ41PBO1HFAEGOPXE"); // Replace with actual token or retrieve it from config

        // Build URL with query parameters
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("account_number", accountNumber)
                .queryParam("limit", 10)
                .toUriString();

        // Create HTTP entity
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // Make API call
        try {
            ResponseEntity<BankResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    BankResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("Successfully fetched bank transactions. Status: {}", response.getBody().getStatus());
                return response.getBody();
            } else {
                log.warn("Failed to fetch bank transactions. Status code: {}", response.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            log.error("Error fetching bank transactions: {}", e.getMessage());
            throw new RuntimeException("Error fetching bank transactions: " + e.getMessage(), e);
        }
    }
}

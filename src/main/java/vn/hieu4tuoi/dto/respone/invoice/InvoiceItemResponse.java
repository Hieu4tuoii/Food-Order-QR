package vn.hieu4tuoi.dto.respone.invoice;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private Double priceAtOrder;
    private Long quantity;
    private Double totalPrice;
}

package vn.hieu4tuoi.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "tbl_food")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Food extends AbstractEntity<Long> {
    private String name;
    private String description;
    private Double price;
    // Add other food properties as needed
    private String imageUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}

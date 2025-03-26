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
public class Food extends AbstractEntity<Long>{
    private String name;
    private String imageUrl;
    private Long price;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}

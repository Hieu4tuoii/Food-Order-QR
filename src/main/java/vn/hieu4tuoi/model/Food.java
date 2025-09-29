package vn.hieu4tuoi.model;

import jakarta.persistence.*;
import lombok.*;
import vn.hieu4tuoi.common.FoodStatus;

@Getter
@Setter
@Entity
@Table(name = "tbl_food")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Food extends AbstractEntity<Long> {
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Double price;
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private FoodStatus status = FoodStatus.AVAILABLE; // mac dinh la con hang
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}

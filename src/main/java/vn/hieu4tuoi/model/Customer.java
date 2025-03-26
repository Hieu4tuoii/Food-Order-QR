package vn.hieu4tuoi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "tbl_customer")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends AbstractEntity<Long>{
    private String name;
}

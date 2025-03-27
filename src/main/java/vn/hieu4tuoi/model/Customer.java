package vn.hieu4tuoi.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_customer")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends AbstractEntity<Long>{
    private String name;
    //lien ket 1-n den bang cartdetail
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartDetail> cartDetails = new ArrayList<>();
}

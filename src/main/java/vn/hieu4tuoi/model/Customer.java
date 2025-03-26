package vn.hieu4tuoi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_customer")
public class Customer extends AbstractEntity<Long>{
    private String name;
}

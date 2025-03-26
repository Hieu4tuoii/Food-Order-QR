package vn.hieu4tuoi.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "tbl_dining_table")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiningTable extends AbstractEntity<Long> {
    private String name;
}

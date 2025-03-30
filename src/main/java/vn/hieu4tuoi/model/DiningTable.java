package vn.hieu4tuoi.model;

import jakarta.persistence.*;
import lombok.*;
import vn.hieu4tuoi.common.TableStatus;

@Getter
@Setter
@Entity
@Table(name = "tbl_dining_table")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiningTable extends AbstractEntity<Long> {
    private String name;
    @Enumerated(EnumType.STRING)
    private TableStatus status = TableStatus.EMPTY; // mac dinh la co san
}

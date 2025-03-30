package vn.hieu4tuoi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import vn.hieu4tuoi.common.TableStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tbl_dining_table")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiningTable {
    @Id
    @Column(name = "id")
    private String id;

    private String name;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private TableStatus status = TableStatus.EMPTY; // mac dinh la co san

    @Column(name = "created_at", length = 255)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", length = 255)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    public void generateUUID() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}

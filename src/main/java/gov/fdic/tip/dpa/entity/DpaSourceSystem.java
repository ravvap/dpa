package gov.fdic.tip.dpa.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "dpa_source_system")
@Data
public class DpaSourceSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name; // e.g., CALL, RRPS, SIMS

    @Column(name = "description", length = 1000)
    private String description;
}
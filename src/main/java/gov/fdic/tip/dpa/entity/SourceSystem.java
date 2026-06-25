package gov.fdic.tip.dpa.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "source_system")
@Data
public class SourceSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;
}
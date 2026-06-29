package gov.fdic.tip.dpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.ZonedDateTime;

@Entity
@Table(name = "dpa_operation_rules")
@Data
public class DpaOperationRules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "line_item", nullable = false)
    private Integer lineItem;

    @Column(name = "field_name", length = 255, nullable = false)
    private String fieldName; // Used to populate the Description drop-down filter

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "field_group_id")
    private Long fieldGroupId;

    @Column(name = "source_system", length = 10)
    private String sourceSystem;

    @Column(name = "pre_cutoff_op")
    private Long preCutoffOp;

    @Column(name = "post_cutoff_op")
    private Long postCutoffOp;

    @Column(name = "display", nullable = false)
    private Boolean display; // Criteria flag filter: main grid only renders when display = true

    @Column(name = "effective_start")
    private ZonedDateTime effectiveStart;

    @Column(name = "effective_end")
    private ZonedDateTime effectiveEnd;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;
}
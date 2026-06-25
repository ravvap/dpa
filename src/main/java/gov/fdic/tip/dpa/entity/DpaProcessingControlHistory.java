package gov.fdic.tip.dpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.ZonedDateTime;

@Entity
@Table(name = "dpa_processing_control_history")
@Data
public class DpaProcessingControlHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "line_item_number", nullable = false)
    private Integer lineItemNumber;

    @Column(name = "line_item_description", length = 255)
    private String lineItemDescription;

    @Column(name = "processing_period", length = 10, nullable = false)
    private String processingPeriod;

    @Column(name = "pre_cutoff_control", length = 10, nullable = false)
    private String preCutoffControl;

    @Column(name = "post_cutoff_control", length = 10, nullable = false)
    private String postCutoffControl;

    @Column(name = "change_date", nullable = false)
    private ZonedDateTime changeDate;

    @Column(name = "changed_by", length = 100, nullable = false)
    private String changedBy;

    @Column(name = "change_type", length = 20, nullable = false)
    private String changeType; // MANUAL_EDIT, QUARTER_INIT, or MIGRATION
}
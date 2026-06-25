package gov.fdic.tip.dpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.ZonedDateTime;

@Entity
@Table(name = "dpa_processing_controls", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"line_item_number", "processing_period"})
})
@Data
public class DpaProcessingControl {

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

    @Column(name = "modified_date", nullable = false)
    private ZonedDateTime modifiedDate;

    @Column(name = "modified_by", length = 100, nullable = false)
    private String modifiedBy;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version = 0;
}
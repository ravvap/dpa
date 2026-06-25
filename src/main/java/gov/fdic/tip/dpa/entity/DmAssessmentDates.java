package gov.fdic.tip.dpa.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "dm_assessment_dates")
@Data
public class DmAssessmentDates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_category", length = 50)
    private String dataCategory;

    @Column(name = "date_type", length = 255)
    private String dateType;

    @Column(name = "quarter", length = 10)
    private String quarter;

    @Column(name = "assessment_year")
    private Integer assessmentYear;

    @Column(name = "assessment_period", length = 10)
    private String assessmentPeriod;

    @Column(name = "description", length = 1000)
    private String description;
}
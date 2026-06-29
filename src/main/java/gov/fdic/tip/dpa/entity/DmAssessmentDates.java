package gov.fdic.tip.dpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "dm_assessment_dates")
@Data
public class DmAssessmentDates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_category")
    private String dateCategory;

    @Column(name = "date_type", length = 100)
    private String dateType; // Checked against 'Period Begin Date' and 'Period End Date'

    @Column(name = "date_value")
    private LocalDate dateValue;

    @Column(name = "quarter", length = 15)
    private String quarter;

    @Column(name = "assessment_year")
    private Integer assessmentYear;

    @Column(name = "assessment_period", length = 20)
    private String assessmentPeriod;
}
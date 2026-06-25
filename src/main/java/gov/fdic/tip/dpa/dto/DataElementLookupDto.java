package gov.fdic.tip.dpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataElementLookupDto {
    private Integer lineItemNumber;
    private String description;
}
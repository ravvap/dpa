/**
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * System Name       : TIP
 * Subsystem         : Data Processing Admin (DPA)
 * Class Name        : DpaGridControllerTest
 * Description       : Unit tests validating the REST endpoints, ensuring correct
 * response formats, and verification of query parameters.
 *
 * Primary Developer : Prasad Ravva
 * Creation Date     : June 29, 2026
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 */
package gov.fdic.tip.dpa.controller;

import gov.fdic.tip.dpa.constants.DpaConstants;
import gov.fdic.tip.dpa.dto.MainGridRowDto;
import gov.fdic.tip.dpa.service.DpaManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DpaGridControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DpaManagementService managementService;

    @InjectMocks
    private DpaGridController dpaGridController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dpaGridController).build();
    }

    @Test
    @DisplayName("GET /lookup/descriptions - Success State")
    void getDescriptionFilterDropdown_ShouldReturnList() throws Exception {
        List<String> mockDescriptions = List.of("AVE_MTH_USED", "ASSET_VAL_CALC");
        when(managementService.getDescriptionDropdownValues()).thenAnswer(inv -> mockDescriptions);

        mockMvc.perform(get(DpaConstants.Routes.LOOKUP_DESCRIPTIONS)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("AVE_MTH_USED"))
                .andExpect(jsonPath("$").value("ASSET_VAL_CALC"));
    }

    @Test
    @DisplayName("GET /grid-search - Valid Parameters Returns Payload")
    void fetchActiveGrid_WithValidParams_ShouldReturnFilteredRows() throws Exception {
        MainGridRowDto row = MainGridRowDto.builder()
                .lineItem(5250)
                .fieldName("AVE_MTH_USED")
                .sourceSystem("CALL")
                .assessmentPeriod("2026Q1")
                .preCutoffOp(1L)
                .postCutoffOp(2L)
                .build();

        when(managementService.searchMainGrid("AVE_MTH_USED", "CALL")).thenAnswer(inv -> List.of(row));

        mockMvc.perform(get(DpaConstants.Routes.GRID_SEARCH)
                .param("description", "AVE_MTH_USED")
                .param("sourceSystem", "CALL")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lineItem").value(5250))
                .andExpect(jsonPath("$.sourceSystem").value("CALL"))
                .andExpect(jsonPath("$.assessmentPeriod").value("2026Q1"));
    }
}
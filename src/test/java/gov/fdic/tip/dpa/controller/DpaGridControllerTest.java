/**
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * System Name       : TIP (Technology Integration Platform)
 * Subsystem         : Data Processing Admin (DPA)
 * Class Name        : DpaGridControllerTest
 * Description       : Unit tests verifying paginated grid search controllers, parameter
 * validation handoffs, and default Pageable configurations.
 *
 * Primary Developer : Prasad Ravva
 * Creation Date     : June 29, 2026
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 */
package gov.fdic.tip.dpa.controller;

import gov.fdic.tip.dpa.dto.MainGridRowDto;
import gov.fdic.tip.dpa.service.DpaManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(DpaGridController.class)
class DpaGridControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DpaManagementService managementService;

    @Test
    @WithMockUser(authorities = "DPA_VIEW")
    @DisplayName("Fetch Active Grid - Valid Parameters Returns Paginated Data")
    void fetchActiveGrid_WithValidParams_ShouldReturnPage() throws Exception {
        MainGridRowDto rowDto = MainGridRowDto.builder()
                .lineItem(5250)
                .fieldName("ASSET")
                .description("Total Assets")
                .sourceSystem("CALL")
                .assessmentPeriod("FZ1 1Q-2026")
                .build();

        Page<MainGridRowDto> mockPage = new PageImpl<>(Collections.singletonList(rowDto), PageRequest.of(0, 25), 1);

        Mockito.when(managementService.searchMainGridPaginated(eq("Total Assets"), eq("CALL"), any(Pageable.class)))
               .thenReturn(mockPage);

        mockMvc.perform(get("/api/v1/data-processing-admin/grid-search")
                .param("description", "Total Assets")
                .param("sourceSystem", "CALL")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.lineItem").value(5250))
                .andExpect(jsonPath("$.content.assessmentPeriod").value("FZ1 1Q-2026"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser(authorities = "DPA_VIEW")
    @DisplayName("Fetch Active Grid - Throws Exception if Parameters are Conflicting or Empty")
    void fetchActiveGrid_WithEmptyParams_ShouldPropagateError() throws Exception {
        Mockito.when(managementService.searchMainGridPaginated(null, null, PageRequest.of(0, 25)))
               .thenThrow(new IllegalArgumentException("Search requires either description or source system."));

        mockMvc.perform(get("/api/v1/data-processing-admin/grid-search")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
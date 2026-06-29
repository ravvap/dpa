/**
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * System Name       : TIP
 * Subsystem         : Data Processing Admin (DPA)
 * Class Name        : DpaConstants
 * Description       : Centralized constants registry maintaining application endpoints,
 * security SpEL permissions, system validation messages, and native queries.
 *
 * Developer 		 : Prasad Ravva
 * Creation Date     : June 29, 2026
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 */
package gov.fdic.tip.dpa.constants;

public final class DpaConstants {

    // Prevent instantiation
    private DpaConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * URL Mapping Route Constants
     */
    public static final class Routes {
        public static final String BASE_V1 = "/api/v1/dpa";
        
        // Lookup Metadata Endpoints
        public static final String LOOKUP_DESCRIPTIONS = BASE_V1 + "/lookup/descriptions";
        public static final String LOOKUP_SOURCE_SYSTEMS = BASE_V1 + "/lookup/source-systems";
        
        // Core Functional Grid and Process Action Endpoints
        public static final String GRID_SEARCH = BASE_V1 + "/grid-search";
        public static final String VIEW_HISTORY = BASE_V1 + "/history";
        public static final String UPDATE_CONTROL = BASE_V1 + "/update";
        public static final String EXPORT_GRID = BASE_V1 + "/export";
    }

    /**
     * Security & PreAuthorize Permission SpEL Expressions
     */
    public static final class Permissions {
        public static final String VIEW_CODE = "DPA_LINE_ITEM_VIEW";
        public static final String EDIT_CODE = "DPA_LINE_ITEM_EDIT_MODE";

        public static final String HAS_VIEW_AUTHORITY = "hasAuthority('" + VIEW_CODE + "')";
        public static final String HAS_EDIT_AUTHORITY = "hasAuthority('" + EDIT_CODE + "')";
    }

    /**
     * API Exception & UI Validation Message Constants
     */
    public static final class Messages {
        public static final String ERROR_CONFLICTING_PARAMS = 
            "Conflicting search parameters. Supply either dataElement OR sourceSystem+processingPeriod.";
        public static final String ERROR_MISSING_CRITERIA = 
            "Missing required query criteria options.";
        public static final String ERROR_INVALID_FORMAT = 
            "Unsupported export format requested. Only 'xlsx' format is allowed.";
        
        public static final String EXPORT_REPORT_TITLE = "Data Processing Admin Export";
        public static final String AUDIT_MANUAL_EDIT = "MANUAL_EDIT";
        public static final String DEFAULT_USER = "SYSTEM_ANALYST";
    }

    /**
     * Data Mapping Operational Identifiers
     */
    public static final class OperationCodes {
        public static final String UPD = "UPD";
        public static final String PAMND = "PAMND";
        public static final String CALCULATE = "CALCULATE";
        public static final String RAMND = "RAMND";
    }

    /**
     * Named Native SQL Queries
     */
    public static final class SqlQueries {
        public static final String FIND_VALID_WORKFLOW_PERIODS = 
            "SELECT DISTINCT d.assessment_period FROM dm_assessment_dates d " +
            "WHERE d.date_type IN ('Period Begin Date', 'Period End Date') " +
            "ORDER BY d.assessment_period DESC";
            
        public static final String FIND_DISTINCT_FIELD_NAMES = 
            "SELECT DISTINCT r.field_name FROM dpa_operation_rules r WHERE r.field_name IS NOT NULL ORDER BY r.field_name ASC";
            
        public static final String FIND_SOURCE_SYSTEM_NAMES = 
            "SELECT s.name FROM dpa_source_system s ORDER BY s.name ASC";
    }
}
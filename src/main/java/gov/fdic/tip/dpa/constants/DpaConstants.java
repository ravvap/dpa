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
        public static final String BASE_V1 = "/api/v1";
        public static final String DPA_METADATA = BASE_V1 + "/dpa/metadata";
        public static final String DPA_SOURCE_SYSTEMS = BASE_V1 + "/dpa/sourcesystems";
        public static final String DPA_DATA_ELEMENTS = BASE_V1 + "/dpa/dataelements";
        public static final String DPA_HISTORY = BASE_V1 + "/dpa/history";
        public static final String DPA_EXPORT = BASE_V1 + "/dpa/export";
        public static final String DPA_PROCESSING_PERIODS = BASE_V1 + "/processingperiods";
    }

    /**
     * Security & PreAuthorize Permission SpEL Expressions
     */
    public static final class Permissions {
        // Raw Permission Codes
        public static final String VIEW_CODE = "DPA_LINE_ITEM_VIEW";
        public static final String EDIT_CODE = "DPA_LINE_ITEM_EDIT_MODE";

        // Spring Security SpEL Expressions for @PreAuthorize
        public static final String HAS_VIEW_AUTHORITY = "hasAuthority('" + VIEW_CODE + "')";
        public static final String HAS_EDIT_AUTHORITY = "hasAuthority('" + EDIT_CODE + "')";
    }

    /**
     * API Exception & Validation Message Constants
     */
    public static final class Messages {
        public static final String ERROR_CONFLICTING_PARAMS = 
            "Conflicting search parameters. Supply either dataElement OR sourceSystem+processingPeriod.";
        public static final String ERROR_MISSING_CRITERIA = 
            "Missing required query criteria options.";
        public static final String ERROR_INVALID_FORMAT = 
            "Unsupported export format requested. Only 'xlsx' format is allowed.";
        
        public static final String EXPORT_REPORT_TITLE = "Data Processing Admin Export";
        public static final String SYSTEM_MODIFIER = "SYSTEM_MIGRATION";
    }

    /**
     * Named Native SQL Queries
     */
    public static final class SqlQueries {
        public static final String FIND_DISTINCT_ASSESSMENT_PERIODS = 
            "SELECT DISTINCT d.assessment_period FROM dm_assessment_dates d ORDER BY d.assessment_period DESC";
    }
}
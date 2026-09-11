package hk.org.ha.iams.termsearch.constant;

public final class TermServiceMessages {

    public static final String SUCCESS = "success";
    public static final String ERROR_NO_RECORD = "no record found";
    public static final String ERROR_MISSING_RECORD = "some records not found";
    public static final String ERROR_MISSING_KEYWORD_CODES_TERMID = "in_keyword, in_termCodes, in_termIds are compulsory";
    public static final String ERROR_MISSING_NATURE = "in_natures is compulsory";
    public static final String ERROR_MISSING_REFTERM = "out_refTerm is compulsory";
    public static final String ERROR_MISSING_CODE_TYPE = "out_codeTypes is compulsory";
    public static final String ERROR_SERVICE_UNAVAILABLE = "Service Unavailable";
    public static final String ERROR_EXCEPTION = "exception in IAMS";
    public static final String ERROR_MISSING_CODE = "Code is compulsory for code search";
    public static final String ERROR_MISSING_CODE_EXTENSION_ICD9 = "Extension is compulsory for code + extension search with code type ICD9Dx and ICD9Px";
    public static final String ERROR_MISSING_CODE_EXTENSION = "Code is compulsory for code + extension search";
    public static final String ERROR_MISSING_REQUEST_SYSTEM = "in_requestSystem is compulsory";

    private TermServiceMessages() {
    }
}

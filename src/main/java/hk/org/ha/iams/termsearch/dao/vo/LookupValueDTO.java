package hk.org.ha.iams.termsearch.dao.vo;

public class LookupValueDTO {

    private Integer valueKey;
    private String dataValue;

    public LookupValueDTO() {
    }

    public LookupValueDTO(Integer valueKey, String dataValue) {
        this.valueKey = valueKey;
        this.dataValue = dataValue;
    }

    public Integer getValueKey() {
        return valueKey;
    }

    public void setValueKey(Integer valueKey) {
        this.valueKey = valueKey;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }
}

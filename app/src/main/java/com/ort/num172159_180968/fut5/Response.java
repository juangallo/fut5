package com.ort.num172159_180968.fut5;

/**
 * Created by JuanMartin on 9/26/2015.
 */
public class Response {

    /**
     * fieldId : 1
     * fieldName : 0 Stress
     * fieldLat : -34.8736473
     * fieldLon : -56.1775762
     */

    private int fieldId;
    private String fieldName;
    private double fieldLat;
    private double fieldLon;

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setFieldLat(double fieldLat) {
        this.fieldLat = fieldLat;
    }

    public void setFieldLon(double fieldLon) {
        this.fieldLon = fieldLon;
    }

    public int getFieldId() {
        return fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public double getFieldLat() {
        return fieldLat;
    }

    public double getFieldLon() {
        return fieldLon;
    }
}

package com.ort.num172159_180968.fut5.model.persistance;

/**
 * Created by JuanMartin on 19-Nov-15.
 */
public class Field {
    private int fieldId;
    private String fieldName;
    private double fieldLat;
    private double fieldLon;

    // constructors
    public Field() {
    }

    public Field(int fieldId, String fieldName, double fieldLat, double fieldLon) {
        this.fieldId = fieldId;
        this.fieldName = fieldName;
        this.fieldLat = fieldLat;
        this.fieldLon = fieldLon;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public double getFieldLat() {
        return fieldLat;
    }

    public void setFieldLat(double fieldLat) {
        this.fieldLat = fieldLat;
    }

    public double getFieldLon() {
        return fieldLon;
    }

    public void setFieldLon(double fieldLon) {
        this.fieldLon = fieldLon;
    }

}

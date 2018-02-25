package com.makris.site.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserSnatchEnvelope implements Serializable{
    private static final long serialVersionUID = -5617482065991830143L;

    private Long id;
    private Long envelopeId;
    private Long userId;
    private Double priceSnatched;
    private Timestamp grabTime;
    private String note;

    public UserSnatchEnvelope(Long envelopeId, Long userId, Double priceSnatched,
                              Timestamp grabTime, String note) {
        this.envelopeId = envelopeId;
        this.userId = userId;
        this.priceSnatched = priceSnatched;
        this.grabTime = grabTime;
        this.note = note;
    }

    public UserSnatchEnvelope(){}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEnvelopeId() {
        return envelopeId;
    }

    public void setEnvelopeId(Long envelopeId) {
        this.envelopeId = envelopeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getPriceSnatched() {
        return priceSnatched;
    }

    public void setPriceSnatched(Double priceSnatched) {
        this.priceSnatched = priceSnatched;
    }

    public Timestamp getGrabTime() {
        return grabTime;
    }

    public void setGrabTime(Timestamp grabTime) {
        this.grabTime = grabTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

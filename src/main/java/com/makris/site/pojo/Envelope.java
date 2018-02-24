package com.makris.site.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

public class Envelope implements Serializable{
    private static final long serialVersionUID = 1049397724701962381L;

    private Long id;
    private Long userId;
    private Double totalPrice;      // 紅包總金額
    private Double unitPrice;       // 單位紅包金額
    private Timestamp sendDate;     // 發送時間
    private Integer totalAmount;    // 紅包總個量
    private Integer remainAmount;   // 紅包剩餘個數
    private Integer version;
    private String note;

    public Envelope(Long userId, Double totalPrice, Double unitPrice,
                    Timestamp sendDate, Integer totalAmount, Integer remainAmount,
                    Integer version, String note) {
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.unitPrice = unitPrice;
        this.sendDate = sendDate;
        this.totalAmount = totalAmount;
        this.remainAmount = remainAmount;
        this.version = version;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Timestamp getSendDate() {
        return sendDate;
    }

    public void setSendDate(Timestamp sendDate) {
        this.sendDate = sendDate;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(Integer remainAmount) {
        this.remainAmount = remainAmount;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

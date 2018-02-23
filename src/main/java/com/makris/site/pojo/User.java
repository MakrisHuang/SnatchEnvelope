package com.makris.site.pojo;

import java.util.List;

public class User {
    private String username;
    private String password;
    private Integer money;
    private Integer remainTimes;
    private List<Envelop> envelops;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getRemainTimes() {
        return remainTimes;
    }

    public void setRemainTimes(Integer remainTimes) {
        this.remainTimes = remainTimes;
    }

    public List<Envelop> getEnvelops() {
        return envelops;
    }

    public void setEnvelops(List<Envelop> envelops) {
        this.envelops = envelops;
    }
}

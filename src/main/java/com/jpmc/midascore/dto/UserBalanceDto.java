package com.jpmc.midascore.dto;

public class UserBalanceDto {
    private Long id;
    private String name;
    private float balance;

    public UserBalanceDto() {
    }

    public UserBalanceDto(Long id, String name, float balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}

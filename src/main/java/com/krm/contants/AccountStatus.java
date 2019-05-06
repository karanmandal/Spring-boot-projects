package com.krm.contants;

public enum AccountStatus {

    ACTIVATED("Activated"),
    DEACTIVATED("Deactivated"),
    NOT_VERIFIED("Not verified");

    private final String status;

    AccountStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

package com.awintech.transpo;

public class Note {

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCancelState() {
        return cancelState;
    }

    public void setCancelState(String cancelState) {
        this.cancelState = cancelState;
    }

    String from;
    String to;
    String companyName;
    String date;
    String cancelState;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    String transactionId;
}

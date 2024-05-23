package com.hust.ict.aims.entity.payment;

public class VnPay {
    private String paymentAccountName;
    private String getPaymentAccountNumber;
    private String bank;
    private String imageQrUrl;

    public VnPay(String paymentAccountName, String getPaymentAccountNumber, String bank, String imageQrUrl) {
        this.paymentAccountName = paymentAccountName;
        this.getPaymentAccountNumber = getPaymentAccountNumber;
        this.bank = bank;
        this.imageQrUrl = imageQrUrl;
    }

    public VnPay() {}

    public String getPaymentAccountName() {
        return paymentAccountName;
    }

    public void setPaymentAccountName(String paymentAccountName) {
        this.paymentAccountName = paymentAccountName;
    }

    public String getGetPaymentAccountNumber() {
        return getPaymentAccountNumber;
    }

    public void setGetPaymentAccountNumber(String getPaymentAccountNumber) {
        this.getPaymentAccountNumber = getPaymentAccountNumber;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getImageQrUrl() {
        return imageQrUrl;
    }

    public void setImageQrUrl(String imageQrUrl) {
        this.imageQrUrl = imageQrUrl;
    }
}

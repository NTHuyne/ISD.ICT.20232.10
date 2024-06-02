package com.hust.ict.aims.entity.payment;

import com.hust.ict.aims.entity.invoice.Invoice;

import java.math.BigInteger;
import java.util.Date;

public class PaymentTransaction {
    private BigInteger id;
    private Date createAt;
    private Date paymentTime;
    private String content;
    private VnPay vnpay;
    private String method;
    private String status;
    private Invoice invoice;

    public PaymentTransaction(BigInteger id, Date createAt, Date paymentTime, String content, VnPay vnpay, String method, String status, Invoice invoice) {
        this.setId(id);
        this.setCreateAt(createAt);
        this.setPaymentTime(paymentTime);
        this.setContent(content);
        this.setVnpay(vnpay);
        this.setMethod(method);
        this.setStatus(status);
        this.setInvoice(invoice);
    }

    public PaymentTransaction() {}

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public VnPay getVnpay() {
        return vnpay;
    }

    public void setVnpay(VnPay vnpay) {
        this.vnpay = vnpay;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}

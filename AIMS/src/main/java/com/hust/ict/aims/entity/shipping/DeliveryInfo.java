package com.hust.ict.aims.entity.shipping;

public class DeliveryInfo {
    private String name;
    private String phone;
    private String province;
    private String address;
    private String email;
    private String shippingInstructions;

    public DeliveryInfo (){
    }

    public DeliveryInfo(String name, String phone, String province, String address, String shippingInstructions) {
        this.setName(name);
        this.setPhone(phone);
        this.setProvince(province);
        this.setAddress(address);
        this.setShippingInstructions(shippingInstructions);
    }

    public DeliveryInfo(String name, String phone, String province, String address, String shippingInstructions, String email) {
        this.setName(name);
        this.setPhone(phone);
        this.setProvince(province);
        this.setAddress(address);
        this.setShippingInstructions(shippingInstructions);
        this.setEmail(email);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getShippingInstructions() {
        return shippingInstructions;
    }

    public void setShippingInstructions(String shippingInstructions) {
        this.shippingInstructions = shippingInstructions;
    }
}

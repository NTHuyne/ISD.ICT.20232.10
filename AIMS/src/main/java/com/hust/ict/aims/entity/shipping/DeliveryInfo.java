package com.hust.ict.aims.entity.shipping;

public class DeliveryInfo {
	private int deliveryId;
    private String name;
    private String phone;
    private String province;
    private String address;
    private String email;
    private String shippingInstructions;

    public DeliveryInfo(int deliveryId, String name, String phone, String province, String address, String email,
			String shippingInstructions) {
		super();
		this.deliveryId = deliveryId;
		this.name = name;
		this.phone = phone;
		this.province = province;
		this.address = address;
		this.email = email;
		this.shippingInstructions = shippingInstructions;
	}

	public DeliveryInfo(String name, String phone, String province, String address, String email,
			String shippingInstructions) {
		super();
		this.name = name;
		this.phone = phone;
		this.province = province;
		this.address = address;
		this.email = email;
		this.shippingInstructions = shippingInstructions;
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

	public int getDeliveryId() {
		return deliveryId;
	}
}

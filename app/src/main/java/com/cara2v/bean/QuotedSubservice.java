package com.cara2v.bean;

/**
 * Created by chiranjib on 18/1/18.
 */

public class QuotedSubservice {

    /**
     * id : 10
     * name : AMIT
     * parts : 50
     * price : 12
     */

    private String id;
    private String name;
    private String parts;
    private String price;
    private String flatPrice;
    private String flatRemark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParts() {
        return parts;
    }

    public void setParts(String parts) {
        this.parts = parts;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFlatPrice() {
        return flatPrice;
    }

    public void setFlatPrice(String flatPrice) {
        this.flatPrice = flatPrice;
    }

    public String getFlatRemark() {
        return flatRemark;
    }

    public void setFlatRemark(String flatRemark) {
        this.flatRemark = flatRemark;
    }
}

package com.cara2v.bean;

import java.util.ArrayList;

/**
 * Created by chiranjib on 17/1/18.
 */

public class QuotedServicesBean {
    private String name;
    private String id;
    private String serviceImage;
    private ArrayList<QuotedSubServiceBean> quotedSubServiceBeans;
    private boolean isVisible;

    public String getServiceImage() {
        return serviceImage;
    }

    public void setServiceImage(String serviceImage) {
        this.serviceImage = serviceImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<QuotedSubServiceBean> getQuotedSubServiceBeans() {
        return quotedSubServiceBeans;
    }

    public void setQuotedSubServiceBeans(ArrayList<QuotedSubServiceBean> quotedSubServiceBeans) {
        this.quotedSubServiceBeans = quotedSubServiceBeans;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

   /* public class QuotedSubServiceBean {
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
    }*/
}

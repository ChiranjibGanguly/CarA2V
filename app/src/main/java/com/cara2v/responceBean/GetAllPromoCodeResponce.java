package com.cara2v.responceBean;

import java.util.List;

/**
 * Created by chiranjib on 23/3/18.
 */

public class GetAllPromoCodeResponce {

    /**
     * api : 1
     * status : success
     * message : Promo Code List
     * data : [{"expire":"2018-03-24 07:00:00","amount":"25","title":"WYFCH","useCount":0,"name":"Offer","id":5}]
     */

    private String api;
    private String status;
    private String message;
    private List<DataBean> data;


    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * expire : 2018-03-24 07:00:00
         * amount : 25
         * title : WYFCH
         * useCount : 0
         * name : Offer
         * id : 5
         */

        private String expire;
        private String amount;
        private String title;
        private int useCount;
        private String name;
        private int id;

        public String getExpire() {
            return expire;
        }

        public void setExpire(String expire) {
            this.expire = expire;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getUseCount() {
            return useCount;
        }

        public void setUseCount(int useCount) {
            this.useCount = useCount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}

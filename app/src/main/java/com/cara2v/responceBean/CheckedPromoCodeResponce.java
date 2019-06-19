package com.cara2v.responceBean;

/**
 * Created by chiranjib on 31/3/18.
 */

public class CheckedPromoCodeResponce {

    /**
     * status : success
     * message : Promo code apply successfully..!
     * data : {"_id":74,"__v":0,"upd":"2018-03-31T11:05:21.242Z","crd":"2018-03-31T11:05:21.242Z","isShow":1,"status":1,"useCount":0,"amount":"20","expire":"2018-04-30","title":"BCDEFG","name":"extra off","userId":7}
     */

    private String status;
    private String message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * _id : 74
         * __v : 0
         * upd : 2018-03-31T11:05:21.242Z
         * crd : 2018-03-31T11:05:21.242Z
         * isShow : 1
         * status : 1
         * useCount : 0
         * amount : 20
         * expire : 2018-04-30
         * title : BCDEFG
         * name : extra off
         * userId : 7
         */

        private int _id;
        private int __v;
        private String upd;
        private String crd;
        private int isShow;
        private int status;
        private int useCount;
        private String amount;
        private String expire;
        private String title;
        private String name;
        private int userId;

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public String getUpd() {
            return upd;
        }

        public void setUpd(String upd) {
            this.upd = upd;
        }

        public String getCrd() {
            return crd;
        }

        public void setCrd(String crd) {
            this.crd = crd;
        }

        public int getIsShow() {
            return isShow;
        }

        public void setIsShow(int isShow) {
            this.isShow = isShow;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getUseCount() {
            return useCount;
        }

        public void setUseCount(int useCount) {
            this.useCount = useCount;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getExpire() {
            return expire;
        }

        public void setExpire(String expire) {
            this.expire = expire;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}

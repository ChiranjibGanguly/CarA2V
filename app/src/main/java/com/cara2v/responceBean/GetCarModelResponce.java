package com.cara2v.responceBean;

import java.util.List;

/**
 * Created by chiranjib on 22/12/17.
 */

public class GetCarModelResponce {

    /**
     * status : SUCCESS
     * message : Car model list
     * data : [{"_id":72,"carId":6,"carModelName":"Convertible","__v":0,"upd":"2017-12-15T06:58:39.289Z","crd":"2017-12-15T06:58:39.289Z","status":1},{"_id":73,"carId":6,"carModelName":"Coupe","__v":0,"upd":"2017-12-15T06:58:39.289Z","crd":"2017-12-15T06:58:39.289Z","status":1},{"_id":74,"carId":6,"carModelName":"Sedan","__v":0,"upd":"2017-12-15T06:58:39.289Z","crd":"2017-12-15T06:58:39.289Z","status":1}]
     */

    private String status;
    private String message;
    private List<DataBean> data;

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
         * _id : 72
         * carId : 6
         * carModelName : Convertible
         * __v : 0
         * upd : 2017-12-15T06:58:39.289Z
         * crd : 2017-12-15T06:58:39.289Z
         * status : 1
         */

        private int _id;
        private int carId;
        private String carModelName;
        private int __v;
        private String upd;
        private String crd;
        private int status;

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public int getCarId() {
            return carId;
        }

        public void setCarId(int carId) {
            this.carId = carId;
        }

        public String getCarModelName() {
            return carModelName;
        }

        public void setCarModelName(String carModelName) {
            this.carModelName = carModelName;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return carModelName;
        }
    }
}

package com.cara2v.responceBean;

import java.util.List;

/**
 * Created by chiranjib on 22/12/17.
 */

public class GetCarBrandResponce {

    /**
     * status : SUCCESS
     * message : Car list
     * data : [{"_id":1,"carName":"Acura","__v":0,"upd":"2017-12-15T06:58:14.607Z","crd":"2017-12-15T06:58:14.607Z","status":1},{"_id":2,"carName":"Alfa Romeo","__v":0,"upd":"2017-12-15T06:58:19.023Z","crd":"2017-12-15T06:58:19.023Z","status":1},{"_id":3,"carName":"AMC","__v":0,"upd":"2017-12-15T06:58:24.341Z","crd":"2017-12-15T06:58:24.341Z","status":1},{"_id":4,"carName":"Aston Martin","__v":0,"upd":"2017-12-15T06:58:29.015Z","crd":"2017-12-15T06:58:29.015Z","status":1},{"_id":5,"carName":"Audi","__v":0,"upd":"2017-12-15T06:58:34.261Z","crd":"2017-12-15T06:58:34.261Z","status":1},{"_id":6,"carName":"Avanti","__v":0,"upd":"2017-12-15T06:58:39.286Z","crd":"2017-12-15T06:58:39.286Z","status":1},{"_id":7,"carName":"Bentley","__v":0,"upd":"2017-12-15T06:58:44.044Z","crd":"2017-12-15T06:58:44.044Z","status":1},{"_id":8,"carName":"BMW","__v":0,"upd":"2017-12-15T07:00:19.537Z","crd":"2017-12-15T07:00:19.537Z","status":1},{"_id":9,"carName":"Buick","__v":0,"upd":"2017-12-15T07:00:24.929Z","crd":"2017-12-15T07:00:24.929Z","status":1},{"_id":10,"carName":"Hyundai","__v":0,"upd":"2017-12-15T07:00:29.018Z","crd":"2017-12-15T07:00:29.018Z","status":1},{"_id":11,"carName":"Cadillac","__v":0,"upd":"2017-12-15T07:00:34.908Z","crd":"2017-12-15T07:00:34.908Z","status":1},{"_id":12,"carName":"Isuzu","__v":0,"upd":"2017-12-15T07:00:39.024Z","crd":"2017-12-15T07:00:39.024Z","status":1},{"_id":13,"carName":"Chevrolet","__v":0,"upd":"2017-12-15T07:00:44.590Z","crd":"2017-12-15T07:00:44.590Z","status":1},{"_id":14,"carName":"Jeep","__v":0,"upd":"2017-12-15T07:00:49.027Z","crd":"2017-12-15T07:00:49.027Z","status":1},{"_id":15,"carName":"Chrysler","__v":0,"upd":"2017-12-15T07:02:25.537Z","crd":"2017-12-15T07:02:25.537Z","status":1},{"_id":16,"carName":"Plymouth","__v":0,"upd":"2017-12-15T07:02:29.030Z","crd":"2017-12-15T07:02:29.030Z","status":1},{"_id":17,"carName":"Daewoo","__v":0,"upd":"2017-12-15T07:02:35.454Z","crd":"2017-12-15T07:02:35.454Z","status":1},{"_id":18,"carName":"Porsche","__v":0,"upd":"2017-12-15T07:02:39.030Z","crd":"2017-12-15T07:02:39.030Z","status":1},{"_id":19,"carName":"Daihatsu","__v":0,"upd":"2017-12-15T07:02:45.122Z","crd":"2017-12-15T07:02:45.122Z","status":1},{"_id":20,"carName":"Renault","__v":0,"upd":"2017-12-15T07:02:49.033Z","crd":"2017-12-15T07:02:49.033Z","status":1},{"_id":21,"carName":"Sterling","__v":0,"upd":"2017-12-15T07:03:24.053Z","crd":"2017-12-15T07:03:24.053Z","status":1},{"_id":22,"carName":"Datsun","__v":0,"upd":"2017-12-15T07:04:40.688Z","crd":"2017-12-15T07:04:40.688Z","status":1},{"_id":23,"carName":"DeLorean","__v":0,"upd":"2017-12-15T07:04:40.949Z","crd":"2017-12-15T07:04:40.949Z","status":1},{"_id":24,"carName":"Dodge","__v":0,"upd":"2017-12-15T07:04:41.590Z","crd":"2017-12-15T07:04:41.590Z","status":1},{"_id":25,"carName":"Eagle","__v":0,"upd":"2017-12-15T07:04:45.651Z","crd":"2017-12-15T07:04:45.651Z","status":1},{"_id":26,"carName":"GMC","__v":0,"upd":"2017-12-15T07:06:45.711Z","crd":"2017-12-15T07:06:45.711Z","status":1},{"_id":27,"carName":"Kia","__v":0,"upd":"2017-12-15T07:07:25.734Z","crd":"2017-12-15T07:07:25.734Z","status":1},{"_id":28,"carName":"Fisker","__v":0,"upd":"2017-12-15T07:08:05.869Z","crd":"2017-12-15T07:08:05.869Z","status":1},{"_id":29,"carName":"FIAT","__v":0,"upd":"2017-12-15T07:08:05.894Z","crd":"2017-12-15T07:08:05.894Z","status":1},{"_id":30,"carName":"Ferrari","__v":0,"upd":"2017-12-15T07:08:06.044Z","crd":"2017-12-15T07:08:06.044Z","status":1},{"_id":31,"carName":"Honda","__v":0,"upd":"2017-12-15T07:10:09.905Z","crd":"2017-12-15T07:10:09.905Z","status":1},{"_id":32,"carName":"HUMMER","__v":0,"upd":"2017-12-15T07:10:10.018Z","crd":"2017-12-15T07:10:10.018Z","status":1},{"_id":33,"carName":"Infiniti","__v":0,"upd":"2017-12-15T07:12:23.826Z","crd":"2017-12-15T07:12:23.826Z","status":1},{"_id":34,"carName":"Jaguar","__v":0,"upd":"2017-12-15T07:14:11.235Z","crd":"2017-12-15T07:14:11.235Z","status":1},{"_id":35,"carName":"Lancia","__v":0,"upd":"2017-12-15T07:16:11.918Z","crd":"2017-12-15T07:16:11.918Z","status":1},{"_id":36,"carName":"Lamborghini","__v":0,"upd":"2017-12-15T07:16:12.039Z","crd":"2017-12-15T07:16:12.039Z","status":1},{"_id":37,"carName":"Land Rover","__v":0,"upd":"2017-12-15T07:16:13.004Z","crd":"2017-12-15T07:16:13.004Z","status":1},{"_id":38,"carName":"Lexus","__v":0,"upd":"2017-12-15T07:16:13.239Z","crd":"2017-12-15T07:16:13.239Z","status":1},{"_id":39,"carName":"Lincoln","__v":0,"upd":"2017-12-15T07:16:24.827Z","crd":"2017-12-15T07:16:24.827Z","status":1},{"_id":40,"carName":"Lotus","__v":0,"upd":"2017-12-15T07:16:25.108Z","crd":"2017-12-15T07:16:25.108Z","status":1},{"_id":41,"carName":"Maserati","__v":0,"upd":"2017-12-15T07:18:12.405Z","crd":"2017-12-15T07:18:12.405Z","status":1},{"_id":42,"carName":"Maybach","__v":0,"upd":"2017-12-15T07:18:12.690Z","crd":"2017-12-15T07:18:12.690Z","status":1},{"_id":43,"carName":"Mazda","__v":0,"upd":"2017-12-15T07:18:13.259Z","crd":"2017-12-15T07:18:13.259Z","status":1},{"_id":44,"carName":"McLaren","__v":0,"upd":"2017-12-15T07:18:13.731Z","crd":"2017-12-15T07:18:13.731Z","status":1},{"_id":45,"carName":"Mercedes-Benz","__v":0,"upd":"2017-12-15T07:18:25.561Z","crd":"2017-12-15T07:18:25.561Z","status":1},{"_id":46,"carName":"Mercury","__v":0,"upd":"2017-12-15T07:18:25.703Z","crd":"2017-12-15T07:18:25.703Z","status":1},{"_id":47,"carName":"Merkur","__v":0,"upd":"2017-12-15T07:20:12.993Z","crd":"2017-12-15T07:20:12.993Z","status":1},{"_id":48,"carName":"MINI","__v":0,"upd":"2017-12-15T07:20:13.247Z","crd":"2017-12-15T07:20:13.247Z","status":1},{"_id":49,"carName":"Mitsubishi","__v":0,"upd":"2017-12-15T07:20:13.786Z","crd":"2017-12-15T07:20:13.786Z","status":1},{"_id":50,"carName":"Nissan","__v":0,"upd":"2017-12-15T07:20:14.272Z","crd":"2017-12-15T07:20:14.272Z","status":1},{"_id":51,"carName":"Oldsmobile","__v":0,"upd":"2017-12-15T07:20:26.050Z","crd":"2017-12-15T07:20:26.050Z","status":1},{"_id":52,"carName":"Peugeot","__v":0,"upd":"2017-12-15T07:20:26.305Z","crd":"2017-12-15T07:20:26.305Z","status":1},{"_id":53,"carName":"Pontiac","__v":0,"upd":"2017-12-15T07:22:14.071Z","crd":"2017-12-15T07:22:14.071Z","status":1},{"_id":54,"carName":"RAM","__v":0,"upd":"2017-12-15T07:22:26.595Z","crd":"2017-12-15T07:22:26.595Z","status":1},{"_id":55,"carName":"Rolls-Royce","__v":0,"upd":"2017-12-15T07:24:14.036Z","crd":"2017-12-15T07:24:14.036Z","status":1},{"_id":56,"carName":"Saab","__v":0,"upd":"2017-12-15T07:24:14.318Z","crd":"2017-12-15T07:24:14.318Z","status":1},{"_id":57,"carName":"Saturn","__v":0,"upd":"2017-12-15T07:24:14.750Z","crd":"2017-12-15T07:24:14.750Z","status":1},{"_id":58,"carName":"Scion","__v":0,"upd":"2017-12-15T07:24:15.051Z","crd":"2017-12-15T07:24:15.051Z","status":1},{"_id":59,"carName":"smart","__v":0,"upd":"2017-12-15T07:24:27.223Z","crd":"2017-12-15T07:24:27.223Z","status":1},{"_id":60,"carName":"SRT","__v":0,"upd":"2017-12-15T07:26:14.293Z","crd":"2017-12-15T07:26:14.293Z","status":1},{"_id":61,"carName":"Subaru","__v":0,"upd":"2017-12-15T07:26:15.026Z","crd":"2017-12-15T07:26:15.026Z","status":1},{"_id":62,"carName":"Suzuki","__v":0,"upd":"2017-12-15T07:26:15.264Z","crd":"2017-12-15T07:26:15.264Z","status":1},{"_id":63,"carName":"Tesla","__v":0,"upd":"2017-12-15T07:26:15.587Z","crd":"2017-12-15T07:26:15.587Z","status":1},{"_id":64,"carName":"Triumph","__v":0,"upd":"2017-12-15T07:26:28.022Z","crd":"2017-12-15T07:26:28.022Z","status":1},{"_id":65,"carName":"Toyota","__v":0,"upd":"2017-12-15T07:26:28.765Z","crd":"2017-12-15T07:26:28.765Z","status":1},{"_id":66,"carName":"Volkswagen","__v":0,"upd":"2017-12-15T07:28:14.551Z","crd":"2017-12-15T07:28:14.551Z","status":1},{"_id":67,"carName":"Volvo","__v":0,"upd":"2017-12-15T07:28:15.841Z","crd":"2017-12-15T07:28:15.841Z","status":1},{"_id":68,"carName":"Yugo","__v":0,"upd":"2017-12-15T07:28:16.140Z","crd":"2017-12-15T07:28:16.140Z","status":1},{"_id":69,"carName":"Ford","__v":0,"upd":"2017-12-15T11:28:11.947Z","crd":"2017-12-15T11:28:11.947Z","status":1},{"_id":70,"carName":"Freightliner","__v":0,"upd":"2017-12-15T11:28:18.668Z","crd":"2017-12-15T11:28:18.668Z","status":1},{"_id":71,"carName":"Geo","__v":0,"upd":"2017-12-15T11:28:21.949Z","crd":"2017-12-15T11:28:21.949Z","status":1}]
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
         * _id : 1
         * carName : Acura
         * __v : 0
         * upd : 2017-12-15T06:58:14.607Z
         * crd : 2017-12-15T06:58:14.607Z
         * status : 1
         */

        private int _id;
        private String carName;
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

        public String getCarName() {
            return carName;
        }

        public void setCarName(String carName) {
            this.carName = carName;
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
            return carName;
        }
    }
}

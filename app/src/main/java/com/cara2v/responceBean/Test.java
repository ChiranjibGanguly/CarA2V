package com.cara2v.responceBean;

import java.util.List;

/**
 * Created by chiranjib on 16/1/18.
 */

public class Test {

    /**
     * status : success
     * message : My Car list
     * data : [{"_id":2,"userId":4,"brand":{"brandId":"5","brandName":"Audi"},"model":{"modelId":"52","modelName":"allroad"},"__v":0,"upd":"2018-03-05T06:16:19.329Z","crd":"2018-03-05T06:16:19.329Z","status":1,"myCarImage":"108.179.225.88:2405/img/carImage/15202305521231520230513511.jpg,108.179.225.88:2405/img/carImage/15202305646511520230536147.jpg","mileage":"25.50","fuelType":"Diesel","carType":"Hatchback","carYear":"2011","plateNumberStatus":"1","VINCodeStatus":"1","plateNumber":"mp454u","VINCode":"DFHFGHH","carName":"carA2v"},{"_id":5,"userId":4,"brand":{"brandId":"10","brandName":"Hyundai"},"model":{"modelId":"197","modelName":"Accent"},"__v":0,"upd":"2018-03-05T10:15:55.242Z","crd":"2018-03-05T10:15:55.242Z","status":1,"myCarImage":"108.179.225.88:2405/img/myImage/cara2vDemoImage.png","mileage":"1000","fuelType":"Diesel","carType":"Hatchback","carYear":"2010","plateNumberStatus":"1","VINCodeStatus":"1","plateNumber":"wtgg","VINCode":"QWERTYUI","carName":"carA2v"},{"_id":6,"userId":4,"brand":{"brandId":"8","brandName":"BMW"},"model":{"modelId":"89","modelName":"318ti"},"__v":0,"upd":"2018-03-05T12:40:28.186Z","crd":"2018-03-05T12:40:28.186Z","status":1,"myCarImage":"108.179.225.88:2405/img/myImage/cara2vDemoImage.png","mileage":"10000","fuelType":"Diesel","carType":"Hatchback","carYear":"2017","plateNumberStatus":"1","VINCodeStatus":"1","plateNumber":"tt55","VINCode":"YY55","carName":"carA2v"},{"_id":95,"userId":4,"brand":{"brandId":"26","brandName":"GMC"},"model":{"modelId":"457","modelName":"Jimmy"},"__v":0,"upd":"2018-04-02T09:58:02.715Z","crd":"2018-04-02T09:58:02.715Z","status":1,"myCarImage":"108.179.225.88:2405/img/carImage/15226630802181522663044499.jpg,108.179.225.88:2405/img/carImage/15226630825301522663062426.jpg","mileage":"123","fuelType":"Diesel","carType":"Hatchback","carYear":"2015","plateNumberStatus":"1","VINCodeStatus":"1","plateNumber":"qwerty","VINCode":"QAFJKLUFHHCGHVDTG","carName":"carA2v"},{"_id":98,"userId":4,"brand":{"brandId":"59","brandName":"smart"},"model":{"modelId":"1062","modelName":"fortwo"},"__v":0,"upd":"2018-04-04T13:26:45.029Z","crd":"2018-04-04T13:26:45.029Z","status":1,"myCarImage":"108.179.225.88:2405/img/carImage/15228484006221522848395254.jpg","mileage":"12","fuelType":"Gasoline","carType":"Hatchback","carYear":"2000","plateNumberStatus":"0","VINCodeStatus":"0","plateNumber":"aishwary","VINCode":"AISHWARYSINGHRATH","carName":"carA2v"},{"_id":109,"userId":4,"brand":{"brandId":"2","brandName":"Alfa Romeo"},"model":{"modelId":"21","modelName":"164"},"__v":0,"upd":"2018-04-24T08:20:39.147Z","crd":"2018-04-24T08:20:39.147Z","status":1,"myCarImage":"108.179.225.88:2405/img/myImage/cara2vDemoImage.png","mileage":"25","fuelType":"Diesel","carType":"Sedan","carYear":"2016","plateNumberStatus":"1","VINCodeStatus":"0","plateNumber":"","VINCode":"36464664477575855","carName":"carA2v"}]
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
         * _id : 2
         * userId : 4
         * brand : {"brandId":"5","brandName":"Audi"}
         * model : {"modelId":"52","modelName":"allroad"}
         * __v : 0
         * upd : 2018-03-05T06:16:19.329Z
         * crd : 2018-03-05T06:16:19.329Z
         * status : 1
         * myCarImage : 108.179.225.88:2405/img/carImage/15202305521231520230513511.jpg,108.179.225.88:2405/img/carImage/15202305646511520230536147.jpg
         * mileage : 25.50
         * fuelType : Diesel
         * carType : Hatchback
         * carYear : 2011
         * plateNumberStatus : 1
         * VINCodeStatus : 1
         * plateNumber : mp454u
         * VINCode : DFHFGHH
         * carName : carA2v
         */

        private int _id;
        private int userId;
        private BrandBean brand;
        private ModelBean model;
        private int __v;
        private String upd;
        private String crd;
        private int status;
        private String myCarImage;
        private String mileage;
        private String fuelType;
        private String carType;
        private String carYear;
        private String plateNumberStatus;
        private String VINCodeStatus;
        private String plateNumber;
        private String VINCode;
        private String carName;

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public BrandBean getBrand() {
            return brand;
        }

        public void setBrand(BrandBean brand) {
            this.brand = brand;
        }

        public ModelBean getModel() {
            return model;
        }

        public void setModel(ModelBean model) {
            this.model = model;
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

        public String getMyCarImage() {
            return myCarImage;
        }

        public void setMyCarImage(String myCarImage) {
            this.myCarImage = myCarImage;
        }

        public String getMileage() {
            return mileage;
        }

        public void setMileage(String mileage) {
            this.mileage = mileage;
        }

        public String getFuelType() {
            return fuelType;
        }

        public void setFuelType(String fuelType) {
            this.fuelType = fuelType;
        }

        public String getCarType() {
            return carType;
        }

        public void setCarType(String carType) {
            this.carType = carType;
        }

        public String getCarYear() {
            return carYear;
        }

        public void setCarYear(String carYear) {
            this.carYear = carYear;
        }

        public String getPlateNumberStatus() {
            return plateNumberStatus;
        }

        public void setPlateNumberStatus(String plateNumberStatus) {
            this.plateNumberStatus = plateNumberStatus;
        }

        public String getVINCodeStatus() {
            return VINCodeStatus;
        }

        public void setVINCodeStatus(String VINCodeStatus) {
            this.VINCodeStatus = VINCodeStatus;
        }

        public String getPlateNumber() {
            return plateNumber;
        }

        public void setPlateNumber(String plateNumber) {
            this.plateNumber = plateNumber;
        }

        public String getVINCode() {
            return VINCode;
        }

        public void setVINCode(String VINCode) {
            this.VINCode = VINCode;
        }

        public String getCarName() {
            return carName;
        }

        public void setCarName(String carName) {
            this.carName = carName;
        }

        public static class BrandBean {
            /**
             * brandId : 5
             * brandName : Audi
             */

            private String brandId;
            private String brandName;

            public String getBrandId() {
                return brandId;
            }

            public void setBrandId(String brandId) {
                this.brandId = brandId;
            }

            public String getBrandName() {
                return brandName;
            }

            public void setBrandName(String brandName) {
                this.brandName = brandName;
            }
        }

        public static class ModelBean {
            /**
             * modelId : 52
             * modelName : allroad
             */

            private String modelId;
            private String modelName;

            public String getModelId() {
                return modelId;
            }

            public void setModelId(String modelId) {
                this.modelId = modelId;
            }

            public String getModelName() {
                return modelName;
            }

            public void setModelName(String modelName) {
                this.modelName = modelName;
            }
        }
    }
}

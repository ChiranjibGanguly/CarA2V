package com.cara2v.responceBean;

import java.util.List;

/**
 * Created by chiranjib on 20/12/17.
 */

public class SignInRespoce {

    /**
     * api : 1
     * status : success
     * message : Login Successfully
     * data : {"_id":8,"userType":2,"addressLatLong":[23.0123,74.7966],"locationLatLong":[23.0123,74.7966],"__v":0,"upd":"2018-01-03T08:38:26.550Z","crd":"2018-01-03T08:38:26.550Z","status":1,"userService":"","fireBaseId":"","deviceType":1,"deviceToken":"dddaaaadaaaaaaaaaaaaaaaaaaa","socialType":"","socialId":"","location":"thandla","address":"thandla","authToken":"hRo8krxABRrhN4P85jNh","businessName":"","profileImage":"","phoneNumber":"1111111111","countryCode":"","password":"36267214ec619cd99aa9d83c3a487139","email":"aish@gmail.com","userName":"raana"}
     */

    private String api;
    private String status;
    private String message;
    private DataBean data;
    private List<MyCarBean> myCar;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public List<MyCarBean> getMyCar() {
        return myCar;
    }

    public void setMyCar(List<MyCarBean> myCar) {
        this.myCar = myCar;
    }

    public static class DataBean {
        /**
         * _id : 8
         * userType : 2
         * addressLatLong : [23.0123,74.7966]
         * locationLatLong : [23.0123,74.7966]
         * __v : 0
         * upd : 2018-01-03T08:38:26.550Z
         * crd : 2018-01-03T08:38:26.550Z
         * status : 1
         * userService :
         * fireBaseId :
         * licenseStatus :2
         * licenseImage :
         * fireBaseId :
         * deviceType : 1
         * deviceToken : dddaaaadaaaaaaaaaaaaaaaaaaa
         * socialType :
         * socialId :
         * location : thandla
         * address : thandla
         * authToken : hRo8krxABRrhN4P85jNh
         * businessName :
         * profileImage :
         * phoneNumber : 1111111111
         * countryCode :
         * password : 36267214ec619cd99aa9d83c3a487139
         * email : aish@gmail.com
         * userName : raana
         */

        private int _id;
        private int userType;
        private int __v;
        private String upd;
        private String crd;
        private int status;
        private String userService;
        private String fireBaseId;
        private int deviceType;
        private String deviceToken;
        private String socialType;
        private String socialId;
        private String location;
        private String address;
        private String authToken;
        private String businessName;
        private String profileImage;
        private String phoneNumber;
        private String countryCode;
        private String password;
        private String email;
        private String userName;
        private String licenseImage;
        private int licenseStatus;
        private int accountAdd;
        private List<Double> addressLatLong;
        private List<Double> locationLatLong;

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
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

        public String getUserService() {
            return userService;
        }

        public void setUserService(String userService) {
            this.userService = userService;
        }

        public String getFireBaseId() {
            return fireBaseId;
        }

        public void setFireBaseId(String fireBaseId) {
            this.fireBaseId = fireBaseId;
        }

        public int getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(int deviceType) {
            this.deviceType = deviceType;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public String getSocialType() {
            return socialType;
        }

        public void setSocialType(String socialType) {
            this.socialType = socialType;
        }

        public String getSocialId() {
            return socialId;
        }

        public void setSocialId(String socialId) {
            this.socialId = socialId;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAuthToken() {
            return authToken;
        }

        public void setAuthToken(String authToken) {
            this.authToken = authToken;
        }

        public String getBusinessName() {
            return businessName;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getLicenseImage() {
            return licenseImage;
        }

        public void setLicenseImage(String licenseImage) {
            this.licenseImage = licenseImage;
        }

        public int getLicenseStatus() {
            return licenseStatus;
        }

        public void setLicenseStatus(int licenseStatus) {
            this.licenseStatus = licenseStatus;
        }

        public List<Double> getAddressLatLong() {
            return addressLatLong;
        }

        public void setAddressLatLong(List<Double> addressLatLong) {
            this.addressLatLong = addressLatLong;
        }

        public List<Double> getLocationLatLong() {
            return locationLatLong;
        }

        public void setLocationLatLong(List<Double> locationLatLong) {
            this.locationLatLong = locationLatLong;
        }

        public int getAccountAdd() {
            return accountAdd;
        }

        public void setAccountAdd(int accountAdd) {
            this.accountAdd = accountAdd;
        }

    }

    public static class MyCarBean {
        /**
         * _id : 2
         * userId : 4
         * brand : {"brandId":"69","brandName":"Ford"}
         * model : {"modelId":"1193","modelName":"E-250 and Econoline 250"}
         * __v : 0
         * upd : 2018-01-22T09:12:52.920Z
         * crd : 2018-01-22T09:12:52.920Z
         * status : 1
         * myCarImage : 108.179.225.88:2405/img/carImage/15166123544041516612318998.jpg,108.179.225.88:2405/img/carImage/15166123623941516612349564.jpg
         * mileage : 25.96
         * fuelType : Diesel
         * carYear : 2014
         * carType : Hatchback
         * plateNumberStatus : 0
         * VINCodeStatus : 0
         * plateNumber : mp 45 bb 4545
         * VINCode : GHJEIHVBDKSKBJGG
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
        private String carYear;
        private String carType;
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

        public String getCarYear() {
            return carYear;
        }

        public void setCarYear(String carYear) {
            this.carYear = carYear;
        }

        public static class BrandBean {
            /**
             * brandId : 69
             * brandName : Ford
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
             * modelId : 1193
             * modelName : E-250 and Econoline 250
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

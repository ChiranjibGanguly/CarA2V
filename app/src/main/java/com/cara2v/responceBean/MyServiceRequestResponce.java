package com.cara2v.responceBean;

import java.util.List;

/**
 * Created by chiranjib on 10/1/18.
 */

public class MyServiceRequestResponce {

    /**
     * status : success
     * message : Service Request List
     * data : [{"_id":1,"car":{"carId":"6","carName":"Acura "},"addressLatLong":[22.7093523,75.9014392],"__v":0,"upd":"2018-01-10T06:13:07.696Z","crd":"2018-01-10T06:13:07.696Z","status":1,"acceptUser":"","usersRequest":"","notifyUser":"","SearchService":"1,2,3","address":"Pipliyahana, Indore, Madhya Pradesh, India","description":"of our website for your help on a 42","service":[{"id":"1","name":"Auto Body Shop ","subService":[{"id":"9","name":" Auto Body Repair"},{"id":"54","name":" Unibody Repair"},{"id":"57","name":"Spray Truck Bed Lining"}]},{"id":"2","name":"Auto Detailing","subService":[{"id":"30","name":" Paint Sealant & Protection"},{"id":"34","name":"Engine  Compartment Detailing"},{"id":"36","name":"Dirt / Pet Hair Removal"}]},{"id":"3","name":"Auto Glass ","subService":[{"id":"2","name":"Catalytic Converter Replacement"},{"id":"5","name":"Exhaust System Replacement"},{"id":"12","name":"Front glass"}]}],"dateAndTime":"2018-01-12T17:41:00.000Z","userId":7}]
     */

    private String status;
    private String message;
    private List<DataBean> data;
    private int licenseStatus;

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

    public int getLicenseStatus() {
        return licenseStatus;
    }

    public void setLicenseStatus(int licenseStatus) {
        this.licenseStatus = licenseStatus;
    }

    public static class DataBean {
        /**
         * _id : 1
         * car : {"carId":"6","carName":"Acura "}
         * addressLatLong : [22.7093523,75.9014392]
         * __v : 0
         * upd : 2018-01-10T06:13:07.696Z
         * crd : 2018-01-10T06:13:07.696Z
         * status : 1
         * acceptUser :
         * usersRequest :
         * notifyUser :
         * SearchService : 1,2,3
         * address : Pipliyahana, Indore, Madhya Pradesh, India
         * description : of our website for your help on a 42
         * service : [{"id":"1","name":"Auto Body Shop ","subService":[{"id":"9","name":" Auto Body Repair"},{"id":"54","name":" Unibody Repair"},{"id":"57","name":"Spray Truck Bed Lining"}]},{"id":"2","name":"Auto Detailing","subService":[{"id":"30","name":" Paint Sealant & Protection"},{"id":"34","name":"Engine  Compartment Detailing"},{"id":"36","name":"Dirt / Pet Hair Removal"}]},{"id":"3","name":"Auto Glass ","subService":[{"id":"2","name":"Catalytic Converter Replacement"},{"id":"5","name":"Exhaust System Replacement"},{"id":"12","name":"Front glass"}]}]
         * dateAndTime : 2018-01-12T17:41:00.000Z
         * userId : 7
         * myRequest : 1
         */

        private int _id;
        private CarBean car;
        private int __v;
        private String upd;
        private String crd;
        private int status;
        private String acceptUser;
        private String usersRequest;
        private String SearchService;
        private String address;
        private String description;
        private String dateAndTime;
        private String timeSlot;
        private int userId;
        private int myRequest;
        private List<Double> addressLatLong;
        private List<ServiceBean> service;
        private boolean isSwipe;

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public CarBean getCar() {
            return car;
        }

        public void setCar(CarBean car) {
            this.car = car;
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

        public String getAcceptUser() {
            return acceptUser;
        }

        public void setAcceptUser(String acceptUser) {
            this.acceptUser = acceptUser;
        }

        public String getUsersRequest() {
            return usersRequest;
        }

        public void setUsersRequest(String usersRequest) {
            this.usersRequest = usersRequest;
        }

        public String getSearchService() {
            return SearchService;
        }

        public void setSearchService(String SearchService) {
            this.SearchService = SearchService;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDateAndTime() {
            return dateAndTime;
        }

        public void setDateAndTime(String dateAndTime) {
            this.dateAndTime = dateAndTime;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getMyRequest() {
            return myRequest;
        }

        public void setMyRequest(int myRequest) {
            this.myRequest = myRequest;
        }

        public List<Double> getAddressLatLong() {
            return addressLatLong;
        }

        public void setAddressLatLong(List<Double> addressLatLong) {
            this.addressLatLong = addressLatLong;
        }

        public List<ServiceBean> getService() {
            return service;
        }

        public void setService(List<ServiceBean> service) {
            this.service = service;
        }

        public boolean isSwipe() {
            return isSwipe;
        }

        public void setSwipe(boolean swipe) {
            isSwipe = swipe;
        }

        public String getTimeSlot() {
            return timeSlot;
        }

        public void setTimeSlot(String timeSlot) {
            this.timeSlot = timeSlot;
        }

        public static class CarBean {

            /**
             * carYear : 2017
             * fuelType : Diesel
             * carType : carType
             * plateNumberStatus : 1
             * VINCodeStatus : 1
             * plateNumber : tt55
             * VINCode : YY55
             * modelName : 318ti
             * carId : 6
             * carName : Acura
             */
            private String carYear;
            private String fuelType;
            private String carType;
            private String plateNumberStatus;
            private String VINCodeStatus;
            private String plateNumber;
            private String VINCode;
            private String modelName;
            private String carId;
            private String carName;

            public String getCarId() {
                return carId;
            }

            public void setCarId(String carId) {
                this.carId = carId;
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

            public String getModelName() {
                return modelName;
            }

            public void setModelName(String modelName) {
                this.modelName = modelName;
            }
        }

        public static class ServiceBean {
            /**
             * id : 1
             * name : Auto Body Shop
             * subService : [{"id":"9","name":" Auto Body Repair"},{"id":"54","name":" Unibody Repair"},{"id":"57","name":"Spray Truck Bed Lining"}]
             */

            private String id;
            private String name;
            private List<SubServiceBean> subService;

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

            public List<SubServiceBean> getSubService() {
                return subService;
            }

            public void setSubService(List<SubServiceBean> subService) {
                this.subService = subService;
            }

            public static class SubServiceBean {
                /**
                 * id : 9
                 * name :  Auto Body Repair
                 */

                private String id;
                private String name;

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

                @Override
                public String toString() {
                    return name;
                }
            }
        }
    }
}

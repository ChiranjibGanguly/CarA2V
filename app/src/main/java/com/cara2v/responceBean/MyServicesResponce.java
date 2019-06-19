package com.cara2v.responceBean;

import java.util.List;

/**
 * Created by chiranjib on 30/1/18.
 */

public class MyServicesResponce {

    /**
     * status : success
     * message : Service Request List
     * data : [{"_id":28,"car":{"fuelType":"Gasoline","carType":"SUV","plateNumberStatus":"1","VINCodeStatus":"1","plateNumber":"2465DGV","VINCode":"246YIGDGJNVXV","carName":"Aston Martin","carId":20},"addressLatLong":[22.718937,75.884408],"__v":0,"upd":"2018-01-30T09:29:09.031Z","crd":"2018-01-30T09:29:09.031Z","changeStatusDate":{"Ask":"Wait","End":"Wait","Almost":"Wait","Progress":"Wait","Start":"30 Jan, 07:34 am","Confirm":"30 Jan, 07:33 am"},"status":3,"acceptUser":30,"usersRequest":"6","notifyUser":"30","SearchService":"9","address":"Manorama Ganj, Indore, Madhya Pradesh 452001, India","description":"sis","service":[{"id":"9","name":"Auto Service ","subService":[{"id":"21","name":"Oil change, Filter & lube ( including fluid level check  )"},{"id":"22","name":"Synthetic Oil & Filter Change  ( including fluid level check  )"},{"id":"23","name":" Wheel Alignment"},{"id":"24","name":"Air Conditioning and Heating"}]}],"dateAndTime":"2018-02-01T08:50:00.000Z","userId":31}]
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
         * _id : 28
         * car : {"fuelType":"Gasoline","carType":"SUV","plateNumberStatus":"1","VINCodeStatus":"1","plateNumber":"2465DGV","VINCode":"246YIGDGJNVXV","carName":"Aston Martin","carId":20}
         * addressLatLong : [22.718937,75.884408]
         * __v : 0
         * upd : 2018-01-30T09:29:09.031Z
         * crd : 2018-01-30T09:29:09.031Z
         * changeStatusDate : {"Ask":"Wait","End":"Wait","Almost":"Wait","Progress":"Wait","Start":"30 Jan, 07:34 am","Confirm":"30 Jan, 07:33 am"}
         * status : 3
         * acceptUser : 30
         * usersRequest : 6
         * notifyUser : 30
         * SearchService : 9
         * address : Manorama Ganj, Indore, Madhya Pradesh 452001, India
         * description : sis
         * service : [{"id":"9","name":"Auto Service ","subService":[{"id":"21","name":"Oil change, Filter & lube ( including fluid level check  )"},{"id":"22","name":"Synthetic Oil & Filter Change  ( including fluid level check  )"},{"id":"23","name":" Wheel Alignment"},{"id":"24","name":"Air Conditioning and Heating"}]}]
         * dateAndTime : 2018-02-01T08:50:00.000Z
         * userId : 31
         */

        private int _id;
        private CarBean car;
        private int __v;
        private String upd;
        private String crd;
        private ChangeStatusDateBean changeStatusDate;
        private int status;
        private int acceptUser;
        private String usersRequest;
       // private String notifyUser;
        private String SearchService;
        private String address;
        private String description;
        private String dateAndTime;
        private String timeSlot;
        private int userId;
        private List<Double> addressLatLong;
        private List<ServiceBean> service;
        private List<InvoiceBean> invoice;

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

        public ChangeStatusDateBean getChangeStatusDate() {
            return changeStatusDate;
        }

        public void setChangeStatusDate(ChangeStatusDateBean changeStatusDate) {
            this.changeStatusDate = changeStatusDate;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getAcceptUser() {
            return acceptUser;
        }

        public void setAcceptUser(int acceptUser) {
            this.acceptUser = acceptUser;
        }

        public String getUsersRequest() {
            return usersRequest;
        }

        public void setUsersRequest(String usersRequest) {
            this.usersRequest = usersRequest;
        }

        /*public String getNotifyUser() {
            return notifyUser;
        }

        public void setNotifyUser(String notifyUser) {
            this.notifyUser = notifyUser;
        }*/

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

        public List<InvoiceBean> getInvoice() {
            return invoice;
        }

        public void setInvoice(List<InvoiceBean> invoice) {
            this.invoice = invoice;
        }

        public String getTimeSlot() {
            return timeSlot;
        }

        public void setTimeSlot(String timeSlot) {
            this.timeSlot = timeSlot;
        }

        public static class CarBean {
            /**
             * carYear : 2011
             * fuelType : Gasoline
             * carType : SUV
             * plateNumberStatus : 1
             * VINCodeStatus : 1
             * plateNumber : 2465DGV
             * VINCode : 246YIGDGJNVXV
             * carName : Aston Martin
             * modelName : allroad
             * carId : 20
             */
            private String carYear;
            private String fuelType;
            private String carType;
            private String plateNumberStatus;
            private String VINCodeStatus;
            private String plateNumber;
            private String VINCode;
            private String carName;
            private String modelName;
            private int carId;

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

            public int getCarId() {
                return carId;
            }

            public void setCarId(int carId) {
                this.carId = carId;
            }

            public String getCarYear() {
                return carYear;
            }

            public void setCarYear(String carYear) {
                this.carYear = carYear;
            }

            public String getModelName() {
                return modelName;
            }

            public void setModelName(String modelName) {
                this.modelName = modelName;
            }
        }

        public static class ChangeStatusDateBean {
            /**
             * Ask : Wait
             * End : Wait
             * Almost : Wait
             * Progress : Wait
             * Start : 30 Jan, 07:34 am
             * Confirm : 30 Jan, 07:33 am
             */

            private String Ask;
            private String End;
            private String Almost;
            private String Progress;
            private String Start;
            private String Confirm;

            public String getAsk() {
                return Ask;
            }

            public void setAsk(String Ask) {
                this.Ask = Ask;
            }

            public String getEnd() {
                return End;
            }

            public void setEnd(String End) {
                this.End = End;
            }

            public String getAlmost() {
                return Almost;
            }

            public void setAlmost(String Almost) {
                this.Almost = Almost;
            }

            public String getProgress() {
                return Progress;
            }

            public void setProgress(String Progress) {
                this.Progress = Progress;
            }

            public String getStart() {
                return Start;
            }

            public void setStart(String Start) {
                this.Start = Start;
            }

            public String getConfirm() {
                return Confirm;
            }

            public void setConfirm(String Confirm) {
                this.Confirm = Confirm;
            }
        }

        public static class ServiceBean {
            /**
             * id : 9
             * name : Auto Service
             * subService : [{"id":"21","name":"Oil change, Filter & lube ( including fluid level check  )"},{"id":"22","name":"Synthetic Oil & Filter Change  ( including fluid level check  )"},{"id":"23","name":" Wheel Alignment"},{"id":"24","name":"Air Conditioning and Heating"}]
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
                 * id : 21
                 * name : Oil change, Filter & lube ( including fluid level check  )
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
            }
        }
        public static class InvoiceBean {
            /**
             * _id : 4
             * upd : 2018-02-22T08:33:47.329Z
             * crd : 2018-02-22T08:33:47.329Z
             * status : 1
             * distance : 0
             * extraPrice : 0.00
             * commission : 6.0
             * tax : 6.0
             * depositPrice : 0.0
             * totalAmount : 1060.0
             * disAmount : 0
             * coupon :
             * requestJson : [{"price":"250","parts":"250","name":"Oil and Filter Change ","id":"11","flatRemark":"hi hi to everyone who wants to work on it and friends is a good time to work with you","flatPrice":"0"},{"price":"0","parts":"0","name":"Oil and Filter Change ( Full Synthetic )","id":"12","flatRemark":"I am writing this email and friends is a good day please find the attached file is scanned and sent","flatPrice":"500"}]
             * requestId : 2
             * userId : 19
             * __v : 0
             */

            private int _id;
            private String upd;
            private String crd;
            private int status;
            private String distance;
            private String extraPrice;
            private String commission;
            private String tax;
            private String depositPrice;
            private String totalAmount;
            private String disAmount;
            private String coupon;
            private int requestId;
            private int userId;
            private int __v;
            private List<RequestJsonBean> requestJson;

            public int get_id() {
                return _id;
            }

            public void set_id(int _id) {
                this._id = _id;
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

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getExtraPrice() {
                return extraPrice;
            }

            public void setExtraPrice(String extraPrice) {
                this.extraPrice = extraPrice;
            }

            public String getCommission() {
                return commission;
            }

            public void setCommission(String commission) {
                this.commission = commission;
            }

            public String getTax() {
                return tax;
            }

            public void setTax(String tax) {
                this.tax = tax;
            }

            public String getDepositPrice() {
                return depositPrice;
            }

            public void setDepositPrice(String depositPrice) {
                this.depositPrice = depositPrice;
            }

            public String getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(String totalAmount) {
                this.totalAmount = totalAmount;
            }

            public String getDisAmount() {
                return disAmount;
            }

            public void setDisAmount(String disAmount) {
                this.disAmount = disAmount;
            }

            public String getCoupon() {
                return coupon;
            }

            public void setCoupon(String coupon) {
                this.coupon = coupon;
            }

            public int getRequestId() {
                return requestId;
            }

            public void setRequestId(int requestId) {
                this.requestId = requestId;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int get__v() {
                return __v;
            }

            public void set__v(int __v) {
                this.__v = __v;
            }

            public List<RequestJsonBean> getRequestJson() {
                return requestJson;
            }

            public void setRequestJson(List<RequestJsonBean> requestJson) {
                this.requestJson = requestJson;
            }

            public static class RequestJsonBean {
                /**
                 * price : 250
                 * parts : 250
                 * name : Oil and Filter Change
                 * id : 11
                 * flatRemark : hi hi to everyone who wants to work on it and friends is a good time to work with you
                 * flatPrice : 0
                 */

                private String price;
                private String parts;
                private String name;
                private String id;
                private String flatRemark;
                private String flatPrice;

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getParts() {
                    return parts;
                }

                public void setParts(String parts) {
                    this.parts = parts;
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

                public String getFlatRemark() {
                    return flatRemark;
                }

                public void setFlatRemark(String flatRemark) {
                    this.flatRemark = flatRemark;
                }

                public String getFlatPrice() {
                    return flatPrice;
                }

                public void setFlatPrice(String flatPrice) {
                    this.flatPrice = flatPrice;
                }
            }
        }
    }
}

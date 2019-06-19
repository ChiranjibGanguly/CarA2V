package com.cara2v.responceBean;

import java.util.List;

/**
 * Created by chiranjib on 29/1/18.
 */

public class GetMyJobsResponce {

    /**
     * api : 1
     * status : success
     * message : Request list..!
     * data : [{"_id":22,"car":{"carId":13,"carName":"Hyundai","VINCode":"BDJDNF","plateNumber":"mpshd-3663","VINCodeStatus":"0","plateNumberStatus":"0","carType":"Crossover","fuelType":"Gasoline"},"addressLatLong":[22.7093523,75.9014392],"upd":"2018-01-29T07:14:34.863Z","crd":"2018-01-29T07:14:34.863Z","status":2,"acceptUser":3,"usersRequest":"1","notifyUser":"3","SearchService":"9,8,6,3","address":"Pipliyahana, Indore, Madhya Pradesh, India","description":"hi I am a bit the bullet point the only thing is I don't have any other person is willing and able and the other day I paid the bill for the deposit I have to do this but just in case of","service":[{"subService":[{"name":"Oil change, Filter & lube ( including fluid level check  )","id":"21"},{"name":"Synthetic Oil & Filter Change  ( including fluid level check  )","id":"22"},{"name":"Mufflers &amp; Exhaust System ( add detail )","id":"47"}],"name":"Auto Service ","id":"9"},{"subService":[{"name":"Need a Tow Truck","id":"68"},{"name":"Flat Tire/Dead Battery","id":"71"}],"name":"Requests Tow Truck ","id":"8"},{"subService":[{"name":" Collision and Auto Body Repair","id":"59"},{"name":" Frame Work  ( main structure of a motor vehicle )","id":"60"}],"name":"Auto Frame Work","id":"6"},{"subService":[{"name":"Driver Window Replacement / Window won't go up or down","id":"4"},{"name":"Passenger Window Replacement / Window won't go up or down","id":"5"},{"name":"Right rear window replacement / window won't go up or down","id":"6"}],"name":"Auto Glass / Window Tinting","id":"3"}],"dateAndTime":"2018-02-15T18:43:00.000Z","userId":4,"__v":0,"userDetail":[{"_id":3,"userType":1,"addressLatLong":[22.705177799999994,75.9091074],"locationLatLong":[22.705177799999994,75.9091074],"upd":"2018-01-22T09:04:55.159Z","crd":"2018-01-22T09:04:55.159Z","status":1,"rating":"","userService":"8,7,6,4,3,2,1,9","fireBaseId":"","licenseStatus":1,"licenseImage":"","deviceType":2,"deviceToken":"ecB0IN9UnkI:APA91bEiwyoJ51DwDAElRphg2krdfa8rGVxf7sn1UNuhb-TOhzvT5H3tpYXs_HSc3wP0XcTeXBIQ-FUUTGP-oTL2m4TWGMa0MpIoSrw3-2pSG_vweoTW3OeDk8_D22ld0VNi2tpz33NT","socialType":"","socialId":"","location":"502, Krishna Tower, Above ICICI Bank, Pipliyahana Main Road, Brajeshwari Extension, Greater Brajeshwari, Indore, Madhya Pradesh 452001, India","address":"502, Krishna Tower, Above ICICI Bank, Pipliyahana Main Road, Brajeshwari Extension, Greater Brajeshwari, Indore, Madhya Pradesh 452001, India","authToken":"U6P0i3prRDpCyeawBkYu","businessName":"Mindiii Technology indore","profileImage":"hQga587Cnh_profileImage.jpg","phoneNumber":"1234567890","countryCode":"+ 91","password":"7c8e08334b6b8ee38977b11b173658dc","email":"mindiii@mindiii.com","userName":"chiruowner","__v":0}]}]
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
         * _id : 22
         * car : {"carId":13,"carName":"Hyundai","VINCode":"BDJDNF","plateNumber":"mpshd-3663","VINCodeStatus":"0","plateNumberStatus":"0","carType":"Crossover","fuelType":"Gasoline"}
         * addressLatLong : [22.7093523,75.9014392]
         * upd : 2018-01-29T07:14:34.863Z
         * crd : 2018-01-29T07:14:34.863Z
         * status : 2
         * acceptUser : 3
         * usersRequest : 1
         * notifyUser : 3
         * SearchService : 9,8,6,3
         * address : Pipliyahana, Indore, Madhya Pradesh, India
         * description : hi I am a bit the bullet point the only thing is I don't have any other person is willing and able and the other day I paid the bill for the deposit I have to do this but just in case of
         * service : [{"subService":[{"name":"Oil change, Filter & lube ( including fluid level check  )","id":"21"},{"name":"Synthetic Oil & Filter Change  ( including fluid level check  )","id":"22"},{"name":"Mufflers &amp; Exhaust System ( add detail )","id":"47"}],"name":"Auto Service ","id":"9"},{"subService":[{"name":"Need a Tow Truck","id":"68"},{"name":"Flat Tire/Dead Battery","id":"71"}],"name":"Requests Tow Truck ","id":"8"},{"subService":[{"name":" Collision and Auto Body Repair","id":"59"},{"name":" Frame Work  ( main structure of a motor vehicle )","id":"60"}],"name":"Auto Frame Work","id":"6"},{"subService":[{"name":"Driver Window Replacement / Window won't go up or down","id":"4"},{"name":"Passenger Window Replacement / Window won't go up or down","id":"5"},{"name":"Right rear window replacement / window won't go up or down","id":"6"}],"name":"Auto Glass / Window Tinting","id":"3"}]
         * dateAndTime : 2018-02-15T18:43:00.000Z
         * userId : 4
         * __v : 0
         * userDetail : [{"_id":3,"userType":1,"addressLatLong":[22.705177799999994,75.9091074],"locationLatLong":[22.705177799999994,75.9091074],"upd":"2018-01-22T09:04:55.159Z","crd":"2018-01-22T09:04:55.159Z","status":1,"rating":"","userService":"8,7,6,4,3,2,1,9","fireBaseId":"","licenseStatus":1,"licenseImage":"","deviceType":2,"deviceToken":"ecB0IN9UnkI:APA91bEiwyoJ51DwDAElRphg2krdfa8rGVxf7sn1UNuhb-TOhzvT5H3tpYXs_HSc3wP0XcTeXBIQ-FUUTGP-oTL2m4TWGMa0MpIoSrw3-2pSG_vweoTW3OeDk8_D22ld0VNi2tpz33NT","socialType":"","socialId":"","location":"502, Krishna Tower, Above ICICI Bank, Pipliyahana Main Road, Brajeshwari Extension, Greater Brajeshwari, Indore, Madhya Pradesh 452001, India","address":"502, Krishna Tower, Above ICICI Bank, Pipliyahana Main Road, Brajeshwari Extension, Greater Brajeshwari, Indore, Madhya Pradesh 452001, India","authToken":"U6P0i3prRDpCyeawBkYu","businessName":"Mindiii Technology indore","profileImage":"hQga587Cnh_profileImage.jpg","phoneNumber":"1234567890","countryCode":"+ 91","password":"7c8e08334b6b8ee38977b11b173658dc","email":"mindiii@mindiii.com","userName":"chiruowner","__v":0}]
         */

        private int _id;
        private CarBean car;
        private String upd;
        private String crd;
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
        private int __v;
        private List<Double> addressLatLong;
        private List<ServiceBean> service;
        private List<UserDetailBean> userDetail;
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

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
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

        public List<UserDetailBean> getUserDetail() {
            return userDetail;
        }

        public void setUserDetail(List<UserDetailBean> userDetail) {
            this.userDetail = userDetail;
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
             * carId : 13
             * carName : Hyundai
             * modelName : allroad
             * VINCode : BDJDNF
             * plateNumber : mpshd-3663
             * VINCodeStatus : 0
             * plateNumberStatus : 0
             * carType : Crossover
             * fuelType : Gasoline
             */

            private String carYear;
            private int carId;
            private String carName;
            private String modelName;
            private String VINCode;
            private String plateNumber;
            private String VINCodeStatus;
            private String plateNumberStatus;
            private String carType;
            private String fuelType;

            public int getCarId() {
                return carId;
            }

            public void setCarId(int carId) {
                this.carId = carId;
            }

            public String getCarName() {
                return carName;
            }

            public void setCarName(String carName) {
                this.carName = carName;
            }

            public String getVINCode() {
                return VINCode;
            }

            public void setVINCode(String VINCode) {
                this.VINCode = VINCode;
            }

            public String getPlateNumber() {
                return plateNumber;
            }

            public void setPlateNumber(String plateNumber) {
                this.plateNumber = plateNumber;
            }

            public String getVINCodeStatus() {
                return VINCodeStatus;
            }

            public void setVINCodeStatus(String VINCodeStatus) {
                this.VINCodeStatus = VINCodeStatus;
            }

            public String getPlateNumberStatus() {
                return plateNumberStatus;
            }

            public void setPlateNumberStatus(String plateNumberStatus) {
                this.plateNumberStatus = plateNumberStatus;
            }

            public String getCarType() {
                return carType;
            }

            public void setCarType(String carType) {
                this.carType = carType;
            }

            public String getFuelType() {
                return fuelType;
            }

            public void setFuelType(String fuelType) {
                this.fuelType = fuelType;
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

        public static class ServiceBean {
            /**
             * subService : [{"name":"Oil change, Filter & lube ( including fluid level check  )","id":"21"},{"name":"Synthetic Oil & Filter Change  ( including fluid level check  )","id":"22"},{"name":"Mufflers &amp; Exhaust System ( add detail )","id":"47"}]
             * name : Auto Service
             * id : 9
             */

            private String name;
            private String id;
            private List<SubServiceBean> subService;

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

            public List<SubServiceBean> getSubService() {
                return subService;
            }

            public void setSubService(List<SubServiceBean> subService) {
                this.subService = subService;
            }

            public static class SubServiceBean {
                /**
                 * name : Oil change, Filter & lube ( including fluid level check  )
                 * id : 21
                 */

                private String name;
                private String id;

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
            }
        }

        public static class UserDetailBean {
            /**
             * _id : 3
             * userType : 1
             * addressLatLong : [22.705177799999994,75.9091074]
             * locationLatLong : [22.705177799999994,75.9091074]
             * upd : 2018-01-22T09:04:55.159Z
             * crd : 2018-01-22T09:04:55.159Z
             * status : 1
             * rating :
             * userService : 8,7,6,4,3,2,1,9
             * fireBaseId :
             * licenseStatus : 1
             * licenseImage :
             * deviceType : 2
             * deviceToken : ecB0IN9UnkI:APA91bEiwyoJ51DwDAElRphg2krdfa8rGVxf7sn1UNuhb-TOhzvT5H3tpYXs_HSc3wP0XcTeXBIQ-FUUTGP-oTL2m4TWGMa0MpIoSrw3-2pSG_vweoTW3OeDk8_D22ld0VNi2tpz33NT
             * socialType :
             * socialId :
             * location : 502, Krishna Tower, Above ICICI Bank, Pipliyahana Main Road, Brajeshwari Extension, Greater Brajeshwari, Indore, Madhya Pradesh 452001, India
             * address : 502, Krishna Tower, Above ICICI Bank, Pipliyahana Main Road, Brajeshwari Extension, Greater Brajeshwari, Indore, Madhya Pradesh 452001, India
             * authToken : U6P0i3prRDpCyeawBkYu
             * businessName : Mindiii Technology indore
             * profileImage : hQga587Cnh_profileImage.jpg
             * phoneNumber : 1234567890
             * countryCode : + 91
             * password : 7c8e08334b6b8ee38977b11b173658dc
             * email : mindiii@mindiii.com
             * userName : chiruowner
             * __v : 0
             */

            private int _id;
            private int userType;
            private String upd;
            private String crd;
            private int status;
            private String rating;
            private String userService;
            private String fireBaseId;
            private int licenseStatus;
            private String licenseImage;
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
            private int __v;
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

            public String getRating() {
                return rating;
            }

            public void setRating(String rating) {
                this.rating = rating;
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

            public int getLicenseStatus() {
                return licenseStatus;
            }

            public void setLicenseStatus(int licenseStatus) {
                this.licenseStatus = licenseStatus;
            }

            public String getLicenseImage() {
                return licenseImage;
            }

            public void setLicenseImage(String licenseImage) {
                this.licenseImage = licenseImage;
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

            public int get__v() {
                return __v;
            }

            public void set__v(int __v) {
                this.__v = __v;
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

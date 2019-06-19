package com.cara2v.responceBean;

import java.util.List;

/**
 * Created by chiranjib on 11/1/18.
 */

public class ServiceDetailsResponce {

    /**
     * status : success
     * message : Service Detail
     * data : {"_id":31,"car":{"_id":6,"userId":4,"carName":"carA2v","brand":{"brandId":"32","brandName":"HUMMER"},"model":{"modelId":"598","modelName":"H3T"},"VINCode":"WTXG467IVJFC4678","plateNumber":"MP 08 478T","carType":"Hatchback","fuelType":"Biofuels (biodiesel)","mileage":"200","myCarImage":"108.179.225.88:2405/img/carImage/15157531812271515753161726.jpg,108.179.225.88:2405/img/carImage/15157531815081515753171940.jpg,108.179.225.88:2405/img/carImage/15157531817871515753183758.jpg","__v":0,"upd":"2018-01-12T10:33:01.821Z","crd":"2018-01-12T10:33:01.821Z","status":1},"addressLatLong":[23.012295599999998,74.7966079],"upd":"2018-01-17T05:36:04.165Z","crd":"2018-01-17T05:36:04.165Z","status":1,"acceptUser":"","usersRequest":"","notifyUser":"18,19,14,17,5,2,11,15,10,6,21","SearchService":"9,8","address":"Petlawad, Madhya Pradesh 457773, India","description":"hi guys have been able in advance payment at a while back from them for your kind regards Paul Paul","service":[{"subService":[{"name":"Oil change, Filter & lube ( including fluid level check  )","id":"21"},{"name":"Synthetic Oil & Filter Change  ( including fluid level check  )","id":"22"},{"name":" Wheel Alignment","id":"23"},{"name":"Air Conditioning and Heating","id":"24"}],"name":"Auto Service ","id":"9"},{"subService":[{"name":"Need a Tow Truck","id":"68"},{"name":"Fuel  Delivery","id":"69"}],"name":"Requests Tow Truck ","id":"8"}],"dateAndTime":"2018-01-17 00:10:68","userId":4,"__v":0,"orderdetails":[{"_id":15,"upd":"2018-01-17T05:37:15.715Z","crd":"2018-01-17T05:37:15.715Z","status":1,"distance":"115.35351767286376","tax":50,"depositPrice":50,"totalAmount":"382.5","disAmount":"","coupon":"","requestJson":[{"parts":"12","price":"12","name":"Oil change, Filter & lube ( including fluid level check  )","id":"21"},{"parts":"13","price":"13","name":"Synthetic Oil & Filter Change  ( including fluid level check  )","id":"22"},{"parts":"14","price":"14","name":" Wheel Alignment","id":"23"},{"parts":"15","price":"15","name":"Air Conditioning and Heating","id":"24"},{"parts":"45","price":"45","name":"Need a Tow Truck","id":"68"},{"parts":"54","price":"54","name":"Fuel  Delivery","id":"69"}],"requestId":31,"userId":18,"__v":0,"userInfo":{"_id":18,"userType":1,"addressLatLong":[23.0122956,74.79660790000003],"locationLatLong":[0,0],"upd":"2018-01-16T13:46:08.495Z","crd":"2018-01-16T13:46:08.495Z","status":1,"rating":"","userService":"9,8","fireBaseId":"Fg6QZu1BDGQd07neMNgn4pgNOBC3","licenseImage":"","deviceType":null,"deviceToken":"","socialType":"google","socialId":"105098852066875778974","location":"","address":"Petlawad, Madhya Pradesh, India","authToken":"tyictViqzDDRByhLEhDg","businessName":"AISHWARY","profileImage":"https://lh6.googleusercontent.com/-dBg-I3n09wg/AAAAAAAAAAI/AAAAAAAAAAc/IIj_XI5WSk4/s96-c/photo.jpg","phoneNumber":"7509528888","countryCode":"+1","password":"7c8e08334b6b8ee38977b11b173658dc","email":"a2vcar2017@gmail.com","userName":"Car A2V","__v":0}}],"userInfo":""}
     * myPromoCode : [{"_id":3,"amount":"123","title":"6DOHC1"},{"_id":6,"amount":"25","title":"L9N6IJ"}]
     * adminDetail : {"_id":1,"__v":0,"upd":"2018-01-22T08:56:11.464Z","crd":"2018-01-22T08:56:11.464Z","status":1,"percentage":"10"}
     */

    private String status;
    private String message;
    private DataBean data;
    private AdminDetailBean adminDetail;
    private List<MyPromoCodeBean> myPromoCode;
    private List<UpdateDataBean> updateData;

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

    public List<MyPromoCodeBean> getMyPromoCode() {
        return myPromoCode;
    }

    public void setMyPromoCode(List<MyPromoCodeBean> myPromoCode) {
        this.myPromoCode = myPromoCode;
    }

    public AdminDetailBean getAdminDetail() {
        return adminDetail;
    }

    public void setAdminDetail(AdminDetailBean adminDetail) {
        this.adminDetail = adminDetail;
    }

    public List<UpdateDataBean> getUpdateData() {
        return updateData;
    }

    public void setUpdateData(List<UpdateDataBean> updateData) {
        this.updateData = updateData;
    }

    public static class DataBean {
        /**
         * _id : 31
         * car : {"_id":6,"userId":4,"carName":"carA2v","brand":{"brandId":"32","brandName":"HUMMER"},"model":{"modelId":"598","modelName":"H3T"},"VINCode":"WTXG467IVJFC4678","plateNumber":"MP 08 478T","carType":"Hatchback","fuelType":"Biofuels (biodiesel)","mileage":"200","myCarImage":"108.179.225.88:2405/img/carImage/15157531812271515753161726.jpg,108.179.225.88:2405/img/carImage/15157531815081515753171940.jpg,108.179.225.88:2405/img/carImage/15157531817871515753183758.jpg","__v":0,"upd":"2018-01-12T10:33:01.821Z","crd":"2018-01-12T10:33:01.821Z","status":1}
         * addressLatLong : [23.012295599999998,74.7966079]
         * upd : 2018-01-17T05:36:04.165Z
         * crd : 2018-01-17T05:36:04.165Z
         * status : 1
         * acceptUser :
         * usersRequest :
         * notifyUser : 18,19,14,17,5,2,11,15,10,6,21
         * changeStatusDate : {"Confirm":"12 Feb, 04:53 am","Start":"13 Feb, 01:02 am","Progress":"13 Feb, 01:02 am","Almost":"13 Feb, 01:02 am","End":"13 Feb, 01:02 am","Ask":"13 Feb, 01:07 am"}
         * SearchService : 9,8
         * address : Petlawad, Madhya Pradesh 457773, India
         * description : hi guys have been able in advance payment at a while back from them for your kind regards Paul Paul
         * service : [{"subService":[{"name":"Oil change, Filter & lube ( including fluid level check  )","id":"21"},{"name":"Synthetic Oil & Filter Change  ( including fluid level check  )","id":"22"},{"name":" Wheel Alignment","id":"23"},{"name":"Air Conditioning and Heating","id":"24"}],"name":"Auto Service ","id":"9"},{"subService":[{"name":"Need a Tow Truck","id":"68"},{"name":"Fuel  Delivery","id":"69"}],"name":"Requests Tow Truck ","id":"8"}]
         * dateAndTime : 2018-01-17 00:10:68
         * userId : 4
         * __v : 0
         * orderdetails : [{"_id":15,"upd":"2018-01-17T05:37:15.715Z","crd":"2018-01-17T05:37:15.715Z","status":1,"distance":"115.35351767286376","tax":50,"depositPrice":50,"totalAmount":"382.5","disAmount":"","coupon":"","requestJson":[{"parts":"12","price":"12","name":"Oil change, Filter & lube ( including fluid level check  )","id":"21"},{"parts":"13","price":"13","name":"Synthetic Oil & Filter Change  ( including fluid level check  )","id":"22"},{"parts":"14","price":"14","name":" Wheel Alignment","id":"23"},{"parts":"15","price":"15","name":"Air Conditioning and Heating","id":"24"},{"parts":"45","price":"45","name":"Need a Tow Truck","id":"68"},{"parts":"54","price":"54","name":"Fuel  Delivery","id":"69"}],"requestId":31,"userId":18,"__v":0,"userInfo":{"_id":18,"userType":1,"addressLatLong":[23.0122956,74.79660790000003],"locationLatLong":[0,0],"upd":"2018-01-16T13:46:08.495Z","crd":"2018-01-16T13:46:08.495Z","status":1,"rating":"","userService":"9,8","fireBaseId":"Fg6QZu1BDGQd07neMNgn4pgNOBC3","licenseImage":"","deviceType":null,"deviceToken":"","socialType":"google","socialId":"105098852066875778974","location":"","address":"Petlawad, Madhya Pradesh, India","authToken":"tyictViqzDDRByhLEhDg","businessName":"AISHWARY","profileImage":"https://lh6.googleusercontent.com/-dBg-I3n09wg/AAAAAAAAAAI/AAAAAAAAAAc/IIj_XI5WSk4/s96-c/photo.jpg","phoneNumber":"7509528888","countryCode":"+1","password":"7c8e08334b6b8ee38977b11b173658dc","email":"a2vcar2017@gmail.com","userName":"Car A2V","__v":0}}]
         * userInfo :
         */

        private int _id;
        private CarBean car;
        private String upd;
        private String crd;
        private int status;
        private String acceptUser;
        private String usersRequest;
        private String SearchService;
        private String address;
        private String description;
        private String timeSlot;
        private String dateAndTime;
        private int userId;
        private int __v;
        private String userInfo;
        private List<Double> addressLatLong;
        private List<ServiceBean> service;
        private List<OrderdetailsBean> orderdetails;
        private List<ConDetailBean> conDetail;
        private ChangeStatusDateBean changeStatusDate;
        private ConReviewBean conReview;
        private OwnReviewBean ownReview;

        public int getMyRequest() {
            return myRequest;
        }

        public void setMyRequest(int myRequest) {
            this.myRequest = myRequest;
        }

        private int myRequest;

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

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public String getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(String userInfo) {
            this.userInfo = userInfo;
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

        public List<OrderdetailsBean> getOrderdetails() {
            return orderdetails;
        }

        public void setOrderdetails(List<OrderdetailsBean> orderdetails) {
            this.orderdetails = orderdetails;
        }

        public ChangeStatusDateBean getChangeStatusDate() {
            return changeStatusDate;
        }

        public void setChangeStatusDate(ChangeStatusDateBean changeStatusDate) {
            this.changeStatusDate = changeStatusDate;
        }

        public ConReviewBean getConReview() {
            return conReview;
        }

        public void setConReview(ConReviewBean conReview) {
            this.conReview = conReview;
        }

        public OwnReviewBean getOwnReview() {
            return ownReview;
        }

        public void setOwnReview(OwnReviewBean ownReview) {
            this.ownReview = ownReview;
        }

        public List<ConDetailBean> getConDetail() {
            return conDetail;
        }

        public void setConDetail(List<ConDetailBean> conDetail) {
            this.conDetail = conDetail;
        }

        public String getTimeSlot() {
            return timeSlot;
        }

        public void setTimeSlot(String timeSlot) {
            this.timeSlot = timeSlot;
        }

        public static class CarBean {
            /**
             * _id : 6
             * userId : 4
             * carName : carA2v
             * brand : {"brandId":"32","brandName":"HUMMER"}
             * model : {"modelId":"598","modelName":"H3T"}
             * VINCode : WTXG467IVJFC4678
             * plateNumber : MP 08 478T
             * carType : Hatchback
             * carYear : 2014,
             * fuelType : Biofuels (biodiesel)
             * mileage : 200
             * myCarImage : 108.179.225.88:2405/img/carImage/15157531812271515753161726.jpg,108.179.225.88:2405/img/carImage/15157531815081515753171940.jpg,108.179.225.88:2405/img/carImage/15157531817871515753183758.jpg
             * __v : 0
             * upd : 2018-01-12T10:33:01.821Z
             * crd : 2018-01-12T10:33:01.821Z
             * status : 1
             * plateNumberStatus: 1,
             * VINCodeStatus: 1,
             */

            private int _id;
            private int userId;
            private String carName;
            private BrandBean brand;
            private ModelBean model;
            private String VINCode;
            private String plateNumber;
            private String carType;
            private String carYear;
            private String fuelType;
            private String mileage;
            private String myCarImage;

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

            private String plateNumberStatus;
            private String VINCodeStatus;
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

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getCarName() {
                return carName;
            }

            public void setCarName(String carName) {
                this.carName = carName;
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

            public String getMileage() {
                return mileage;
            }

            public void setMileage(String mileage) {
                this.mileage = mileage;
            }

            public String getMyCarImage() {
                return myCarImage;
            }

            public void setMyCarImage(String myCarImage) {
                this.myCarImage = myCarImage;
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

            public String getCarYear() {
                return carYear;
            }

            public void setCarYear(String carYear) {
                this.carYear = carYear;
            }

            public static class BrandBean {
                /**
                 * brandId : 32
                 * brandName : HUMMER
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
                 * modelId : 598
                 * modelName : H3T
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

        public static class ChangeStatusDateBean {
            /**
             * Confirm : 12 Feb, 04:53 am
             * Start : 13 Feb, 01:02 am
             * Progress : 13 Feb, 01:02 am
             * Almost : 13 Feb, 01:02 am
             * End : 13 Feb, 01:02 am
             * Ask : 13 Feb, 01:07 am
             * payment:04 Apr, 04:57 am
             */

            private String Confirm;
            private String Start;
            private String Progress;
            private String Almost;
            private String End;
            private String Ask;
            private String payment;

            public String getConfirm() {
                return Confirm;
            }

            public void setConfirm(String Confirm) {
                this.Confirm = Confirm;
            }

            public String getStart() {
                return Start;
            }

            public void setStart(String Start) {
                this.Start = Start;
            }

            public String getProgress() {
                return Progress;
            }

            public void setProgress(String Progress) {
                this.Progress = Progress;
            }

            public String getAlmost() {
                return Almost;
            }

            public void setAlmost(String Almost) {
                this.Almost = Almost;
            }

            public String getEnd() {
                return End;
            }

            public void setEnd(String End) {
                this.End = End;
            }

            public String getAsk() {
                return Ask;
            }

            public void setAsk(String Ask) {
                this.Ask = Ask;
            }

            public String getPayment() {
                return payment;
            }

            public void setPayment(String payment) {
                this.payment = payment;
            }
        }

        public static class ConReviewBean {
            /**
             * rating : 3
             * review : goodf
             */

            private String rating;
            private String review;
            private String time;

            public String getRating() {
                return rating;
            }

            public void setRating(String rating) {
                this.rating = rating;
            }

            public String getReview() {
                return review;
            }

            public void setReview(String review) {
                this.review = review;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }

        public static class OwnReviewBean {
            /**
             * rating : 4
             * review : good
             */

            private String rating;
            private String review;
            private String time;

            public String getRating() {
                return rating;
            }

            public void setRating(String rating) {
                this.rating = rating;
            }

            public String getReview() {
                return review;
            }

            public void setReview(String review) {
                this.review = review;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }

        public static class ServiceBean {
            /**
             * subService : [{"name":"Oil change, Filter & lube ( including fluid level check  )","id":"21"},{"name":"Synthetic Oil & Filter Change  ( including fluid level check  )","id":"22"},{"name":" Wheel Alignment","id":"23"},{"name":"Air Conditioning and Heating","id":"24"}]
             * name : Auto Service
             * id : 9
             * serviceImage : http://108.179.225.88:2405/img/service/LdqrBigPTnTs3cMFWBN9_auto_service_1.png
             */

            private String name;
            private String id;
            private String serviceImage;
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

            public String getServiceImage() {
                return serviceImage;
            }

            public void setServiceImage(String serviceImage) {
                this.serviceImage = serviceImage;
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

                @Override
                public String toString() {
                    return name;
                }
            }
        }

        public static class OrderdetailsBean {
            /**
             * _id : 15
             * upd : 2018-01-17T05:37:15.715Z
             * crd : 2018-01-17T05:37:15.715Z
             * status : 1
             * distance : 115.35351767286376
             * commission : 0.00
             * tax : 50
             * depositPrice : 50
             * totalAmount : 382.5
             * disAmount :
             * coupon :
             * requestJson : [{"parts":"12","price":"12","name":"Oil change, Filter & lube ( including fluid level check  )","id":"21"},{"parts":"13","price":"13","name":"Synthetic Oil & Filter Change  ( including fluid level check  )","id":"22"},{"parts":"14","price":"14","name":" Wheel Alignment","id":"23"},{"parts":"15","price":"15","name":"Air Conditioning and Heating","id":"24"},{"parts":"45","price":"45","name":"Need a Tow Truck","id":"68"},{"parts":"54","price":"54","name":"Fuel  Delivery","id":"69"}]
             * requestId : 31
             * userId : 18
             * __v : 0
             * now : 2018-01-22 04:11:58
             * userInfo : {"_id":18,"userType":1,"addressLatLong":[23.0122956,74.79660790000003],"locationLatLong":[0,0],"upd":"2018-01-16T13:46:08.495Z","crd":"2018-01-16T13:46:08.495Z","status":1,"rating":"","userService":"9,8","fireBaseId":"Fg6QZu1BDGQd07neMNgn4pgNOBC3","licenseImage":"","deviceType":null,"deviceToken":"","socialType":"google","socialId":"105098852066875778974","location":"","address":"Petlawad, Madhya Pradesh, India","authToken":"tyictViqzDDRByhLEhDg","businessName":"AISHWARY","profileImage":"https://lh6.googleusercontent.com/-dBg-I3n09wg/AAAAAAAAAAI/AAAAAAAAAAc/IIj_XI5WSk4/s96-c/photo.jpg","phoneNumber":"7509528888","countryCode":"+1","password":"7c8e08334b6b8ee38977b11b173658dc","email":"a2vcar2017@gmail.com","userName":"Car A2V","__v":0}
             */

            private int _id;
            private String upd;
            private String crd;
            private int status;
            private String distance;
            private String commission;
            private String tax;
            private String depositPrice;
            private String totalAmount;
            private String disAmount;
            private String coupon;
            private int requestId;
            private int userId;
            private int __v;
            private String now;
            private UserInfoBean userInfo;
            private List<ExtraJsonBean> extraJson;
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

            public UserInfoBean getUserInfo() {
                return userInfo;
            }

            public String getNow() {
                return now;
            }

            public void setNow(String now) {
                this.now = now;
            }

            public void setUserInfo(UserInfoBean userInfo) {
                this.userInfo = userInfo;
            }

            public List<RequestJsonBean> getRequestJson() {
                return requestJson;
            }

            public void setRequestJson(List<RequestJsonBean> requestJson) {
                this.requestJson = requestJson;
            }

            public List<ExtraJsonBean> getExtraJson() {
                return extraJson;
            }

            public void setExtraJson(List<ExtraJsonBean> extraJson) {
                this.extraJson = extraJson;
            }

            public static class UserInfoBean {
                /**
                 * _id : 18
                 * userType : 1
                 * addressLatLong : [23.0122956,74.79660790000003]
                 * locationLatLong : [0,0]
                 * upd : 2018-01-16T13:46:08.495Z
                 * crd : 2018-01-16T13:46:08.495Z
                 * status : 1
                 * rating :
                 * userService : 9,8
                 * fireBaseId : Fg6QZu1BDGQd07neMNgn4pgNOBC3
                 * licenseImage :
                 * deviceType : null
                 * deviceToken :
                 * socialType : google
                 * socialId : 105098852066875778974
                 * location :
                 * address : Petlawad, Madhya Pradesh, India
                 * authToken : tyictViqzDDRByhLEhDg
                 * businessName : AISHWARY
                 * profileImage : https://lh6.googleusercontent.com/-dBg-I3n09wg/AAAAAAAAAAI/AAAAAAAAAAc/IIj_XI5WSk4/s96-c/photo.jpg
                 * phoneNumber : 7509528888
                 * countryCode : +1
                 * password : 7c8e08334b6b8ee38977b11b173658dc
                 * email : a2vcar2017@gmail.com
                 * userName : Car A2V
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
                private String licenseImage;
                private Object deviceType;
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

                public String getLicenseImage() {
                    return licenseImage;
                }

                public void setLicenseImage(String licenseImage) {
                    this.licenseImage = licenseImage;
                }

                public Object getDeviceType() {
                    return deviceType;
                }

                public void setDeviceType(Object deviceType) {
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

            public static class ExtraJsonBean {
                /**
                 * flatPrice : 0
                 * flatRemark : hello sir I would be great to
                 * id : 1
                 * name : Tinting all window's
                 * parts : 68
                 * price : 25
                 */

                private String flatPrice;
                private String flatRemark;
                private String id;
                private String name;
                private String parts;
                private String price;

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
            }

            public static class RequestJsonBean {
                /**
                 * parts : 12
                 * price : 12
                 * name : Oil change, Filter & lube ( including fluid level check  )
                 * flatRemark: hello my friend and your family and friends to work
                 * id : 21
                 */

                private String parts;
                private String price;
                private String name;
                private String flatPrice;
                private String id;
                private String flatRemark;

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

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getFlatPrice() {
                    return flatPrice;
                }

                public void setFlatPrice(String flatPrice) {
                    this.flatPrice = flatPrice;
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
            }
        }

        public static class ConDetailBean {
            /**
             * _id : 18
             * userType : 2
             * addressLatLong : [22.7181206,75.8874601]
             * locationLatLong : [22.7181206,75.8874601]
             * upd : 2018-02-15T06:01:06.243Z
             * crd : 2018-02-15T06:01:06.243Z
             * status : 1
             * rating : 5
             * userService :
             * fireBaseId :
             * accountAdd : 0
             * licenseStatus : 0
             * licenseImage :
             * deviceType : 2
             * deviceToken : fuG3Fzjynls:APA91bFWGJKrKf7Yo0vHkq3u-CqKtl4orniK5m1dInmiHA-vrSCZ3SZi19EasT0v1v6tgJZZ6mCcEs6oHp62xb9ll-Ye3FziWq_yj6epgnULiEeOyb86HlR8cfeIpB4xHCDcQm3ConRm
             * socialType :
             * socialId :
             * location : No. 13C, Geeta Bhawan Main Road, Near Geeta Bhawan Mandir, Kailash Park, Manoramaganj, Kailash Park Colony, Indore, Madhya Pradesh 452001, India
             * address : No. 13C, Geeta Bhawan Main Road, Near Geeta Bhawan Mandir, Kailash Park, Manoramaganj, Kailash Park Colony, Indore, Madhya Pradesh 452001, India
             * authToken : yDBq7izjhamSVmrviFD9
             * businessName :
             * profileImage : http://cara2v.com:2405/img/profile/DrkbhDQYlz_profileImage.jpg
             * phoneNumber : 8116174365
             * countryCode : + 91
             * password : 7c8e08334b6b8ee38977b11b173658dc
             * email : chiranjibg91@gmail.com
             * userName : chiruconsumer
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
            private int accountAdd;
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

            public int getAccountAdd() {
                return accountAdd;
            }

            public void setAccountAdd(int accountAdd) {
                this.accountAdd = accountAdd;
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
    }

    public static class MyPromoCodeBean {
        /**
         * _id : 3
         * amount : 123
         * title : 6DOHC1
         */

        private int _id;
        private String amount;
        private String title;
        private boolean isSelected;

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
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

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }

    public static class AdminDetailBean {
        /**
         * _id : 1
         * __v : 0
         * upd : 2018-01-22T08:56:11.464Z
         * crd : 2018-01-22T08:56:11.464Z
         * status : 1
         * percentage : 10
         */

        private int _id;
        private int __v;
        private String upd;
        private String crd;
        private int status;
        private String percentage;

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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getPercentage() {
            return percentage;
        }

        public void setPercentage(String percentage) {
            this.percentage = percentage;
        }
    }

    public static class UpdateDataBean {
        /**
         * _id : 7
         * upd : 2017-12-27T10:34:52.392Z
         * crd : 2017-12-27T10:34:52.392Z
         * status : 1
         * serviceImage : PP9esDaDRrA1rjfihoRQ_car_interior.png
         * serviceDescription : Many people may assume that car upholstery strictly refers to the seats within the vehicle, but that doesn't quite cover it. Upholstery and upholstery repairs can apply to many components of a car's interior. A few examples of items an automotive upholsterer or trimmer may be able to repair, replace or customize
         * serviceName : Auto Upholstery  (  interior )
         * __v : 0
         * position : 3
         * subService : [{"_id":31,"upd":"2018-02-16T02:10:49.085Z","crd":"2018-02-16T02:10:49.085Z","status":1,"subServiceName":"Headliner Repair / replacement","serviceId":7,"__v":0},{"_id":32,"upd":"2018-02-16T02:11:28.661Z","crd":"2018-02-16T02:11:28.661Z","status":1,"subServiceName":"Fabric / Leather repair","serviceId":7,"__v":0},{"_id":33,"upd":"2018-02-16T02:12:06.549Z","crd":"2018-02-16T02:12:06.549Z","status":1,"subServiceName":"Leather Conditioner","serviceId":7,"__v":0},{"_id":34,"upd":"2018-02-16T02:12:49.828Z","crd":"2018-02-16T02:12:49.828Z","status":1,"subServiceName":"Leather Re-Dye / repair","serviceId":7,"__v":0},{"_id":35,"upd":"2018-02-16T02:13:38.234Z","crd":"2018-02-16T02:13:38.234Z","status":1,"subServiceName":"Door Panel  repair/ replacemet","serviceId":7,"__v":0}]
         */

        private int _id;
        private String upd;
        private String crd;
        private int status;
        private String serviceImage;
        private String serviceDescription;
        private String serviceName;
        private int __v;
        private String position;
        private List<SubServiceBeanX> subService;

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

        public String getServiceImage() {
            return serviceImage;
        }

        public void setServiceImage(String serviceImage) {
            this.serviceImage = serviceImage;
        }

        public String getServiceDescription() {
            return serviceDescription;
        }

        public void setServiceDescription(String serviceDescription) {
            this.serviceDescription = serviceDescription;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public List<SubServiceBeanX> getSubService() {
            return subService;
        }

        public void setSubService(List<SubServiceBeanX> subService) {
            this.subService = subService;
        }

        public static class SubServiceBeanX {
            /**
             * _id : 31
             * upd : 2018-02-16T02:10:49.085Z
             * crd : 2018-02-16T02:10:49.085Z
             * status : 1
             * subServiceName : Headliner Repair / replacement
             * serviceId : 7
             * __v : 0
             */

            private int _id;
            private String upd;
            private String crd;
            private int status;
            private String subServiceName;
            private int serviceId;
            private int __v;

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

            public String getSubServiceName() {
                return subServiceName;
            }

            public void setSubServiceName(String subServiceName) {
                this.subServiceName = subServiceName;
            }

            public int getServiceId() {
                return serviceId;
            }

            public void setServiceId(int serviceId) {
                this.serviceId = serviceId;
            }

            public int get__v() {
                return __v;
            }

            public void set__v(int __v) {
                this.__v = __v;
            }
        }
    }
}

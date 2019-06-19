package com.cara2v.responceBean;

import java.util.List;

/**
 * Created by chiranjib on 25/4/18.
 */

public class GetMyNotifcationResponce {


    /**
     * api : 1
     * status : success
     * message : notification list..!
     * data : [{"_id":"5ad87d36f092b84df9e0e46a","crd":"2018-04-19 06:27:50","status":3,"notification":{"sound":"default","click_action":"MainActivity","requestId":"4","NotifyType":"Start","body":"Your Ferrari service has started.","title":"Your Ferrari service has started."},"userId":47,"UserDetail":[{"profileImage":"http://108.179.225.88:2405/img/profile/SjhQCaxEUihNrQzwhSRr_file.png","userName":"aish"}]},{"_id":"5ad89633c0119b580dc726b6","crd":"2018-04-19 08:14:27","status":3,"notification":{"sound":"default","click_action":"MainActivity","requestId":"6","NotifyType":"Quote","body":"cshop from Modern Service Center send quotation for Volkswagen","title":"cshop from Modern Service Center send quotation for Volkswagen"},"userId":47,"UserDetail":[{"profileImage":"http://108.179.225.88:2405/img/profile/owrPWTgbYbPLA1Qdi1oU_file.png","userName":"cshop"}]},{"_id":"5ad896fcc0119b580dc726b9","crd":"2018-04-19 08:17:48","status":3,"notification":{"sound":"default","click_action":"MainActivity","requestId":"5","NotifyType":"Ask","body":"aish from Aishwary  send Invoice for Ferrari","title":"aish from Aishwary  send Invoice for Ferrari"},"userId":47,"UserDetail":[{"profileImage":"http://108.179.225.88:2405/img/profile/SjhQCaxEUihNrQzwhSRr_file.png","userName":"aish"}]},{"_id":"5ad89f4bdc4b8b63ce701ce4","crd":"2018-04-19 08:53:15","status":3,"notification":{"sound":"default","click_action":"MainActivity","requestId":"7","NotifyType":"Almost","body":"Your Ferrari service request has almost done.","title":"Your Ferrari service request has almost done."},"userId":47,"UserDetail":[{"profileImage":"http://108.179.225.88:2405/img/profile/SjhQCaxEUihNrQzwhSRr_file.png","userName":"aish"}]},{"_id":"5adf3183439a31523073ac85","crd":"2018-04-24 08:30:43","status":3,"notification":{"sound":"default","click_action":"MainActivity","requestId":"37","NotifyType":"Quote","body":"cshop from Modern Service Center send quotation for Oldsmobile","title":"cshop from Modern Service Center send quotation for Oldsmobile"},"userId":47,"UserDetail":[{"profileImage":"http://108.179.225.88:2405/img/profile/owrPWTgbYbPLA1Qdi1oU_file.png","userName":"cshop"}]}]
     * date : 2018-04-25 05:37:00
     */

    private String api;
    private String status;
    private String message;
    private String date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * _id : 5ad87d36f092b84df9e0e46a
         * crd : 2018-04-19 06:27:50
         * status : 3
         * notification : {"sound":"default","click_action":"MainActivity","requestId":"4","NotifyType":"Start","body":"Your Ferrari service has started.","title":"Your Ferrari service has started."}
         * userId : 47
         * UserDetail : [{"profileImage":"http://108.179.225.88:2405/img/profile/SjhQCaxEUihNrQzwhSRr_file.png","userName":"aish"}]
         */

        private String _id;
        private String crd;
        private int status;
        private NotificationBean notification;
        private int userId;
        private List<UserDetailBean> UserDetail;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
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

        public NotificationBean getNotification() {
            return notification;
        }

        public void setNotification(NotificationBean notification) {
            this.notification = notification;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public List<UserDetailBean> getUserDetail() {
            return UserDetail;
        }

        public void setUserDetail(List<UserDetailBean> UserDetail) {
            this.UserDetail = UserDetail;
        }

        public static class NotificationBean {
            /**
             * sound : default
             * click_action : MainActivity
             * requestId : 4
             * NotifyType : Start
             * body : Your Ferrari service has started.
             * title : Your Ferrari service has started.
             */

            private String sound;
            private String click_action;
            private String requestId;
            private String NotifyType;
            private String body;
            private String title;

            public String getSound() {
                return sound;
            }

            public void setSound(String sound) {
                this.sound = sound;
            }

            public String getClick_action() {
                return click_action;
            }

            public void setClick_action(String click_action) {
                this.click_action = click_action;
            }

            public String getRequestId() {
                return requestId;
            }

            public void setRequestId(String requestId) {
                this.requestId = requestId;
            }

            public String getNotifyType() {
                return NotifyType;
            }

            public void setNotifyType(String NotifyType) {
                this.NotifyType = NotifyType;
            }

            public String getBody() {
                return body;
            }

            public void setBody(String body) {
                this.body = body;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class UserDetailBean {
            /**
             * profileImage : http://108.179.225.88:2405/img/profile/SjhQCaxEUihNrQzwhSRr_file.png
             * userName : aish
             */

            private String profileImage;
            private String userName;

            public String getProfileImage() {
                return profileImage;
            }

            public void setProfileImage(String profileImage) {
                this.profileImage = profileImage;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }
        }
    }
}

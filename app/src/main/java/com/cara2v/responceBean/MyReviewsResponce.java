package com.cara2v.responceBean;

import java.util.List;

/**
 * Created by chiranjib on 6/4/18.
 */

public class MyReviewsResponce {

    /**
     * api : 1
     * status : success
     * message : User Revice Rating..!
     * data : [{"_id":"5ac60582825d2618243f4434","crd":"2018-04-05 06:16:18","review":"this is new notify for cuser","rating":5,"userDetail":[{"profileImage":"http://108.179.225.88:2405/img/profile/TSlxhcNylE_BkCVV0tIcAAE6u-.jpg","userName":"aish"}]},{"_id":"5ac73d362b93ba16843c06aa","crd":"2018-04-06 04:26:14","review":"nice cooperation","rating":4,"userDetail":[{"profileImage":"http://108.179.225.88:2405/img/profile/pgQgBoBTiT_profileImage.jpg","userName":"cshop"}]}]
     * now : 2018-04-06 04:28:10
     */

    private String api;
    private String status;
    private String message;
    private String now;
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

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * _id : 5ac60582825d2618243f4434
         * crd : 2018-04-05 06:16:18
         * review : this is new notify for cuser
         * rating : 5
         * userDetail : [{"profileImage":"http://108.179.225.88:2405/img/profile/TSlxhcNylE_BkCVV0tIcAAE6u-.jpg","userName":"aish"}]
         */

        private String _id;
        private String crd;
        private String review;
        private int rating;
        private List<UserDetailBean> userDetail;

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

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public List<UserDetailBean> getUserDetail() {
            return userDetail;
        }

        public void setUserDetail(List<UserDetailBean> userDetail) {
            this.userDetail = userDetail;
        }

        public static class UserDetailBean {
            /**
             * profileImage : http://108.179.225.88:2405/img/profile/TSlxhcNylE_BkCVV0tIcAAE6u-.jpg
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

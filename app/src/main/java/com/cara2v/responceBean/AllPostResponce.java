package com.cara2v.responceBean;

import java.util.List;

/**
 * Created by chiranjib on 12/3/18.
 */

public class AllPostResponce {


    /**
     * status : success
     * message : Post List
     * now : 2018-03-13 05:31:03
     * data : [{"_id":12,"crd":"2018-03-13 02:00:22","CommentCount":2,"LikeCount":1,"postShowType":1,"postType":3,"postImage":"http://108.179.225.88:2405/img/postImage/1520924422312_4384db25-b3fc-470f-9f44-78d69e399d12.jpg","postStatus":"nice services","userId":7,"userDetail":[{"businessName":"Modern Service Center","profileImage":"http://108.179.225.88:2405/img/profile/pgQgBoBTiT_profileImage.jpg","email":"chiranjib.mindiii@gmail.com","userName":"cshop"}],"isLike":0,"comment":[{"_id":4,"crd":"2018-03-13 05:30:35","postComment":"payment is received a letter for","userImage":"http://108.179.225.88:2405/img/profile/pgQgBoBTiT_profileImage.jpg","userName":"cshop","userId":7},{"_id":5,"crd":"2018-03-13 05:31:02","postComment":"the first place in my favorite place 54 you for all that is what it means I can you","userImage":"http://108.179.225.88:2405/img/profile/pgQgBoBTiT_profileImage.jpg","userName":"cshop","userId":7}]}]
     */

    private String status;
    private String message;
    private String now;
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
         * _id : 12
         * crd : 2018-03-13 02:00:22
         * CommentCount : 2
         * LikeCount : 1
         * postShowType : 1
         * postType : 3
         * postImage : http://108.179.225.88:2405/img/postImage/1520924422312_4384db25-b3fc-470f-9f44-78d69e399d12.jpg
         * postStatus : nice services
         * userId : 7
         * userDetail : [{"businessName":"Modern Service Center","profileImage":"http://108.179.225.88:2405/img/profile/pgQgBoBTiT_profileImage.jpg","email":"chiranjib.mindiii@gmail.com","userName":"cshop"}]
         * isLike : 0
         * comment : [{"_id":4,"crd":"2018-03-13 05:30:35","postComment":"payment is received a letter for","userImage":"http://108.179.225.88:2405/img/profile/pgQgBoBTiT_profileImage.jpg","userName":"cshop","userId":7},{"_id":5,"crd":"2018-03-13 05:31:02","postComment":"the first place in my favorite place 54 you for all that is what it means I can you","userImage":"http://108.179.225.88:2405/img/profile/pgQgBoBTiT_profileImage.jpg","userName":"cshop","userId":7}]
         */

        private int _id;
        private String crd;
        private int CommentCount;
        private int LikeCount;
        private int postShowType;
        private int postType;
        private String postImage;
        private String postStatus;
        private int userId;
        private int isLike;
        private List<UserDetailBean> userDetail;
        private List<CommentBean> comment;

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public String getCrd() {
            return crd;
        }

        public void setCrd(String crd) {
            this.crd = crd;
        }

        public int getCommentCount() {
            return CommentCount;
        }

        public void setCommentCount(int CommentCount) {
            this.CommentCount = CommentCount;
        }

        public int getLikeCount() {
            return LikeCount;
        }

        public void setLikeCount(int LikeCount) {
            this.LikeCount = LikeCount;
        }

        public int getPostShowType() {
            return postShowType;
        }

        public void setPostShowType(int postShowType) {
            this.postShowType = postShowType;
        }

        public int getPostType() {
            return postType;
        }

        public void setPostType(int postType) {
            this.postType = postType;
        }

        public String getPostImage() {
            return postImage;
        }

        public void setPostImage(String postImage) {
            this.postImage = postImage;
        }

        public String getPostStatus() {
            return postStatus;
        }

        public void setPostStatus(String postStatus) {
            this.postStatus = postStatus;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getIsLike() {
            return isLike;
        }

        public void setIsLike(int isLike) {
            this.isLike = isLike;
        }

        public List<UserDetailBean> getUserDetail() {
            return userDetail;
        }

        public void setUserDetail(List<UserDetailBean> userDetail) {
            this.userDetail = userDetail;
        }

        public List<CommentBean> getComment() {
            return comment;
        }

        public void setComment(List<CommentBean> comment) {
            this.comment = comment;
        }

        public static class UserDetailBean {
            /**
             * businessName : Modern Service Center
             * profileImage : http://108.179.225.88:2405/img/profile/pgQgBoBTiT_profileImage.jpg
             * email : chiranjib.mindiii@gmail.com
             * userName : cshop
             */

            private String businessName;
            private String profileImage;
            private String email;
            private String userName;

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
        }

        public static class CommentBean {
            /**
             * _id : 4
             * crd : 2018-03-13 05:30:35
             * postComment : payment is received a letter for
             * userImage : http://108.179.225.88:2405/img/profile/pgQgBoBTiT_profileImage.jpg
             * userName : cshop
             * userId : 7
             */

            private int _id;
            private String crd;
            private String postComment;
            private String userImage;
            private String userName;
            private int userId;

            public int get_id() {
                return _id;
            }

            public void set_id(int _id) {
                this._id = _id;
            }

            public String getCrd() {
                return crd;
            }

            public void setCrd(String crd) {
                this.crd = crd;
            }

            public String getPostComment() {
                return postComment;
            }

            public void setPostComment(String postComment) {
                this.postComment = postComment;
            }

            public String getUserImage() {
                return userImage;
            }

            public void setUserImage(String userImage) {
                this.userImage = userImage;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }
    }
}

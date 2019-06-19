package com.cara2v.responceBean;

/**
 * Created by chiranjib on 15/5/18.
 */

public class GetAccDetailsResponce {


    /**
     * api : 1
     * status : success
     * bankAccount : {"_id":1,"userId":7,"__v":0,"upd":"2018-05-15T06:00:40.349Z","crd":"2018-05-15T06:00:40.349Z","status":1,"accountDetailObj":{"accountHolderType":"individual","currency":"USD","postal_code":"90210","ssnLast":"1234","accountNo":"000123456789","routingNumber":"110000000","country":"US","lastName":"RATHORE","firstName":"AISH"},"accountId":"acct_1CRwsLIbRQj1PAGS"}
     */

    private String api;
    private String status;
    private BankAccountBean bankAccount;

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

    public BankAccountBean getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccountBean bankAccount) {
        this.bankAccount = bankAccount;
    }

    public static class BankAccountBean {
        /**
         * _id : 1
         * userId : 7
         * __v : 0
         * upd : 2018-05-15T06:00:40.349Z
         * crd : 2018-05-15T06:00:40.349Z
         * status : 1
         * accountDetailObj : {"accountHolderType":"individual","currency":"USD","postal_code":"90210","ssnLast":"1234","accountNo":"000123456789","routingNumber":"110000000","country":"US","lastName":"RATHORE","firstName":"AISH"}
         * accountId : acct_1CRwsLIbRQj1PAGS
         */

        private int _id;
        private int userId;
        private int __v;
        private String upd;
        private String crd;
        private int status;
        private AccountDetailObjBean accountDetailObj;
        private String accountId;

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

        public AccountDetailObjBean getAccountDetailObj() {
            return accountDetailObj;
        }

        public void setAccountDetailObj(AccountDetailObjBean accountDetailObj) {
            this.accountDetailObj = accountDetailObj;
        }

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public static class AccountDetailObjBean {
            /**
             * accountHolderType : individual
             * currency : USD
             * postal_code : 90210
             * ssnLast : 1234
             * accountNo : 000123456789
             * routingNumber : 110000000
             * country : US
             * lastName : RATHORE
             * firstName : AISH
             */

            private String accountHolderType;
            private String currency;
            private String postal_code;
            private String ssnLast;
            private String accountNo;
            private String routingNumber;
            private String country;
            private String lastName;
            private String firstName;

            public String getAccountHolderType() {
                return accountHolderType;
            }

            public void setAccountHolderType(String accountHolderType) {
                this.accountHolderType = accountHolderType;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getPostal_code() {
                return postal_code;
            }

            public void setPostal_code(String postal_code) {
                this.postal_code = postal_code;
            }

            public String getSsnLast() {
                return ssnLast;
            }

            public void setSsnLast(String ssnLast) {
                this.ssnLast = ssnLast;
            }

            public String getAccountNo() {
                return accountNo;
            }

            public void setAccountNo(String accountNo) {
                this.accountNo = accountNo;
            }

            public String getRoutingNumber() {
                return routingNumber;
            }

            public void setRoutingNumber(String routingNumber) {
                this.routingNumber = routingNumber;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getLastName() {
                return lastName;
            }

            public void setLastName(String lastName) {
                this.lastName = lastName;
            }

            public String getFirstName() {
                return firstName;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }
        }
    }
}

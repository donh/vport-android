package com.a21vianet.wallet.vport.library.commom.http.vchain.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wang.rongqiang on 2017/6/9.
 */

public class VPortCreateResponse {

    @SerializedName("loginResult")
    private ResultBean result;
    @SerializedName("time")
    private String time;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static class ResultBean {

        @SerializedName("user")
        private UserBean user;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * address : 1G6DLbZi1A1s1utakX3mLpbkrqe9HtBozg
             * controller : 071173dbfd2ba49c540dc098a4f464c6fefe3519
             * name : 冬冬
             * phone : 0910345967
             * privateKey : Kwv62ktzJxTtoZJ5sX3P8FD5RptiXWGMJ2U5ZYWytvQJ249rP2qb
             * proxy : 4293abb2ce7b42f2fa43c01f750d8ec18bd4c67b
             * publicKey : 03e4c8693106d5b76a7534a16a0789bd394ee9dde6367b59dffcee5ba73f5611c2
             * recovery : 3fb4bd0739089ec6ec3c039fd2dcff85f2bd67b3
             */

            @SerializedName("address")
            private String address;
            @SerializedName("controller")
            private String controller;
            @SerializedName("name")
            private String name;
            @SerializedName("phone")
            private String phone;
            @SerializedName("privateKey")
            private String privateKey;
            @SerializedName("proxy")
            private String proxy;
            @SerializedName("publicKey")
            private String publicKey;
            @SerializedName("recovery")
            private String recovery;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getController() {
                return controller;
            }

            public void setController(String controller) {
                this.controller = controller;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getPrivateKey() {
                return privateKey;
            }

            public void setPrivateKey(String privateKey) {
                this.privateKey = privateKey;
            }

            public String getProxy() {
                return proxy;
            }

            public void setProxy(String proxy) {
                this.proxy = proxy;
            }

            public String getPublicKey() {
                return publicKey;
            }

            public void setPublicKey(String publicKey) {
                this.publicKey = publicKey;
            }

            public String getRecovery() {
                return recovery;
            }

            public void setRecovery(String recovery) {
                this.recovery = recovery;
            }
        }
    }
}

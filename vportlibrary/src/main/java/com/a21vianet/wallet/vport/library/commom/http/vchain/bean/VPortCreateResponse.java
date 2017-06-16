package com.a21vianet.wallet.vport.library.commom.http.vchain.bean;

/**
 * Created by wang.rongqiang on 2017/6/9.
 */

public class VPortCreateResponse {

    /**
     * result : {"user":{"address":"1G6DLbZi1A1s1utakX3mLpbkrqe9HtBozg",
     * "controller":"6a07a45c1bbf421be5d9d1a064ab033204a3d443","id":"123456200808081234",
     * "name":"哈哈","phone":"18515288448",
     * "privateKey":"Kwv62ktzJxTtoZJ5sX3P8FD5RptiXWGMJ2U5ZYWytvQJ249rP2qb",
     * "proxy":"46ef13b9935b1a98d402b2fd9296a2472af1a946",
     * "publicKey":"03e4c8693106d5b76a7534a16a0789bd394ee9dde6367b59dffcee5ba73f5611c2",
     * "recovery":"d8f18acef9761f8db3ba21d47d0e73edececb888"}}
     * time : 2017-06-09 11:51:50
     */

    private ResultBean result;
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
        /**
         * user : {"address":"1G6DLbZi1A1s1utakX3mLpbkrqe9HtBozg",
         * "controller":"6a07a45c1bbf421be5d9d1a064ab033204a3d443","id":"123456200808081234",
         * "name":"哈哈","phone":"18515288448",
         * "privateKey":"Kwv62ktzJxTtoZJ5sX3P8FD5RptiXWGMJ2U5ZYWytvQJ249rP2qb",
         * "proxy":"46ef13b9935b1a98d402b2fd9296a2472af1a946",
         * "publicKey":"03e4c8693106d5b76a7534a16a0789bd394ee9dde6367b59dffcee5ba73f5611c2",
         * "recovery":"d8f18acef9761f8db3ba21d47d0e73edececb888"}
         */

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
             * controller : 6a07a45c1bbf421be5d9d1a064ab033204a3d443
             * id : 123456200808081234
             * name : 哈哈
             * phone : 18515288448
             * privateKey : Kwv62ktzJxTtoZJ5sX3P8FD5RptiXWGMJ2U5ZYWytvQJ249rP2qb
             * proxy : 46ef13b9935b1a98d402b2fd9296a2472af1a946
             * publicKey : 03e4c8693106d5b76a7534a16a0789bd394ee9dde6367b59dffcee5ba73f5611c2
             * recovery : d8f18acef9761f8db3ba21d47d0e73edececb888
             */

            private String address;
            private String controller;
            private String id;
            private String name;
            private String phone;
            private String privateKey;
            private String proxy;
            private String publicKey;
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

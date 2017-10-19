package com.a21vianet.wallet.vport.library.commom.http.vchain.bean;


public class Recover {

    public Data data;

    public String apiVersion;

    public class Data {

        public boolean is_success;

        @Override
        public String toString() {
            return "Data{" +
                    "is_success=" + is_success +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Recover{" +
                "data=" + data +
                ", apiVersion='" + apiVersion + '\'' +
                '}';
    }
}

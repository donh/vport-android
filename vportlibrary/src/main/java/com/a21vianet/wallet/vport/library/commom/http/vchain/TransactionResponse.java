package com.a21vianet.wallet.vport.library.commom.http.vchain;


import com.a21vianet.wallet.vport.library.commom.crypto.bean.Contract;
import com.a21vianet.wallet.vport.library.commom.http.vchain.bean.Bind;

public class TransactionResponse {

    public Contract contract;
    public Bind bind;

    @Override
    public String toString() {
        return "TransactionResponse{" +
                "contract=" + contract +
                ", bind=" + bind +
                '}';
    }
}

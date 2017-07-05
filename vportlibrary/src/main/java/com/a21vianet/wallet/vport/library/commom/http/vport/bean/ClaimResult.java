package com.a21vianet.wallet.vport.library.commom.http.vport.bean;


public class ClaimResult {

    public ClaimContent claim;
    public boolean valid;

    @Override
    public String toString() {
        return "ClaimResult{" +
                "claim=" + claim +
                ", valid=" + valid +
                '}';
    }
}

package com.rippleitt.modals;

/**
 * Created by manishautomatic on 20/05/18.
 */

public class UserWalletResponseTemplate {

    private String response_code="";
    private String response_message="";
    private String wallet_balance="";
    private WalletObjectTemplate data[];
//    private WalletObject data[];
//    private WalletOrder orders[];


    public String getWallet_balance() {
        return wallet_balance;
    }

    public void setWallet_balance(String wallet_balance) {
        this.wallet_balance = wallet_balance;
    }

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getResponse_message() {
        return response_message;
    }

    public void setResponse_message(String response_message) {
        this.response_message = response_message;
    }

    public WalletObjectTemplate[] getData() {
        return data;
    }

    public void setData(WalletObjectTemplate[] data) {
        this.data = data;
    }

    //    public WalletObject[] getData() {
//        return data;
//    }
//
//    public void setData(WalletObject[] data) {
//        this.data = data;
//    }
//
//    public WalletOrder[] getOrders() {
//        return orders;
//    }
//
//    public void setOrders(WalletOrder[] orders) {
//        this.orders = orders;
//    }
}

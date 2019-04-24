package com.rippleitt.modals;

public class AccountDetailTemplate {
    private String response_code="";
    private String response_message="";
    private String bank_account_name="";
    private String bank_name="";
    private String account_type="";
    private String holder_type="";
    private String bank_account_bsb="";
    private String bank_account_number="";


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

    public String getAccount_name() {
        return bank_account_name;
    }

    public void setAccount_name(String account_name) {
        this.bank_account_name = account_name;
    }

    public String getAccount_bsb() {
        return bank_account_bsb;
    }

    public void setAccount_bsb(String account_bsb) {
        this.bank_account_bsb = account_bsb;
    }

    public String getAccount_number() {
        return bank_account_number;
    }

    public void setAccount_number(String account_number) {
        this.bank_account_number = account_number;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getHolder_type() {
        return holder_type;
    }

    public void setHolder_type(String holder_type) {
        this.holder_type = holder_type;
    }
}

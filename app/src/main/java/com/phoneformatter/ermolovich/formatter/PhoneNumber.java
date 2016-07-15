package com.phoneformatter.ermolovich.formatter;

public class PhoneNumber {

    private String countryName;
    private String countryCode;
    private String dialCode;
    private String operator;
    private String number;

    private OutputTypeForPhoneNumber typeForPhoneNumber;

    public enum OutputTypeForPhoneNumber {
        DEFAULT,
        CUSTOM
    }

    public PhoneNumber() {
        this.typeForPhoneNumber = OutputTypeForPhoneNumber.DEFAULT;
    }

    public PhoneNumber(String countryName, String countryCode, String dialCode, String operator, String number) {
        this();
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.dialCode = dialCode;
        this.operator = operator;
        this.number = number;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getDialCode() {
        return dialCode;
    }

    public String getNumber() {
        return number;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        if (this.typeForPhoneNumber == OutputTypeForPhoneNumber.CUSTOM) {
            return "";
        } else {
            return "+" + this.dialCode + " " + this.number;
        }
    }
}

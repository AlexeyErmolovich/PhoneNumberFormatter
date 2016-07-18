package com.phoneformatter.ermolovich.formatter;

public class PhoneNumber {

    private String countryName;
    private String countryCode;
    private String dialCode;
    private String operator;
    private String number;
    private String maskNumber;

    public PhoneNumber(String dialCode, String operator, String number) {
        this.dialCode = dialCode;
        this.operator = operator;
        this.number = number;
    }

    public PhoneNumber(String countryName, String countryCode, String dialCode, String number) {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.dialCode = dialCode;
        this.number = number;
    }

    public PhoneNumber(String countryName, String countryCode, String dialCode, String operator,
                       String number, String maskNumber) {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.dialCode = dialCode;
        this.operator = operator;
        this.number = number;
        this.maskNumber = maskNumber;
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

    public String getMaskNumber() {
        return maskNumber;
    }

    @Override
    public String toString() {
        String result = "+";
        if (dialCode != null) {
            result += dialCode;
        }
        if (operator != null) {
            result += operator;
        }
        if (number != null) {
            result += number;
        }
        if (result.length() > 1) {
            return result;
        } else {
            return null;
        }
    }
}

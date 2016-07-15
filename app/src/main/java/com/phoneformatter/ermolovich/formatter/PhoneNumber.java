package com.phoneformatter.ermolovich.formatter;

public class PhoneNumber {

    private String countryName;
    private String countryCode;
    private int dialCode;
    private String number;

    public PhoneNumber(String countryName, String countryCode, int dialCode, String number) {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.dialCode = dialCode;
        this.number = number;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public int getDialCode() {
        return dialCode;
    }

    public String getNumber() {
        return number;
    }
}

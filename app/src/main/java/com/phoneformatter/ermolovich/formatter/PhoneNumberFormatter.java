package com.phoneformatter.ermolovich.formatter;

import android.content.Context;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PhoneNumberFormatter {

    private static PhoneNumberFormatter instance;

    private JSONArray json;

    private PhoneNumberMask phoneNumberMask;

    public PhoneNumberMask getPhoneNumberMask() {
        return phoneNumberMask;
    }

    public void setPhoneNumberMask(PhoneNumberMask phoneNumberMask) {
        this.phoneNumberMask = phoneNumberMask;
    }

    public enum PhoneNumberMask {
        INTERNATIONAL,
        SUBSCRIBER_NUMBER,
        PHONE_NUMBER
    }

    private PhoneNumberFormatter(Context context) {
        this.phoneNumberMask = PhoneNumberMask.INTERNATIONAL;
        readJsonFromFile(context);
    }

    private void readJsonFromFile(Context context) {
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("phonenumber.json")));
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            this.json = new JSONArray(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static PhoneNumberFormatter getInstance(Context context) {
        if (instance == null) {
            instance = new PhoneNumberFormatter(context);
        }
        return instance;
    }

    public PhoneNumber parseToPhoneNumber(String telephoneNumber) {
        PhoneNumber phoneNumber = null;
        checkingPhoneNumber(telephoneNumber);
        telephoneNumber = removeSymbols(telephoneNumber);
        if (!isDigit(telephoneNumber)) {
            return null;
        }

        try {
            phoneNumber = getPhoneNumber(telephoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return phoneNumber;
    }

    public String parseToString(String telephoneNumber) {
        PhoneNumber phoneNumber = parseToPhoneNumber(telephoneNumber);
        if (phoneNumber != null) {
            return parseToString(phoneNumber);
        } else {
            return telephoneNumber;
        }
    }

    public String parseToString(PhoneNumber phoneNumber) {
        String telephoneNumber = null;
        checkingPhoneNumber(phoneNumber.toString());
        if (phoneNumberMask == PhoneNumberMask.INTERNATIONAL) {
            telephoneNumber = getPhoneNumberForInternationalMask(phoneNumber);
        } else if (phoneNumberMask == PhoneNumberMask.SUBSCRIBER_NUMBER) {
            telephoneNumber = getPhoneNumberForSubscriberNumberMask(phoneNumber);
        } else if (phoneNumberMask == PhoneNumberMask.PHONE_NUMBER) {
            telephoneNumber = phoneNumber.toString();
        }
        return telephoneNumber;
    }

    private PhoneNumber getPhoneNumber(String phoneNumber) throws JSONException {
        PhoneNumber number = null;
        String dialCodeNumber = "";

        for (int i = 0; i < phoneNumber.length(); i++) {
            dialCodeNumber += phoneNumber.substring(i, i + 1);
            for (int j = 0; j < this.json.length(); j++) {
                JSONObject jsonObject = this.json.getJSONObject(j);
                if (jsonObject.has("dial_code")) {
                    String dial_code = jsonObject.getString("dial_code").replace("+", "");
                    if (dial_code.equals(dialCodeNumber)) {
                        number = initPhoneNumber(phoneNumber, jsonObject, dial_code);
                    }
                }
            }
            if (i > 5) {
                break;
            }
        }

        if(number==null) {
            number = new PhoneNumber(null, null, phoneNumber);
        }

        return number;
    }

    @NonNull
    private PhoneNumber initPhoneNumber(String phoneNumber, JSONObject jsonObject, String dial_code) throws JSONException {
        PhoneNumber number = null;
        String countryName = null;
        String countryCode = null;
        if (jsonObject.has("name")) {
            countryName = jsonObject.getString("name");
        }
        if (jsonObject.has("code")) {
            countryCode = jsonObject.getString("code");
        }
        if (jsonObject.has("operators")) {
            JSONArray operators = jsonObject.getJSONArray("operators");
            String operator = "";
            phoneNumber = phoneNumber.replaceFirst(dial_code, "");
            for (int i = 0; i < phoneNumber.length(); i++) {
                operator += phoneNumber.substring(i, i + 1);
                for (int j = 0; j < operators.length(); j++) {
                    JSONObject jsonOperator = operators.getJSONObject(j);
                    if (jsonObject.has("code")) {
                        String code = jsonOperator.getString("code");
                        if (code.equals(operator)) {
                            String maskNumber = jsonOperator.getString("format");
                            int length_phone_number = jsonOperator.getInt("length_phone_number");
                            phoneNumber = phoneNumber.replaceFirst(operator, "");
                            if (length_phone_number < phoneNumber.length()) {
                                phoneNumber = phoneNumber.substring(0, length_phone_number);
                            }
                            number = new PhoneNumber(countryName, countryCode, dial_code, operator,
                                    phoneNumber, maskNumber);
                        }
                    }
                }
                if (i > 5) {
                    break;
                }
            }
        }
        if (number == null) {
            number = new PhoneNumber(countryName, countryCode, dial_code, phoneNumber.replaceFirst(dial_code, ""));
        }
        return number;
    }

    @NonNull
    private String getPhoneNumberForInternationalMask(PhoneNumber phoneNumber) {
        String telephoneNumber;
        String defaultNumber = phoneNumber.toString();
        telephoneNumber = "+";
        if (phoneNumber.getDialCode() != null) {
            telephoneNumber += phoneNumber.getDialCode();
            if (defaultNumber.length() > telephoneNumber.length()) {
                telephoneNumber += " ";
            }
        }
        if (phoneNumber.getOperator() != null) {
            telephoneNumber += phoneNumber.getOperator();
            if (defaultNumber.length() + 1 > telephoneNumber.length()) {
                telephoneNumber += " ";
            }
        }
        if (phoneNumber.getNumber() != null) {
            telephoneNumber += convertPhoneNumberToPhoneMask(phoneNumber);
        }
        if (telephoneNumber.length() > 1) {
            return telephoneNumber;
        } else {
            return "";
        }
    }

    @NonNull
    private String getPhoneNumberForSubscriberNumberMask(PhoneNumber phoneNumber) {
        String telephoneNumber;
        String defaultNumber = phoneNumber.toString();
        telephoneNumber = "+";
        if (phoneNumber.getDialCode() != null) {
            telephoneNumber += phoneNumber.getDialCode();
            if (defaultNumber.length() > telephoneNumber.length()) {
                telephoneNumber += " ";
            }
        }
        if (phoneNumber.getOperator() != null) {
            if (defaultNumber.length() + 1 > telephoneNumber.length() + phoneNumber.getOperator().length()) {
                telephoneNumber += "(";
                telephoneNumber += phoneNumber.getOperator();
                telephoneNumber += ")";
                if (defaultNumber.length() + 3 > telephoneNumber.length()) {
                    telephoneNumber += " ";
                }
            } else {
                telephoneNumber += phoneNumber.getOperator();
            }
        }
        if (phoneNumber.getNumber() != null) {
            telephoneNumber += convertPhoneNumberToPhoneMask(phoneNumber).replace(" ", "-");
        }
        if (telephoneNumber.length() > 1) {
            return telephoneNumber;
        } else {
            return "";
        }
    }

    private String convertPhoneNumberToPhoneMask(PhoneNumber phoneNumber) {
        StringBuilder out = new StringBuilder();
        char[] mask = null;
        if (phoneNumber.getMaskNumber() != null) {
            mask = phoneNumber.getMaskNumber().toCharArray();
        } else {
            return phoneNumber.getNumber();
        }
        char[] telephoneNumber = phoneNumber.getNumber().toCharArray();
        for (int i = 0, j = 0; j < telephoneNumber.length; i++) {
            if (mask[i] == 'X') {
                out.append(telephoneNumber[j]);
                j++;
            } else {
                out.append(' ');
            }
        }
        return out.toString();
    }

    private void checkingPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            new NullPointerException();
        }
    }

    private String removeSymbols(String phoneNumber) {
        String result = phoneNumber.replace("+", "");
        result = result.replace("(", "");
        result = result.replace(")", "");
        result = result.replace(" ", "");
        result = result.replace("-", "");
        return result;
    }

    private boolean isDigit(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}

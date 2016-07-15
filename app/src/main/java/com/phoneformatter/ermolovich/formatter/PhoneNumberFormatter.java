package com.phoneformatter.ermolovich.formatter;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PhoneNumberFormatter {

    private static PhoneNumberFormatter instance;
    private Context context;

    private JSONArray json;

    private PhoneNumber phoneNumber;

    private PhoneNumberFormatter(Context context) {
        this.context = context;
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

    public String parseToPhoneNumber(String phoneNumber) {
        checkingPhoneNumber(phoneNumber);
        phoneNumber = removeSymbolPlus(phoneNumber);
        if (!isDigit(phoneNumber)) {
            return null;
        }

        try {
            this.phoneNumber = getPhoneNumber(phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (this.phoneNumber == null) {
            return null;
        } else {
            return this.phoneNumber.toString();
        }
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
                        String countryName = jsonObject.getString("name");
                        String countryCode = jsonObject.getString("code");
//                        number = new PhoneNumber(countryName, countryCode, dial_code, phoneNumber.replace(dial_code, ""));
                        return number;
                    }
                }
            }
        }

        return null;
    }

    private void checkingPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            new NullPointerException();
        }
    }

    private String removeSymbolPlus(String phoneNumber) {
        return phoneNumber.replace("+", "");
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

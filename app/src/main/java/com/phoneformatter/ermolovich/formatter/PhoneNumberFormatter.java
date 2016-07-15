package com.phoneformatter.ermolovich.formatter;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PhoneNumberFormatter {

    private static PhoneNumberFormatter instance;
    private Context context;

    private List<PhoneNumber> history;
    private JSONArray json;

    private PhoneNumber phoneNumber;

    private PhoneNumberFormatter(Context context) {
        this.context =context;
        this.history = new ArrayList<>();
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
        if(instance==null){
            instance = new PhoneNumberFormatter(context);
        }
        return instance;
    }

    public String parseToPhoneNumber(String phoneNumber){
        checkingPhoneNumber(phoneNumber);
        phoneNumber = removeSymbolPlus(phoneNumber);
        if(!isDigit(phoneNumber)){
            return null;
        }

        this.phoneNumber = getPhoneNumber(phoneNumber);

        return phoneNumber;
    }

    private PhoneNumber getPhoneNumber(String phoneNumber){
        PhoneNumber number = null;
        String dialCode;

        for(int i=0;i<phoneNumber.length();i++){

        }

        return number;
    }

    private void checkingPhoneNumber(String phoneNumber){
        if(phoneNumber == null){
            new NullPointerException();
        }
    }

    private String removeSymbolPlus(String phoneNumber){
        return phoneNumber.replace("+","");
    }

    private boolean isDigit(String value){
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}

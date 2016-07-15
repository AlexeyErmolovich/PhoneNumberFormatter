package com.phoneformatter.ermolovich;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.phoneformatter.ermolovich.formatter.PhoneNumberFormatter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PhoneNumberFormatter phoneNumberFormatter = PhoneNumberFormatter.getInstance(this);
        String test = phoneNumberFormatter.parseToPhoneNumber("jh");
        Toast.makeText(this, test, Toast.LENGTH_LONG).show();
    }
}

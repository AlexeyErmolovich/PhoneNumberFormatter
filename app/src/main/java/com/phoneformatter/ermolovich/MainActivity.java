package com.phoneformatter.ermolovich;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.phoneformatter.ermolovich.formatter.PhoneNumber;
import com.phoneformatter.ermolovich.formatter.PhoneNumberFormatter;

public class MainActivity extends AppCompatActivity {

    private PhoneNumberFormatter phoneNumberFormatter;
    private EditText editText;
    private Button button;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.editText = (EditText) findViewById(R.id.editText);
        this.button = (Button) findViewById(R.id.button);
        this.textView1 = (TextView) findViewById(R.id.textView1);
        this.textView2 = (TextView) findViewById(R.id.textView2);
        this.textView3 = (TextView) findViewById(R.id.textView3);

        this.phoneNumberFormatter = PhoneNumberFormatter.getInstance(this);

        this.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            boolean update = false;

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (update) {
                    update = false;
                    phoneNumberFormatter.setPhoneNumberMask(PhoneNumberFormatter.PhoneNumberMask.SUBSCRIBER_NUMBER);
                    String text = phoneNumberFormatter.parseToString(editText.getText().toString());
                    editText.setText(text);
                    editText.setSelection(text.length());
                } else {
                    update = true;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneNumber phoneNumber = phoneNumberFormatter.parseToPhoneNumber(editText.getText().toString());
                if (phoneNumber != null) {
                    phoneNumberFormatter.setPhoneNumberMask(PhoneNumberFormatter.PhoneNumberMask.SUBSCRIBER_NUMBER);
                    textView1.setText(phoneNumberFormatter.parseToString(phoneNumber));
                    phoneNumberFormatter.setPhoneNumberMask(PhoneNumberFormatter.PhoneNumberMask.INTERNATIONAL);
                    textView2.setText(phoneNumberFormatter.parseToString(phoneNumber));
                    phoneNumberFormatter.setPhoneNumberMask(PhoneNumberFormatter.PhoneNumberMask.PHONE_NUMBER);
                    textView3.setText(phoneNumberFormatter.parseToString(phoneNumber));
                }
            }
        });
    }
}

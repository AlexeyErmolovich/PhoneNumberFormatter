package com.phoneformatter.ermolovich;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.phoneformatter.ermolovich.formatter.PhoneNumberFormatter;

public class MainActivity extends AppCompatActivity {

    private PhoneNumberFormatter phoneNumberFormatter;
    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.editText = (EditText) findViewById(R.id.editText);
        this.button = (Button) findViewById(R.id.button);

        this.phoneNumberFormatter = PhoneNumberFormatter.getInstance(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = phoneNumberFormatter.parseToPhoneNumber(editText.getText().toString());
                Toast.makeText(MainActivity.this, phoneNumber, Toast.LENGTH_LONG).show();
            }
        });
    }
}

package ssu.btetris.beacon_test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {
    private EditText viewPhoneNumber;
    private EditText viewMENU;
    private String PhoneNumber;
    private String MENU;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        viewPhoneNumber = (EditText) findViewById(R.id.editview1);
        viewMENU = (EditText) findViewById(R.id.editview2);
        Intent reqmsg = getIntent();
        PhoneNumber = reqmsg.getStringExtra("PhoneNumber");
        MENU = reqmsg.getStringExtra("MENU");
        if (PhoneNumber == null) {
            PhoneNumber = "01012341234";
            MENU = "sandwich";
        }
        viewPhoneNumber.setText(PhoneNumber);
        viewMENU.setText(MENU);
    }
    public void onClick(View v){
        switch(v.getId()) {
            case R.id.confirm:
                Intent resmsg = new Intent();
                resmsg.putExtra("PhoneNumber", viewPhoneNumber.getText().toString());
                resmsg.putExtra("MENU", viewMENU.getText().toString());
                setResult(RESULT_OK, resmsg);
                break;
            case R.id.cancel:
                setResult(RESULT_CANCELED);
                break;
        }
        finish(); // to self-terminate this Activity
    }
}

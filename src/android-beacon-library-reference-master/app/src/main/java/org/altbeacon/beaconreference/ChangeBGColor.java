package org.altbeacon.beaconreference;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class ChangeBGColor extends Activity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful);

        textView = (TextView) findViewById(R.id.textView);


        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                finish();
            }
        }, 2000);// 0.6초 정도 딜레이를 준 후 시작
    }
}

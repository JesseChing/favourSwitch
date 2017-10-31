package com.wei.favourswitch;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    NumberSwitchView numberSwitchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberSwitchView = findViewById(R.id.view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        numberSwitchView.startAnimation();
    }
}

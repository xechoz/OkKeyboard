package com.xechoz.tool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.xechoz.keyboardlistener.KeyboardChangedListener;
import com.xechoz.keyboardlistener.KeyboardResolver;
import com.xechoz.keyboardlistener.OkKeyboard;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = findViewById(R.id.edit);

        OkKeyboard.watch(view, new KeyboardChangedListener() {
            @Override
            public void onKeyboardOpen() {
                Log.d(TAG, "onKeyboardOpen");
            }

            @Override
            public void onKeyboardHide() {
                Log.d(TAG, "onKeyboardHide");
            }
        });
    }
}

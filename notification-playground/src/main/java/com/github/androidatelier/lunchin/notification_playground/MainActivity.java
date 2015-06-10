package com.github.androidatelier.lunchin.notification_playground;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.androidatelier.lunchin.notification.Constants;

public class MainActivity extends Activity {
  private TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    textView = (TextView) findViewById(R.id.text);
    textView.setText(String.valueOf(Constants.ANSWER));
  }
}
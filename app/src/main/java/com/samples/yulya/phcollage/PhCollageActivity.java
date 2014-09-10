package com.samples.yulya.phcollage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PhCollageActivity extends Activity {

    private EditText mLogin;
    private String act = "key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ph_collage);

        mLogin = (EditText)findViewById(R.id.login);

        final Button but = (Button) findViewById(R.id.button1);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userLogin = mLogin.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("userLogin", userLogin);
                intent.putExtra("ac", act);
                intent.setClass(getApplicationContext(),PhotopickerActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ph_collage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

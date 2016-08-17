package auth0.customfieldsdemo.activities;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;

import auth0.customfieldsdemo.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Intent lockIntent = new Intent(this, MyLockActivity.class);
        startActivity(lockIntent);
    }

}

package auth0.customfieldsdemo.activities;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.authentication.ParameterBuilder;
import com.auth0.android.authentication.request.DatabaseConnectionRequest;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.lock.internal.configuration.Connection;
import com.auth0.android.request.Request;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Map;

import auth0.customfieldsdemo.R;
import auth0.customfieldsdemo.application.App;

public class MainActivity extends AppCompatActivity {

    private static final String FIRST_NAME_KEY = "firstName";
    private static final String LAST_NAME_KEY = "lastName";

    private AuthenticationAPIClient apiClient;
    private Credentials loginCredentials;
    private UserProfile profile;
    private TextView welcomeText;
    private TextView firstName;
    private TextView lastName;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        welcomeText = (TextView) findViewById(R.id.welcome_message);
        profileImage = (ImageView) findViewById(R.id.profile_image);
        firstName = (TextView) findViewById(R.id.first_name);
        lastName = (TextView) findViewById(R.id.last_name);
        Auth0 auth0 = new Auth0(getResources().getString(R.string.auth0_client_id), getResources().getString(R.string.auth0_domain));
        this.apiClient = new AuthenticationAPIClient(auth0);
        getUserProfile(App.getInstance().getUserCredentials());
    }

    public void logout(View view) {
        App.getInstance().setUserCredentials(null);

        startActivity(new Intent(this, StartActivity.class));
    }

    private void getUserProfile (Credentials credentials) {
        Request<UserProfile, AuthenticationException> request = apiClient.tokenInfo(credentials.getIdToken());
        request.start(new BaseCallback<UserProfile, AuthenticationException>() {
            @Override
            public void onSuccess(UserProfile payload) {
                Log.d("MainActivity", payload.getEmail());
                Log.d("user.user_id", payload.getId());
                profile = payload;
                updateUI(payload);
            }

            @Override
            public void onFailure(AuthenticationException error) {
                Snackbar.make(MainActivity.this.getWindow().getDecorView().getRootView(), "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void updateUI (final UserProfile profile) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                welcomeText.setText("Welcome " + profile.getEmail());
                if (profile.getPictureURL() != null) {
                    ImageLoader.getInstance().displayImage(profile.getPictureURL(), profileImage);
                }
                if (profile.getUserMetadata() != null) {
                    String fName = (String) profile.getUserMetadata().get(FIRST_NAME_KEY);
                    String lName = (String) profile.getUserMetadata().get(LAST_NAME_KEY);
                    firstName.setText("First Name: " + fName);
                    lastName.setText("Last Name: " + lName);
                }
            }
        });
    }

    public void changePassword(View v) {
        Log.d("change password", profile.getEmail());

        apiClient.resetPassword(profile.getEmail(), "Username-Password-Authentication")
                .start(new BaseCallback<Void, AuthenticationException>() {
                    @Override
                    public void onSuccess(Void payload) {
                        Log.d("success", "Change password requested");
                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                        Log.d("error", "Failed Change password requested");
                    }
                });
    }
}

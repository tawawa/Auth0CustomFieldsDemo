package auth0.customfieldsdemo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.authentication.ParameterBuilder;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.internal.configuration.Theme;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.result.Credentials;
import com.auth0.android.lock.utils.CustomField;
import com.auth0.android.provider.WebAuthActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import auth0.customfieldsdemo.R;
import auth0.customfieldsdemo.application.App;
/**
 * Created by vikasjayaram on 9/08/2016.
 */
public class MyLockActivity extends Activity {

    private Lock lock;
    private AuthenticationException authException;
    public static final String EMAIL_REGEX = Patterns.EMAIL_ADDRESS.pattern();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("EMAIL_REGEX", EMAIL_REGEX);
        Auth0 auth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
        // Add custom fields to Sign up form
        CustomField firstName = new CustomField(R.drawable.com_auth0_lock_ic_username, CustomField.FieldType.TYPE_NAME, "firstName", R.string.first_name_hint);
        CustomField lastName = new CustomField(R.drawable.com_auth0_lock_ic_username, CustomField.FieldType.TYPE_NAME, "lastName", R.string.last_name_hint);
        List<CustomField> customFields = new ArrayList<>();
        customFields.add(firstName);
        customFields.add(lastName);

        // Add additional authParams
        ParameterBuilder builder = ParameterBuilder.newBuilder();
        // Additional authentication parameter
        Map<String, Object> authenticationParameters = builder.setScope("openid offline_access email name").asDictionary();

        this.lock = Lock.newBuilder(auth0, callback)
                //Add parameters to the build
                .withSignUpFields(customFields)
                .withConnectionScope("facebook", "openid email")
                .withConnectionScope("google-oauth2", "openid email")
                .withAuthenticationParameters(authenticationParameters)
                .build(this);
        //lock.onCreate(this);



        startActivity(this.lock.newIntent(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Your own Activity code
        lock.onDestroy(this);
        lock = null;
    }

    private LockCallback callback = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {
            Toast.makeText(getApplicationContext(), "Log In - Success", Toast.LENGTH_SHORT).show();
            App.getInstance().setUserCredentials(credentials);
            Log.d("refresh token", credentials.getRefreshToken());
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        @Override
        public void onCanceled() {
            Toast.makeText(getApplicationContext(), "Log In - Cancelled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(LockException error) {
            Toast.makeText(getApplicationContext(), "Log In - Error Occurred", Toast.LENGTH_SHORT).show();
        }
    };


}

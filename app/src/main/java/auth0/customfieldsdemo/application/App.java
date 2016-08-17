package auth0.customfieldsdemo.application;

import android.app.Application;
import com.auth0.android.result.Credentials;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by vikasjayaram on 9/08/2016.
 */
public class App extends Application {
    Credentials userCredentials;

    private static App appSingleton;

    public static App getInstance() {
        return appSingleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appSingleton = this;
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(configuration);
    }


    public Credentials getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(Credentials userCredentials) {
        this.userCredentials = userCredentials;
    }
}

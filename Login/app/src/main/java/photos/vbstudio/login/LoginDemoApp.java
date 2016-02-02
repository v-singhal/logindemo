package photos.vbstudio.login;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class LoginDemoApp extends Application {

    private final String TAG = this.getClass().getSimpleName();

    public static final String BASE_URL = "http://staging.curofy.com/";
    private BaseActivity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        //INITIALIZE CALLIGRAPHY
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath(getResources().getString(R.string.custom_font_normal))
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    public BaseActivity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(BaseActivity mCurrentActivity) {
        this.currentActivity = mCurrentActivity;
    }
}

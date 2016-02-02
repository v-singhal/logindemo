package photos.vbstudio.login.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class LoginDemoSharedPreferences implements SharedPreferences {

    private final static String LOGIN_DEMO_INFO = "loginDemoInfo";
    private final static String IS_LOGGED_IN = "isLoggedIn";


    /**
     * Get SharedPreferences for LoginDemoInfo
     *
     * @param context
     * @return SharedPreferences belonging to "loginDemoInfo"
     */
    public static SharedPreferences getLoginDemoSharedPreferences(Context context) {
        return context.getSharedPreferences(LOGIN_DEMO_INFO, Context.MODE_PRIVATE);
    }

    /**
     * Clear all SharedPreferences for LoginDemoInfo
     *
     * @param context
     */
    public static void clearAll(Context context) {
        Editor editor = getLoginDemoSharedPreferences(context).edit();
        editor.clear().commit();
    }

    //******************************************/

    public static boolean getIsLoggedIn(Context context) {
        return getLoginDemoSharedPreferences(context).getBoolean(IS_LOGGED_IN, false);
    }

    public static boolean setLastLocationEventMode(Context context, boolean isLoggedIn) {
        Editor editor = getLoginDemoSharedPreferences(context).edit();
        return editor.putBoolean(IS_LOGGED_IN, isLoggedIn).commit();
    }
}

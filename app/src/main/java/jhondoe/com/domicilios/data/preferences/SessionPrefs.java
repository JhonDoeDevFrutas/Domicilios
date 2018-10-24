package jhondoe.com.domicilios.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import static android.content.Context.MODE_PRIVATE;

public class SessionPrefs {

    public static final String PREFS_NAME = "CLIENT_PREFS";

    public static final String USER_KEY = "USER_NAME";
    public static final String PWD_KEY = "PASSWORD";

    public static final String PHONE = "PHONE";

    private final SharedPreferences mPrefs;

    private boolean mIsLoggedIn = false;

    private static SessionPrefs INSTANCE;

    private SessionPrefs(Context context){
        mPrefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        mIsLoggedIn = !TextUtils.isEmpty(mPrefs.getString(PHONE, null));
    }

    public static SessionPrefs get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SessionPrefs(context);
        }
        return INSTANCE;
    }

    public boolean isLoggedIn() {
        return mIsLoggedIn;
    }


    public void saveUser(String phone){
        SharedPreferences.Editor editor = mPrefs.edit();
        /*editor.putString(USER_KEY, user);
        editor.putString(PWD_KEY, password);*/

        editor.putString(PHONE, phone);
        editor.apply();

        mIsLoggedIn = true;
    }

    public void logOut(){
        mIsLoggedIn = false;

        SharedPreferences.Editor editor = mPrefs.edit();
        editor.remove(USER_KEY);
        editor.remove(PWD_KEY);
        editor.clear();
        editor.apply();
    }

    public String getPhone(){
        return mPrefs.getString(PHONE, null);
    }
}

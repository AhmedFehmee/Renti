package extra4it.fahmy.com.rentei;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ahmed Aziz on 1/14/2018.
 */

public class SharedPref {

    final static String FileName = "UserFile";

    public static String readSharedPref (Context context , String sittingName , String defaultValue ){
        SharedPreferences sharedPrefe = context.getSharedPreferences(FileName ,Context.MODE_PRIVATE);
        return sharedPrefe.getString(sittingName,defaultValue);
    }

    public static void saveSharedPref (Context ctx , String sittingName,String settingVaule){
        SharedPreferences sharedPref = ctx.getSharedPreferences(FileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPref.edit();
        editor.putString(sittingName,settingVaule);
        editor.apply();
    }
}

package extra4it.fahmy.com.rentei;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import me.anwarshahriar.calligrapher.Calligrapher;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Ahmed Aziz on 1/11/2018.
 */

public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Cairo-SemiBold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}

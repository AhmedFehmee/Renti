package extra4it.fahmy.com.rentei;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;

import extra4it.fahmy.com.rentei.Model.TokenModel;
import extra4it.fahmy.com.rentei.R;
import extra4it.fahmy.com.rentei.Rest.ApiClient;
import extra4it.fahmy.com.rentei.Rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;

import static android.content.ContentValues.TAG;

/**
 * Created by abdelmageed on 18/06/16.
 */
public class RegistrationServices extends IntentService {

    SharedPreferences.Editor prefEditor;
    public static final String mypreference = "mypref";

    public static final String REGISTRATION_COMPLETE = "RegistrationComplete";
    public static final String REGISTRATION_ERROR = "RegistrationError";

    SharedPreferences preferences;
    private String token;
    private String refreshed;
    String userId;
    String MY_PREFS_NAME = "UserFile";
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    ApiInterface apiService;

    public RegistrationServices() {
        super("RegistrationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // get Default Shard Preferences

        // get token from Firebase
        token = FirebaseInstanceId.getInstance().getToken();
        // check if intent is null or not if it isn't null we will ger refreshed value and
        // if its true we will override token_sent value to false and apply
        if (intent.getExtras() != null) {
            refreshed = intent.getExtras().getString("Token_Shared");
            //MainApp.mSharedPreferences.edit().putString(Constants.Device_Token_Shared,refreshed).commit();
        } else {
            //MainApp.mSharedPreferences.edit().putString(Constants.Device_Token_Shared,token).commit();
        }
        registerGCM();
    }


    public void registerGCM() {
        Intent registrationComplete = null;
        //InstanceID instanceID = InstanceID.getInstance(this);

        Log.d("DAMN", token + " ");

        registrationComplete = new Intent(REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", token);

        sendTokenToServer(token);
        registrationComplete = new Intent(REGISTRATION_ERROR);

        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    public void sendTokenToServer(final String token) {

       // userId = cn.getId();

        sendTokenService();
    }

    private void sendTokenService() {
        prefs = getSharedPreferences("UserFile", MODE_PRIVATE);
        final String userId = prefs.getString("id", null);

        apiService = ApiClient.getClient(ApiInterface.class);
        Call<TokenModel> call = apiService.refreshToken(userId,"android",token);
        call.enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, retrofit2.Response<TokenModel> response) {
                if (response.body() != null) {

                    Log.i("tokennnn" , " " + response.body().getStatus() + "//"+userId);
                }
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }
}

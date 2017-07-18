package ra.sumbayak.aparinspector.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ra.sumbayak.aparinspector.BaseActivity;
import ra.sumbayak.aparinspector.R;
import ra.sumbayak.aparinspector.api.ApiInterfaceBuilder;
import ra.sumbayak.aparinspector.api.QRCallback;
import ra.sumbayak.aparinspector.inspector.InspectorActivity;
import ra.sumbayak.aparinspector.supervisor.SupervisorActivity;
import retrofit2.Response;

import static ra.sumbayak.aparinspector.Constant.*;

public class LoginActivity extends BaseActivity {
    
    @BindView (R.id.username) EditText et_username;
    @BindView (R.id.password) EditText et_password;
    private LoginCredentials loginCredentials;
    
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        
        SharedPreferences sp = getSharedPreferences (SPNAME, MODE_PRIVATE);
        if (sp.contains (SPKEY_TOKEN) && sp.contains (SPKEY_ACCESS_LEVEL))
            authenticate ();
        else
            sp
                .edit ()
                .remove (SPKEY_TOKEN)
                .remove (SPKEY_ACCESS_LEVEL)
                .apply ();
        
        setContentView (R.layout.activity_login);
        ButterKnife.bind (this);
    }
    
    @OnClick (R.id.login)
    public void login () {
        showProgressDialog ();
        loginCredentials = new LoginCredentials ();
        
        ApiInterfaceBuilder
            .build (this)
            .token (loginCredentials)
            .enqueue (new QRCallback<JsonObject> () {
                @Override
                public void onSuccessful (@NonNull Response<JsonObject> response) {
                    Log.e ("LoginActivity", response.toString ());
                    JsonObject body = response.body ();
                    assert body != null;
                    getSharedPreferences (SPNAME, MODE_PRIVATE)
                        .edit ()
                        .putString (SPKEY_TOKEN, body
                            .get (SERIALIZED_NAME_TOKEN)
                            .getAsString ())
                        .apply ();
                    
                    ApiInterfaceBuilder
                        .build (getApplicationContext ())
                        .user (loginCredentials.username)
                        .enqueue (new QRCallback<JsonObject> () {
                            @Override
                            public void onSuccessful (@NonNull Response<JsonObject> response) {
                                Log.e ("LoginActivity", response.toString ());
                                JsonObject body = response.body ();
                                assert body != null;
                                getSharedPreferences (SPNAME, MODE_PRIVATE)
                                    .edit ()
                                    .putInt (SPKEY_ACCESS_LEVEL, body
                                        .get (SERIALIZED_NAME_ACCESS_LEVEL)
                                        .getAsInt ())
                                    .apply ();
                                authenticate ();
                            }
    
                            @Override
                            protected void onFailure () {
                                LoginActivity.this.onFailure ();
                            }
                        });
                }
    
                @Override
                protected void onFailure () {
                    LoginActivity.this.onFailure ();
                }
            });
    }
    
    private void authenticate () {
        int access_level = getSharedPreferences (SPNAME, MODE_PRIVATE)
            .getInt (SPKEY_ACCESS_LEVEL, 2);
        if (access_level == 2)
            startActivity (new Intent (this, InspectorActivity.class));
        else
            startActivity (new Intent (this, SupervisorActivity.class));
        finish ();
    }
    
    private void onFailure () {
        dismissProgressDialog ();
        Toast.makeText (LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show ();
    }
    
    @Override
    public void onUpdate () {
        
    }
    
    @Override
    public void onUpdateFailed () {
        
    }
    
    public class LoginCredentials {
        @SerializedName ("username") String username;
        @SerializedName ("password") String password;
        
        private LoginCredentials () {
            username = et_username.getText ().toString ();
            password = et_password.getText ().toString ();
        }
    }
}

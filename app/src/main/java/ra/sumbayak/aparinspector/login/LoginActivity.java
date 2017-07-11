package ra.sumbayak.aparinspector.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ra.sumbayak.aparinspector.GlobalLoadingDialog;
import ra.sumbayak.aparinspector.R;
import ra.sumbayak.aparinspector.api.ApiInterface;
import ra.sumbayak.aparinspector.api.ApiInterfaceBuilder;
import ra.sumbayak.aparinspector.home.HomeActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ra.sumbayak.aparinspector.Constant.*;

public class LoginActivity extends AppCompatActivity {
    
    @BindView (R.id.username) EditText username;
    @BindView (R.id.password) EditText password;
    private ApiInterface apiInterface;
    
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        
        if (getSharedPreferences (SPNAME, MODE_PRIVATE).contains (SPKEY_TOKEN))
            authenticate ();
        
        setContentView (R.layout.activity_login);
        ButterKnife.bind (this);
        apiInterface = ApiInterfaceBuilder.build (this);
    }
    
    @OnClick (R.id.login)
    public void login () {
        GlobalLoadingDialog.show (this);
        
        Call<JsonObject> call = apiInterface.token (new LoginCredentials ());
        call.enqueue (new Callback<JsonObject> () {
            @Override
            public void onResponse (@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful ()) {
                    JsonObject body = response.body ();
                    assert body != null;
                    String token = body.get (SERIALIZED_NAME_TOKEN).getAsString ();
                    
                    SharedPreferences.Editor editor = getSharedPreferences (SPNAME, MODE_PRIVATE).edit ();
                    editor.putString (SPKEY_TOKEN, token);
                    editor.apply ();
                    
                    authenticate ();
                    GlobalLoadingDialog.hide ();
                }
                else {
                    GlobalLoadingDialog.hide ();
                    Toast.makeText (LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show ();
                }
            }
    
            @Override
            public void onFailure (@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                t.printStackTrace ();
                GlobalLoadingDialog.hide ();
                Toast.makeText (LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show ();
            }
        });
    }
    
    private void authenticate () {
        startActivity (new Intent (this, HomeActivity.class));
        finish ();
    }
    
    public class LoginCredentials {
        @SerializedName ("username") String username;
        @SerializedName ("password") String password;
        
        LoginCredentials () {
            this.username = LoginActivity.this.username.getText ().toString ();
            this.password = LoginActivity.this.password.getText ().toString ();
        }
    }
}

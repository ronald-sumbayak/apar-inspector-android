package ra.sumbayak.aparinspector.api;

import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import ra.sumbayak.aparinspector.home.InspectionFragment;
import ra.sumbayak.aparinspector.login.LoginActivity;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiInterface {
    @POST ("api-token-auth") Call<JsonObject> token (@Body LoginActivity.LoginCredentials credentials);
    @GET ("all") Call<List<Apar>> all ();
    @GET ("{id}") Call<Apar> get (@Path ("id") int id);
    @PUT ("{id}") Call<Apar> put (@Path ("id") int id, @Body Apar apar);
    @DELETE ("{id}") Call<ResponseBody> delete (@Path ("id") int id);
    @POST ("add") Call<Apar> add (@Body Apar apar);
    @POST ("inspect/{id}") Call<Apar> inspect (@Path ("id") int id, @Body InspectionFragment.InspectionForm form);
    @POST ("refill/{id}") Call<Apar> refill (@Path ("id") int id);
}

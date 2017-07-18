package ra.sumbayak.aparinspector.api;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import ra.sumbayak.aparinspector.inspector.InspectionFragment;
import ra.sumbayak.aparinspector.login.LoginActivity;
import ra.sumbayak.aparinspector.supervisor.InspectionListPage;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @POST ("token") Call<JsonObject> token (@Body LoginActivity.LoginCredentials credentials);
    @GET ("user/{username}") Call<JsonObject> user (@Path ("username") String username);
    @GET ("apar/all") Call<List<Apar>> aparAll ();
    @GET ("apar/detail/{id}") Call<Apar> aparDetail (@Path ("id") int id);
    @GET ("inspection/all") Call<List<Inspection>> inspectionAll ();
    @POST ("inspect") Call<JSONObject> inspect (@Body InspectionFragment.InspectionForm form);
    @POST ("verify") Call<JSONObject> verify (@Body InspectionListPage.VerificationForm form);
}

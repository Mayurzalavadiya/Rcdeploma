package rcdiploma.app.RetrofitClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCountryData {

    @SerializedName("Status")
    @Expose
    public String status;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("response")
    @Expose
    public List<GetCountryResponse> response = null;


    public class GetCountryResponse {
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("image")
        @Expose
        public String image;
    }
}

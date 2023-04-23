package rcdiploma.app.RetrofitClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetBannerData {

    @SerializedName("Status")
    @Expose
    public String status;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("response")
    @Expose
    public List<GetBannerResponse> response = null;


    public class GetBannerResponse {
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("image")
        @Expose
        public String image;
    }
}

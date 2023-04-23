package rcdiploma.app.RetrofitClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetNotificationData {

    @SerializedName("Status")
    @Expose
    public String status;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("response")
    @Expose
    public List<GetNotificationResponse> response = null;

    public class GetNotificationResponse {
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("message")
        @Expose
        public String message;
        @SerializedName("isRead")
        @Expose
        public String isRead;
        @SerializedName("created_date")
        @Expose
        public String createdDate;
    }
}

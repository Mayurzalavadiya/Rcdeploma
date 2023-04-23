package rcdiploma.app.RetrofitClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetWishlistData {

    @SerializedName("Status")
    @Expose
    public String status;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("response")
    @Expose
    public List<GetWishlistResponse> response = null;

    public class GetWishlistResponse {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("productId")
        @Expose
        public String productId;
        @SerializedName("userId")
        @Expose
        public String userId;
        @SerializedName("created_date")
        @Expose
        public String createdDate;
        @SerializedName("subCategoryId")
        @Expose
        public String subCategoryId;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("price")
        @Expose
        public String price;
        @SerializedName("unit")
        @Expose
        public String unit;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("image")
        @Expose
        public String image;

    }
}

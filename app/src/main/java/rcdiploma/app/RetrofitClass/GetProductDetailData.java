package rcdiploma.app.RetrofitClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetProductDetailData {

    @SerializedName("Status")
    @Expose
    public String status;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("response")
    @Expose
    public List<GetProductDetailResponse> response = null;

    public class GetProductDetailResponse {
        @SerializedName("id")
        @Expose
        public String id;
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

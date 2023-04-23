package rcdiploma.app.RetrofitClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetProductData {

    @SerializedName("Status")
    @Expose
    public String status;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("CartTotal")
    @Expose
    public String cartTotal;
    @SerializedName("response")
    @Expose
    public List<GetProductResponse> response = null;

    public class GetProductResponse {
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
        @SerializedName("wishlistFlag")
        @Expose
        public String wishlistFlag;
        @SerializedName("cartId")
        @Expose
        public String cartId;
        @SerializedName("cartQty")
        @Expose
        public String cartQty;
        @SerializedName("cartTotalPrice")
        @Expose
        public String cartTotalPrice;
    }
}

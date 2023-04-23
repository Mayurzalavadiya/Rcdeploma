package rcdiploma.app.RetrofitClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCartData {

    @SerializedName("Status")
    @Expose
    public String status;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("cartId")
    @Expose
    public Integer cartId;

}

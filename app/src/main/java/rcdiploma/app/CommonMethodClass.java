package rcdiploma.app;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class CommonMethodClass {

    public CommonMethodClass(Context context,String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public CommonMethodClass(View view,String message){
        Snackbar.make(view,message, Snackbar.LENGTH_SHORT).show();
    }

    public CommonMethodClass(Context context,Class<?> nextClass){
        Intent intent = new Intent(context,nextClass);
        context.startActivity(intent);
    }

}

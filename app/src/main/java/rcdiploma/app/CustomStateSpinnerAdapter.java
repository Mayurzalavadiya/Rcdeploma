package rcdiploma.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomStateSpinnerAdapter extends ArrayAdapter<StateSpinnerList> {

    Context context;
    ArrayList<StateSpinnerList> arrayList;

    public CustomStateSpinnerAdapter(CustomSpinnerActivity customSpinnerActivity, ArrayList<StateSpinnerList> arrayList) {
        super(customSpinnerActivity,0,arrayList);
        this.context = customSpinnerActivity;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner,parent,false);
        ImageView imageView = convertView.findViewById(R.id.custom_spinner_iv);
        TextView name = convertView.findViewById(R.id.custom_spinner_name);
        name.setText(arrayList.get(position).getName());
        //imageView.setImageResource(arrayList.get(position).getImage());
        if(arrayList.get(position).getImage().equalsIgnoreCase("")){
         imageView.setVisibility(View.GONE);
        }
        else {
            imageView.setVisibility(View.VISIBLE);
            Picasso.get().load(arrayList.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(imageView);
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner,parent,false);
        ImageView imageView = convertView.findViewById(R.id.custom_spinner_iv);
        TextView name = convertView.findViewById(R.id.custom_spinner_name);
        name.setText(arrayList.get(position).getName());
        //imageView.setImageResource(arrayList.get(position).getImage());
        if(arrayList.get(position).getImage().equalsIgnoreCase("")){
            imageView.setVisibility(View.GONE);
        }
        else {
            imageView.setVisibility(View.VISIBLE);
            Picasso.get().load(arrayList.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(imageView);
        }
        return convertView;
    }
}

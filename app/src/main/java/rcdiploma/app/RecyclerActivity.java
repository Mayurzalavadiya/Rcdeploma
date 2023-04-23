package rcdiploma.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String[] nameArray = {"Android", "Java", "Php", "IOS", "Flutter"};
    int[] imageArray = {R.drawable.android_image,
            R.drawable.java,
            R.drawable.calendar_icon,
            R.drawable.clock_24,
            R.drawable.flutter_logo};
    ArrayList<RecyclerList> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        recyclerView = findViewById(R.id.recycler_view);

        //Recyclerview As a List
        //recyclerView.setLayoutManager(new LinearLayoutManager(RecyclerActivity.this));

        //Recyclerview As a Grid
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        //Recyclerview As a Horizontal Scroll
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        arrayList = new ArrayList<>();
        for (int i = 0; i < nameArray.length; i++) {
            RecyclerList list = new RecyclerList();
            list.setName(nameArray[i]);
            list.setImage(imageArray[i]);
            arrayList.add(list);
        }
        RecyclerListAdapter adapter = new RecyclerListAdapter(RecyclerActivity.this, arrayList);
        recyclerView.setAdapter(adapter);
    }

    private class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.MyHolder> {

        Context context;
        ArrayList<RecyclerList> arrayList;

        public RecyclerListAdapter(RecyclerActivity recyclerActivity, ArrayList<RecyclerList> arrayList) {
            this.context = recyclerActivity;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recycler,parent,false);
            return new MyHolder(view);
        }

        public class MyHolder extends RecyclerView.ViewHolder {

            TextView name;
            ImageView imageView;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.custom_recycler_name);
                imageView = itemView.findViewById(R.id.custom_recycler_image);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.name.setText(arrayList.get(position).getName());
            holder.imageView.setImageResource(arrayList.get(position).getImage());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new CommonMethodClass(context,arrayList.get(position).getName());
                }
            });

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}
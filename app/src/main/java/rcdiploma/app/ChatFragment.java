package rcdiploma.app;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ChatFragment extends Fragment {

    GridView listView;

    String[] nameArray = {"Android", "Java", "Php", "IOS", "Flutter"};
    int[] imageArray = {R.drawable.android_image, R.drawable.java, R.drawable.calendar_icon, R.drawable.clock_24, R.drawable.flutter_logo};
    String[] priceArray = {"10000","12000","8000","15000","18000"};

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        listView = view.findViewById(R.id.chat_listview);
        ChatAdapter adapter = new ChatAdapter(getActivity(), nameArray, imageArray,priceArray);
        listView.setAdapter(adapter);
        return view;
    }

    private class ChatAdapter extends BaseAdapter {

        Context context;
        String[] nameArray;
        int[] imageArray;
        String[] priceArray;

        public ChatAdapter(FragmentActivity activity, String[] nameArray, int[] imageArray,String[] priceArray) {
            this.context = activity;
            this.nameArray = nameArray;
            this.imageArray = imageArray;
            this.priceArray = priceArray;
        }


        @Override
        public int getCount() {
            return nameArray.length;
        }

        @Override
        public Object getItem(int i) {
            return nameArray[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_chat, null);
            ImageView imageView = view.findViewById(R.id.custom_chat_iv);
            TextView name = view.findViewById(R.id.custom_chat_name);
            TextView price = view.findViewById(R.id.custom_chat_price);

            name.setText(nameArray[i]);
            price.setText("Rs."+priceArray[i]);
            imageView.setImageResource(imageArray[i]);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new CommonMethodClass(context,nameArray[i]+" Rs."+priceArray[i]);
                }
            });

            return view;
        }
    }

}
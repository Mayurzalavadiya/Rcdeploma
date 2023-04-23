package rcdiploma.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyHolder> {

    Context context;
    ArrayList<NotificationList> arrayList;

    public NotificationAdapter(NotificationActivity notificationActivity, ArrayList<NotificationList> arrayList) {
        this.context = notificationActivity;
        this.arrayList = arrayList;
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_notification,parent,false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView message,date;

        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.custom_notification_card);
            message = itemView.findViewById(R.id.custom_notification_message);
            date = itemView.findViewById(R.id.custom_notification_date);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        holder.message.setText(arrayList.get(position).getMessage());
        holder.date.setText(arrayList.get(position).getCreateDate());

        if(arrayList.get(position).getIsRead().equals("read")){
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }
        else{
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.light_gray));
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

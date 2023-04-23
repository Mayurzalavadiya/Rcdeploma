package rcdiploma.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyHolder> {

    Context context;
    ArrayList<VideoList> arrayList;
    SharedPreferences sp;

    public VideoListAdapter(VideoListActivity videoListActivity, ArrayList<VideoList> arrayList) {
        this.context = videoListActivity;
        this.arrayList = arrayList;
        sp = context.getSharedPreferences(ConstantUrl.PREF,Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_video_list,parent,false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        /*ImageView imageView;
        TextView title,description,date;*/

        VideoView videoView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.custom_video_list_videoview);
            /*imageView = itemView.findViewById(R.id.custom_video_list_image);
            title = itemView.findViewById(R.id.custom_video_list_title);
            description = itemView.findViewById(R.id.custom_video_list_description);
            date = itemView.findViewById(R.id.custom_video_list_date);*/
        }

        void setVideoData(VideoList videoItem) {
            videoView.setVideoPath(videoItem.getVideo());

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    //mediaPlayer = mp;
                    mp.start();
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //mediaPlayer = mp;
                    mp.start();
                }
            });

            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    videoView.setVideoPath(videoItem.getVideo());
                    videoView.start();
                    return true;
                }
            });

        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        /*holder.title.setText(arrayList.get(position).getTitle());
        holder.description.setText(arrayList.get(position).getDescription());
        holder.date.setText(arrayList.get(position).getDate());
        Glide.with(context).asBitmap().load(arrayList.get(position).getVideo()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);
        //Picasso.get().load(arrayList.get(position).getVideo()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantUrl.VIDEO_URL,arrayList.get(position).getVideo()).commit();
                new CommonMethodClass(context,VideoDetailActivity.class);
            }
        });*/
        holder.setVideoData(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

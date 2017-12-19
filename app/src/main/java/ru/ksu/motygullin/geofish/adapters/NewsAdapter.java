package ru.ksu.motygullin.geofish.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ru.ksu.motygullin.geofish.GlideApp;
import ru.ksu.motygullin.geofish.R;
import ru.ksu.motygullin.geofish.entities.Datum;
import ru.ksu.motygullin.geofish.entities.Posts;

import static android.content.Context.MODE_PRIVATE;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    Context context;
    Posts posts;

    SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.shared_preferences_name), MODE_PRIVATE);

    public NewsAdapter(Context context, Posts posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item, parent, false);
        return new NewsAdapter.NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        final Datum post = posts.getData().get(position);

        holder.username.setText(preferences.getString("name", "") + " " + preferences.getString("surname", ""));
        holder.text.setText(post.getAttributes().getText());
        GlideApp.with(context)
                .load(preferences.getString("photo", ""))
                .apply(RequestOptions.circleCropTransform())
                .into(holder.profilePhoto);

        GlideApp.with(context)
                .load(post.getRelationships().getPostPhotos().getData())
                .into(holder.contentPhoto);




    }

    @Override
    public int getItemCount() {
        return posts.getData().size();
    }

    public void setData(Posts data) {
        posts = data;
        notifyDataSetChanged();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView  text;
        TextView  likes;
        TextView  comments;
        ImageView profilePhoto;
        ImageView contentPhoto;
        TextView  timeStamp;
        TextView  username;

        public NewsViewHolder(View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.content_text);
            likes = itemView.findViewById(R.id.likes);
            comments = itemView.findViewById(R.id.comments);
            profilePhoto = itemView.findViewById(R.id.author_photo);
            contentPhoto = itemView.findViewById(R.id.post_photo);
            timeStamp = itemView.findViewById(R.id.time_of_post);
            username = itemView.findViewById(R.id.name_of_author);
        }
    }
}

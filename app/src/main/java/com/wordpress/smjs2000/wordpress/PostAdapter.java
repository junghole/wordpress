package com.wordpress.smjs2000.wordpress;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by smjs2000 on 6/4/15.
 */
public class PostAdapter extends BaseAdapter {

    Context context;
    int layout = R.layout.item_post;
    ArrayList<PostItem> list;
    LayoutInflater inflater;

    public PostAdapter(Context context, ArrayList<PostItem> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int index=position;
        if (convertView==null){
            convertView=inflater.inflate(layout, parent, false);
        }
        ImageLoader imageLoader = ImageLoader.getInstance();

        ((TextView) convertView.findViewById(R.id.post_title)).setText(list.get(index).getTitle());
        ((TextView) convertView.findViewById(R.id.post_date)).setText(list.get(index).getDate());
        ((TextView) convertView.findViewById(R.id.post_author)).setText(list.get(index).getAuthor());

        ImageView imageView = (ImageView) convertView.findViewById(R.id.post_image);
        imageLoader.displayImage(list.get(index).getImgUrl(), imageView);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HTMLActivity.class);
                // TODO put title and url
                intent.putExtra("title", list.get(index).getTitle());
                intent.putExtra("url", list.get(index).getPostUrl());
                intent.putExtra("content", list.get(index).getContent());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}

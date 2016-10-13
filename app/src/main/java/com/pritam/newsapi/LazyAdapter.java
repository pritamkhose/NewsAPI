package com.pritam.newsapi;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pritam.newsapi.utility.ImageLoader;


public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;
    static final String[] str = {"name", "description", "category", "urlsToLogo_s"};
    static final String[] str1 = {"title", "description", "author", "urlToImage"};

    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

        artist.setTypeface(MainActivity.t2);
        duration.setTypeface(MainActivity.t3);

        HashMap<String, String> hm = new HashMap<String, String>();
        hm = data.get(position);

        // Setting all values in listview
        if(activity instanceof MainActivity) {
            title.setTypeface(MainActivity.t4);
            title.setText(hm.get(str[0]));
            artist.setText(hm.get(str[1]));
            duration.setText(hm.get(str[2]));
            imageLoader.DisplayImage(hm.get(str[3]), thumb_image);
        }
        else {
            title.setTypeface(MainActivity.t2);
            title.setText(hm.get(str1[0]));
            artist.setText(hm.get(str1[1]));
            duration.setText("");//duration.setText(hm.get(str1[2]));
            imageLoader.DisplayImage(hm.get(str1[3]), thumb_image);
        }
        return vi;
    }
}
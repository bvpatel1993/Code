package com.darkweb.android.testing2;


import java.util.ArrayList;
import java.util.HashMap;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ImageLoader imageLoader;
    HashMap<String, String> resultp = new HashMap<String, String>();

    public ListViewAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
        imageLoader = new ImageLoader(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare Variables
        TextView fname,lname,des;

        ImageView flag;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.listview_item, parent,false);
        // Get the position
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        fname = (TextView) itemView.findViewById(R.id.display_id);
        lname = (TextView) itemView.findViewById(R.id.display_name);


        flag = (ImageView) itemView.findViewById(R.id.icon);


        fname.setText(resultp.get(MainActivity.fname1));
        lname.setText(resultp.get(MainActivity.lname1));
        imageLoader.DisplayImage(resultp.get(MainActivity.avatar1), flag);


        return itemView;
    }
}
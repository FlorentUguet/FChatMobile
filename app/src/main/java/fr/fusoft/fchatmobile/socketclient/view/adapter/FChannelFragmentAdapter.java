package fr.fusoft.fchatmobile.socketclient.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.socketclient.view.fragment.channels.ChannelFragment;

/**
 * Created by Florent on 06/09/2017.
 */

public class FChannelFragmentAdapter extends ArrayAdapter<ChannelFragment> {

    private ArrayList<ChannelFragment> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        ImageView icon;
        TextView label;
    }

    public FChannelFragmentAdapter(ArrayList<ChannelFragment> data, Context context) {
        super(context, R.layout.item_channel, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public int getCount(){
        return this.dataSet.size();
    }

    @Override
    public void add(ChannelFragment entry){
        this.dataSet.add(entry);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(ChannelFragment... items){
        for(ChannelFragment e : items){
            this.add(e);
        }
    }


    private int lastPosition = -1;

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ChannelFragment entry = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_channel, parent, false);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.label = (TextView) convertView.findViewById(R.id.label);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        String label = entry.getChannelName();
        String icon = entry.getIcon();

        if(icon.equals(""))
            viewHolder.icon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_channel_icon));
        else
            Picasso.with(mContext).load(icon).into(viewHolder.icon);

        viewHolder.label.setText(label);
        // Return the completed view to render on screen
        return convertView;
    }
}

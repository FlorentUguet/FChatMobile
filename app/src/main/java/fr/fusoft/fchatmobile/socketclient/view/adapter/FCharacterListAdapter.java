package fr.fusoft.fchatmobile.socketclient.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.socketclient.model.FCharacter;
import fr.fusoft.fchatmobile.socketclient.view.activity.FChatActivity;
import fr.fusoft.fchatmobile.socketclient.view.fragment.ChannelFragment;
import fr.fusoft.fchatmobile.utils.network.DownloadImageTask;

/**
 * Created by Florent on 05/09/2017.
 */

public class FCharacterListAdapter extends ArrayAdapter<FCharacter> {

    private ArrayList<FCharacter> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        ImageView avatar;
        ImageView status;
        TextView username;
    }

    public FCharacterListAdapter(ArrayList<FCharacter> data, Context context) {
        super(context, R.layout.item_user, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public int getCount(){
        return this.dataSet.size();
    }

    @Override
    public void add(FCharacter entry){
        this.dataSet.add(entry);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(FCharacter... items){
        for(FCharacter e : items){
            this.add(e);
        }
    }


    private int lastPosition = -1;

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FCharacter entry = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_user, parent, false);
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            viewHolder.username = (TextView) convertView.findViewById(R.id.username);
            viewHolder.status = (ImageView) convertView.findViewById(R.id.status);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.username.setText(entry.getName());

        new DownloadImageTask(mContext, viewHolder.avatar).execute(entry.getAvatarUrl());

        int colorStatus;

        switch(entry.getStatus()){
            case ONLINE:
                colorStatus = ContextCompat.getColor(mContext, R.color.colorOnline);
                break;
            case AWAY:
                colorStatus = ContextCompat.getColor(mContext, R.color.colorAway);
                break;
            case BUSY:
                colorStatus = ContextCompat.getColor(mContext, R.color.colorBusy);
                break;
            case LOOKING:
                colorStatus = ContextCompat.getColor(mContext, R.color.colorLooking);
                break;
            default:
                colorStatus = ContextCompat.getColor(mContext, R.color.colorUndefined);
                break;
        }
        viewHolder.status.setBackgroundColor(colorStatus);

        lastPosition = position;
        return convertView;
    }
}
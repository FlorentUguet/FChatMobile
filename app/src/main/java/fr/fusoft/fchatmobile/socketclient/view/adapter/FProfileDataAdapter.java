package fr.fusoft.fchatmobile.socketclient.view.adapter;

import android.content.Context;
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
import fr.fusoft.fchatmobile.socketclient.model.ProfileData;
import fr.fusoft.fchatmobile.utils.network.DownloadImageTask;

/**
 * Created by Florent on 07/10/2017.
 */

public class FProfileDataAdapter extends ArrayAdapter<ProfileData> {
    private ArrayList<ProfileData> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView key;
        TextView value;
    }

    public FProfileDataAdapter(ArrayList<ProfileData> data, Context context) {
        super(context, R.layout.item_profile_data, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public int getCount(){
        return this.dataSet.size();
    }

    @Override
    public void add(ProfileData data){
        this.dataSet.add(data);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(ProfileData... items){
        for(ProfileData e : items){
            this.add(e);
        }
    }

    private int lastPosition = -1;

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ProfileData entry = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        lastPosition = position;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_profile_data, parent, false);
            viewHolder.key = (TextView) convertView.findViewById(R.id.textViewKey);
            viewHolder.value = (TextView) convertView.findViewById(R.id.textViewValue);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.key.setText(entry.getDataKey());
        viewHolder.value.setText(entry.getValue());

        lastPosition = position;
        return convertView;
    }

}

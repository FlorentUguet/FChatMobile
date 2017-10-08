package fr.fusoft.fchatmobile.socketclient.view.adapter.characterlist;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.socketclient.model.FCharacter;

/**
 * Created by Florent on 07/10/2017.
 */

public class FCharacterListLargeAdapter extends FCharacterListAdapter {
    // View lookup cache
    private static class ViewHolder {
        ImageView avatar;
        ImageView status;
        TextView username;
    }

    public FCharacterListLargeAdapter(List<FCharacter> data, Context context) {
        super(context, R.layout.item_user, data);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FCharacter entry = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        lastPosition = position;

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
        viewHolder.username.setTextColor(ContextCompat.getColor(mContext, entry.getGender().getColor()));

        Picasso.with(mContext).load(entry.getAvatarUrl()).into(viewHolder.avatar);

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

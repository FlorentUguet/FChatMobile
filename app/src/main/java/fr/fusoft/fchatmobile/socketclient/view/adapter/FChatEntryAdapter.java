package fr.fusoft.fchatmobile.socketclient.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.socketclient.model.messages.FChatEntry;

/**
 * Created by Florent on 06/09/2017.
 */

public class FChatEntryAdapter extends ArrayAdapter<FChatEntry> {

    private ArrayList<FChatEntry> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView header;
        TextView content;
    }

    public FChatEntryAdapter(ArrayList<FChatEntry> data, Context context) {
        super(context, R.layout.item_chat_entry, data);
        this.dataSet = data;
        this.mContext=context;

    }

    int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FChatEntry entry = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_chat_entry, parent, false);
            viewHolder.header = (TextView) convertView.findViewById(R.id.entry_header);
            viewHolder.content = (TextView) convertView.findViewById(R.id.entry_content);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.header.setText(entry.getHeader());
        viewHolder.content.setText(entry.getContent());
        // Return the completed view to render on screen
        return convertView;
    }
}

package fr.fusoft.fchatmobile.socketclient.view.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.socketclient.model.messages.FChatEntry;
import fr.fusoft.fchatmobile.socketclient.view.fragment.ChannelFragment;

/**
 * Created by Florent on 06/09/2017.
 */

public class FChannelFragmentAdapter extends ArrayAdapter<ChannelFragment> {

    private ArrayList<ChannelFragment> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView header;
        TextView content;
    }

    public FChannelFragmentAdapter(ArrayList<ChannelFragment> data, Context context) {
        super(context, R.layout.item_channel, data);
        this.dataSet = data;
        this.mContext=context;
    }
}

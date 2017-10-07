package fr.fusoft.fchatmobile.socketclient.view.adapter.characterlist;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fr.fusoft.fchatmobile.R;
import fr.fusoft.fchatmobile.socketclient.model.FCharacter;
import fr.fusoft.fchatmobile.socketclient.view.activity.FChatActivity;
import fr.fusoft.fchatmobile.socketclient.view.fragment.ChannelFragment;
import fr.fusoft.fchatmobile.utils.network.DownloadImageTask;

/**
 * Created by Florent on 05/09/2017.
 */

public class FCharacterListAdapter extends ArrayAdapter<FCharacter>{

    protected ArrayList<FCharacter> dataSet;
    protected Context mContext;

    // View lookup cache
    private static class ViewHolder {
    }

    public FCharacterListAdapter(Context context, int resource, ArrayList<FCharacter> objects) {
        super(context, resource, objects);
        this.dataSet = objects;
        this.mContext = context;
    }

    @Override
    public int getCount(){
        return this.dataSet.size();
    }

    @Override
    public void add(FCharacter entry) {
        this.dataSet.add(entry);
        notifyDataSetChanged();
    }

    protected int lastPosition = -1;

    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView;
    }


}
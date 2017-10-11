package fr.fusoft.fchatmobile.socketclient.view.adapter.characterlist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import fr.fusoft.fchatmobile.socketclient.model.FCharacter;

/**
 * Created by Florent on 05/09/2017.
 */

public class FCharacterListAdapter extends ArrayAdapter<FCharacter>{

    protected List<FCharacter> dataSet;
    protected Context mContext;

    // View lookup cache
    private static class ViewHolder {
    }

    public FCharacterListAdapter(Context context, int resource, List<FCharacter> objects) {
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
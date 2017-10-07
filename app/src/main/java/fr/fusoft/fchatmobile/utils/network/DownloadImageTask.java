package fr.fusoft.fchatmobile.utils.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Florent on 10/09/2017.
 */

//Courtesy of https://stackoverflow.com/a/9288544/6026551

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    Context context;

    public DownloadImageTask(Context c, ImageView bmImage) {
        this.bmImage = bmImage;
        this.context = c;
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap mIcon11 = null;

        String filename = url.substring(url.lastIndexOf("/") + 1, url.length());
        File file = new File(context.getCacheDir(), filename);

        if(true){
            try{
                boolean ok = file.createNewFile();
                OutputStream out = new FileOutputStream(file);
                InputStream in = new java.net.URL(url).openStream();

                byte[] buffer = new byte[in.available()];
                in.read(buffer);
                out.write(buffer);
                out.close();
            }catch(Exception e){
                Log.e("DownloadImageTask", "Error writing file " + filename + " : " +  e.getMessage());
            }

        }

        try {
            InputStream in = new FileInputStream(file);
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("DownloadImageTask", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
package fr.fusoft.fchatmobile.socketclient.model.messages;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fr.fusoft.fchatmobile.socketclient.model.FCharacter;

/**
 * Created by Florent on 06/09/2017.
 */

public abstract class FChatEntry {

    enum Type{
        MSG,
        LRP,
        NLN,
        FLN,
        JCH,
        LCH
    }

    protected Type type;

    protected FCharacter character;
    protected String content;
    protected String header;
    protected Date timestamp;

    protected SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());

    public abstract String getHeader();
    public abstract String getContent();
}

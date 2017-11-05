package fr.fusoft.fchatmobile.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Florent on 11/10/2017.
 */

public class MessageFormatter {

    public static String format(String input){
        //Check global modifiers
        String html = setGlobalModifiers(input);

        //Apply BBCode
        html = BBCode.toHtml(html);

        //Return
        return html;
    }

    public static String setGlobalModifiers(String input){

        String html = input;

        if(input.startsWith("/me")){
            html = "<i>" + html.substring(3) + "<i>";
        }

        //Filters
        Map<String,String> filters = new HashMap<>();

        filters.put("\\*\\*(.+?)\\*\\*", "<b>$1</b>");
        filters.put("\\*(.+?)\\*", "<i'>$1<i>");

        for (Map.Entry entry: filters.entrySet()) {
            html = html.replaceAll(entry.getKey().toString(), entry.getValue().toString());
        }

        return  html;
    }


}

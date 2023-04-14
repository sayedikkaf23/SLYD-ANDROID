package chat.hola.com.app.Utilities;
/*
 * Created by moda on 07/04/16.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * To sort the chats based on the date of last message in the chatlist
 */


 public class TimestampSorter implements Comparator {

    private Date date1, date2;


    @SuppressWarnings("unchecked")
    public int compare(Object firstObjToCompare, Object secondObjToCompare) {
        String firstDateString = (String) ((Map<String, Object>) firstObjToCompare).get("lastMessageDate");
        String secondDateString = (String) ((Map<String, Object>) secondObjToCompare).get("lastMessageDate");

        if (secondDateString == null || firstDateString == null) {
            return 0;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);


        try {

            date1 = sdf.parse(firstDateString);
            date2 = sdf.parse(secondDateString);


        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (date1.after(date2)) return -1;
        else if (date1.before(date2)) return 1;
        else return 0;
    }

}

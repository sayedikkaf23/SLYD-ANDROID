package chat.hola.com.app.Utilities;



import java.util.Comparator;

import chat.hola.com.app.ModelClasses.SelectUserItem;

/**
 * Created by moda on 02/08/17.
 */

public class SortUsers implements Comparator {


    @SuppressWarnings("unchecked")
    public int compare(Object firstObjToCompare, Object secondObjToCompare) {
        String firstNameString = ((SelectUserItem) firstObjToCompare).getUserName();
        String secondNameString = ((SelectUserItem) secondObjToCompare).getUserName();

        if (secondNameString == null || firstNameString == null) {
            return 0;
        }


        return firstNameString.compareToIgnoreCase(secondNameString);
    }

}
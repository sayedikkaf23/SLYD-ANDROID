package chat.hola.com.app.Utilities;



import java.util.Comparator;

import chat.hola.com.app.ModelClasses.ContactsItem;

/**
 * Created by moda on 06/12/16.
 */

public class SortContacts implements Comparator {


    @SuppressWarnings("unchecked")
    public int compare(Object firstObjToCompare, Object secondObjToCompare) {
        String firstNameString = ((ContactsItem) firstObjToCompare).getContactName();
        String secondNameString = ((ContactsItem) secondObjToCompare).getContactName();

        if (secondNameString == null || firstNameString == null) {
            return 0;
        }


        return firstNameString.compareToIgnoreCase(secondNameString);
    }

}
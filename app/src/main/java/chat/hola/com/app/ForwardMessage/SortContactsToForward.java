package chat.hola.com.app.ForwardMessage;

import java.util.Comparator;

/**
 * Created by moda on 30/08/17.
 */

public class SortContactsToForward implements Comparator {


    @SuppressWarnings("unchecked")
    public int compare(Object firstObjToCompare, Object secondObjToCompare) {
        String firstNameString = ((Forward_ContactItem) firstObjToCompare).getContactName();
        String secondNameString = ((Forward_ContactItem) secondObjToCompare).getContactName();

        if (secondNameString == null || firstNameString == null) {
            return 0;
        }


        return firstNameString.compareToIgnoreCase(secondNameString);
    }

}
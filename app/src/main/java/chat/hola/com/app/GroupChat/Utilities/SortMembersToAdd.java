package chat.hola.com.app.GroupChat.Utilities;

import java.util.Comparator;

import chat.hola.com.app.GroupChat.ModelClasses.AddMemberItem;

/**
 * Created by moda on 26/09/17.
 */

public class SortMembersToAdd implements Comparator {


    @SuppressWarnings("unchecked")
    public int compare(Object firstObjToCompare, Object secondObjToCompare) {
        String firstNameString = ((AddMemberItem) firstObjToCompare).getContactName();
        String secondNameString = ((AddMemberItem) secondObjToCompare).getContactName();

        if (secondNameString == null || firstNameString == null) {
            return 0;
        }


        return firstNameString.compareToIgnoreCase(secondNameString);
    }

}
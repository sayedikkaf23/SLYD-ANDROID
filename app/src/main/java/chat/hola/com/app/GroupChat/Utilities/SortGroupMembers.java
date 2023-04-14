package chat.hola.com.app.GroupChat.Utilities;

import java.util.Comparator;

import chat.hola.com.app.GroupChat.ModelClasses.GroupInfoMemberItem;

/**
 * Created by moda on 23/09/17.
 */


public class SortGroupMembers implements Comparator {


    @SuppressWarnings("unchecked")
    public int compare(Object firstObjToCompare, Object secondObjToCompare) {
        String firstNameString = ((GroupInfoMemberItem) firstObjToCompare).getContactName();
        String secondNameString = ((GroupInfoMemberItem) secondObjToCompare).getContactName();

        if (secondNameString == null || firstNameString == null) {
            return 0;
        }


        return firstNameString.compareToIgnoreCase(secondNameString);
    }

}
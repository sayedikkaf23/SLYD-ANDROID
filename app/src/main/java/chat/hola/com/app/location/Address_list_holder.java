package chat.hola.com.app.location;
import java.util.ArrayList;
/**
 * @since  6/2/17.
 */

public class Address_list_holder
{
    private ArrayList<Address_list_item_pojo> list_of_address=new ArrayList<>();

    public ArrayList<Address_list_item_pojo> getList_of_address()
    {
        return list_of_address;
    }

    public void setList_of_address(ArrayList<Address_list_item_pojo> list_address)
    {
        this.list_of_address = list_address;
    }

}


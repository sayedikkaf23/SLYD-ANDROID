package chat.hola.com.app.CustomGallery;

import androidx.annotation.NonNull;

/**
 * Created by moda on 09/05/17.
 */

public class GalleryItemDetailsPojo implements Comparable<GalleryItemDetailsPojo>
{
    private String path;

    private String created_date;
    private String bukket_name;

    public String getBukket_name()
    {
        return bukket_name;
    }

    public void setBukket_name(String bukket_name)
    {
        this.bukket_name = bukket_name;
    }

    public String getCreated_date()
    {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }


    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    @Override
    public int compareTo(@NonNull GalleryItemDetailsPojo item)
    {
        String first_date=this.getCreated_date();
        String second_date=item.getCreated_date();
        if(first_date==null||second_date==null)
        {
            return 0;
        }
        long first=Long.parseLong(first_date.trim());
        long sec=Long.parseLong(second_date);
        if (first <sec) {
            return 1;
        }
        else if(first >sec){
            return -1;
        }

        return 0;
    }
}

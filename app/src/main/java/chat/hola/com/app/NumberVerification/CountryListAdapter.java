package chat.hola.com.app.NumberVerification;

/*
 * Created by moda on 30/12/16.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezcall.android.R;

import java.util.List;

import chat.hola.com.app.AppController;


/**
 * Adapter class for the list of the countries
 */
class CountryListAdapter extends BaseAdapter {


    private List<Country> countries;
    private LayoutInflater inflater;
   private AppController appController=AppController.getInstance();
   private Typeface fontMedium;
 /*   private int getResId(String drawableName) {

        try {

            Field field = (R.drawable.class).getField(drawableName);

            return field.getInt(null);
        } catch (Exception e) {
            Log.e("CountryCodePicker", "Failure to get drawable id.", e);
        }
        return -1;
    }
*/
    CountryListAdapter(Context context, List<Country> countries) {
        super();

        this.countries = countries;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * @return number of items
     */
    @Override
    public int getCount() {
        return countries.size();
    }


    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cellView = convertView;
        Cell cell;
        Country country = countries.get(position);
        fontMedium=appController.getMediumFont();
        if (convertView == null) {
            cell = new Cell();
            cellView = inflater.inflate(R.layout.country_row, null);
            cell.textView = (TextView) cellView.findViewById(R.id.row_title);
            cell.textView.setTypeface(fontMedium);
            cell.countryCodeTextView = (TextView) cellView.findViewById(R.id.country_code);
            cell.countryCodeTextView.setTypeface(fontMedium);
            cell.imageView = (ImageView) cellView.findViewById(R.id.row_icon);
            cellView.setTag(cell);
        } else {
            cell = (Cell) cellView.getTag();
        }

        cell.textView.setText(country.getName());
        cell.countryCodeTextView.setText(country.getDialCode());
       /* String drawableName = "flag_"
                + country.getCode().toLowerCase(Locale.ENGLISH);*/
        cell.imageView.setImageResource(country.getFlag());
        return cellView;
    }

    private static class Cell {
        public TextView textView;
        public ImageView imageView;
        public TextView countryCodeTextView;
    }

}
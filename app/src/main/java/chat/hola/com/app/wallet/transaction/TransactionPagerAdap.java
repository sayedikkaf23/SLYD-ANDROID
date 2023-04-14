package chat.hola.com.app.wallet.transaction;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TransactionPagerAdap extends FragmentPagerAdapter {

    ArrayList<Fragment> fragList;
    ArrayList<String> titleList = new ArrayList<>();
    public TransactionPagerAdap(@NonNull FragmentManager fm,ArrayList<Fragment> fragList) {
        super(fm);
        this.fragList = fragList;
        titleList.add("All");
        titleList.add("Coin In");
        titleList.add("Coin Out");

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragList.get(position);
    }

    @Override
    public int getCount() {
        return fragList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(titleList.get(position) != null)
            return titleList.get(position);
        return super.getPageTitle(position);
    }
}

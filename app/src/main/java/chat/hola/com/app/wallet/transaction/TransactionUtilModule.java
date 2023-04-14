package chat.hola.com.app.wallet.transaction;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Named;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.wallet.transaction.Model.TransactionData;
import chat.hola.com.app.wallet.transaction.all.AllFragment;
import chat.hola.com.app.wallet.transaction.credit.CreditFragment;
import chat.hola.com.app.wallet.transaction.debit.DebitFragment;
import dagger.Module;
import dagger.Provides;

@Module
public class TransactionUtilModule {

    public static final String  ALL_LIST ="all_list";
    public static final String  CREDIT_LIST ="credit_list";
    public static final String  DEBIT_LIST ="debit_list";
    @Named(ALL_LIST)
    @Provides
    @ActivityScoped
    ArrayList<TransactionData> allArrayList(){
        return new ArrayList<>();
    }

    @Named(CREDIT_LIST)
    @Provides
    @ActivityScoped
    ArrayList<TransactionData> creditArrayList(){
        return new ArrayList<>();
    }

    @Named(DEBIT_LIST)
    @Provides
    @ActivityScoped
    ArrayList<TransactionData> debitArrayList(){
        return new ArrayList<>();
    }


    @Provides
    @ActivityScoped
    ArrayList<Fragment> provideFragmentsList(AllFragment allFragment, CreditFragment creditFragment, DebitFragment debitFragment)
    {
        return new ArrayList<>(Arrays.asList(allFragment,creditFragment,debitFragment));
    }

    @Provides
    @ActivityScoped
    TransactionPagerAdap provideTransactionPagerAdap(FragmentManager fragmentManager,ArrayList<Fragment> fragmentArrayList){
        return new TransactionPagerAdap(fragmentManager,fragmentArrayList);
    }

    @Provides
    @ActivityScoped
    FragmentManager fragmentManager(Activity activity){
        return ((AppCompatActivity)activity).getSupportFragmentManager();
    }

}

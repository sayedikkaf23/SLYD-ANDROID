package chat.hola.com.app.wallet.transaction;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.dagger.FragmentScoped;
import chat.hola.com.app.wallet.transaction.all.AllFragment;
import chat.hola.com.app.wallet.transaction.all.AllModule;
import chat.hola.com.app.wallet.transaction.credit.CreditFragment;
import chat.hola.com.app.wallet.transaction.credit.CreditModule;
import chat.hola.com.app.wallet.transaction.debit.DebitFragment;
import chat.hola.com.app.wallet.transaction.debit.DebitModule;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface TransactionModule {

    @ActivityScoped
    @Binds
    TransactionContract.Presenter presenter(TransactionPresenter presenter);

    @ActivityScoped
    @Binds
    TransactionContract.View view(TransactionActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(TransactionActivity activity);

    @FragmentScoped
    @ContributesAndroidInjector(modules = AllModule.class)
    AllFragment allFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = CreditModule.class)
    CreditFragment creditFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = DebitModule.class)
    DebitFragment debitFragment();



}

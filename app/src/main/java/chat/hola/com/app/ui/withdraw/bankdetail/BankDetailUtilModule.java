package chat.hola.com.app.ui.withdraw.bankdetail;


import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.ui.cards.CardActivity;
import dagger.Module;
import dagger.Provides;

@Module
public class BankDetailUtilModule {
    @ActivityScoped
    @Provides
    Loader loader(BankDetailActivity context) {
        return new Loader(context);
    }
}

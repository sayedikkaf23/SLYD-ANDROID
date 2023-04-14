package chat.hola.com.app.ui.withdraw.method;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.models.WithdrawMethod;
import chat.hola.com.app.ui.adapter.WithdrawMethodAdapter;
import dagger.Module;
import dagger.Provides;

@Module
public class WithdrawMethodUtilModule {
    @ActivityScoped
    @Provides
    Loader loader(WithdrawMethodActivity context) {
        return new Loader(context);
    }

    @ActivityScoped
    @Provides
    List<WithdrawMethod> withdrawMethods() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    WithdrawMethodAdapter withdrawMethodAdapter(Context context, List<WithdrawMethod> cards, TypefaceManager typefaceManager) {
        return new WithdrawMethodAdapter(context, cards, typefaceManager);
    }
}

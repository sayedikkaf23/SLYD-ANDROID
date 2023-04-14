package chat.hola.com.app.ui.cards;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.models.Card;
import chat.hola.com.app.ui.adapter.CardAdapter;
import dagger.Module;
import dagger.Provides;

@Module
public class CardUtilModule {
    @ActivityScoped
    @Provides
    Loader loader(CardActivity context) {
        return new Loader(context);
    }

    @ActivityScoped
    @Provides
    List<Card> cards() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    CardAdapter cardAdapter(Context context, List<Card> cards, TypefaceManager typefaceManager) {
        return new CardAdapter(context, cards, typefaceManager);
    }
}

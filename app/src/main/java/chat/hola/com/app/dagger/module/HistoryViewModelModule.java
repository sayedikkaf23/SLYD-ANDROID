package chat.hola.com.app.dagger.module;

import androidx.lifecycle.ViewModel;

import com.appscrip.myapplication.injection.ViewModelKey;
import com.appscrip.myapplication.injection.module.ViewModelFactory;

import chat.hola.com.app.orders.historyscreen.HistoryViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class HistoryViewModelModule extends ViewModelFactory {
    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel.class)
    protected abstract ViewModel productFilterViewModel(
            HistoryViewModel historyOrderFilterViewModel);
}

package chat.hola.com.app.coin.pack

import chat.hola.com.app.dagger.FragmentScoped
import dagger.Binds
import dagger.Module

@Module
interface CoinPackModule {
    @FragmentScoped
    @Binds
    fun presenter(presenter: CoinPackPresenter): CoinPackContract.Presenter
}
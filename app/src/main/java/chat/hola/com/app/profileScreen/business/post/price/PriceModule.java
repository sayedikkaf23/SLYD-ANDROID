package chat.hola.com.app.profileScreen.business.post.price;


import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>PriceModule</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 09 September 2019
 */
@Module
public interface PriceModule {

    @FragmentScoped
    @Binds
    PriceContract.Presenter pricePresenter(PricePresenter presenter);
}

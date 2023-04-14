package chat.hola.com.app.home.comment;


import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface CommentFragmentModule {
    @FragmentScoped
    @Binds
    CommentFragmentContract.Presenter presenter(CommentFragmentPresenter presenter);
}

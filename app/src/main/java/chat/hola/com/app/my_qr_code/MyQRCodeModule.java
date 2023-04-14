package chat.hola.com.app.my_qr_code;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface MyQRCodeModule {
    @ActivityScoped
    @Binds
    MyQRCodeContract.Presenter presenter(MyQRCodePrensenter prensenter);

    @ActivityScoped
    @Binds
    MyQRCodeContract.View view(MyQRCodeActivity myQRCodeActivity);

}

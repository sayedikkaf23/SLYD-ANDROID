package chat.hola.com.app.dagger;

import android.app.Application;
import android.content.Context;

import com.appscrip.myapplication.UiThread;
import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.DataSourceImpl;
import com.kotlintestgradle.data.executor.JobExecutor;
import com.kotlintestgradle.data.repository.UserRepositoryImpl;
import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.repository.UserRepository;

import chat.hola.com.app.AppController;
import dagger.Binds;
import dagger.Module;

/**
 * Created by ankit on 20/2/18.
 */

//@Singleton
@Module
public interface AppModule {

    @Binds
    Context bindContext(Application application);

    @Binds
    UserRepository userRepositoryImpl(UserRepositoryImpl userRepository);

    @Binds
    DataSource provideDataSource(DataSourceImpl dataSource);

    @Binds
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor);

    @Binds
    PostExecutionThread proPostExecutionThread(UiThread uiThread);

}

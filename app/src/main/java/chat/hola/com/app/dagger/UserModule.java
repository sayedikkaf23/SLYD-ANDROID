package chat.hola.com.app.dagger;

import com.jio.consumer.domain.interactor.user.handler.MqttHandler;
import com.jio.consumer.domain.interactor.user.handler.MqttHandlerImpl;
import com.jio.consumer.domain.interactor.user.handler.UserHandler;
import com.jio.consumer.domain.interactor.user.handler.UserHandlerImpl;
import com.kotlintestgradle.data.repository.MqttRepositoryImpl;
import com.kotlintestgradle.repository.MqttRepository;

import dagger.Binds;
import dagger.Module;

@Module
public interface UserModule {

    @ActivityScoped
    @Binds
    UserHandler userHandler(UserHandlerImpl userHandler);

    @ActivityScoped
    @Binds
    MqttHandler mqttHandler(MqttHandlerImpl mqttHandler);

    @ActivityScoped
    @Binds
    MqttRepository mqttRepositoryImpl(MqttRepositoryImpl mqttRepositoryImpl);
}

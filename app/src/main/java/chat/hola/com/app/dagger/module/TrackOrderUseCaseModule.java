package chat.hola.com.app.dagger.module;


import com.kotlintestgradle.data.repository.order.TrackingRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.order.TrackingOrderUseCase;
import com.kotlintestgradle.repository.TrackingRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class TrackOrderUseCaseModule {
    @Provides
    TrackingRepository provideRepository(
            TrackingRepositoryImpl trackingRepository) {
        return trackingRepository;
    }

    @Provides
    public UseCase<TrackingOrderUseCase.RequestValues, TrackingOrderUseCase.ResponseValues> getTrackingUseCaseModule(
            TrackingOrderUseCase trackingOrderUseCase) {
        return trackingOrderUseCase;
    }
}
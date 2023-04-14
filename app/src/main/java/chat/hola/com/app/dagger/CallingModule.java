package chat.hola.com.app.dagger;

import com.kotlintestgradle.data.repository.CallingRepositoryImpl;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.calling.AnswerCallUseCase;
import com.kotlintestgradle.interactor.calling.DisconnectCallUseCase;
import com.kotlintestgradle.interactor.calling.PostCallUseCase;
import com.kotlintestgradle.interactor.dashboard.GetParticipantsInCallUseCase;
import com.kotlintestgradle.repository.CallingRepository;

import chat.hola.com.app.request_star_profile.request_star.RequestStarContract;
import chat.hola.com.app.request_star_profile.request_star.RequestStarPresenter;
import chat.hola.com.app.request_star_profile.request_star.RequestStarProfileActivity;
import dagger.Binds;
import dagger.Module;

@Module
public interface CallingModule {

    @ActivityScoped
    @Binds
    RequestStarContract.View view(RequestStarProfileActivity requestStarProfileActivity);

    @ActivityScoped
    @Binds
    RequestStarContract.Presenter presenter(RequestStarPresenter requestStarPresenter);

    @ActivityScoped
    @Binds
    CallingRepository callingRepository(CallingRepositoryImpl provideRepository);

    @ActivityScoped
    @Binds
    UseCase<PostCallUseCase.RequestValues, PostCallUseCase.ResponseValues> postCallUseCase(PostCallUseCase postCallUseCase);

    @ActivityScoped
    @Binds
    UseCase<AnswerCallUseCase.RequestValues, AnswerCallUseCase.ResponseValues> answerCallUseCase(AnswerCallUseCase answerCallUseCase);

    @ActivityScoped
    @Binds
    UseCase<DisconnectCallUseCase.RequestValues, DisconnectCallUseCase.ResponseValues> disconnectCallUseCase(DisconnectCallUseCase disconnectCallUseCase);

    @ActivityScoped
    @Binds
    UseCase<GetParticipantsInCallUseCase.RequestValues, GetParticipantsInCallUseCase.ResponseValues> getParticipantsUseCase(GetParticipantsInCallUseCase disconnectCallUseCase);

}

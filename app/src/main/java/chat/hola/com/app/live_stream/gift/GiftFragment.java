package chat.hola.com.app.live_stream.gift;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.LiveStreamService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.live_stream.adapter.GiftAdapter;
import chat.hola.com.app.models.GiftCategories;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class GiftFragment extends Fragment {
    private GiftCategories.Data.Category category;
    private static LiveStreamService service;
    private GiftAdapter giftAdapter;
    private static GiftAdapter.GiftListener listener;

    @BindView(R.id.rvGifts)
    RecyclerView rvGifts;

    public static Fragment newInstance(int position, GiftCategories.Data.Category category, LiveStreamService liveStreamService, GiftAdapter.GiftListener giftListener) {
        GiftFragment myFragment = new GiftFragment();

        Bundle args = new Bundle();
        args.putSerializable("category", category);
        args.putSerializable("position", position);
        myFragment.setArguments(args);
        service = liveStreamService;
        listener = giftListener;
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            category = (GiftCategories.Data.Category) bundle.getSerializable("category");
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        ButterKnife.bind(this, view);
        if (category != null) {
            giftAdapter = new GiftAdapter(getContext(), listener);
            rvGifts.setLayoutManager(new GridLayoutManager(getContext(), 3));
            rvGifts.setAdapter(giftAdapter);
            gifts(category.getId());
        }
    }

    private int getLayoutId() {
        return R.layout.fragment_layout;
    }

    private void gifts(String categoryId) {
        service.giftsByCategory(AppController.getInstance().getApiToken(), Constants.LANGUAGE, "1", categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<GiftDataResponse>>() {
                    @Override
                    public void onNext(Response<GiftDataResponse> response) {
                        if (response.code() == 200) {
                            List<GiftDataResponse.Data.Gift> gifts = response.body().getData().getGifts();
                            if (gifts != null) {
                                giftAdapter.setGifts(gifts);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}

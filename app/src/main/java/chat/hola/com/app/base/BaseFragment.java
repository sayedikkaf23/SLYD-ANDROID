package chat.hola.com.app.base;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.inject.Inject;

import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TypefaceManager;
import dagger.android.support.DaggerFragment;

/**
 * <h1>BaseFragment</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 31 August 2019
 */
public abstract class BaseFragment extends DaggerFragment {


    protected String TAG = this.getClass().getName();

    @Inject
    TypefaceManager font;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        bindViews(view);
        initView(view);
        setTypeface();

        return view;
    }

    /**
     * Every object annotated with {@link butterknife} its gonna injected trough butterknife
     */
    private void bindViews(View view) {
        ButterKnife.bind(this, view);
    }

    /**
     * Use this method to initialize view components. This method is called after {@link
     * BaseActivity()}
     * @param view
     */
    public void initView(View view) {
    }

    /**
     * Use this method to set custom font family
     */
    public void setTypeface() {
    }

    /**
     * @return The layout id that's gonna be the activity view.
     */
    protected abstract int getLayoutId();

}

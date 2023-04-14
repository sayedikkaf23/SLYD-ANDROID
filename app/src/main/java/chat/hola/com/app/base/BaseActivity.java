package chat.hola.com.app.base;


import android.os.Bundle;

import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>BaseActivity</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 31 August 2019
 */
public abstract class BaseActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        bindViews();
        initView();
    }

    /**
     * Use this method to initialize view components. This method is called after {@link
     * BaseActivity#bindViews()}
     */
    public void initView() {
    }

    /**
     * Every object annotated with {@link butterknife} its gonna injected trough butterknife
     */
    private void bindViews() {
        ButterKnife.bind(this);
    }

    /**
     * @return The layout id that's gonna be the activity view.
     */
    protected abstract int getLayoutId();

    public void setTypeface(){}
}

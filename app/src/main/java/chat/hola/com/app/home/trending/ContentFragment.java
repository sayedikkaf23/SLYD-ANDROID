package chat.hola.com.app.home.trending;

import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ezcall.android.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.SpannedGridLayoutManager;
import chat.hola.com.app.home.trending.model.TrendingContentAdapter;
import dagger.android.support.DaggerFragment;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 6/26/2018.
 */

public class ContentFragment extends DaggerFragment {

    @Inject
    public ContentFragment() {
    }

    @Inject
    TrendingContentAdapter contentAdapter;
    @BindView(R.id.rvContent)
    RecyclerView rvContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_content, container, false);
        ButterKnife.bind(this, rootView);
        // contentAdapter.setPostListner(presenter);
        SpannedGridLayoutManager glm = new SpannedGridLayoutManager(position -> {
            if (position == 1 || position == 18) {
                return new SpannedGridLayoutManager.SpanInfo(2, 2);
            } else {
                return new SpannedGridLayoutManager.SpanInfo(1, 1);
            }
        }, 3 /* Three columns */, 1f /* We want our items to be 1:1 ratio */);
        rvContent.setLayoutManager(glm);
        rvContent.setHasFixedSize(true);
        rvContent.setAdapter(contentAdapter);
        return rootView;
    }

}

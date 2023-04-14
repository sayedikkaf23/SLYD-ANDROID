package chat.hola.com.app.post.location;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.ezcall.android.R;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.models.Place;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>LocationActivity</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 23 September 2019
 */
public class LocationActivity extends DaggerAppCompatActivity implements LocationContract.View, LocationAdapter.ClickListner {

    @Inject
    LocationAdapter adapter;
    @Inject
    LocationContract.Presenter presenter;

    @BindView(R.id.rvLocation)
    RecyclerView rvLocation;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.etSearch)
    EditText etSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_place);
        ButterKnife.bind(this);
        presenter.nearByLocation(LocationActivity.this);
        refresh.setOnRefreshListener(this::refresh);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvLocation.setLayoutManager(layoutManager);
        rvLocation.addItemDecoration(new DividerItemDecoration(rvLocation.getContext(), layoutManager.getOrientation()));
        adapter.setClickListner(this);
        rvLocation.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0)
                    presenter.location(s.toString());
                else presenter.nearByLocation(LocationActivity.this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.ibBack)
    public void back() {
        super.onBackPressed();
    }

    @Override
    public void showLoader() {
        refresh.setRefreshing(true);
    }

    @Override
    public void hideLoader() {
        if (refresh.isRefreshing())
            refresh.setRefreshing(false);
    }

    @Override
    public void refresh() {
        presenter.nearByLocation(LocationActivity.this);
    }

    @Override
    public void onAddressSelect(Place place) {
        Intent intent = new Intent();
        intent.putExtra("placeId", place.getId());
        intent.putExtra("locationName", place.getTitle());
        intent.putExtra("locationDetails", place.getAddress());
        intent.putExtra("latitude", place.getLatitude());
        intent.putExtra("longitude", place.getLogitude());
        setResult(RESULT_OK, intent);
        finish();
    }
}

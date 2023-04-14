package chat.hola.com.app.request_star_profile.star_category;

/**
 * <h1>{@link chat.hola.com.app.request_star_profile.star_category.StarCategoryActivity} </h1>
 * <p>This class is used to select star category.</p>
 * @author : Hardik Karkar
 * @since : 23rd May 2019
 * @version : 1.0
 * {@link chat.hola.com.app.request_star_profile.star_category.StarCatPresenter}
 *
 */


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.DaggerAppCompatActivity;

public class StarCategoryActivity extends DaggerAppCompatActivity implements StarCatContract.View {

    @BindView(R.id.rV_category)
    RecyclerView rV_category;

    Unbinder unbinder;

    @Inject
    StarCatContract.Presenter presenter;

    // This data list is used to store the list of category data.
    List<Data> data = new ArrayList<>();

    // This adapter is for attach the category list  to recyleview.
    StarCategoryAdap categoryAdap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_category);
        unbinder = ButterKnife.bind(this);
        categoryAdap = new StarCategoryAdap(this,data);
        rV_category.setLayoutManager(new LinearLayoutManager(this));
        rV_category.setAdapter(categoryAdap);
        presenter.getStarCategory();

    }

    @OnClick(R.id.iV_back)
    public void ivBack() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void showMessage(String msg, int msgId) {

    }

    @Override
    public void sessionExpired() {

    }

    @Override
    public void isInternetAvailable(boolean flag) {

    }

    @Override
    public void userBlocked() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void getCategorySuccuss(List<Data> data) {

        /* Here getting the category list response from api*/

        this.data.clear();
        this.data.addAll(data);
        categoryAdap.notifyDataSetChanged();

    }

    /**
     * <h1>{@link StarCategoryAdap}</h1>
    * <p> This is the adapter class which is used to attach with recycle view</p>
    */
    public class StarCategoryAdap extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        Context mContext;
        List<Data> data;

        /**
         * @param mContext :  getting reference of activity
         * @param data : category data list which is used within adapter*/
        StarCategoryAdap(Context mContext, List<Data> data) {
            this.mContext = mContext;
            this.data = data;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(mContext).inflate(R.layout.single_row_star_category_item,null,false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            MyViewHolder holder1 = (MyViewHolder) holder;
            holder1.tV_category.setText(data.get(position).getCategorie());

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        /**
         * <h1>{@link MyViewHolder}</h1>
         * <p>ViewHolder class for attach single Itemview to Adapter</p> */
        private class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tV_category;
            private MyViewHolder(@NonNull View itemView) {
                super(itemView);

                tV_category = itemView.findViewById(R.id.tV_category);

                /*Particular item click event. */
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("categoryData",data.get(getAdapterPosition()));
                        setResult(201,intent);
                        onBackPressed();
                    }
                });
            }
        }
    }
}

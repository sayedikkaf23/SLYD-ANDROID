package chat.hola.com.app.Profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;

import com.ezcall.android.R;

import chat.hola.com.app.Status.StatusAdapter;
import chat.hola.com.app.Status.StatusItem;
import chat.hola.com.app.Utilities.CustomLinearLayoutManager;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;

/**
 * Created by moda on 29/07/17.
 */


/**
 * To update mine own status on the server
 */
public class UpdateStatus extends AppCompatActivity {


    private TextView currentStatus;

    private static final int RESULT_UPDATE_STATUS = 0;


    private ArrayList<StatusItem> statusItems = new ArrayList<>();


    private StatusAdapter mAdapter;
    private boolean statusUpdated = false;
    private Bus bus = AppController.getBus();
    //  private CustomLinearLayoutManager llm;

    @SuppressWarnings("unchecked,TryWithIdenticalCatches")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);


        setContentView(R.layout.update_status);

        currentStatus = (TextView) findViewById(R.id.textViewStatus);
        currentStatus.setTypeface(AppController.getInstance().getMediumFont());
        String oldStatus = getIntent().getStringExtra("status");
        if (oldStatus != null)
            currentStatus.setText(oldStatus);

        /*
         * To setup the arraylist
         */
        mAdapter = new StatusAdapter(UpdateStatus.this, statusItems);

        //    llm = new CustomLinearLayoutManager(UpdateStatus.this, LinearLayoutManager.VERTICAL, false);

        RecyclerView recyclerView_status = (RecyclerView) findViewById(R.id.listViewStatus);
        recyclerView_status.setLayoutManager(new CustomLinearLayoutManager(UpdateStatus.this, LinearLayoutManager.VERTICAL, false));
        recyclerView_status.setItemAnimator(new DefaultItemAnimator());
        recyclerView_status.setAdapter(mAdapter);
        recyclerView_status.setHasFixedSize(true);

        setupActivity();


        recyclerView_status.addOnItemTouchListener(new RecyclerItemClickListener(UpdateStatus.this, recyclerView_status, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (position >= 0) {
//                    final StatusItem item = statusItems.get(position);
//
//                    if (item.getStatusType() == 0) {
//
//
//                    } else {
//
//
//                    }

                    if (!currentStatus.getText().toString().equals(statusItems.get(position).getStatus())) {
                        currentStatus.setText(statusItems.get(position).getStatus());

                        statusUpdated = true;
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));


        RelativeLayout saveStatus = (RelativeLayout) findViewById(R.id.rl7);

        ImageView deleteAll = (ImageView) findViewById(R.id.delete);
        ImageView close = (ImageView) findViewById(R.id.close);


        close.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageView editStatus = (ImageView) findViewById(R.id.editStatus);


        /*
         * To edit the status manually
         */
        editStatus.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(UpdateStatus.this, WriteNewNameOrStatus.class);
                intent.putExtra("currentValue", currentStatus.getText().toString());
                intent.putExtra("type", 1);
                startActivityForResult(intent, RESULT_UPDATE_STATUS);


            }
        });
        saveStatus.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                if (statusUpdated) {

                    Intent intent = new Intent();
                    intent.putExtra("updatedValue", currentStatus.getText().toString());
                    setResult(RESULT_OK, intent);
                    supportFinishAfterTransition();

                } else {


                    Intent intent = new Intent();

                    setResult(RESULT_CANCELED, intent);
                    supportFinishAfterTransition();

                }
            }
        });


        deleteAll.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {


                AppController.getInstance().getDbController().deleteAllStatus(AppController.getInstance().getStatusDocId());


            }
        });


        /*
         * To set the typeface values
         */
        TextView title = (TextView) findViewById(R.id.title);

        TextView tv1 = (TextView) findViewById(R.id.currentStatus);
        TextView tv2 = (TextView) findViewById(R.id.selectStatus);


        tv1.setTypeface(AppController.getInstance().getSemiboldFont());
        tv2.setTypeface(AppController.getInstance().getSemiboldFont());
        title.setTypeface(AppController.getInstance().getSemiboldFont());
        bus.register(this);
    }


    private void setupActivity() {

        //currentStatus.setText(AppController.getInstance().getUserStatus());
        addAllStatus();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setupActivity();


    }


    private void addAllStatus() {


        ArrayList<String> statusList = AppController.getInstance().getDbController().fetchAllStatus(AppController.getInstance().getStatusDocId());

        StatusItem statusItem;


        /*
         * To add the user defined list of status
         */

        for (int i = 0; i < statusList.size(); i++) {
            statusItem = new StatusItem();


            statusItem.setStatusType(0);
            statusItem.setStatus(statusList.get(i));

            statusItems.add(statusItem);


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mAdapter.notifyItemInserted(statusItems.size() - 1);
                }
            });
        }


        /*
         * To add the predefined list of status
         */

        String[] values = {getResources().getString(R.string.Available), getResources().getString(R.string.Busy), getResources().getString(R.string.AtSchool), getResources().getString(R.string.AtTheMovies),
                getResources().getString(R.string.AtWork), getResources().getString(R.string.BatteryAboutToDie)};


        for (int i = 0; i < values.length; i++) {
            statusItem = new StatusItem();


            statusItem.setStatusType(1);
            statusItem.setStatus(values[i]);

            statusItems.add(statusItem);


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mAdapter.notifyItemInserted(statusItems.size() - 1);
                }
            });
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RESULT_UPDATE_STATUS) {

            /*
             *
             * For return of the update user name activity
             */


            if (resultCode == Activity.RESULT_OK) {


                currentStatus.setText(data.getExtras().getString("updatedValue"));

                statusUpdated = true;
            }

        }
    }

    private void minimizeCallScreen(JSONObject obj) {
        try {
            Intent intent = new Intent(UpdateStatus.this, ChatMessageScreen.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("receiverUid", obj.getString("receiverUid"));
            intent.putExtra("receiverName", obj.getString("receiverName"));
            intent.putExtra("documentId", obj.getString("documentId"));
            intent.putExtra("isStar", obj.getBoolean("isStar"));
            intent.putExtra("receiverImage", obj.getString("receiverImage"));
            intent.putExtra("colorCode", obj.getString("colorCode"));

            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        bus.unregister(this);
    }

    @Subscribe
    public void getMessage(JSONObject object) {
        try {
            if (object.getString("eventName").equals("callMinimized")) {

                minimizeCallScreen(object);
            }

        } catch (
                JSONException e)

        {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {


        if (AppController.getInstance().isActiveOnACall()) {
            if (AppController.getInstance().isCallMinimized()) {
                super.onBackPressed();
                supportFinishAfterTransition();
            }
        } else {
            super.onBackPressed();
            supportFinishAfterTransition();
        }

    }
}

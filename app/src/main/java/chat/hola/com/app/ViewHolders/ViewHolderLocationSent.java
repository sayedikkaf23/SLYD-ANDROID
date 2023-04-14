package chat.hola.com.app.ViewHolders;
/*
 * Created by moda on 02/04/16.
 */

import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ezcall.android.R;

import chat.hola.com.app.AppController;


/**
 * View holder for location sent recycler view item
 */
public class ViewHolderLocationSent extends RecyclerView.ViewHolder implements OnMapReadyCallback {

//    public TextView senderName;

    public TextView time, date, previousMessage_head, previousMessage_content;

    public ImageView singleTick, doubleTickGreen, doubleTickBlue, clock, forward, previousMessage_iv,pic;//,blocked;


    public MapView mapView;


    public GoogleMap mMap;


    public String nameSelected = "";


    public LatLng positionSelected = new LatLng(0, 0);

    public RelativeLayout messageRoot, previousMessage_rl;

    public ViewHolderLocationSent(View view) {
        super(view);

        //  senderName = (TextView) view.findViewById(R.id.lblMsgFrom);
        forward = (ImageView) view.findViewById(R.id.forward_iv);
        messageRoot = (RelativeLayout) view.findViewById(R.id.message_root);

        pic = (ImageView)view.findViewById(R.id.pic);
          /*
         * For message reply feature
         */
        previousMessage_rl = (RelativeLayout) view.findViewById(R.id.initialMessage_rl);
        previousMessage_head = (TextView) view.findViewById(R.id.senderName_tv);
        previousMessage_iv = (ImageView) view.findViewById(R.id.initialMessage_iv);
        previousMessage_content = (TextView) view.findViewById(R.id.message_tv);

        ObjectAnimator animation = ObjectAnimator.ofFloat(forward, "rotationY", 0.0f, 180f);
        animation.setDuration(0);

        animation.start();

        date = (TextView) view.findViewById(R.id.date);

//        blocked = (ImageView) view.findViewById(R.id.blocked);
        time = (TextView) view.findViewById(R.id.ts);

        singleTick = (ImageView) view.findViewById(R.id.single_tick_green);

        doubleTickGreen = (ImageView) view.findViewById(R.id.double_tick_green);

        doubleTickBlue = (ImageView) view.findViewById(R.id.double_tick_blue);

        clock = (ImageView) view.findViewById(R.id.clock);


        mapView = (MapView) view.findViewById(R.id.map);

        mapView.onCreate(null);
        mapView.onResume();
        mapView.getMapAsync(this);

        Typeface tf = AppController.getInstance().getRegularFont();
        time.setTypeface(tf, Typeface.ITALIC);

        date.setTypeface(tf, Typeface.ITALIC);


        if (previousMessage_head != null) {
            previousMessage_head.setTypeface(tf, Typeface.BOLD);

            previousMessage_content.setTypeface(tf, Typeface.NORMAL);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.getUiSettings().setMapToolbarEnabled(true);

        mMap.addMarker(new MarkerOptions().position(positionSelected).title(nameSelected));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionSelected, 16.0f));


    }


}

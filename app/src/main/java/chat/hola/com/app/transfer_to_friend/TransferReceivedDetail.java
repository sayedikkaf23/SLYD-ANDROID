package chat.hola.com.app.transfer_to_friend;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.transfer_to_friend.model.TransactionDetailData;
import chat.hola.com.app.transfer_to_friend.model.TransactionDetailMain;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import retrofit2.Response;

public class TransferReceivedDetail extends DaggerAppCompatActivity {

  private SessionApiCall sessionApiCall = new SessionApiCall();

  @BindView(R.id.rL_root)
  RelativeLayout rL_root;
  @BindView(R.id.rL_accept)
  RelativeLayout rL_accept;
  @BindView(R.id.iV_accepted)
  ImageView iV_accepted;
  @BindView(R.id.pbProgress)
  ProgressBar pbProgress;
  @BindView(R.id.tV_accepted)
  TextView tV_accepted;
  @BindView(R.id.tV_acceptAmt)
  TextView tV_acceptAmt;
  @BindView(R.id.iV_userImage)
  ImageView iV_userImage;
  @BindView(R.id.tV_status)
  TextView tV_status;
  @BindView(R.id.tV_amount)
  TextView tV_amount;
  @BindView(R.id.tV_userName)
  TextView tV_userName;

  @BindView(R.id.iV_tick)
  ImageView iV_tick;
  @BindView(R.id.tV_amountMsg)
  TextView tV_amountMsg;
  @BindView(R.id.tV_time)
  TextView tV_time;
  @BindView(R.id.tV_reason)
  TextView tV_reason;
  @BindView(R.id.rL_reason)
  RelativeLayout rL_reason;

  @BindView(R.id.tV_transId)
  TextView tV_transId;
  @BindView(R.id.tV_from)
  TextView tV_from;
  @BindView(R.id.tV_to)
  TextView tV_to;
  @Inject
  HowdooService service;
  @Inject
  SessionManager sessionManager;

  Animation scaleUp, scaleDown;

  Unbinder unbinder;

  String message = "", receiverName = "", receiverImage = "", messageId = "", transactionId = "",
      userName = "", amount = "", time = "", transferStatus = "", transferStatusText = "";
  private String to = "", from = "";

  /*
   * * -------------------------------------
   * transferStatus | transferStatusText |
   * -------------------------------------
   *       1        |   new              |
   *       2        |   confirm          |
   *       3        |   timeout          |
   *       4        |   denied           |
   *       5        |   canceled         |
   * -------------------------------------
   * */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_transfer_received_detail);
    unbinder = ButterKnife.bind(this);

    if (getIntent().getBooleanExtra("isAccepted", false)) {
      rL_root.setVisibility(View.GONE);
      rL_accept.setVisibility(View.VISIBLE);

      scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
      scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
      amount = getIntent().getStringExtra("amount");

      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          scaleDown();
        }
      }, 1000);
    } else {
      rL_root.setVisibility(View.VISIBLE);
      rL_accept.setVisibility(View.GONE);
      getIntentData();
      initializeView();
      getTransactionDetail();
    }
  }

  private void getIntentData() {
    if (getIntent() != null) {
      receiverName = getIntent().getStringExtra("receiverName");
      receiverImage = getIntent().getStringExtra("receiverImage");
      amount = getIntent().getStringExtra("amount");
      transferStatus = getIntent().getStringExtra("transferStatus");
      transferStatusText = getIntent().getStringExtra("transferStatusText");
      time = getIntent().getStringExtra("time");
      message = getIntent().getStringExtra("message");
      messageId = getIntent().getStringExtra("messageId");

      userName = sessionManager.getFirstName() + " " + sessionManager.getLastName();
    }
  }

  private void scaleDown() {
    pbProgress.setVisibility(View.GONE);
    Animation animation = scaleDown;
    animation.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
      }

      @Override
      public void onAnimationEnd(Animation animation) {
        scaleUpTick();
      }

      @Override
      public void onAnimationRepeat(Animation animation) {
      }
    });
    pbProgress.setAnimation(animation);
  }

  private void scaleUpTick() {

    iV_accepted.setVisibility(View.VISIBLE);
    tV_accepted.setText(getString(R.string.payment_accepted));
    String ruc = sessionManager.getCurrencySymbol() + " " + amount;
    tV_acceptAmt.setText(ruc);
    Animation in_anim = scaleUp;
    in_anim.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
      }

      @Override
      public void onAnimationEnd(Animation animation) {
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            finish();
          }
        }, 2000);
      }

      @Override
      public void onAnimationRepeat(Animation animation) {
      }
    });
    iV_accepted.setAnimation(in_anim);
  }

  private void initializeView() {

    Glide.with(this).load(receiverImage).asBitmap().centerCrop()
        .signature(new StringSignature(
            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
        //.signature(new StringSignature(String.valueOf(System.currentTimeMillis()))).diskCacheStrategy(DiskCacheStrategy.NONE)
        //    .skipMemoryCache(true)
        .into(new BitmapImageViewTarget(iV_userImage) {
          @Override
          protected void setResource(Bitmap resource) {
            RoundedBitmapDrawable circularBitmapDrawable =
                RoundedBitmapDrawableFactory.create(getResources(), resource);
            circularBitmapDrawable.setCircular(true);
            iV_userImage.setImageDrawable(circularBitmapDrawable);
          }
        });

    tV_amount.setText(amount);

    tV_userName.setText(receiverName);

    tV_time.setText(time);

    if (to != null && !to.isEmpty()) tV_to.setText(to);

    if (from != null && !from.isEmpty()) tV_from.setText(from);

    if (transactionId != null && !transactionId.isEmpty()) tV_transId.setText(transactionId);

    if (message != null && !message.isEmpty()) {
      tV_reason.setText(message);
      rL_reason.setVisibility(View.VISIBLE);
    } else {
      rL_reason.setVisibility(View.GONE);
    }

    String msg = "";

    switch (transferStatus) {
      case "1":
        msg = getString(R.string.waiting_for_confirmation);
        tV_amountMsg.setText(msg);
        tV_status.setText(getString(R.string.from));
        break;
      case "2":
        msg = getString(R.string.received) + " " + sessionManager.getCurrencySymbol() + " " + amount;
        tV_status.setText(getString(R.string.received_from));
        iV_tick.setVisibility(View.VISIBLE);
        tV_amountMsg.setText(msg);
        break;
      case "3":
        msg = getString(R.string.the_payment_sent_by) + " " + receiverName + " " + getString(
            R.string.has_now_expired);
        tV_amountMsg.setText(msg);
        tV_status.setText(getString(R.string.from));
        break;
      case "4":
        msg = getString(R.string.you_are_rejected_this_payment);
        tV_amountMsg.setText(msg);
        tV_status.setText(getString(R.string.from));
        break;
      case "5":
        msg = receiverName + " " + getString(R.string.is_canceled_this_payment);
        tV_amountMsg.setText(msg);
        tV_status.setText(getString(R.string.from));
        break;
      default:
        tV_amountMsg.setText(transferStatusText);
        tV_status.setText(getString(R.string.from));
        break;
    }

    if (message != null && !message.isEmpty()) {
      tV_reason.setText(message);
      tV_reason.setVisibility(View.VISIBLE);
    }
  }

  private void getTransactionDetail() {
    service.getTransactionDetail(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
        messageId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<TransactionDetailMain>>() {
          @Override
          public void onNext(Response<TransactionDetailMain> response) {
            TransactionDetailData data;
            switch (response.code()) {
              case 200:
                if (response.body() != null && response.body().getData() != null) {
                  data = response.body().getData();
                  from = getString(R.string.from_colon) + " " + data.getFrom();
                  to = getString(R.string.to_colon) + " " + data.getTo();
                  transactionId = data.getTxnId();
                  amount = data.getAmount() + "";
                  transferStatus = data.getTransferStatus() + "";
                  initializeView();
                }
                break;
              case 406:
                SessionObserver sessionObserver = new SessionObserver();
                sessionObserver.getObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Boolean>() {
                      @Override
                      public void onNext(Boolean flag) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                          @Override
                          public void run() {
                            getTransactionDetail();
                          }
                        }, 1000);
                      }

                      @Override
                      public void onError(Throwable e) {

                      }

                      @Override
                      public void onComplete() {
                      }
                    });
                sessionApiCall.getNewSession(sessionObserver);
                break;
            }
          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onComplete() {

          }
        });
  }

  @OnClick(R.id.iV_back)
  public void back() {
    onBackPressed();
  }

  @Override
  protected void onDestroy() {
    unbinder.unbind();
    super.onDestroy();
  }
}

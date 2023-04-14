package chat.hola.com.app.camera;

//import com.camerakit.CameraKit;
//import com.camerakit.CameraKitView;
//

public class CameraControls{// extends LinearLayout {

//    private int cameraViewId = -1;
//    private CameraView cameraView;
//
//    private int coverViewId = -1;
//    private View coverView;
//    private int sec = 0;
//    private TimerTask mTt1;
//    @BindView(R.id.facingButton)
//    ImageView facingButton;
//    @BindView(R.id.flashButton)
//    ImageView flashButton;
//    @BindView(R.id.tvTimer)
//    TextView tvTimer;
//    @BindView(R.id.captureButton)
//    ImageView captureButton;
//    @BindView(R.id.tabLayout)
//    public TabLayout tabLayout;
//    @BindView(R.id.cross)
//    ImageView cross;
//    @BindView(R.id.iv_gallery)
//    ImageView iv_gallery;
//    @BindView(R.id.tick)
//    ImageView tick;
//
//    TypefaceManager typefaceManager;
//    private long captureDownTime;
//    private long captureStartTime;
//    private boolean pendingVideoCapture;
//    private boolean capturingVideo;
//    private long startTime = 0L;
//    private Handler customHandler = new Handler();
//    long timeInMilliseconds = 0L;
//    long timeSwapBuff = 0L;
//    long updatedTime = 0L;
//    int timeCap = 0;
//    int timerManin = 0;
//    private OptionClickListner optionClickListner;
//    private boolean isDubly = true;
//    int duration = 0;
//    private String call;
//    private boolean onlyPhoto = false;
//    private VideoListner mListener;
////    private CaptureCompleteListener captureListener;
//
//
//    public void setListner(VideoListner listner) {
//        this.mListener = listner;
//    }
//
////    public void setCaptureListener(CaptureCompleteListener listner) {
////        this.captureListener = listner;
////    }
//
//    public void setDuration(int millSecond) {
//
//        this.duration = millSecond;
//    }
//
//    public void setCall(String call) {
//        this.call = call;
//        flashButton.setVisibility(GONE);
//        switch (call) {
//            case "SaveProfile":
//                onlyPhoto = true;
//                tabLayout.setVisibility(GONE);
//                break;
//        }
//    }
//
//    public interface VideoListner {
//        void video(boolean flag, int i);
//
//        void face(boolean isFace);
//
//        void progress();
//
//        void stopProgress();
//
//        void getCount();
//    }
//
////    public interface CaptureCompleteListener {
////        void imageCaptureDone(byte[] imageArray);
////
////       void videoCaptureDone(Object obj);
////
////
////    }
//
//
//    public interface OptionClickListner {
//        void selectOption(int position);
//    }
//
//    public void setOptionClickListner(OptionClickListner optionClickListner, int sec) {
//        this.optionClickListner = optionClickListner;
//        this.sec = sec;
//    }
//
//    public void isDubly(boolean flag) {
//        isDubly = flag;
//        if (tabLayout != null)
//            tabLayout.removeTabAt(2);
//    }
//
//    public CameraControls(Context context) {
//        this(context, null);
//    }
//
//    public CameraControls(Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public CameraControls(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        typefaceManager = new TypefaceManager(context);
//        LayoutInflater.from(getContext()).inflate(R.layout.camera_activity_controls, this);
//        ButterKnife.bind(this);
//        tvTimer.setTypeface(AppController.getInstance().getSemiboldFont());
//
//        if (attrs != null) {
//            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CameraControls, 0, 0);
//            try {
//                cameraViewId = a.getResourceId(R.styleable.CameraControls_camera, -1);
//                coverViewId = a.getResourceId(R.styleable.CameraControls_cover, -1);
//            } finally {
//                a.recycle();
//            }
//        }
//
//        tabLayout.addTab(tabLayout.newTab());
//        // tabLayout.addTab(tabLayout.newTab());
//
//        TextView t1 = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.tab_textview, null);
//        t1.setTypeface(typefaceManager.getSemiboldFont());
//        t1.setText("CAMERA");
//        t1.setTextColor(context.getResources().getColor(R.color.whiteOverlay));
//        tabLayout.getTabAt(0).setCustomView(t1);
//
//        /*TextView t2 = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.tab_textview, null);
//        t2.setTypeface(typefaceManager.getSemiboldFont());
//        t2.setText("GALLERY");
//        t2.setTextColor(context.getResources().getColor(R.color.whiteOverlay));
//        tabLayout.getTabAt(1).setCustomView(t2);*/
//
//
//        TabLayout.Tab tab = tabLayout.getTabAt(0);
//        tab.select();
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                optionClickListner.selectOption(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//
//    }
//
//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        if (cameraViewId != -1) {
//            View view = getRootView().findViewById(cameraViewId);
////            if (view instanceof CameraKitView) {
////                cameraView = (CameraKitView) view;
//            if (view instanceof CameraView) {
//                cameraView = (CameraView) view;
////              CameraView camera;
////
////                camera.bindCameraKitListener();
//                cameraView.bindCameraKitListener(this);
////
////                cameraView.setFlash(CameraKit.FLASH_OFF);
////                cameraView.setFacing(CameraKit.FACING_FRONT);
//                cameraView.setFlash(CameraKit.Constants.FLASH_OFF);
//                cameraView.setFacing(CameraKit.Constants.FACING_FRONT);
//                setFacingImageBasedOnCamera();
//            }
//        }
//
//        if (coverViewId != -1) {
//            View view = getRootView().findViewById(coverViewId);
//            if (view != null) {
//                coverView = view;
//                coverView.setVisibility(GONE);
//            }
//        }
//    }
//
//    private void setFacingImageBasedOnCamera() {
//        if (cameraView.isFacingFront()) {
//            facingButton.setImageResource(R.drawable.ic_facing_front);
//        } else {
//            facingButton.setImageResource(R.drawable.ic_facing_back);
//        }
//    }
//
//
//    @OnTouch(R.id.captureButton)
//    boolean onTouchCapture(View view, MotionEvent motionEvent) {
//        handleViewTouchFeedback(view, motionEvent);
//        switch (motionEvent.getAction()) {
//            case MotionEvent.ACTION_DOWN: {
//                if (!onlyPhoto) {
//                    pendingVideoCapture = true;
//                    mListener.getCount();
//                    postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (pendingVideoCapture) {
//                                capturingVideo = true;
//                                startTime = SystemClock.uptimeMillis();
//                                // customHandler.postDelayed(updateTimerThread, 0);
//                                tvTimer.setVisibility(GONE);
//                                captureButton.setColorFilter(ContextCompat.getColor(getContext(), R.color.doodle_color_red), android.graphics.PorterDuff.Mode.SRC_IN);
//                                cameraView.captureVideo();
////                                cameraView.captureVideo(new CameraKitView.VideoCallback() {
////                                    @Override
////                                    public void onVideo(CameraKitView cameraKitView, Object o) {
////
////                                        captureListener.videoCaptureDone(o);
////                                    }
////                                });
//
//                                startProgressBar();
//                                mListener.video(true, timerManin);
//                            }
//
//                        }
//                    }, 250);
//                }
//                break;
//            }
//            case MotionEvent.ACTION_UP: {
//                pendingVideoCapture = false;
//                if (capturingVideo) {
//                    stopVideo(2);
//                } else {
////                    if (cameraView != null){
//
////                        cameraView  .captureImage(new CameraKitView.ImageCallback() {
////                            @Override
////                            public void onImage(CameraKitView cameraKitView, byte[] bytes) {
////
////
////
////                                captureListener.imageCaptureDone(bytes);
////                            }
////                        });
////                    }
//                    cameraView.captureImage();
//                }
//                break;
//            }
//        }
//        return true;
//
//    }
//
//    private void stopVideo(int i) {
//        cancel();
//        mListener.stopProgress();
//        cameraView.stopVideo();
//        mListener.video(false, timerManin);
//        capturingVideo = false;
//
//
//        tvTimer.setVisibility(GONE);
//        timerManin = 0;
//        captureButton.setColorFilter(null);
//
//
//    }
//
//    @OnTouch(R.id.facingButton)
//    boolean onTouchFacing(final View view, MotionEvent motionEvent) {
//        handleViewTouchFeedback(view, motionEvent);
//        switch (motionEvent.getAction()) {
//            case MotionEvent.ACTION_UP: {
//                coverView.setAlpha(0);
//                coverView.setVisibility(VISIBLE);
//                coverView.animate().alpha(1)
//                        .setStartDelay(0)
//                        .setDuration(300)
//                        .setListener(new AnimatorListenerAdapter() {
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                super.onAnimationEnd(animation);
//                                if (cameraView.isFacingFront()) {
//                                    cameraView.setFacing(CameraKit.Constants.FACING_BACK);
//                                    changeViewImageResource((ImageView) view, R.drawable.ic_facing_front);
//                                } else {
//                                    cameraView.setFacing(CameraKit.Constants.FACING_FRONT);
//                                    changeViewImageResource((ImageView) view, R.drawable.ic_facing_back);
//                                }
//                                mListener.face(cameraView.isFacingFront());
//                                coverView.animate()
//                                        .alpha(0)
//                                        .setStartDelay(200)
//                                        .setDuration(300)
//                                        .setListener(new AnimatorListenerAdapter() {
//                                            @Override
//                                            public void onAnimationEnd(Animator animation) {
//                                                super.onAnimationEnd(animation);
//                                                coverView.setVisibility(GONE);
//                                            }
//                                        })
//                                        .start();
//                            }
//                        })
//                        .start();
//
//                break;
//            }
//        }
//        return true;
//    }
//
//    @OnTouch(R.id.flashButton)
//    boolean onTouchFlash(View view, MotionEvent motionEvent) {
//        handleViewTouchFeedback(view, motionEvent);
//        switch (motionEvent.getAction()) {
//            case MotionEvent.ACTION_DOWN: {
//                if (cameraView.getFlash() == CameraKit.Constants.FLASH_OFF) {
//                    cameraView.setFlash(CameraKit.Constants.FLASH_ON);
//                    changeViewImageResource((ImageView) view, R.drawable.ic_flash_on);
//                } else {
//                    cameraView.setFlash(CameraKit.Constants.FLASH_OFF);
//                    changeViewImageResource((ImageView) view, R.drawable.ic_flash_off);
//                }
//
//                break;
//            }
//        }
//        return true;
//    }
//
//    boolean handleViewTouchFeedback(View view, MotionEvent motionEvent) {
//        switch (motionEvent.getAction()) {
//            case MotionEvent.ACTION_DOWN: {
//                touchDownAnimation(view);
//                return true;
//            }
//
//            case MotionEvent.ACTION_UP: {
//                touchUpAnimation(view);
//                return true;
//            }
//
//            default: {
//                return true;
//            }
//        }
//    }
//
//    void touchDownAnimation(View view) {
//        view.animate()
//                .scaleX(0.88f)
//                .scaleY(0.88f)
//                .setDuration(300)
//                .setInterpolator(new OvershootInterpolator())
//                .start();
//    }
//
//    void touchUpAnimation(View view) {
//        view.animate()
//                .scaleX(1f)
//                .scaleY(1f)
//                .setDuration(300)
//                .setInterpolator(new OvershootInterpolator())
//                .start();
//    }
//
//    void changeViewImageResource(final ImageView imageView, @DrawableRes final int resId) {
//        imageView.setRotation(0);
//        imageView.animate()
//                .rotationBy(360)
//                .setDuration(400)
//                .setInterpolator(new OvershootInterpolator())
//                .start();
//        imageView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                imageView.setImageResource(resId);
//            }
//        }, 120);
//    }
//
//    public void startProgressBar() {
//        mTt1 = new TimerTask() {
//            public void run() {
//
//                customHandler.post(new Runnable() {
//                    public void run() {
//                        timerManin++;
//                        mListener.progress();
//                        Log.d("timers", "run: " + timerManin);
//                       /* if (sec != 0) {
//                            if (timerManin >= sec) {
//                                stopVideo(1);
//                            }
//                        } else if (timerManin >= 60) {
//                            stopVideo(1);
//
//                        }*/
//
//
//                    }
//                });
//            }
//        };
//        new Timer().schedule(mTt1, 0, 1000);
//    }
//
//    public void cancel() {
//
//        if (mTt1 != null)
//            mTt1.cancel();
//    }
//
//    private Runnable updateTimerThread = new Runnable() {
//
//        public void run() {
//            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
//
//
//            Log.d("camera", "run: " + timeInMilliseconds + "Df " + timeSwapBuff + "Asd" + updatedTime);
//            updatedTime = timeSwapBuff + timeInMilliseconds;
//            Log.d("camera", "run: " + timeInMilliseconds + "Df " + timeSwapBuff + "Asd" + updatedTime);
//
//            int secs = (int) (updatedTime / 1000);
//            int mins = secs / 60;
//            secs = secs % 60;
//            timeCap = secs;
//            //  long milliseconds = secs * 1000;
//            tvTimer.setText("" + mins + ":" + String.format("%02d", secs));
//            customHandler.postDelayed(this, 0);
//            if (sec != 0) {
//                if (secs >= sec) {
//                    stopVideo(1);
//                }
//            }
//        }
//    };
}

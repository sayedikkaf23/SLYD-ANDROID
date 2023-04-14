package chat.hola.com.app.DublyCamera;

import androidx.appcompat.app.AppCompatActivity;

//CAMERA
public class PreviewActivity extends AppCompatActivity {//implements FilterAdapter.SelectFilterListner, TextEditorDialogFragment.OnTextLayerCallback {
//    private final int CAMERA_REQUEST = 222;
//    private File file;
//    @BindView(R.id.image)
//    ImageView imageView;
//    @BindView(R.id.video)
//    VideoView videoView;
//    @BindView(R.id.bottom_sheet)
//    View bottomSheet;
//    @BindView(R.id.rvFilters)
//    RecyclerView rvFilters;
//    @BindView(R.id.ibFilter)
//    ImageButton ibFilter;
//    @BindView(R.id.eT_caption)
//    EditText eT_caption;
//
//    private BottomSheetBehavior behavior;
//
//    String path = "", type = "";
//    String name;
//    Bundle bundle = new Bundle();
//
//    private int i = 1;
//    private String appName;
//
//    /*  for image editing  */
//    private View includeImageEditLayout, swipeForFilters;
//    public static final int SELECT_STICKER_REQUEST_CODE = 123;
//    private static final String TAG = CameraControlPanel.class.getSimpleName();
//    private Activity mActivity;
//    private ImageView originalImage;
//    private String folderPath, capturedImagePath;
//    private GPUImageView mGPUImageView;
//    private int count = 0;
//    private File temp;
//
//    private ArrayList<String> files;
//
//    private LinearLayout selectColour;
//    private DrawViewOnImage drawView;
//    private RelativeLayout doodleView;
//    private ImageView redButton, blackButton, greenButton, blueButton, addStickers, drawDoodle, ivUndo;
//    private TextView addText;
//    private boolean isUndoActive, isStickerActive, isTextActive, isDoodleActive;
//
//    protected MotionView motionView;
//    protected View textEntityEditPanel;
//    private FontProvider fontProvider;
//    private String musicId;
//
//    private String audioFile;
//    boolean isDubly = false;
//
//    private MotionView motionView_Filter;
//    private View view_filter;
//    private GPUImageView gpuImageView_Video;
//
//    private List<Filter> filterImages;
//    private FilterAdapter filterAdapter;
//
//    private final MotionView.MotionViewCallback motionViewCallback = new MotionView.MotionViewCallback() {
//        @Override
//        public void onEntitySelected(@Nullable MotionEntity entity) {
//            if (entity instanceof TextEntity) {
//                textEntityEditPanel.setVisibility(View.VISIBLE);
//            } else {
//                textEntityEditPanel.setVisibility(GONE);
//            }
//        }
//
//        @Override
//        public void onEntityDoubleTap(@NonNull MotionEntity entity) {
//            startTextEntityEditing(currentTextEntity());
//        }
//    };
//    private boolean isPlaying = false;
//    private MediaPlayer player;
//    private int filter = 0;
//    private String filterColor = "";
//
//    @OnClick(R.id.back_button)
//    public void back() {
//        ResultHolder.dispose();
//        onBackPressed();
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.camera_activity_preview);
//        ButterKnife.bind(this);
//        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
//        appName = "Demo";//getResources().getString(R.string.app_name);
//
//        final File imageFolder;
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            imageFolder = new File(Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.app_name) + "/Media/");
//
//        } else {
//
//            imageFolder = new File(getFilesDir() + "/" + getResources().getString(R.string.app_name) + "/Media/");
//        }
//        if (!imageFolder.exists() && !imageFolder.isDirectory())
//            imageFolder.mkdirs();
//
//
//        folderPath = imageFolder.getAbsolutePath();
//
//        File video = ResultHolder.getVideo();
//
//        isDubly = getIntent().getBooleanExtra("isDubly", false);
//        files = getIntent().getStringArrayListExtra("videoArray");
//        audioFile = getIntent().getStringExtra("audio");
//
//        eT_caption.setVisibility(ResultHolder.getCall().equals("story") ? VISIBLE : GONE);
//
//        if (ResultHolder.getType().equals("image")) {
//            //image
//            type = Constants.Post.IMAGE;
//            file = new File(ResultHolder.getPath());
//            path = file.getAbsolutePath();
//            name = file.getName();
//
//            // ResultHolder.setDuration(getDuration(file));
//
//
//            imageView.setVisibility(VISIBLE);
//            Bitmap bitmap = BitmapFactory.decodeFile(path);
//
//            if (bitmap == null) {
//                finish();
//                return;
//            }
//
//            imageView.setImageBitmap(bitmap);
//
//            mActivity = PreviewActivity.this;
//            includeImageEditLayout = findViewById(R.id.image_edit_layout);
//            includeImageEditLayout.setVisibility(VISIBLE);
//            createEditingLayout(path);
//        } else if (ResultHolder.getType().equals("video")) {
//            //video
//            boolean isFrontFace = getIntent().getBooleanExtra("isFrontFace", false);
//            type = Constants.Post.VIDEO;
//            ibFilter.setVisibility(VISIBLE);
////            if (isFrontFace)
////                videoView.setRotation(90.0f);
//
//            includeImageEditLayout = findViewById(R.id.image_edit_layout);
//            includeImageEditLayout.setVisibility(GONE);
//            createVideoFilter();
//            try {
//                musicId = getIntent().getStringExtra("musicId");
//                if (video != null) {
//                    if (audioFile != null) {
//                        player = MediaPlayer.create(PreviewActivity.this, Uri.parse(audioFile));
//
//
//                        player.setLooping(false);
//                        player.start();
//
//                    }
//                    Display display = getWindowManager().getDefaultDisplay();
//                    Point size = new Point();
//                    display.getSize(size);
//                    videoView.getHolder().setFixedSize(size.x, size.y);
//                    videoView.setVisibility(VISIBLE);
//
//                    MediaController mediaController = new MediaController(PreviewActivity.this);
//                    mediaController.setVisibility(GONE);
//                    videoView.setMediaController(mediaController);
//                    path = video.getAbsolutePath();
//                    videoView.setVideoPath(files.get(0));
//                    path = files.get(0);
//                } else {
//                    path = ResultHolder.getPath();
//                    videoView.setVideoPath(path);
//                }
//
//                videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//                    @Override
//                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
//                        return false;
//                    }
//                });
//
//                videoView.setOnPreparedListener(mp -> {
//                    mp.setLooping(false);
//                    // videoView.setZOrderOnTop(true);
//                    videoView.start();
//
//                });
//
//                //videoView.start();
//            } catch (Exception ignored) {
//
//            }
//        } else {
//            finish();
//        }
//
//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//
//
//                if (files.size() > 0) {
//                    if (i < files.size()) {
//                        Display display = getWindowManager().getDefaultDisplay();
//                        Point size = new Point();
//                        display.getSize(size);
//                        videoView.getHolder().setFixedSize(size.x, size.y);
//                        videoView.setVisibility(VISIBLE);
//                        MediaController mediaController = new MediaController(PreviewActivity.this);
//                        mediaController.setVisibility(GONE);
//                        videoView.setMediaController(mediaController);
//                        videoView.setVideoPath(files.get(i++));
//                    } else {
//                        if (player != null) {
//                            player.pause();
//                            player.seekTo(0);
//                            player.start();
//                        }
//
//                        Display display = getWindowManager().getDefaultDisplay();
//                        Point size = new Point();
//                        display.getSize(size);
//                        videoView.getHolder().setFixedSize(size.x, size.y);
//                        videoView.setVisibility(VISIBLE);
//                        MediaController mediaController = new MediaController(PreviewActivity.this);
//                        mediaController.setVisibility(GONE);
//                        videoView.setMediaController(mediaController);
//                        videoView.setVideoPath(files.get(0));
//                        i = 0;
//                        i++;
//                    }
//                }
//
//   /*     if (files.size()>1){
//
//            for (i=0;i<files.size();i++){
//                Display display = getWindowManager().getDefaultDisplay();
//                Point size = new Point();
//                display.getSize(size);
//                videoView.getHolder().setFixedSize(size.x,size.y);
//                videoView.setVisibility(VISIBLE);
//                MediaController mediaController = new MediaController(PreviewActivity.this);
//                mediaController.setVisibility(GONE);
//                videoView.setMediaController(mediaController);
//                videoView.setVideoPath(files.get(i));
//
//
//            }
//        }*/
//
//            }
//        });
//
//        behavior = BottomSheetBehavior.from(bottomSheet);
//
//
//        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//        filterImages = new ArrayList<>();
//        filterImages.add(new Filter(R.drawable.filter0, "", true));
//        filterImages.add(new Filter(R.drawable.filter1, "#507FF489", false));
//        filterImages.add(new Filter(R.drawable.filter2, "#50dc070e", false));
//        filterImages.add(new Filter(R.drawable.filter3, "#503362c1", false));
//        filterImages.add(new Filter(R.drawable.filter4, "#50c13378", false));
//        filterImages.add(new Filter(R.drawable.filter5, "#5033c144", false));
//        filterImages.add(new Filter(R.drawable.filter6, "#50712989", false));
//
//
//        filterAdapter = new FilterAdapter(this, filterImages, this);
//        rvFilters.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        rvFilters.setAdapter(filterAdapter);
//    }
//
//    @OnClick(R.id.ibFilter)
//    public void filters() {
//        bottomSheet.setVisibility(View.VISIBLE);
//        bottomSheet.post(() -> {
//            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        });
//    }
//
//    private void createVideoFilter() {
//        gpuImageView_Video = (GPUImageView) findViewById(R.id.iv_video_GPU);
//        motionView_Filter = (MotionView) findViewById(R.id.main_motion_view_video);
//        view_filter = (View) findViewById(R.id.swipe_for_filter_video);
//        initializeViewForFiltersVideo();
//    }
//
//    private void initializeViewForFiltersVideo() {
//        view_filter.setVisibility(VISIBLE);
//        view_filter.setOnTouchListener(new OnSwipeTouchListener(mActivity) {
//
//
//            public void onSwipeRight() {
//
//                filter++;
//                if (filter > 10) {
//                    filter = 0;
//                }
//                switchFilterToVideo(filter);
//
//            }
//
//            public void onSwipeLeft() {
//
//                filter--;
//                if (filter < 1) {
//                    filter = 7;
//                }
//                switchFilterToVideo(filter);
//
//            }
//
//        });
//    }
//
//    private void switchFilterToVideo(int filter) {
//        for (int i = 0; i < filterImages.size(); i++) {
//            filterImages.get(i).setSelected(i == filter);
//        }
//        filterAdapter.notifyDataSetChanged();
//        switch (filter) {
//            case 0:
//                view_filter.setBackgroundColor(Color.TRANSPARENT);
//                filterColor = "";
//            case 1:
//                view_filter.setBackgroundColor(Color.parseColor("#507FF489"));
//                filterColor = "#507FF489";//1
//                break;
//            case 2:
//                view_filter.setBackgroundColor(Color.parseColor("#50dc070e"));
//                filterColor = "#50dc070e";//6
//                break;
//            case 3:
//                view_filter.setBackgroundColor(Color.parseColor("#503362c1"));
//                filterColor = "#503362c1"; //2
//                break;
//            case 4:
//                view_filter.setBackgroundColor(Color.parseColor("#50c13378"));
//                filterColor = "#50c13378";//5
//                break;
//            case 5:
//                view_filter.setBackgroundColor(Color.parseColor("#5033c144"));
//                filterColor = "#5033c144";//4
//                break;
//            case 6:
//                view_filter.setBackgroundColor(Color.parseColor("#50712989"));
//                filterColor = "#50712989";//3
//                break;
//        }
//    }
//
//    private long getDuration(Uri file) {
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(this, file);
//        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//        long timeInMillisec = Long.parseLong(time);
//        retriever.release();
//        return timeInMillisec;
//    }
//
//    @OnClick(R.id.ivNext)
//    public void next() {
//        if (isPlaying) {
//            videoView.pause();
//            isPlaying = false;
//        } else {
//            videoView.resume();
//            isPlaying = true;
//        }
////        startNextActivity();
//        // saveAndSendImage.setVisibility(GONE);
//        ProgressBar sending = (ProgressBar) includeImageEditLayout.findViewById(R.id.pb_sending_image);
//        sending.setVisibility(View.VISIBLE);
//        if (type.equals(Constants.Post.IMAGE)) {
//            hideDoodleView(true);
//            saveFilteredImage();
//        } else {
//            startNextActivity();
//        }
//    }
//
//    private void startNextActivity() {
//        Intent intent;
//        switch (ResultHolder.getCall()) {
//            case "story":
//                chat.hola.com.howdoo.post.model.PostData postData = new chat.hola.com.howdoo.post.model.PostData();
//                Post post = new Post();
//                PostDb db = new PostDb(this);
//
//
//                Long tsLong = System.currentTimeMillis() / 1000;
//                String ts = tsLong.toString();
//                post.setPathForCloudinary(path);
//                post.setTypeForCloudinary(type);
//                post.setId(ts);
//                Log.i("POST-ID", "" + post.getId());
//                post.setFiles(files);
//                post.setAudioFile(audioFile);
////                post.setFilterColor(filterColor);
//                post.setStory(true);
//                if (type.equals(Constants.Post.VIDEO))
//                    post.setDub(true);
//                if (musicId != null)
//                    post.setMusicId(musicId);
//
//                post.setPrivateStory(false);
//
//                if (!eT_caption.getText().toString().trim().isEmpty())
//                    bundle.putString("caption", eT_caption.getText().toString().trim());
//                if (!type.equals("image")) {
//                    bundle.putString("duration", String.valueOf(getDuration(Uri.parse(files.get(0)))));
//                }
//
//                postData.setUserId(AppController.getInstance().getUserId());
//                postData.setData(new Gson().toJson(post));
//                postData.setStatus(0);
//                postData.setId(ts);
//
//                postData.setMerged(false);
//                db.addData(postData);
//
//                try {
////                    JSONObject obj = new JSONObject();
////                    obj.put("postData", postData);
//
//                    //AppController.getBus().post(obj);
//                    AppController.getInstance().addNewPost(postData);
//                }
////                catch (JSONException e) {
////                    e.printStackTrace();
////                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//
////                bundle.putString("path", path);
////                bundle.putInt("type", type.equals("image") ? 0 : 1);
////                bundle.putBoolean("isPrivate", false);
////                bundle.putString("duration", String.valueOf(getDuration(Uri.parse(files.get(0)))));
////                intent = new Intent(this, StoryService.class);
////                intent.putExtra("data", bundle);
////                startService(intent);
//                break;
//
//            case "SaveProfile":
//                intent = new Intent(this, SaveProfile.class);
//                intent.putExtra("path", path);
//                Bundle bundle = getIntent().getExtras();
//                String userName = bundle != null ? bundle.getString("userName") : "";
//                String firstName = bundle != null ? bundle.getString("firstName") : "";
//                String lastName = bundle != null ? bundle.getString("lastName") : "";
//                boolean isPrivate = bundle.getBoolean("private");
//                Log.i("PreviewActivity", " private " + isPrivate);
//                if (!TextUtils.isEmpty(userName))
//                    intent.putExtra("userName", userName);
//                if (!TextUtils.isEmpty(firstName))
//                    intent.putExtra("firstName", firstName);
//                if (!TextUtils.isEmpty(lastName))
//                    intent.putExtra("lastName", lastName);
//                intent.putExtra("private", isPrivate ? 1 : 0);
//                startActivity(intent);
//                break;
//            default:
//                intent = new Intent(this, PostActivity.class);
//                intent.putExtra(Constants.Post.PATH, path);
//                intent.putExtra(Constants.Post.TYPE, type);
//                intent.putExtra("videoArray", files);
//                intent.putExtra("audio", audioFile);
//                intent.putExtra("filterColor", filterColor);
//                if (musicId != null)
//                    intent.putExtra("musicId", musicId);
//                startActivityForResult(intent, CAMERA_REQUEST);
//        }
//        finish();
//    }
//
//
//    @Override
//    protected void onStop() {
//
//        try {
//            if (player != null) {
//
//                if (player.isPlaying()) {
//                    player.stop();
//                }
//                player.release();
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        super.onStop();
//    }
//
//
//    private void createEditingLayout(String path) {
//
//        this.fontProvider = new FontProvider(getResources());
//
//        capturedImagePath = path;
//
//        originalImage = (ImageView) includeImageEditLayout.findViewById(R.id.iv_picture);
//      /*  File file = new File(capturedImagePath);
//        Bitmap imageBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//        originalImageBitmap.setImageBitmap(imageBitmap);
//        */
////        originalImageBitmap.setImageURI(Uri.fromFile(new File(capturedImagePath)));
////        originalImageBitmap.setImageURI(Uri.parse(new File(capturedImagePath).toString()));
//
//        mGPUImageView = (GPUImageView) includeImageEditLayout.findViewById(R.id.iv_picture_filter);
//        if (capturedImagePath != null)
//            temp = new File(capturedImagePath);
//        mGPUImageView.setImage(temp);
//
//        motionView = (MotionView) includeImageEditLayout.findViewById(R.id.main_motion_view);
//
//        textEntityEditPanel = includeImageEditLayout.findViewById(R.id.main_motion_text_entity_edit_panel);
//        motionView.setMotionViewCallback(motionViewCallback);
//
//        initTextEntitiesListenersAndOtherViews();
//
//        initializeDoodleLayout();
//
//        initializeColourSelectionView();
//
//        initializeViewForFilters();
//
//    }
//
//
//    private void initializeViewForFilters() {
//        swipeForFilters = (View) includeImageEditLayout.findViewById(R.id.swipe_for_filter);
//        swipeForFilters.setVisibility(VISIBLE);
//        swipeForFilters.setOnTouchListener(new OnSwipeTouchListener(mActivity) {
//            public void onSwipeTop() {
//
//                if (count != 0) {
//                    count = 0;
//                    switchFilterTo();
//                }
//            }
//
//            public void onSwipeRight() {
//
//                if (++count > 15) {
//                    count = 0;
//                }
//                switchFilterTo();
//
//            }
//
//            public void onSwipeLeft() {
//
//                if (--count < 0) {
//                    count = 15;
//                }
//
//                switchFilterTo();
//
//            }
//
//            public void onSwipeBottom() {
//                if (count != 0) {
//                    count = 0;
//                    switchFilterTo();
//                }
//            }
//
//        });
//    }
//
//    private void initializeColourSelectionView() {
//
//        //    initialize select colour view at last..
//
//
//        selectColour = (LinearLayout) includeImageEditLayout.findViewById(R.id.select_colour);
//
//        redButton = (ImageView) includeImageEditLayout.findViewById(R.id.redColour);
//        blackButton = (ImageView) includeImageEditLayout.findViewById(R.id.blackColour);
//        greenButton = (ImageView) includeImageEditLayout.findViewById(R.id.greenColour);
//        blueButton = (ImageView) includeImageEditLayout.findViewById(R.id.blueColour);
//
//        blackButton.setSelected(true);
//        redButton.setSelected(false);
//        greenButton.setSelected(false);
//        blueButton.setSelected(false);
//        redButton.setClickable(true);
//        blackButton.setClickable(true);
//        greenButton.setClickable(true);
//        blueButton.setClickable(true);
//        blackButton.setSelected(true);
//
//        redButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                System.out.println("keyBoardHeight009: redButton");
//
//                blackButton.setSelected(false);
//                redButton.setSelected(true);
//                greenButton.setSelected(false);
//                blueButton.setSelected(false);
//                drawView.mPaint.setColor(ContextCompat.getColor(mActivity, R.color.doodle_color_red));
//
//                setTextColour(ContextCompat.getColor(mActivity, R.color.doodle_color_red));
//            }
//        });
//
//
//        blackButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                System.out.println("keyBoardHeight009: blackButton");
//                try {
//                    blackButton.setSelected(true);
//                    redButton.setSelected(false);
//                    greenButton.setSelected(false);
//                    blueButton.setSelected(false);
//                    drawView.mPaint.setColor(ContextCompat.getColor(mActivity, R.color.doodle_color_black));
//                    setTextColour(ContextCompat.getColor(mActivity, R.color.doodle_color_black));
//                } catch (Exception ignored) {
//                }
//            }
//        });
//
//
//        blueButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                System.out.println("keyBoardHeight009: blueButton");
//                blackButton.setSelected(false);
//                redButton.setSelected(false);
//                greenButton.setSelected(false);
//                blueButton.setSelected(true);
//                drawView.mPaint.setColor(ContextCompat.getColor(mActivity, R.color.doodle_color_blue));
//                setTextColour(ContextCompat.getColor(mActivity, R.color.doodle_color_blue));
//            }
//        });
//
//        greenButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                System.out.println("keyBoardHeight009: greenButton");
//                blackButton.setSelected(false);
//                redButton.setSelected(false);
//                greenButton.setSelected(true);
//                blueButton.setSelected(false);
//                drawView.mPaint.setColor(ContextCompat.getColor(mActivity, R.color.doodle_color_green));
//                setTextColour(ContextCompat.getColor(mActivity, R.color.doodle_color_green));
//            }
//        });
//    }
//
//    private void setTextColour(int selectedColor) {
//        TextEntity textEntity = currentTextEntity();
//        if (textEntity != null) {
//            textEntity.getLayer().getFont().setColor(selectedColor);
//            textEntity.updateEntity();
//            motionView.invalidate();
//        }
//    }
//
//    private void initializeDoodleLayout() {
//
//        doodleView = (RelativeLayout) includeImageEditLayout.findViewById(R.id.draw_doodle);
//        drawView = new DrawViewOnImage(mActivity);
//        doodleView.addView(drawView);
//
//    }
//
//    private void hideDoodleView(boolean addDoodleToImage) {
//
//        if (doodleView != null && doodleView.getVisibility() == View.VISIBLE) {
//
//            if (addDoodleToImage) {
//                addDoodle(drawView.getmBitmap());
////                doodleView.setVisibility(View.GONE);
//            }
//
//            doodleView.removeAllViews();
//            doodleView.invalidate();
//            drawView = new DrawViewOnImage(mActivity);
//            doodleView.addView(drawView);
//            redButton.setClickable(true);
//            blackButton.setClickable(true);
//            greenButton.setClickable(true);
//            blueButton.setClickable(true);
//            blackButton.setSelected(true);
//            redButton.setSelected(false);
//            greenButton.setSelected(false);
//            blueButton.setSelected(false);
//            drawView.mPaint.setColor(ContextCompat.getColor(mActivity, R.color.doodle_color_black));
//            doodleView.setVisibility(GONE);
//
//        }
//
//    }
//
//    private void addDoodle(final Bitmap bitmap) {
//
//        motionView.post(new Runnable() {
//            @Override
//            public void run() {
//                Layer layer = new Layer();
//
//                ImageEntity entity = new ImageEntity(layer, bitmap, motionView.getWidth(), motionView.getHeight());
//
//                motionView.addEntity(entity);
////                motionView.addEntityAndPosition(entity);
//            }
//        });
//    }
//
//    private void addSticker(final int stickerResId) {
//        motionView.post(new Runnable() {
//            @Override
//            public void run() {
//                Layer layer = new Layer();
//                Bitmap pica = BitmapFactory.decodeResource(getResources(), stickerResId);
//
//                ImageEntity entity = new ImageEntity(layer, pica, motionView.getWidth(), motionView.getHeight());
//
//                motionView.addEntityAndPosition(entity);
//            }
//        });
//    }
//
//    private void initTextEntitiesListenersAndOtherViews() {
//        includeImageEditLayout.findViewById(R.id.text_entity_font_size_increase).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                increaseTextEntitySize();
//            }
//        });
//        includeImageEditLayout.findViewById(R.id.text_entity_font_size_decrease).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                decreaseTextEntitySize();
//            }
//        });
//
//        includeImageEditLayout.findViewById(R.id.text_entity_font_change).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                changeTextEntityFont();
//            }
//        });
//        includeImageEditLayout.findViewById(R.id.text_entity_edit).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startTextEntityEditing(currentTextEntity());
//            }
//        });
//
//        ImageView close = (ImageView) includeImageEditLayout.findViewById(R.id.close);
//        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackButtonPressed();
//            }
//        });
//
//
//        addStickers = (ImageView) includeImageEditLayout.findViewById(R.id.iv_add_sticker);
//        addStickers.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (isStickerActive) {
//                    isStickerActive = false;
////                    addStickers.setColorFilter(ContextCompat.getColor(context, R.color.color_white), android.graphics.PorterDuff.Mode..MULTIPLY);
//                    addStickers.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
//                    swipeForFilters.setVisibility(VISIBLE);
//                } else {
//                    isStickerActive = true;
//                    isTextActive = false;
//                    isDoodleActive = false;
//                    isUndoActive = false;
//                    swipeForFilters.setVisibility(GONE);
//
//                    hideDoodleView(true);
//
//                    addStickers.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
//                    drawDoodle.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
//                    addText.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white));
//                    ivUndo.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
//
//                    selectColour.setVisibility(GONE);
//                    Intent intent = new Intent(mActivity, StickerSelectActivity.class);
//                    intent.putExtra("capturedImagePath", capturedImagePath);
//                    mActivity.startActivityForResult(intent, SELECT_STICKER_REQUEST_CODE);
//                }
//
//            }
//        });
//
//        drawDoodle = (ImageView) includeImageEditLayout.findViewById(R.id.iv_draw_doodle);
//        drawDoodle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (isDoodleActive) {
//                    isDoodleActive = false;
//                    drawDoodle.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
//                    selectColour.setVisibility(GONE);
//                    swipeForFilters.setVisibility(VISIBLE);
//
//                } else {
//                    isDoodleActive = true;
//                    isUndoActive = false;
//                    isTextActive = false;
//                    isStickerActive = false;
//                    swipeForFilters.setVisibility(GONE);
//
//                    doodleView.setVisibility(View.VISIBLE);
//                    drawDoodle.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
//                    selectColour.setVisibility(VISIBLE);
//                    addStickers.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
//                    addText.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white));
//                    ivUndo.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
//
//                }
//
//            }
//        });
//
//        ivUndo = (ImageView) includeImageEditLayout.findViewById(R.id.iv_undo);
//        ivUndo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (isUndoActive) {
//                    isUndoActive = false;
//                    ivUndo.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
//                    swipeForFilters.setVisibility(VISIBLE);
//
//                } else {
//                    isUndoActive = true;
//                    isDoodleActive = false;
//                    isTextActive = false;
//                    isStickerActive = false;
//                    swipeForFilters.setVisibility(GONE);
//
//
//                    if (doodleView.getVisibility() == View.VISIBLE)
//                        hideDoodleView(false);
//                    else
//                        undo();
//                    // set the tint for Vector Drawable
//                    ivUndo.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
//                    selectColour.setVisibility(GONE);
//                    drawDoodle.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
//                    addText.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white));
//                    addStickers.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
//
//                }
//
//
//            }
//        });
//
////        final ImageView saveAndSendImage = (ImageView) includeImageEditLayout.findViewById(R.id.save_and_send_image);
////        saveAndSendImage.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////
////                saveAndSendImage.setVisibility(GONE);
////
////                ProgressBar sending = (ProgressBar) includeImageEditLayout.findViewById(R.id.pb_sending_image);
////                sending.setVisibility(View.VISIBLE);
////
////                hideDoodleView(true);
////
////                saveFilteredImage();
////
////            }
////        });
//
//        addText = (TextView) includeImageEditLayout.findViewById(R.id.tv_add_text);
//        addText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (isTextActive) {
//                    isTextActive = false;
//                    addText.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white));
//                    selectColour.setVisibility(GONE);
//                    swipeForFilters.setVisibility(VISIBLE);
//
//                } else {
//                    isTextActive = true;
//                    isUndoActive = false;
//                    isDoodleActive = false;
//                    isStickerActive = false;
//                    swipeForFilters.setVisibility(GONE);
//                    hideDoodleView(true);
//                    selectColour.setVisibility(VISIBLE);
//                    addTextSticker();
//                    addText.setTextColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
//                    drawDoodle.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
//                    addStickers.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
//                    ivUndo.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
//
//                }
//
//            }
//        });
//
//
//        /* filter stuffs  (not much of this is used in this class) */
//
//        count = 0;
//
//    }
//
//
//    private void saveFilteredImage() {
//        try {
//            final String tempPath = System.currentTimeMillis() + appName + ".jpg";
//
//            mGPUImageView.saveToPictures(folderPath, tempPath, new GPUImageView.OnPictureSavedListener() {
//                @Override
//                public void onPictureSaved(final Uri uri) {
//                    originalImage.setImageURI(uri);
////                    System.out.println("picturepath:saveImage925: " + uri.toString());
//
//                    saveImage(motionView);
//
//                }
//            });
//        } catch (NullPointerException ignored) {
//
//        } catch (Exception ignored) {
//        }
//
//    }
//
//    private void increaseTextEntitySize() {
//        TextEntity textEntity = currentTextEntity();
//        if (textEntity != null) {
//            textEntity.getLayer().getFont().increaseSize(TextLayer.Limits.FONT_SIZE_STEP);
//            textEntity.updateEntity();
//            motionView.invalidate();
//        }
//    }
//
//    private void decreaseTextEntitySize() {
//        TextEntity textEntity = currentTextEntity();
//        if (textEntity != null) {
//            textEntity.getLayer().getFont().decreaseSize(TextLayer.Limits.FONT_SIZE_STEP);
//            textEntity.updateEntity();
//            motionView.invalidate();
//        }
//    }
//
//    private void changeTextEntityFont() {
//        final List<String> fonts = fontProvider.getFontNames();
//        FontsAdapter fontsAdapter = new FontsAdapter(mActivity, fonts, fontProvider);
//        new AlertDialog.Builder(mActivity)
//                .setTitle(R.string.select_font)
//                .setAdapter(fontsAdapter, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int which) {
//                        TextEntity textEntity = currentTextEntity();
//                        if (textEntity != null) {
//                            textEntity.getLayer().getFont().setTypeface(fonts.get(which));
//                            textEntity.updateEntity();
//                            motionView.invalidate();
//                        }
//                    }
//                })
//                .show();
//    }
//
//    private void startTextEntityEditing(TextEntity textEntity) {
//
//
//        if (textEntity != null) {
//            TextEditorDialogFragment fragment = TextEditorDialogFragment.getInstance(textEntity.getLayer().getText());
//            fragment.show(getFragmentManager(), TextEditorDialogFragment.class.getName());
//        }
//
//
//    }
//
//    @Nullable
//    private TextEntity currentTextEntity() {
//        if (motionView != null && motionView.getSelectedEntity() instanceof TextEntity) {
//            return ((TextEntity) motionView.getSelectedEntity());
//        } else {
//            return null;
//        }
//    }
//
//
//    protected void addTextSticker() {
//        try {
//            TextLayer textLayer = createTextLayer();
//            TextEntity textEntity = new TextEntity(textLayer, motionView.getWidth(),
//                    motionView.getHeight(), fontProvider);
//
//            motionView.addEntityAndPosition(textEntity);
//
//            // move text sticker up so that its not hidden under keyboard
//            PointF center = textEntity.absoluteCenter();
//            center.y = center.y * 0.5F;
//            textEntity.moveCenterTo(center);
//
//            // redraw
//            motionView.invalidate();
//
//            startTextEntityEditing(currentTextEntity());
//        } catch (Exception ignored) {
//
//        }
//    }
//
//    private TextLayer createTextLayer() {
//        TextLayer textLayer = new TextLayer();
//        Font font = new Font();
//
//        font.setColor(TextLayer.Limits.INITIAL_FONT_COLOR);
//        font.setSize(TextLayer.Limits.INITIAL_FONT_SIZE);
//        font.setTypeface(fontProvider.getDefaultFontName());
//
//        textLayer.setFont(font);
//
//        if (BuildConfig.DEBUG) {
//            textLayer.setText("Hello, " + getResources().getString(R.string.app_name) + "...");
//        }
//
//        return textLayer;
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            if (requestCode == SELECT_STICKER_REQUEST_CODE) {
//                onStickerSelectActivityResult(data);
//            }
//        }
//    }
//
//
//    public void onStickerSelectActivityResult(Intent data) {
//        if (data != null) {
//            int stickerId = data.getIntExtra(StickerSelectActivity.EXTRA_STICKER_ID, 0);
//            if (stickerId != 0) {
//                addSticker(stickerId);
//            }
//        }
//    }
//
//    public void onTextChanged(String text) {
//        TextEntity textEntity = currentTextEntity();
//        if (textEntity != null) {
//            TextLayer textLayer = textEntity.getLayer();
//            if (!text.equals(textLayer.getText())) {
//                textLayer.setText(text);
//                textEntity.updateEntity();
//                motionView.invalidate();
//            }
//        }
//    }
//
//    public void onBackButtonPressed() {
//        mActivity.onBackPressed();
//    }
//
//    public Bitmap getBitmapFromMV(MotionView motionView) {
//
//        int size_x = motionView.getWidth();
//        int size_y = motionView.getHeight();
//        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
//
//        Bitmap bmp = Bitmap.createBitmap(size_x, size_y, conf);
//
//        Canvas final_cnv = new Canvas(bmp);
//        originalImage.draw(final_cnv);
//        List<MotionEntity> mentities = motionView.getEntities();
//        for (int i = 0; i < mentities.size(); i++) {
//            mentities.get(i).draw(final_cnv, new Paint());
//        }
//
//        return bmp;
//    }
//
//    private void saveImage(MotionView motionView) {
//
//        File imageFile;
//        final String SavedImagePath;
//
//        try {
//
//            Bitmap imageBitmap = getBitmapFromMV(motionView);
//
////            System.out.println("cameraBitmap:picturePath: " + imageBitmap.toString());
//
//
//            final File imageFolder;
//            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                imageFolder = new File(Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.app_name) + "/Media/");
//
//            } else {
//
//                imageFolder = new File(mActivity.getFilesDir() + "/" + getResources().getString(R.string.app_name) + "/Media/");
//            }
//
//
//            boolean success = true;
//            if (!imageFolder.exists() && !imageFolder.isDirectory())
//                success = imageFolder.mkdirs();
//
//
//            if (success) {
//                SavedImagePath = imageFolder.getAbsolutePath() + File.separator + String.valueOf(System.currentTimeMillis()) + appName + ".jpg";
//
//                imageFile = new File(SavedImagePath);
//            } else {
//                Toast.makeText(mActivity, "Image Not saved", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//            // Save image into gallery
//            if (imageBitmap != null) {
//                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//            }
//
//            FileOutputStream file_out = new FileOutputStream(imageFile);
//            file_out.write(outputStream.toByteArray());
//            file_out.close();
//            ContentValues values = new ContentValues();
//
//            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
//            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//            values.put(MediaStore.MediaColumns.DATA, imageFile.getAbsolutePath());
//
//            mActivity.getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//
////            System.out.println(TAG + " " + "picturePath= " + SavedImagePath);
//
///*
//            Intent intent = new Intent();
//            intent.putExtra("imagePath", SavedImagePath);
//            setResult(RESULT_OK, intent);
//            finish();
//*/
//
//
////            int mimeType = getMimeType(mActivity, SavedImagePath);
////            SandriosBus.getBus().send(new CameraOutputModel(mimeType, SavedImagePath));
////            mActivity.finish();
//
//            path = SavedImagePath;
//            startNextActivity();
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void switchFilterTo() {
//
//        GPUImageFilter mFilter = GenerateThumbnails
//                .createFilterForType(mActivity,
//                        GenerateThumbnails.FilterType.values()[count]);
//        mGPUImageView.setFilter(mFilter);
//        mGPUImageView.requestRender();
//
//    }
//
//    private void undo() {
//        List<MotionEntity> mentities = motionView.getEntities();
//
//        int entitySize = mentities.size();
//        if (entitySize > 0) {
//            motionView.removeEntity(mentities.get(entitySize - 1));
//        }
//    }
//
//    @Override
//    public void textChanged(@NonNull String text) {
//        onTextChanged(text);
//    }
//
//    @Override
//    public void onFilterSelected(int position) {
//        switchFilterToVideo(position);
//    }
}

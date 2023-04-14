package chat.hola.com.app.DublyCamera;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.PostDb;
import chat.hola.com.app.Doodle.DrawViewOnImage;
import chat.hola.com.app.DublyCamera.CameraInFragments.Filters.Adapters.Edit_Adapter;
import chat.hola.com.app.DublyCamera.CameraInFragments.Filters.Adapters.Filter_Adapter;
import chat.hola.com.app.DublyCamera.CameraInFragments.Filters.CustomGPUImageView;
import chat.hola.com.app.DublyCamera.CameraInFragments.Filters.HelperClasses.FilterTools;
import chat.hola.com.app.DublyCamera.CameraInFragments.Filters.HelperClasses.RangeSeekBar;
import chat.hola.com.app.DublyCamera.CameraInFragments.Filters.ManageFilters;
import chat.hola.com.app.DublyCamera.CameraInFragments.Filters.ModelClasses.Edit_item;
import chat.hola.com.app.DublyCamera.CameraInFragments.Filters.ModelClasses.Filter_item;
import chat.hola.com.app.DublyCamera.CameraInFragments.Filters.NoStatusBarCircleDialog;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.OnSwipeTouchListener;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;
import chat.hola.com.app.cameraActivities.TextEditingListener;
import chat.hola.com.app.motionView.filters.helperClasses.GenerateThumbnails;
import chat.hola.com.app.motionView.motionviews.ui.StickerSelectActivity;
import chat.hola.com.app.motionView.motionviews.ui.TextEditorDialogFragment;
import chat.hola.com.app.motionView.motionviews.ui.adapter.FontsAdapter;
import chat.hola.com.app.motionView.motionviews.utils.FontProvider;
import chat.hola.com.app.motionView.motionviews.viewmodel.Font;
import chat.hola.com.app.motionView.motionviews.viewmodel.Layer;
import chat.hola.com.app.motionView.motionviews.viewmodel.TextLayer;
import chat.hola.com.app.motionView.motionviews.widget.MotionView;
import chat.hola.com.app.motionView.motionviews.widget.entity.ImageEntity;
import chat.hola.com.app.motionView.motionviews.widget.entity.MotionEntity;
import chat.hola.com.app.motionView.motionviews.widget.entity.TextEntity;
import chat.hola.com.app.post.PostActivity;
import chat.hola.com.app.post.model.Post;
import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.google.gson.Gson;
import com.squareup.otto.Bus;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import org.json.JSONException;
import org.json.JSONObject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PreviewImageActivity extends AppCompatActivity
    implements TextEditingListener, TextEditorDialogFragment.OnTextLayerCallback {

  @BindView(R.id.eT_caption)
  EditText eT_caption;
  @BindView(R.id.rL_caption)
  RelativeLayout rL_caption;
  @BindView(R.id.rL_edit)
  RelativeLayout rL_edit;
  //For Insta like filters

  @BindView(R.id.iV_filter)
  ImageView iV_filter;
  @BindView(R.id.iV_tools)
  ImageView iV_tools;
  @BindView(R.id.mainFiltersView)
  NestedScrollView mainFiltersView;


  @BindView(R.id.ll_filters)
  LinearLayout ll_filters;
  @BindView(R.id.ll_tools)
  LinearLayout ll_tools;
  @BindView(R.id.dummy)
  CustomGPUImageView dummyIv;
  @BindView(R.id.rl_seekbar_filter_image)
  RelativeLayout rlSeekBar;
  @BindView(R.id.sb_filters_filter_image)
  RangeSeekBar<Integer> seekBar;

  @BindView(R.id.ll_cancel_filter_image)
  LinearLayout llCancel;

  @BindView(R.id.ll_done_filter_image)
  LinearLayout llDone;

  @BindView(R.id.ll_cancel_done_filter_image)
  LinearLayout llCancelDone;

  @BindView(R.id.tvRangeMin)
  AppCompatTextView tvRangeMin;
  @BindView(R.id.tvRangeMax)
  AppCompatTextView tvRangeMax;
  @BindView(R.id.tvRangeMedian)
  AppCompatTextView tvRangeMedian;

  private ArrayList<Filter_item> mFilterData = new ArrayList<>();
  private ArrayList<Edit_item> mEditData = new ArrayList<>();
  private Filter_Adapter mAdapter;
  private Edit_Adapter mAdapter2;
  private Dialog pDialog;
  private int countFilter = 0;

  private int density_100, density_80;
  private File temp;

  private int min2 = 0, max2 = 100;

  private int min1 = -100, max1 = 100;

  /**
   * Vignette and sharpen 0 - 100 and others -100 to 100
   */

  private boolean briSel = false, conSel = false, shadSel = false, vigSel = false, sharSel = false,
      satSel = false;

  private Animation anim1, anim2;

  private boolean firstTime = true;

  /*  for image editing  */
  private View includeImageEditLayout, swipeForFilters;
  public static final int SELECT_STICKER_REQUEST_CODE = 0;

  private Activity mActivity;
  private ImageView originalImage;
  private String folderPath, capturedImagePath;
  private GPUImageView mGPUImageView;
  private int count = 0;

  private LinearLayout selectColour;
  private DrawViewOnImage drawView;
  private RelativeLayout doodleView;
  private ImageView redButton, blackButton, greenButton, blueButton;

  private boolean textSelected, doodleSelected;

  protected MotionView motionView;
  protected View textEntityEditPanel;
  private FontProvider fontProvider;
  private boolean filterSelected = true;
  private String appName;
  private FilterTools.FilterAdjuster mFilterAdjuster;
  private ArrayList<Integer> excludedIds = new ArrayList<>();


  private int filterId = -1;
  private int[] filterString = {
      R.string.text_filter_in1977, R.string.text_filter_amaro, R.string.text_filter_brannan,
      R.string.text_filter_early_bird, R.string.text_filter_hefe, R.string.text_filter_hudson,
      R.string.text_filter_inkwell, R.string.text_filter_lomofi, R.string.text_filter_lord_kelvin,
      R.string.text_filter_early_bird, R.string.text_filter_rise, R.string.text_filter_sierra,
      R.string.text_filter_sutro, R.string.text_filter_toaster, R.string.text_filter_valencia,
      R.string.text_filter_walden, R.string.text_filter_xproii
  };

  private int[] toolString = {
      R.string.Brightness, R.string.Contrast, R.string.Saturation, /**R.string.Highlight*/
      R.string.Shadow, R.string.Vignette, R.string.Sharpen
  };

  private int[] toolIcons = {
      R.drawable.tool_brightness_whiteout, R.drawable.tool_contrast_whiteout,
      R.drawable.tool_saturation_whiteout, /**R.drawable.tool_highlights_whiteout*/
      R.drawable.tool_shadows_whiteout, R.drawable.tool_vignette_whiteout,
      R.drawable.tool_sharpen_whiteout
  };

  private int currentFilterId;

  private String filterFolderPath;
  private boolean cancelShowing;

  //    private TextEditingListener textEditingListener;

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_camera_image_preview);
    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    mActivity = PreviewImageActivity.this;
    ButterKnife.bind(this);
    //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

    final File imageFolder;
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      imageFolder = new File(getExternalFilesDir(null)
          + "/"
          + getResources().getString(R.string.app_name)
          + "/Media/");
    } else {

      imageFolder =
          new File(getFilesDir() + "/" + getResources().getString(R.string.app_name) + "/Media/");
    }
    if (!imageFolder.exists() && !imageFolder.isDirectory()) imageFolder.mkdirs();

    final File filterImageFolder;
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      filterImageFolder = new File(getExternalFilesDir(null)
          + "/"
          + getResources().getString(R.string.app_name)
          + "/Media/filters");
    } else {

      filterImageFolder = new File(
          getFilesDir() + "/" + getResources().getString(R.string.app_name) + "/Media/filters");
    }
    if (!filterImageFolder.exists() && !filterImageFolder.isDirectory()) filterImageFolder.mkdirs();

    folderPath = imageFolder.getAbsolutePath();

    filterFolderPath = filterImageFolder.getAbsolutePath();

    appName = "Demo";
    //getResources().getString(R.string.app_name);

    //        textEditingListener=this;
    //        setTextEditingListener(textEditingListener);

    eT_caption.setVisibility(
        chat.hola.com.app.DublyCamera.ResultHolder.getCall().equals("story") ? VISIBLE
            : View.INVISIBLE);
    rL_edit.setVisibility(chat.hola.com.app.DublyCamera.ResultHolder.getCall().equals("story")
        || chat.hola.com.app.DublyCamera.ResultHolder.getCall().equals("post") ? VISIBLE : GONE);
    if (chat.hola.com.app.DublyCamera.ResultHolder.getType().equals("image")) {

      //Result of the image capture
      includeImageEditLayout = findViewById(R.id.image_edit_layout);

      createEditingLayout(chat.hola.com.app.DublyCamera.ResultHolder.getPath());
    } else {

      supportFinishAfterTransition();
    }

    //For the instagram like filters
    initializeXmlContent();

    if (firstTime) {
      firstTime = false;
      generatePreviewImages(filterFolderPath);
      addFilterTools();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (AppController.getInstance().isFiltersUpdated()) {

      AppController.getInstance().setFiltersUpdated(false);

      addUpdatedFiltersToList();
    }
  }

  //    public void setTextEditingListener(TextEditingListener textEditingListener) {
  //        this.textEditingListener = textEditingListener;
  //    }

  @OnClick(R.id.ivNext)
  public void next() {

    ProgressBar sending = (ProgressBar) includeImageEditLayout.findViewById(R.id.pb_sending_image);
    sending.setVisibility(View.VISIBLE);
    hideDoodleView(true);
    saveFilteredImage();
  }

  private void createEditingLayout(String path) {

    this.fontProvider = new FontProvider(getResources());

    capturedImagePath = path;

    originalImage = (ImageView) includeImageEditLayout.findViewById(R.id.iv_picture);

    mGPUImageView = (GPUImageView) includeImageEditLayout.findViewById(R.id.iv_picture_filter);

    temp = new File(capturedImagePath);

    if (capturedImagePath != null) {

      mGPUImageView.setImage(temp);
      mGPUImageView.requestRender();
      dummyIv.setImage(temp);
    }

    motionView = (MotionView) includeImageEditLayout.findViewById(R.id.main_motion_view);
    motionView.setMotionViewCallback(motionViewCallback);
    textEntityEditPanel =
        includeImageEditLayout.findViewById(R.id.main_motion_text_entity_edit_panel);

    //        includeImageEditLayout.setOnTouchListener(new View.OnTouchListener() {
    //            @Override
    //            public boolean onTouch(View v, MotionEvent event) {
    //
    //                if(event.getAction()==MotionEvent.ACTION_UP){
    //                    if(mainFiltersView.getVisibility()==VISIBLE)
    //                        mainFiltersView.setVisibility(GONE);
    //                }
    //
    //                return false;
    //            }
    //        });

    initTextEntitiesListenersAndOtherViews();

    initializeDoodleLayout();

    initializeColourSelectionView();

    initializeViewForFilters();
  }

  public static void hideSoftKeyboard(Activity activity) {

    InputMethodManager inputMethodManager =
        (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
    if (activity.getCurrentFocus() != null) {
      inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
  }

  private void initializeViewForFilters() {
    swipeForFilters = (View) includeImageEditLayout.findViewById(R.id.swipe_for_filter);
    swipeForFilters.setVisibility(VISIBLE);
    swipeForFilters.setOnTouchListener(new OnSwipeTouchListener(mActivity) {
      public void onSwipeTop() {

        //                if (count != 0) {
        //                    count = 0;
        //                    switchFilterTo();
        //                }
      }

      @Override
      public void onSingleTap() {

        if (mainFiltersView.getVisibility() == VISIBLE) mainFiltersView.setVisibility(GONE);

        hideSoftKeyboard(PreviewImageActivity.this);

        super.onSingleTap();
      }

      public void onSwipeRight() {

        if (++count > (mFilterData.size() - 2)) {
          count = 0;
        }
        currentFilterId = updateSelectedFilterInList();

        switchFilterTo(currentFilterId);
      }

      public void onSwipeLeft() {
        if (--count < 0) {
          count = mFilterData.size() - 2;
        }
        currentFilterId = updateSelectedFilterInList();
        switchFilterTo(currentFilterId);
      }

      public void onSwipeBottom() {
        //                if (count != 0) {
        //                    count = 0;
        //                    switchFilterTo();
        //                }
      }
    });
  }

  private void switchFilterTo(int currentFilterId) {

    GPUImageFilter mFilter = GenerateThumbnails.createFilterForType(mActivity,
        GenerateThumbnails.FilterType.values()[currentFilterId]);

    mGPUImageView.setFilter(mFilter);
    mGPUImageView.requestRender();
  }

  private void initializeColourSelectionView() {

    //    initialize select colour view at last..

    selectColour = (LinearLayout) includeImageEditLayout.findViewById(R.id.select_colour);

    redButton = (ImageView) includeImageEditLayout.findViewById(R.id.redColour);
    blackButton = (ImageView) includeImageEditLayout.findViewById(R.id.blackColour);
    greenButton = (ImageView) includeImageEditLayout.findViewById(R.id.greenColour);
    blueButton = (ImageView) includeImageEditLayout.findViewById(R.id.blueColour);

    blackButton.setSelected(true);
    redButton.setSelected(false);
    greenButton.setSelected(false);
    blueButton.setSelected(false);
    redButton.setClickable(true);
    blackButton.setClickable(true);
    greenButton.setClickable(true);
    blueButton.setClickable(true);
    blackButton.setSelected(true);

    redButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        blackButton.setSelected(false);
        redButton.setSelected(true);
        greenButton.setSelected(false);
        blueButton.setSelected(false);
        drawView.mPaint.setColor(ContextCompat.getColor(mActivity, R.color.doodle_color_red));
        if (textSelected) {
          setTextColour(ContextCompat.getColor(mActivity, R.color.doodle_color_red));
        }
      }
    });

    blackButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        blackButton.setSelected(true);
        redButton.setSelected(false);
        greenButton.setSelected(false);
        blueButton.setSelected(false);
        drawView.mPaint.setColor(ContextCompat.getColor(mActivity, R.color.doodle_color_black));
        if (textSelected) {
          setTextColour(ContextCompat.getColor(mActivity, R.color.doodle_color_black));
        }
      }
    });

    blueButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        blackButton.setSelected(false);
        redButton.setSelected(false);
        greenButton.setSelected(false);
        blueButton.setSelected(true);
        drawView.mPaint.setColor(ContextCompat.getColor(mActivity, R.color.doodle_color_blue));

        if (textSelected) {
          setTextColour(ContextCompat.getColor(mActivity, R.color.doodle_color_blue));
        }
      }
    });

    greenButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        blackButton.setSelected(false);
        redButton.setSelected(false);
        greenButton.setSelected(true);
        blueButton.setSelected(false);
        drawView.mPaint.setColor(ContextCompat.getColor(mActivity, R.color.doodle_color_green));
        if (textSelected) {
          setTextColour(ContextCompat.getColor(mActivity, R.color.doodle_color_green));
        }
      }
    });
  }

  private void setTextColour(int selectedColor) {
    TextEntity textEntity = currentTextEntity();
    if (textEntity != null) {
      textEntity.getLayer().getFont().setColor(selectedColor);
      textEntity.updateEntity();
      motionView.invalidate();
    }
  }

  private void initializeDoodleLayout() {

    doodleView = (RelativeLayout) includeImageEditLayout.findViewById(R.id.draw_doodle);
    drawView = new DrawViewOnImage(mActivity);
    doodleView.addView(drawView);
  }

  private void hideDoodleView(boolean addDoodleToImage) {

    if (doodleView.getVisibility() == View.VISIBLE) {

      if (addDoodleToImage) {
        addDoodle(drawView.getmBitmap());
      }

      doodleView.removeAllViews();
      doodleView.invalidate();
      drawView = new DrawViewOnImage(mActivity);
      doodleView.addView(drawView);

      if (addDoodleToImage) {
        doodleView.setVisibility(View.GONE);
        redButton.setClickable(true);
        blackButton.setClickable(true);
        greenButton.setClickable(true);
        blueButton.setClickable(true);
        blackButton.setSelected(true);
        redButton.setSelected(false);
        greenButton.setSelected(false);
        blueButton.setSelected(false);
        drawView.mPaint.setColor(ContextCompat.getColor(mActivity, R.color.doodle_color_black));
      }
    }
  }

  private void addDoodle(final Bitmap bitmap) {

    motionView.post(new Runnable() {
      @Override
      public void run() {
        Layer layer = new Layer();

        ImageEntity entity =
            new ImageEntity(layer, bitmap, motionView.getWidth(), motionView.getHeight());

        motionView.addEntity(entity);
      }
    });
  }

  private void addSticker(final int stickerResId) {
    motionView.post(new Runnable() {
      @Override
      public void run() {
        Layer layer = new Layer();
        Bitmap pica = BitmapFactory.decodeResource(getResources(), stickerResId);

        ImageEntity entity =
            new ImageEntity(layer, pica, motionView.getWidth(), motionView.getHeight());

        motionView.addEntityAndPosition(entity);
      }
    });
  }

  private void initTextEntitiesListenersAndOtherViews() {
    includeImageEditLayout.findViewById(R.id.text_entity_font_size_increase)
        .setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            increaseTextEntitySize();
          }
        });
    includeImageEditLayout.findViewById(R.id.text_entity_font_size_decrease)
        .setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            decreaseTextEntitySize();
          }
        });

    includeImageEditLayout.findViewById(R.id.text_entity_font_change)
        .setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            changeTextEntityFont();
          }
        });
    includeImageEditLayout.findViewById(R.id.text_entity_edit)
        .setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            startTextEntityEditing(currentTextEntity());
          }
        });

    ImageView close = (ImageView) includeImageEditLayout.findViewById(R.id.close);
    close.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onBackButtonPressed();
      }
    });

    final ImageView addStickers =
        (ImageView) includeImageEditLayout.findViewById(R.id.iv_add_sticker);
    final ImageView drawDoodle =
        (ImageView) includeImageEditLayout.findViewById(R.id.iv_draw_doodle);
    final ImageView ivUndo = (ImageView) includeImageEditLayout.findViewById(R.id.iv_undo);
    final TextView addText = (TextView) includeImageEditLayout.findViewById(R.id.tv_add_text);

    final ImageView addFilter = (ImageView) includeImageEditLayout.findViewById(R.id.iv_filter);

    addStickers.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if (doodleSelected) {

          hideDoodleView(true);
          selectColour.setVisibility(GONE);
          drawDoodle.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white),
              android.graphics.PorterDuff.Mode.SRC_IN);
          doodleSelected = false;
        } else if (filterSelected) {
          filterSelected = false;
          swipeForFilters.setVisibility(View.GONE);
          addFilter.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white),
              android.graphics.PorterDuff.Mode.SRC_IN);
        } else if (textSelected) {
          textSelected = false;
          textEntityEditPanel.setVisibility(View.GONE);
          selectColour.setVisibility(GONE);
          addText.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white));
        }

        Intent intent = new Intent(mActivity, StickerSelectActivity.class);
        intent.putExtra("capturedImagePath", capturedImagePath);
        mActivity.startActivityForResult(intent, SELECT_STICKER_REQUEST_CODE);
      }
    });

    addFilter.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if (filterSelected) {
          filterSelected = false;
          addFilter.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);
          swipeForFilters.setVisibility(GONE);
        } else {

          filterSelected = true;
          swipeForFilters.setVisibility(VISIBLE);
          addFilter.setColorFilter(ContextCompat.getColor(mActivity, R.color.blue),
              android.graphics.PorterDuff.Mode.SRC_IN);

          if (doodleSelected) {

            doodleSelected = false;
            hideDoodleView(true);
            selectColour.setVisibility(GONE);
            drawDoodle.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white),
                android.graphics.PorterDuff.Mode.SRC_IN);
          } else if (textSelected) {
            textSelected = false;
            textEntityEditPanel.setVisibility(View.GONE);
            selectColour.setVisibility(GONE);
            addText.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white));
          }
        }
      }
    });

    drawDoodle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if (doodleSelected) {
          doodleSelected = false;
          drawDoodle.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white),
              android.graphics.PorterDuff.Mode.SRC_IN);
          hideDoodleView(true);
          selectColour.setVisibility(View.GONE);
        } else {

          doodleSelected = true;
          doodleView.setVisibility(View.VISIBLE);
          selectColour.setVisibility(View.VISIBLE);
          drawDoodle.setColorFilter(ContextCompat.getColor(mActivity, R.color.blue),
              android.graphics.PorterDuff.Mode.SRC_IN);

          if (filterSelected) {
            filterSelected = false;
            swipeForFilters.setVisibility(View.GONE);
            addFilter.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white),
                android.graphics.PorterDuff.Mode.SRC_IN);
          } else if (textSelected) {
            textSelected = false;
            textEntityEditPanel.setVisibility(View.GONE);
            addText.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white));
          }
        }
      }
    });

    addText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if (textSelected) {
          textSelected = false;
          textEntityEditPanel.setVisibility(View.GONE);
          selectColour.setVisibility(View.GONE);
          addText.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white));
        } else {
          textSelected = true;
          selectColour.setVisibility(View.VISIBLE);
          addTextSticker();
          addText.setTextColor(ContextCompat.getColor(mActivity, R.color.blue));

          if (doodleSelected) {

            doodleSelected = false;
            hideDoodleView(true);

            drawDoodle.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white),
                android.graphics.PorterDuff.Mode.SRC_IN);
          } else if (filterSelected) {
            filterSelected = false;
            swipeForFilters.setVisibility(View.GONE);
            addFilter.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white),
                android.graphics.PorterDuff.Mode.SRC_IN);
          }
        }
      }
    });

    ivUndo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        if (doodleSelected) {
          hideDoodleView(false);
        } else if (textSelected) {
            textSelected = false;
            textEntityEditPanel.setVisibility(View.GONE);
            selectColour.setVisibility(GONE);
            addText.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white));
        } else {
            undo();
        }
      }
    });

    final ImageView saveAndSendImage =
        (ImageView) includeImageEditLayout.findViewById(R.id.save_and_send_image);
    saveAndSendImage.setVisibility(View.GONE);
  }

  private void saveFilteredImage() {
    try {
      final String name = System.currentTimeMillis() + "filteredImage.jpg";

      mGPUImageView.saveToPictures(getResources().getString(R.string.app_name), name,
          new GPUImageView.OnPictureSavedListener() {
            @Override
            public void onPictureSaved(final Uri uri) {
              originalImage.setImageURI(uri);

              String tempPath = getExternalFilesDir(null)
                  + "/"
                  + getResources().getString(R.string.app_name)
                  + "/"
                  + name;
              saveImage(motionView, tempPath);
            }
          });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void increaseTextEntitySize() {
    TextEntity textEntity = currentTextEntity();
    if (textEntity != null) {
      textEntity.getLayer().getFont().increaseSize(TextLayer.Limits.FONT_SIZE_STEP);
      textEntity.updateEntity();
      motionView.invalidate();
    }
  }

  private void decreaseTextEntitySize() {
    TextEntity textEntity = currentTextEntity();
    if (textEntity != null) {
      textEntity.getLayer().getFont().decreaseSize(TextLayer.Limits.FONT_SIZE_STEP);
      textEntity.updateEntity();
      motionView.invalidate();
    }
  }

  private void changeTextEntityFont() {
    final List<String> fonts = fontProvider.getFontNames();
    FontsAdapter fontsAdapter = new FontsAdapter(mActivity, fonts, fontProvider);
    new AlertDialog.Builder(mActivity).setTitle(R.string.select_font)
        .setAdapter(fontsAdapter, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int which) {
            TextEntity textEntity = currentTextEntity();
            if (textEntity != null) {
              textEntity.getLayer().getFont().setTypeface(fonts.get(which));
              textEntity.updateEntity();
              motionView.invalidate();
            }
          }
        })
        .show();
  }

  private void startTextEntityEditing(TextEntity textEntity) {
    //
    //        if (textEditingListener != null) {
    //            textEditingListener.startEditingTextEntity(textEntity);
    //        }
    startEditingTextEntity(textEntity);
  }

  @Nullable
  private TextEntity currentTextEntity() {
    if (motionView != null && motionView.getSelectedEntity() instanceof TextEntity) {
      return ((TextEntity) motionView.getSelectedEntity());
    } else {
      return null;
    }
  }

  protected void addTextSticker() {
    TextLayer textLayer = createTextLayer();
    TextEntity textEntity =
        new TextEntity(textLayer, motionView.getWidth(), motionView.getHeight(), fontProvider);

    motionView.addEntityAndPosition(textEntity);

    // move text sticker up so that its not hidden under keyboard
    PointF center = textEntity.absoluteCenter();
    center.y = center.y * 0.5F;
    textEntity.moveCenterTo(center);

    // redraw
    motionView.invalidate();

    startTextEntityEditing(currentTextEntity());
  }

  private TextLayer createTextLayer() {
    TextLayer textLayer = new TextLayer();
    Font font = new Font();

    font.setColor(TextLayer.Limits.INITIAL_FONT_COLOR);
    font.setSize(TextLayer.Limits.INITIAL_FONT_SIZE);
    font.setTypeface(fontProvider.getDefaultFontName());

    textLayer.setFont(font);

    if (BuildConfig.DEBUG) {
      textLayer.setText(getString(R.string.hello)
          + getString(R.string.comma)
          + getString(R.string.space)
          + getResources().getString(R.string.app_name)
          + getString(R.string.cont_dot));
    }

    return textLayer;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      if (requestCode == SELECT_STICKER_REQUEST_CODE) {
        onStickerSelectActivityResult(data);
      }
    }
  }

  public void onStickerSelectActivityResult(Intent data) {
    if (data != null) {
      int stickerId = data.getIntExtra(StickerSelectActivity.EXTRA_STICKER_ID, 0);
      if (stickerId != 0) {
        addSticker(stickerId);
      }
    }
  }

  @Override
  public void textChanged(@NonNull String text) {
    onTextChanged(text);
  }

  public void onTextChanged(String text) {
    TextEntity textEntity = currentTextEntity();
    if (textEntity != null) {
      TextLayer textLayer = textEntity.getLayer();
      if (!text.equals(textLayer.getText())) {
        textLayer.setText(text);
        textEntity.updateEntity();
        motionView.invalidate();
      }
    }
  }

  public void onBackButtonPressed() {

    deleteFileFromDisk(capturedImagePath);
    mActivity.onBackPressed();
  }

  public Bitmap getBitmapFromMV(MotionView motionView) {
    try {

      MotionEntity entity = motionView.getSelectedEntity();

      if (entity != null) {

        motionView.unselectEntity();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    int size_x = motionView.getWidth();
    int size_y = motionView.getHeight();
    Bitmap.Config conf = Bitmap.Config.ARGB_8888;

    Bitmap bmp = Bitmap.createBitmap(size_x, size_y, conf);

    Canvas final_cnv = new Canvas(bmp);
    originalImage.draw(final_cnv);
    List<MotionEntity> motionViewEntities = motionView.getEntities();
    for (int i = 0; i < motionViewEntities.size(); i++) {
      motionViewEntities.get(i).draw(final_cnv, new Paint());
    }

    return bmp;
  }

  private void saveImage(MotionView motionView, String tempPath) {

    File imageFile;
    final String savedImagePath;

    try {

      Bitmap imageBitmap = getBitmapFromMV(motionView);

      final File imageFolder;
      if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        imageFolder = new File(getExternalFilesDir(null)
            + "/"
            + getResources().getString(R.string.app_name)
            + "/Media/Images/");
      } else {

        imageFolder = new File(mActivity.getFilesDir()
            + "/"
            + getResources().getString(R.string.app_name)
            + "/Media/Images/");
      }

      boolean success = true;
      if (!imageFolder.exists() && !imageFolder.isDirectory()) success = imageFolder.mkdirs();

      if (success) {
        savedImagePath = imageFolder.getAbsolutePath()
            + File.separator
            + String.valueOf(System.nanoTime())
            + appName
            + ".jpg";

        imageFile = new File(savedImagePath);
      } else {
        Toast.makeText(mActivity, "Image Not saved", Toast.LENGTH_SHORT).show();
        return;
      }

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

      // Save image into gallery
      if (imageBitmap != null) {
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
      }

      FileOutputStream file_out = new FileOutputStream(imageFile);
      file_out.write(outputStream.toByteArray());
      file_out.close();
      //            ContentValues values = new ContentValues();
      //
      //            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
      //            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
      //            values.put(MediaStore.MediaColumns.DATA, imageFile.getAbsolutePath());
      //
      //            mActivity.getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
      deleteFileFromDisk(capturedImagePath);
      deleteFileFromDisk(tempPath);
      AppController.getInstance().notifyOnImageDeleteRequired(tempPath);
      startNextActivity(savedImagePath);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void switchFilterTo(final GPUImageFilter mFilter, boolean flag) {

    if (flag) {

      mGPUImageView.setFilter(mFilter);
      mGPUImageView.requestRender();
    } else {

      dummyIv.setFilter(mFilter);
      dummyIv.requestRender();
    }
  }

  private void switchFilterTo() {

    GPUImageFilter mFilter = GenerateThumbnails.createFilterForType(mActivity,
        GenerateThumbnails.FilterType.values()[count]);
    mGPUImageView.setFilter(mFilter);
    mGPUImageView.requestRender();
  }

  private void undo() {
    List<MotionEntity> mentities = motionView.getEntities();

    int entitySize = mentities.size();
    if (entitySize > 0) {
      try {

        MotionEntity entity = motionView.getSelectedEntity();

        if (entity != null) {

          motionView.unselectEntity();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      motionView.removeEntity(mentities.get(entitySize - 1));
    }
  }

  private final MotionView.MotionViewCallback motionViewCallback =
      new MotionView.MotionViewCallback() {
        @Override
        public void onEntitySelected(@Nullable MotionEntity entity) {
          if (entity instanceof TextEntity) {
            textEntityEditPanel.setVisibility(View.VISIBLE);
          } else {
            textEntityEditPanel.setVisibility(GONE);
          }
        }

        @Override
        public void onEntityDoubleTap(@NonNull MotionEntity entity) {
          startTextEntityEditing(currentTextEntity());
        }
      };

  private void startNextActivity(String path) {

    disableBackNavigationCameraActivity();
    Intent intent;
    switch (chat.hola.com.app.DublyCamera.ResultHolder.getCall()) {
      case "story":

        chat.hola.com.app.post.model.PostData postData =
            new chat.hola.com.app.post.model.PostData();
        Post post = new Post();
        PostDb db = new PostDb(this);

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        post.setPathForCloudinary(path);
        post.setTypeForCloudinary(Constants.Post.IMAGE);
        post.setId(ts);

        post.setStory(true);
        post.setDub(false);
        post.setPrivateStory(false);

        Bundle bundle = new Bundle();
        if (!eT_caption.getText().toString().trim().isEmpty()) {
          post.setCaption(eT_caption.getText().toString().trim());
          bundle.putString("caption", eT_caption.getText().toString().trim());
        }

        postData.setUserId(AppController.getInstance().getUserId());
        postData.setData(new Gson().toJson(post));
        postData.setStatus(0);
        postData.setId(ts);

        postData.setMerged(false);
        db.addData(postData);

        try {

          AppController.getInstance().addNewPost(postData);
        } catch (Exception e) {
          e.printStackTrace();
        }

        break;

      case "SaveProfile":
        intent = new Intent();
        intent.putExtra("profilePic", path);
        setResult(RESULT_OK, intent);

        break;

      case "ChatAttachment":
        intent = new Intent();
        intent.putExtra("imagePath", path);
        intent.putExtra("isImage", true);
        setResult(RESULT_OK, intent);

        break;

      case "EditProfile":
        intent = new Intent();
        intent.putExtra("imagePath", path);
        setResult(RESULT_OK, intent);

        break;

      default:
        intent = new Intent(this, PostActivity.class);
        intent.putExtra(Constants.Post.PATH, path);
        intent.putExtra(Constants.Post.TYPE, Constants.Post.IMAGE);
        //                intent.putExtra("videoArray", files);
        //                intent.putExtra("audio", audioFile);
        //                intent.putExtra("filterColor", filterColor);
        startActivity(intent);
    }
    supportFinishAfterTransition();
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private void deleteFileFromDisk(String filePath) {
    try {
      File file = new File(filePath);

      if (file.exists()) {

        file.delete();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void disableBackNavigationCameraActivity() {

    Bus bus = AppController.getBus();
    try {
      JSONObject obj = new JSONObject();

      obj.put("eventName", "killCameraActivity");
      bus.post(obj);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void startEditingTextEntity(TextEntity textEntity) {
    if (textEntity != null) {
      TextEditorDialogFragment fragment =
          TextEditorDialogFragment.getInstance(textEntity.getLayer().getText());
      fragment.show(getFragmentManager(), TextEditorDialogFragment.class.getName());
    }
  }

  @OnClick({ R.id.iV_filter })
  public void filterIconClick() {
    mainFiltersView.setVisibility(VISIBLE);

    ll_tools.setVisibility(View.GONE);
    ll_filters.setVisibility(View.VISIBLE);
    llCancelDone.setVisibility(GONE);

    if (rlSeekBar.getVisibility() == View.VISIBLE) {

      rlSeekBar.setVisibility(View.GONE);
    }
  }

  @OnClick({ R.id.iV_tools })
  public void toolsIconClick() {
    mainFiltersView.setVisibility(VISIBLE);

    ll_tools.setVisibility(View.VISIBLE);
    ll_filters.setVisibility(View.GONE);
    llCancelDone.setVisibility(GONE);

    if (rlSeekBar.getVisibility() == View.VISIBLE) {

      rlSeekBar.setVisibility(View.GONE);
    }
  }

  private void addUpdatedFiltersToList() {

    excludedIds = AppController.getInstance().getExcludedFilterIds();
    mFilterData.clear();

    for (int i = 0; i < 17; i++) {
      if (!excludedIds.contains(i + 1)) {
        Filter_item item = new Filter_item();

        item.setFilterId(i + 1);
        item.setSelected(false);

        item.setItemType("0");

        item.setImageUrl(filterFolderPath + "/filter_" + (i) + ".jpg");

        item.setFilterName(getString(filterString[i]));

        mFilterData.add(item);
      }
    }
    count = 0;
    Filter_item item = new Filter_item();

    item.setFilterId(0);
    item.setSelected(true);

    item.setItemType("0");

    item.setImageUrl(capturedImagePath);
    item.setFilterName(getString(R.string.text_filter_normal));

    mFilterData.add(0, item);

    item = new Filter_item();

    item.setFilterId(18);
    item.setSelected(false);

    item.setItemType("1");

    item.setFilterName("Manage");
    mFilterData.add(item);

    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        mAdapter.notifyDataSetChanged();
      }
    });
  }

  private void addFilterTools() {

    Edit_item item;
    for (int i = 0; i < 6; i++) {
      item = new Edit_item();

      item.setEditId(i);

      item.setImageUrl(toolIcons[i]);

      item.setEditName(getString(toolString[i]));

      mEditData.add(item);

      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          mAdapter.notifyDataSetChanged();
        }
      });
    }
  }

  private void generatePreviewImages(String folderPath) {

    /*
     *Loading of the filter images
     */

    pDialog =
        NoStatusBarCircleDialog.getInstance().get_Circle_Progress_bar(PreviewImageActivity.this);
    pDialog.setCancelable(false);
    //pDialog.show();

    excludedIds = AppController.getInstance().getExcludedFilterIds();

    generateThumbnails(folderPath);
  }

  private void initializeXmlContent() {

    ll_tools.setVisibility(View.GONE);

    llDone.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        hideCancelView();
      }
    });
    llCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        /**
         * /*Bug Title- Create Post: Camera>>Capture photo>>add 2 effects>> click done add 2nd effect>>click on cancel--the first 2 effects are also reversed
         * /*Bug Id-NA
         * /*Fix Description-Disable clearing of all filters on click of cancel button
         * /*Developer Name-Ashutosh
         * /*Fix Date-7/5/21
         **/
        //mGPUImageView.setFilter(new GPUImageFilter());
        //
        //mGPUImageView.requestRender();

        //if (filterId != -1) {
        //
        //  switchFilterTo(GenerateThumbnails.createFilterForType(PreviewImageActivity.this,
        //      GenerateThumbnails.FilterType.values()[filterId]), true);
        //}

        if (mFilterAdjuster != null) {
          mFilterAdjuster.adjust(Integer.parseInt(tvRangeMedian.getText().toString()));
        }
        mGPUImageView.requestRender();

        hideCancelView();
      }
    });

    RecyclerView filtered_item_list = (RecyclerView) findViewById(R.id.rv_all_filters_filter_image);
    filtered_item_list.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    mAdapter = new Filter_Adapter(PreviewImageActivity.this, mFilterData);
    filtered_item_list.setAdapter(mAdapter);

    RecyclerView edit_item_list = (RecyclerView) findViewById(R.id.rv_all_tools_tool_image);
    edit_item_list.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    mAdapter2 = new Edit_Adapter(PreviewImageActivity.this, mEditData);
    edit_item_list.setAdapter(mAdapter2);

    count = 0;
    density_100 = (int) ((getResources().getDisplayMetrics().density) * (100));
    density_80 = (int) ((getResources().getDisplayMetrics().density) * (80));

    /*
     * To avoid the abrupt scaling of the item on click
     */

    anim1 = new ScaleAnimation(1f, 0.85f, 1f, 0.85f, Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f);
    anim1.setFillAfter(true);
    anim1.setDuration(100);

    anim2 = new ScaleAnimation(0.85f, 1f, 0.85f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f);
    anim2.setFillAfter(true);
    anim2.setDuration(100);

    filtered_item_list.addOnItemTouchListener(
        new RecyclerItemClickListener(this, filtered_item_list,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(final View view, final int position) {

                view.startAnimation(anim1);

                anim1.setAnimationListener(new Animation.AnimationListener() {

                  @Override
                  public void onAnimationStart(Animation animation) {
                  }

                  @Override
                  public void onAnimationRepeat(Animation animation) {
                  }

                  @Override
                  public void onAnimationEnd(Animation animation) {

                    view.startAnimation(anim2);
                  }
                });

                anim2.setAnimationListener(new Animation.AnimationListener() {

                  @Override
                  public void onAnimationStart(Animation animation) {
                  }

                  @Override
                  public void onAnimationRepeat(Animation animation) {
                  }

                  @Override
                  public void onAnimationEnd(Animation animation) {

                    int itemClickedId = mFilterData.get(position).getFilterId();


                    /*
                     * 0 position is for normal
                     */
                    if (itemClickedId > 0 && itemClickedId < 18) {

                      filterId = itemClickedId;
                      switchFilterTo(
                          GenerateThumbnails.createFilterForType(PreviewImageActivity.this,
                              GenerateThumbnails.FilterType.values()[itemClickedId]), true);

                      Filter_item item;

                      count = position;
                      for (int i = 0; i < mFilterData.size(); i++) {

                        item = mFilterData.get(i);
                        item.setSelected(false);
                        mFilterData.set(i, item);
                      }

                      item = mFilterData.get(position);
                      item.setSelected(true);
                      mFilterData.set(position, item);

                      runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          mAdapter.notifyDataSetChanged();
                        }
                      });
                    } else if (itemClickedId == 0) {

                      count = 0;
                      filterId = -1;

                      if (temp != null) {

                        mGPUImageView.setFilter(new GPUImageFilter());

                        mGPUImageView.requestRender();
                      }
                      Filter_item item;

                      for (int i = 0; i < mFilterData.size(); i++) {

                        item = mFilterData.get(i);
                        item.setSelected(false);
                        mFilterData.set(i, item);
                      }

                      item = mFilterData.get(0);
                      item.setSelected(true);
                      mFilterData.set(0, item);

                      runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          mAdapter.notifyDataSetChanged();
                        }
                      });

                      runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          mAdapter.notifyDataSetChanged();
                        }
                      });
                    } else if (itemClickedId == 18) {

                      startActivity(new Intent(PreviewImageActivity.this, ManageFilters.class));
                    }
                  }
                });
              }

              @Override
              public void onItemLongClick(View view, int position) {

              }
            }));

    edit_item_list.
        addOnItemTouchListener(new RecyclerItemClickListener(this, edit_item_list,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(final View view, final int position) {

                /*
                 * For implementing of the edit tool filters(Custom value filters)
                 */
                view.startAnimation(anim1);

                anim1.setAnimationListener(new Animation.AnimationListener() {

                  @Override
                  public void onAnimationStart(Animation animation) {
                  }

                  @Override
                  public void onAnimationRepeat(Animation animation) {
                  }

                  @Override
                  public void onAnimationEnd(Animation animation) {

                    view.startAnimation(anim2);
                  }
                });
                anim2.setAnimationListener(new Animation.AnimationListener() {

                  @Override
                  public void onAnimationStart(Animation animation) {
                  }

                  @Override
                  public void onAnimationRepeat(Animation animation) {
                  }

                  @Override
                  public void onAnimationEnd(Animation animation) {
                    /**
                     * /*Bug Title- Pointer not present at 0 for any of the edit options,The 100 is not being displayed in scroll to edit options in edit post in Saturation,The data is not being saved once edited in edit post in Contrast,The data is not being saved once edited in edit post in Shadows, Vignette, etc,Pointer not present at 0 for any of the edit options such as brightness, saturation etc
                     * /*Bug Id-DUBAND014,DUBAND015,DUBAND017,DUBAND018,DUBAND020
                     * /*Fix Description-Add placeholder text for minimum and maximum values for selected filter
                     * /*Developer Name-Ashutosh
                     * /*Fix Date-6/4/21
                     **/
                    if (position >= 0) {

                      switch (position) {

                        case 0:

                          briSel = true;
                          conSel = false;

                          satSel = false;
                          shadSel = false;

                          vigSel = false;

                          sharSel = false;
                          seekBar.setRangeValues(min1, max1);
                          tvRangeMedian.setText(String.valueOf(0));
                          seekBar.setSelectedMaxValue(0);
                          break;
                        case 1:

                          briSel = false;
                          conSel = true;

                          satSel = false;
                          shadSel = false;

                          vigSel = false;

                          sharSel = false;
                          seekBar.setRangeValues(min1, max1);
                          seekBar.setSelectedMaxValue(0);
                          tvRangeMedian.setText(String.valueOf(0));
                          break;
                        case 2:
                          briSel = false;
                          conSel = false;

                          satSel = true;
                          shadSel = false;

                          vigSel = false;

                          sharSel = false;

                          seekBar.setRangeValues(min1, max1);
                          seekBar.setSelectedMaxValue(0);
                          tvRangeMedian.setText(String.valueOf(0));
                          break;

                        case 3:
                          briSel = false;
                          conSel = false;

                          satSel = false;
                          shadSel = true;

                          vigSel = false;

                          sharSel = false;

                          seekBar.setRangeValues(min1, max1);
                          tvRangeMedian.setText(String.valueOf(0));
                          seekBar.setSelectedMaxValue(0);
                          break;

                        case 4:
                          briSel = false;
                          conSel = false;

                          satSel = false;
                          shadSel = false;

                          vigSel = true;

                          sharSel = false;

                          seekBar.setRangeValues(min2, max2);
                          seekBar.setSelectedMaxValue(50);
                          tvRangeMedian.setText(String.valueOf(50));
                          break;

                        case 5:
                          briSel = false;
                          conSel = false;

                          satSel = false;
                          shadSel = false;

                          vigSel = false;

                          sharSel = true;
                          seekBar.setRangeValues(min2, max2);
                          seekBar.setSelectedMaxValue(50);
                          tvRangeMedian.setText(String.valueOf(50));
                      }

                      tvRangeMin.setText(String.valueOf(seekBar.getAbsoluteMinValue()));
                      tvRangeMax.setText(String.valueOf(seekBar.getAbsoluteMaxValue()));

                      customEditFilter(FilterTools.createFilterForType(PreviewImageActivity.this,
                          FilterTools.FilterType.values()[position]));

                      rlSeekBar.setVisibility(View.VISIBLE);


                      /*
                       * To show the seekbar to be visible
                       *
                       */

                      ll_tools.setVisibility(View.GONE);

                      if (ll_filters.getVisibility() == View.VISIBLE) {

                        ll_filters.setVisibility(View.GONE);
                      }

                      llCancelDone.setVisibility(View.VISIBLE);

                      //llFiltersTools.setVisibility(View.GONE);

                      cancelShowing = true;
                      //topView.setVisibility(View.GONE);

                      //toolName.setText(toolString[position]);
                      //title.setVisibility(View.VISIBLE);

                      //TODO
                      //                              AppController.getInstance().setCancelShowing(true, false);
                    }
                  }
                });
              }

              @Override
              public void onItemLongClick(View view, int position) {

              }
            }));

    seekBar.setOnRangeSeekBarChangeListener(
        new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {

          @Override
          public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue,
              Integer maxValue) {

            if (mFilterAdjuster != null) {
              mFilterAdjuster.adjust(maxValue);
            }
            mGPUImageView.requestRender();
          }
        });
  }

  private void hideCancelView() {

    try {
      if (cancelShowing) {

        llCancelDone.setVisibility(View.GONE);

        //llFiltersTools.setVisibility(View.VISIBLE);

        ll_tools.setVisibility(View.VISIBLE);

        rlSeekBar.setVisibility(View.GONE);
      }

      cancelShowing = false;

      //title.setVisibility(View.GONE);

      //topView.setVisibility(View.VISIBLE);

      //TODO
      //   AppController.getInstance().setCancelShowing(false, false);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }

    /*
     * To cancel all the filters showing
     */
    briSel = false;
    conSel = false;

    satSel = false;
    shadSel = false;

    vigSel = false;

    sharSel = false;
  }

  private void customEditFilter(final GPUImageFilter mFilter) {


    /*
     * To allow the multiple filters to be added one on top of another
     */
    GPUImageFilterGroup group = new GPUImageFilterGroup();

    group.addFilter(mGPUImageView.getFilter());
    group.addFilter(mFilter);

    mGPUImageView.setFilter(group);

    mFilterAdjuster = new FilterTools.FilterAdjuster(mFilter);
    mGPUImageView.requestRender();
  }

  private void generateThumbnails(final String folderPath) {
    /*
     * For saving of the preview thumbnail images
     */

    if (countFilter > 16) {

      /**
       * Visibility.GONE / Visibility.INVISIBLE not working in moto G4 play,vivo devices
       */
      dummyIv.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
      //dummyIv.setVisibility(View.GONE);
      Filter_item item = new Filter_item();

      item.setFilterId(0);
      item.setSelected(true);

      item.setItemType("0");

      item.setImageUrl(capturedImagePath);
      item.setFilterName(getString(R.string.text_filter_normal));

      mFilterData.add(0, item);

      item = new Filter_item();

      item.setFilterId(18);
      item.setSelected(false);

      item.setItemType("1");

      item.setFilterName("Manage");
      mFilterData.add(item);

      runOnUiThread(new Runnable() {
        @Override
        public void run() {

          mAdapter.notifyDataSetChanged();

          if (pDialog != null && pDialog.isShowing()) {

            pDialog.dismiss();
          }
        }
      });

      return;
    }

    switchFilterTo(GenerateThumbnails.createFilterForType(PreviewImageActivity.this,
        GenerateThumbnails.FilterType.values()[countFilter + 1]), false);

    dummyIv.saveToSpecifiedFolder(folderPath, "filter_" + countFilter + ".jpg", density_80,
        density_100, new CustomGPUImageView.OnPictureSavedListener() {
          @Override
          public void onPictureSaved(Uri uri) {

            if (excludedIds != null && !excludedIds.contains(countFilter + 1)) {

              Filter_item item = new Filter_item();

              item.setFilterId(countFilter + 1);
              item.setSelected(false);

              item.setItemType("0");

              item.setImageUrl(folderPath + "/filter_" + (countFilter) + ".jpg");

              item.setFilterName(getString(filterString[countFilter]));

              mFilterData.add(item);

              runOnUiThread(new Runnable() {
                @Override
                public void run() {

                  mAdapter.notifyDataSetChanged();
                }
              });
            }

            countFilter++;

            generateThumbnails(folderPath);
          }
        });
  }

  private int updateSelectedFilterInList() {

    int currentFilterId = 0;

    Filter_item filterItem;

    for (int i = 0; i < mFilterData.size(); i++) {
      filterItem = mFilterData.get(i);

      if (filterItem.isSelected()) {

        filterItem.setSelected(false);
        mFilterData.set(i, filterItem);

        //               mAdapter.notifyItemChanged(i);
        //         break;
      }
    }

    filterItem = mFilterData.get(count);
    filterItem.setSelected(true);
    currentFilterId = filterItem.getFilterId();
    mFilterData.set(count, filterItem);

    //        mAdapter.notifyItemChanged(count);
    mAdapter.notifyDataSetChanged();

    return currentFilterId;
  }
}

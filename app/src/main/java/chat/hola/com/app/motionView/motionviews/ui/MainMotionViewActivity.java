package chat.hola.com.app.motionView.motionviews.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Doodle.DrawViewOnImage;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;
import chat.hola.com.app.motionView.filters.adapters.Filter_Adapter;
import chat.hola.com.app.motionView.filters.helperClasses.GenerateThumbnails;
import chat.hola.com.app.motionView.filters.modelClasses.Filter_item;
import chat.hola.com.app.motionView.motionviews.ui.adapter.FontsAdapter;
import chat.hola.com.app.motionView.motionviews.utils.FontProvider;
import chat.hola.com.app.motionView.motionviews.viewmodel.Font;
import chat.hola.com.app.motionView.motionviews.viewmodel.Layer;
import chat.hola.com.app.motionView.motionviews.viewmodel.TextLayer;
import chat.hola.com.app.motionView.motionviews.widget.MotionView;
import chat.hola.com.app.motionView.motionviews.widget.entity.ImageEntity;
import chat.hola.com.app.motionView.motionviews.widget.entity.MotionEntity;
import chat.hola.com.app.motionView.motionviews.widget.entity.TextEntity;
import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

public class MainMotionViewActivity extends AppCompatActivity
    implements TextEditorDialogFragment.OnTextLayerCallback {

  public static final int SELECT_STICKER_REQUEST_CODE = 123;
  private static final String TAG = MainMotionViewActivity.class.getSimpleName();
  private Activity mActivity;
  private ImageView originalImage;
  private RecyclerView filtersPreview;
  private Filter_Adapter mAdapter;
  private ArrayList<Filter_item> mFilterData = new ArrayList<>();
  private String folderPathh, capturedImagePath;
  private GPUImageView dummyIv;
  private GPUImageView mGPUImageView;
  private int count = 0;
  private int density_100;
  private File temp;
  private int[] filterString = {
      R.string.text_filter_in1977, R.string.text_filter_amaro, R.string.text_filter_brannan,
      R.string.text_filter_early_bird, R.string.text_filter_hefe, R.string.text_filter_hudson,
      R.string.text_filter_inkwell, R.string.text_filter_lomofi, R.string.text_filter_lord_kelvin,
      R.string.text_filter_early_bird, R.string.text_filter_rise, R.string.text_filter_sierra,
      R.string.text_filter_sutro, R.string.text_filter_toaster, R.string.text_filter_valencia,
      R.string.text_filter_walden, R.string.text_filter_xproii
  };
  private Animation anim1, anim2, anim3, anim4;
  private LinearLayout displayFilter, selectColour;
  private DrawViewOnImage drawView;
  private RelativeLayout doodleView;
  private ImageView redButton, blackButton, greenButton, blueButton;

  protected MotionView motionView;
  protected View textEntityEditPanel;
  private FontProvider fontProvider;

  private final MotionView.MotionViewCallback motionViewCallback =
      new MotionView.MotionViewCallback() {
        @Override
        public void onEntitySelected(@Nullable MotionEntity entity) {
          if (entity instanceof TextEntity) {
            textEntityEditPanel.setVisibility(View.VISIBLE);
          } else {
            textEntityEditPanel.setVisibility(View.GONE);
          }
        }

        @Override
        public void onEntityDoubleTap(@NonNull MotionEntity entity) {
          startTextEntityEditing();
        }
      };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_motionview);

    mActivity = MainMotionViewActivity.this;
    this.fontProvider = new FontProvider(getResources());

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      capturedImagePath = extras.getString("imagePath");
    }
    motionView = (MotionView) findViewById(R.id.main_motion_view);

    textEntityEditPanel = findViewById(R.id.main_motion_text_entity_edit_panel);
    motionView.setMotionViewCallback(motionViewCallback);

    initTextEntitiesListenersAndOtherViews();

    if (capturedImagePath != null) temp = new File(capturedImagePath);
    mGPUImageView.setImage(temp);

    generatePreview(capturedImagePath);

    initializeDoodleLayout();

    initializeColourSelectionView();
  }

  private void initializeColourSelectionView() {

    //    initialize select colour view at last..

    selectColour = (LinearLayout) findViewById(R.id.select_colour);

    redButton = (ImageView) findViewById(R.id.redColour);
    blackButton = (ImageView) findViewById(R.id.blackColour);
    greenButton = (ImageView) findViewById(R.id.greenColour);
    blueButton = (ImageView) findViewById(R.id.blueColour);

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

        setTextColour(ContextCompat.getColor(mActivity, R.color.doodle_color_red));
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
        setTextColour(ContextCompat.getColor(mActivity, R.color.doodle_color_black));
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
        setTextColour(ContextCompat.getColor(mActivity, R.color.doodle_color_blue));
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
        setTextColour(ContextCompat.getColor(mActivity, R.color.doodle_color_green));
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

    doodleView = (RelativeLayout) findViewById(R.id.draw_doodle);
    drawView = new DrawViewOnImage(mActivity);
    doodleView.addView(drawView);
  }

  private void hideDoodleView(boolean addDoodleToImage) {

    if (doodleView.getVisibility() == View.VISIBLE) {

      if (addDoodleToImage) {
        addDoodle(drawView.getmBitmap());
        //                doodleView.setVisibility(View.GONE);
      }

      doodleView.removeAllViews();
      doodleView.invalidate();
      drawView = new DrawViewOnImage(mActivity);
      doodleView.addView(drawView);
      redButton.setClickable(true);
      blackButton.setClickable(true);
      greenButton.setClickable(true);
      blueButton.setClickable(true);
      blackButton.setSelected(true);
      redButton.setSelected(false);
      greenButton.setSelected(false);
      blueButton.setSelected(false);
      drawView.mPaint.setColor(ContextCompat.getColor(mActivity, R.color.doodle_color_black));
      doodleView.setVisibility(View.GONE);
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
        //                motionView.addEntityAndPosition(entity);
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
    findViewById(R.id.text_entity_font_size_increase).setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            increaseTextEntitySize();
          }
        });
    findViewById(R.id.text_entity_font_size_decrease).setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            decreaseTextEntitySize();
          }
        });

    findViewById(R.id.text_entity_font_change).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        changeTextEntityFont();
      }
    });
    findViewById(R.id.text_entity_edit).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startTextEntityEditing();
      }
    });

    ImageView close = (ImageView) findViewById(R.id.close);
    close.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onBackPressed();
      }
    });

    ImageView addStickers = (ImageView) findViewById(R.id.iv_add_sticker);
    addStickers.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        closeFilterPreviews();
        hideDoodleView(true);

        Intent intent = new Intent(mActivity, StickerSelectActivity.class);
        intent.putExtra("capturedImagePath", capturedImagePath);
        startActivityForResult(intent, SELECT_STICKER_REQUEST_CODE);
      }
    });

    final ImageView drawDoodle = (ImageView) findViewById(R.id.iv_draw_doodle);
    drawDoodle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        closeFilterPreviews();
        doodleView.setVisibility(View.VISIBLE);
      }
    });

    final ImageView ivUndo = (ImageView) findViewById(R.id.iv_undo);
    ivUndo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        closeFilterPreviews();

          if (doodleView.getVisibility() == View.VISIBLE) {
              hideDoodleView(false);
          } else {
              undo();
          }
      }
    });

    final ImageView saveAndSendImage = (ImageView) findViewById(R.id.save_and_send_image);
    saveAndSendImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        saveAndSendImage.setVisibility(View.GONE);

        ProgressBar sending = (ProgressBar) findViewById(R.id.pb_sending_image);
        sending.setVisibility(View.VISIBLE);

        hideDoodleView(true);

        saveFilteredImage();
      }
    });

    TextView addText = (TextView) findViewById(R.id.tv_add_text);
    addText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        closeFilterPreviews();
        hideDoodleView(true);

        addTextSticker();
      }
    });

    dummyIv = (GPUImageView) findViewById(R.id.dummy);

    originalImage = (ImageView) findViewById(R.id.iv_picture);
    mGPUImageView = (GPUImageView) findViewById(R.id.iv_picture_filter);

    /* filter stuffs */

    displayFilter = (LinearLayout) findViewById(R.id.ll_filters);

    filtersPreview = (RecyclerView) findViewById(R.id.rv_all_filters_preview);
    filtersPreview.setLayoutManager(
        new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
    mAdapter = new Filter_Adapter(mActivity, mFilterData);
    filtersPreview.setAdapter(mAdapter);

    LinearLayout openFilters = (LinearLayout) findViewById(R.id.ll_open_filters);
    openFilters.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        displayFilter.setVisibility(View.VISIBLE);
        mGPUImageView.startAnimation(anim3);
        motionView.startAnimation(anim3);
        hideDoodleView(true);
        //                mGPUImageView.setAnimation(anim3);
      }
    });

    LinearLayout closeFilters = (LinearLayout) findViewById(R.id.ll_close_filters);
    closeFilters.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        closeFilterPreviews();
      }
    });

    count = 0;
    density_100 = (int) ((getResources().getDisplayMetrics().density) * (90));

    anim1 = new ScaleAnimation(1f, 0.85f, 1f, 0.85f, Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f);
    anim1.setFillAfter(true);
    anim1.setDuration(100);

    anim2 = new ScaleAnimation(0.85f, 1f, 0.85f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f);
    anim2.setFillAfter(true);
    anim2.setDuration(100);

    anim3 = new ScaleAnimation(1f, 0.8f, 1f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f);
    anim3.setFillAfter(true);
    anim3.setDuration(350);

    anim4 = new ScaleAnimation(0.8f, 1f, 0.8f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f);
    anim4.setFillAfter(true);
    anim4.setDuration(350);

    filtersPreview.
        addOnItemTouchListener(new RecyclerItemClickListener(mActivity, filtersPreview,
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
                    switchFilterTo(GenerateThumbnails.createFilterForType(mActivity,
                        GenerateThumbnails.FilterType.values()[itemClickedId]), true);

                    for (int i = 0; i < mFilterData.size(); i++) {

                      mFilterData.get(i).setSelected(i == position);
                    }

                    runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                        mAdapter.notifyDataSetChanged();
                      }
                    });
                  }
                });
              }

              @Override
              public void onItemLongClick(View view, int position) {

              }
            }));
  }

  private void closeFilterPreviews() {

    if (displayFilter.getVisibility() == View.VISIBLE) {
      displayFilter.setVisibility(View.GONE);
      mGPUImageView.startAnimation(anim4);
      motionView.startAnimation(anim4);
    }
  }

  private void saveFilteredImage() {

    final String tempPath = System.currentTimeMillis() + "filteredImage.jpg";

    mGPUImageView.saveToPictures(folderPathh, tempPath, uri -> {
      originalImage.setImageURI(uri);

      deleteAllFilterThumbnails();

      saveImage(motionView);
    });
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
    FontsAdapter fontsAdapter = new FontsAdapter(this, fonts, fontProvider);
    new AlertDialog.Builder(this).setTitle(R.string.select_font)
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

  private void startTextEntityEditing() {
    TextEntity textEntity = currentTextEntity();
    if (textEntity != null) {
      TextEditorDialogFragment fragment =
          TextEditorDialogFragment.getInstance(textEntity.getLayer().getText());
      fragment.show(getFragmentManager(), TextEditorDialogFragment.class.getName());
    }
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

    startTextEntityEditing();
  }

  private TextLayer createTextLayer() {
    TextLayer textLayer = new TextLayer();
    Font font = new Font();

    font.setColor(TextLayer.Limits.INITIAL_FONT_COLOR);
    font.setSize(TextLayer.Limits.INITIAL_FONT_SIZE);
    font.setTypeface(fontProvider.getDefaultFontName());

    textLayer.setFont(font);

    if (BuildConfig.DEBUG) {
      textLayer.setText(getString(R.string.hello) +
              getResources().getString(R.string.app_name) +getString(R.string.cont_dot));
    }

    return textLayer;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      if (requestCode == SELECT_STICKER_REQUEST_CODE) {
        if (data != null) {
          int stickerId = data.getIntExtra(StickerSelectActivity.EXTRA_STICKER_ID, 0);
          if (stickerId != 0) {
            addSticker(stickerId);
          }
        }
      }
    }
  }

  @Override
  public void textChanged(@NonNull String text) {
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

  @Override
  public void onBackPressed() {

    if (AppController.getInstance().isActiveOnACall()) {
      if (AppController.getInstance().isCallMinimized()) {
        super.onBackPressed();
        deleteAllFilterThumbnails();
        supportFinishAfterTransition();
      }
    } else {
      super.onBackPressed();
      deleteAllFilterThumbnails();
      supportFinishAfterTransition();
    }
  }

  public Bitmap getBitmapFromMV(MotionView motionView) {

    int size_x = motionView.getWidth();
    int size_y = motionView.getHeight();
    Bitmap.Config conf = Bitmap.Config.ARGB_8888;

    Bitmap bmp = Bitmap.createBitmap(size_x, size_y, conf);

    Canvas final_cnv = new Canvas(bmp);
    originalImage.draw(final_cnv);
    List<MotionEntity> mentities = motionView.getEntities();
    for (int i = 0; i < mentities.size(); i++) {
      mentities.get(i).draw(final_cnv, new Paint());
    }

    return bmp;
  }

  private void saveImage(MotionView motionView) {

    File imageFile;
    final String SavedImagePath;

    try {

      Bitmap imageBitmap = getBitmapFromMV(motionView);

      final File imageFolder;
      if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        imageFolder = new File(getExternalFilesDir(null)
            + "/"
            + getResources().getString(R.string.app_name)
            + "/Media/");
      } else {

        imageFolder = new File(mActivity.getFilesDir()
            + "/"
            + getResources().getString(R.string.app_name)
            + "/Media/");
      }

      boolean success = true;
      if (!imageFolder.exists() && !imageFolder.isDirectory()) success = imageFolder.mkdirs();

      if (success) {
        SavedImagePath = imageFolder.getAbsolutePath()
            + File.separator
            + String.valueOf(System.nanoTime())
            + "Image.jpg";

        imageFile = new File(SavedImagePath);
      } else {
        Toast.makeText(getBaseContext(), "Image Not saved", Toast.LENGTH_SHORT).show();
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
      ContentValues values = new ContentValues();

      values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
      values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
      values.put(MediaStore.MediaColumns.DATA, imageFile.getAbsolutePath());

      getApplicationContext().getContentResolver()
          .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

      Intent intent = new Intent();
      intent.putExtra("imagePath", SavedImagePath);
      setResult(RESULT_OK, intent);
      finish();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void generatePreview(String image_path) {

    if (image_path != null) {
      temp = new File(image_path);
      dummyIv.setImage(temp);

      folderPathh = new File(getResources().getString(R.string.app_name) + "/Filters").getPath();
      generateThumbnails(folderPathh, image_path);
    }
  }

  private void generateThumbnails(final String folderPath, final String image_path) {
    /*
     * For saving of the preview thumbnail images
     */

    if (count > 16) {

      Filter_item item = new Filter_item();

      item.setFilterId(0);
      item.setSelected(true);

      item.setItemType("0");

      item.setImageUrl(image_path);
      item.setFilterName(getString(R.string.text_filter_normal));

      mFilterData.add(0, item);

      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          mAdapter.notifyDataSetChanged();

          filtersPreviewloaded();
        }
      });

      return;
    }

    switchFilterTo(GenerateThumbnails.createFilterForType(mActivity,
        GenerateThumbnails.FilterType.values()[count + 1]), false);
    dummyIv.saveToPictures(folderPath, "filter_" + count + ".jpg", density_100, density_100,
        new GPUImageView.OnPictureSavedListener() {
          @Override
          public void onPictureSaved(Uri uri) {

            //                if (!excludedIds.contains(count + 1)) {
            Filter_item item = new Filter_item();

            item.setFilterId(count + 1);
            item.setSelected(false);

            item.setItemType("0");

            item.setImageUrl(
                getExternalFilesDir(null) + "/" + folderPath + "/filter_" + (count) + ".jpg");

            item.setFilterName(getString(filterString[count]));

            mFilterData.add(item);

/*
                    if ((count+1)%4 == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();

                            }
                        });
                    }*/

            //                }

            count++;

            generateThumbnails(folderPath, image_path);
          }
        });
  }

  private void filtersPreviewloaded() {

    View blackView = findViewById(R.id.blackView);
    blackView.setVisibility(View.GONE);
    dummyIv.setVisibility(View.GONE);
  }

  private void switchFilterTo(GPUImageFilter mFilter, boolean flag) {

    if (flag) {
      mGPUImageView.setFilter(mFilter);
      mGPUImageView.requestRender();
    } else {

      dummyIv.setFilter(mFilter);
      dummyIv.requestRender();
    }
  }

  private void deleteAllFilterThumbnails() {
    try {
      File f;
      boolean deleted = false;
      for (int i = 0; i < 17; i++) {
        f = new File(getExternalFilesDir(null) + "/" + folderPathh + "/filter_" + i + ".jpg");
        if (f.exists()) {
          deleted = f.delete();
        }
      }
      if (deleted) {
        MediaScannerConnection.scanFile(this, new String[] { getExternalFilesDir(null).toString() },
            null, new MediaScannerConnection.OnScanCompletedListener() {
              public void onScanCompleted(String path, Uri uri) {
              }
            });
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
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
}

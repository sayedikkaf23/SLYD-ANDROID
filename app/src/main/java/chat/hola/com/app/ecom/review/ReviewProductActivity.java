package chat.hola.com.app.ecom.review;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static chat.hola.com.app.Utilities.Constants.CAMERA_ITEM;
import static chat.hola.com.app.Utilities.Constants.CAMERA_PIC;
import static chat.hola.com.app.Utilities.Constants.CROP_IMAGE;
import static chat.hola.com.app.Utilities.Constants.FOUR;
import static chat.hola.com.app.Utilities.Constants.GALLERY_PIC;
import static chat.hola.com.app.Utilities.Constants.IMAGE_DATA;
import static chat.hola.com.app.Utilities.Constants.IMAGE_SELECT;
import static chat.hola.com.app.Utilities.Constants.NOUGHT;
import static chat.hola.com.app.Utilities.Constants.PARENT_FOLDER;
import static chat.hola.com.app.Utilities.Constants.POINT_ZERO;
import static chat.hola.com.app.Utilities.Constants.PRICE;
import static chat.hola.com.app.Utilities.Constants.PRODUCT_COLOUR;
import static chat.hola.com.app.Utilities.Constants.PRODUCT_IMAGE;
import static chat.hola.com.app.Utilities.Constants.PRODUCT_NAME;
import static chat.hola.com.app.Utilities.Constants.PROVIDER;
import static chat.hola.com.app.Utilities.Constants.RATE_PRODUCT;
import static chat.hola.com.app.Utilities.Constants.RATE_SELLER;
import static chat.hola.com.app.Utilities.Constants.REQUEST_CODE_PERMISSION_MULTIPLE;
import static chat.hola.com.app.Utilities.Constants.RETURN_DATA;
import static chat.hola.com.app.Utilities.Constants.REVIEW_TYPE;
import static chat.hola.com.app.Utilities.Constants.SELLER_IMAGE;
import static chat.hola.com.app.Utilities.Constants.SIX;
import static chat.hola.com.app.Utilities.Constants.TRUE;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static com.appscrip.myapplication.utility.Constants.COMING_FROM;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.kotlintestgradle.data.utils.DataConstants.MASTER_ORDER_TYPE;
import static com.kotlintestgradle.remote.RemoteConstants.DRIVER_ID;
import static com.kotlintestgradle.remote.RemoteConstants.ORDER_ID;
import static com.kotlintestgradle.remote.RemoteConstants.ORDER_TYPE;
import static com.kotlintestgradle.remote.RemoteConstants.PARENT_PRODUCT_ID;
import static com.kotlintestgradle.remote.RemoteConstants.PRODUCT_ID;
import static com.kotlintestgradle.remote.RemoteConstants.STORE_CAT;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.Utilities;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ActivityReviewProductBinding;
import com.google.android.material.snackbar.Snackbar;
import com.kotlintestgradle.data.utils.DataConstants;
import com.kotlintestgradle.model.ecom.getratable.RatableAttributesData;
import dagger.android.support.DaggerAppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import javax.inject.Inject;
import org.jetbrains.annotations.NotNull;

/**
 * This activity is used for giving the rating and reviews.
 */
public class ReviewProductActivity extends DaggerAppCompatActivity implements
    ReviewProductCameraClick, RatingOrReviewCallBack {
  @Inject
  ViewModelProvider.Factory mViewModelFactory;
  ArrayList<RatableAttributesData> mRatingAttributesList = new ArrayList<>();
  ArrayList<RatableAttributesData> mSellerRatingAttributesList = new ArrayList<>();
  ArrayList<RatableAttributesData> mDriverRatingAttributesList = new ArrayList<>();
  private ActivityReviewProductBinding mBinding;
  private ReviewProductViewModel mReviewProductViewModel;
  private String mState = Environment.getExternalStorageState();
  private Uri mNewProfileImageUri;
  private File mNewFile;
  private ArrayList<String> mReviewImagesList = new ArrayList<>();
  private ReviewProductsAdapter mReviewProductsAdapter;
  private RatingTypesAdapter mRatingTypesAdapter;
  private RatingTypesAdapter mSellerRatingTypesAdapter;
  private RatingTypesAdapter mdriverRatingTypesAdapter;
  private String mParentProductId;
  private String orderId;
  private String mStoreCatId="";
  private String mDriverId="";
  private  boolean isSuper=false;
  private ArrayList<File> mFiles = new ArrayList<>();
  private double mRating;
  private String mComingFrom="";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initializeView();
    initializeViewModel();
    subscribeToUiActions();
    subscribeAttributesData();
    subscribeSellerRatingData();
    subscribeDriverRatingData();
    subscribeRatingData();
    subscribeShopNowData();
    getIntentData();
    subscribeOnError();
    initializeRatingAdapter();
    initializeSellerRatingAdapter();
    initializeDriverRatingAdapter();
    initializeReviewAdapter();
    if (isSuper){
      mParentProductId="";
      mReviewProductViewModel.callRatingAttributesApi(mParentProductId,orderId,mDriverId,
          DataConstants.STORE_CAT_ID_DEMO);
    }else {
      mReviewProductViewModel.callRatingAttributesApi(mParentProductId,orderId,mDriverId,
          mStoreCatId);
    }

  }

  private void initializeDriverRatingAdapter() {
    mdriverRatingTypesAdapter = new RatingTypesAdapter(mDriverRatingAttributesList, this,
            RATE_SELLER);
    mBinding.rvRateDriver.setHasFixedSize(TRUE);
    mBinding.rvRateDriver.setAdapter(mdriverRatingTypesAdapter);
  }

  private void subscribeDriverRatingData() {
    mReviewProductViewModel.getDriverAttributesList().observe(this,
            ratableAttributesData -> {
      if (ratableAttributesData!=null){
        mBinding.tvIsRider.setText(ratableAttributesData.getDriverName());
        if (ratableAttributesData.getProfilePic()!=null){
          Glide.with(this).load(ratableAttributesData.getProfilePic().replace(" ", "%20"))
              .asBitmap()
              .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
              into(new BitmapImageViewTarget(mBinding.ivRiderImage) {
                @Override
                protected void setResource(Bitmap resource) {
                  RoundedBitmapDrawable circularBitmapDrawable =
                      RoundedBitmapDrawableFactory.create(getResources(), resource);
                  circularBitmapDrawable.setCircular(true);
                  mBinding.ivRiderImage.setImageDrawable(circularBitmapDrawable);
                }
              });
        }


        mDriverRatingAttributesList.clear();
        mDriverRatingAttributesList.addAll(ratableAttributesData.getAttribute());
        mdriverRatingTypesAdapter.notifyDataSetChanged();
        mBinding.tvDriver.setVisibility(mDriverRatingAttributesList.size()>0?View.VISIBLE:View.GONE);
        mBinding.rvRateDriver.setVisibility(mDriverRatingAttributesList.size()>0?View.VISIBLE:View.GONE);
      }else {
        mBinding.tvDriver.setVisibility(View.GONE);
        mBinding.rvRateDriver.setVisibility(View.GONE);
        mBinding.clDriverReview.setVisibility(View.GONE);
        mBinding.tvRateSeller.setVisibility(View.GONE);
        mBinding.rvRateSeller.setVisibility(View.GONE);

      }

            });
  }

  /**
   * Initialising the View using Data Binding
   */
  private void initializeView() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_review_product);
  }

  /**
   * this method is to register for the mError message.
   */
  private void subscribeOnError() {
    mReviewProductViewModel.onError().observe(this, this::showError);
  }

  /**
   * <p>This method is used to show the snackBar message.</p>
   */
  public void showError(String error) {
    Snackbar.make(mBinding.clReviewData, error, Snackbar.LENGTH_SHORT).show();
  }

  /**
   * <p>This method is used initialize the viewModel class for this activity.</p>
   */
  private void initializeViewModel() {
    mReviewProductViewModel = ViewModelProviders.of(this, mViewModelFactory).get(
        ReviewProductViewModel.class);
    mBinding.setViewModel(mReviewProductViewModel);
    mReviewProductViewModel.getDeviceDetails(Utilities.getIpAddress(this));
  }

  /**
   * get the intent mData
   */
  private void getIntentData() {
    Intent bundle = getIntent();
    if (bundle != null) {
      String productName = bundle.getStringExtra(PRODUCT_NAME);
      String productPrice = bundle.getStringExtra(PRICE);
      String productImage = bundle.getStringExtra(PRODUCT_IMAGE);
      String productColor = bundle.getStringExtra(PRODUCT_COLOUR);
      String sellerImage = bundle.getStringExtra(SELLER_IMAGE);
      mParentProductId = bundle.getStringExtra(PARENT_PRODUCT_ID);
      mReviewProductViewModel.productName.set(productName);
      mReviewProductViewModel.productImage.set(productImage);
      mReviewProductViewModel.productPrice.set(productPrice);
      if (getIntent().hasExtra(COMING_FROM)){
        mComingFrom= getIntent().getStringExtra(COMING_FROM);
      }
      if (mComingFrom!=null && mComingFrom.equals(MASTER_ORDER_TYPE)){
        isSuper= true;
        mBinding.tvReviewProTitle.setText(getResources().getString(R.string.rateservice));
        mBinding.tvReviewProSubmit.setBackground(getResources().getDrawable(R.drawable.addons_yellow_box));
        mBinding.tvReviewProSubmit.setTextColor(getResources().getColor(R.color.allAddressPurple));
      }else if (mComingFrom!=null && mComingFrom.equals(ORDER_TYPE)){
        isSuper= true;
      }else {
        mReviewProductViewModel.productName.set(productName);
        mReviewProductViewModel.productImage.set(productImage);
        mReviewProductViewModel.productPrice.set(productPrice);
      }
      mBinding.clProductRating.setVisibility(isSuper?View.GONE:View.VISIBLE);
      mBinding.clEcomRating.setVisibility(isSuper?View.GONE:View.VISIBLE);
      mBinding.clDriver.setVisibility(isSuper?View.VISIBLE:View.GONE);
      mBinding.clEcomHeader.setVisibility(isSuper?View.GONE:View.VISIBLE);
      mBinding.tvReviewProSubmitDine.setVisibility(isSuper?View.VISIBLE:View.GONE);
      mBinding.tvReviewProSubmit.setVisibility(isSuper?View.GONE:View.VISIBLE);
      mBinding.clSellerReview.setVisibility(isSuper?View.VISIBLE:View.GONE);
      mBinding.tvReviewSeller.setVisibility(isSuper?View.GONE:View.GONE);
      mBinding.tvAboutSeller.setVisibility(isSuper?View.GONE:View.GONE);
      mBinding.etReviewAboutSeller.setVisibility(isSuper?View.GONE:View.GONE);

      mBinding.tvReviewProSubmitDine.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mReviewProductViewModel.callRatingsOrReviewsApi(REVIEW_TYPE, 0, mParentProductId, "",
              mReviewProductViewModel.mReviewTitle, mReviewProductViewModel.mReviewDesc,
              POINT_ZERO,
              mReviewImagesList,mReviewProductViewModel.sellerId,mReviewProductViewModel.orderId,
                  mReviewProductViewModel.driverId,mBinding.etReviewDriver.getText()==null?"" :mBinding.etReviewDriver.getText().toString() );
        }
      });
      if (productColor != null && !productColor.isEmpty()) {
        mReviewProductViewModel.productColor.set(productColor);
      } else {
        mBinding.tvReviewProColor.setVisibility(View.GONE);
      }
      if (getIntent().hasExtra(ORDER_ID)){
        orderId= getIntent().getStringExtra(ORDER_ID);
        mReviewProductViewModel.orderId=orderId;
      }
      if (getIntent().hasExtra(STORE_CAT)){
        mStoreCatId= getIntent().getStringExtra(STORE_CAT);
      }
      if (getIntent().hasExtra(STORE_CAT)){
        mStoreCatId= getIntent().getStringExtra(STORE_CAT);
      }

      if (getIntent().hasExtra(DRIVER_ID)){
        mDriverId= getIntent().getStringExtra(DRIVER_ID);
        mReviewProductViewModel.driverId= mDriverId;
      }
      if (sellerImage!=null && sellerImage.isEmpty()){
        Glide.with(this).load(sellerImage).into(mBinding.ivRiderImage);
      }
    }
  }

  /**
   * Listen this method when ui clicked
   */
  private void subscribeToUiActions() {
    mReviewProductViewModel.onClick().observe(this, reviewProductUiAction -> {
      switch (reviewProductUiAction) {
        case FINISH_ACT:
          clearOrCreateDir();
          Intent intent = new Intent();
          intent.putExtra(Constants.RATING, mRating);
          intent.putExtra(PRODUCT_ID,mParentProductId);
          setResult(Activity.RESULT_OK, intent);
          finish();
          break;
        case SUBMIT:
            if (mReviewImagesList != null && mReviewImagesList.contains(CAMERA_ITEM)) {
              mReviewImagesList.remove(CAMERA_ITEM);
              Utilities.printLog("mReviewImagesList" + mReviewImagesList.size());
            }
            mReviewProductViewModel.reviewImgFiles(mReviewImagesList, mFiles, mParentProductId);
          break;
        default:
          break;
      }
    });
  }

  /**
   * initialize Values for rating
   */
  private void initializeRatingAdapter() {
    mRatingTypesAdapter = new RatingTypesAdapter(mRatingAttributesList, this, RATE_PRODUCT);
    mBinding.rvReviewProRatings.setHasFixedSize(TRUE);
    mBinding.rvReviewProRatings.setAdapter(mRatingTypesAdapter);
  }

  /**
   * initialize Values for rating
   */
  private void initializeSellerRatingAdapter() {
    mSellerRatingTypesAdapter = new RatingTypesAdapter(mSellerRatingAttributesList, this,
        RATE_SELLER);
    mBinding.rvRateSeller.setHasFixedSize(TRUE);
    mBinding.rvRateSeller.setAdapter(mSellerRatingTypesAdapter);
  }

  /**
   * initialize Values for reviews
   */
  private void initializeReviewAdapter() {
    mReviewImagesList.clear();
    mReviewImagesList.add(CAMERA_ITEM);
    mReviewProductsAdapter = new ReviewProductsAdapter(mReviewImagesList, this);
    mBinding.rvReviewProImages.setHasFixedSize(TRUE);
    mBinding.rvReviewProImages.setAdapter(mReviewProductsAdapter);
  }

  /**
   * subscribes for rating attribute mData
   */
  private void subscribeAttributesData() {
    if (!isSuper){
      mReviewProductViewModel.getAttributesList().observe(this,
          ratableAttributesData -> {
            mRatingAttributesList.clear();
            RatableAttributesData overallAttributesData = new RatableAttributesData(
                getResources().getString(R.string.reviewProductOverall), "", ZERO);
            mRatingAttributesList.add(ZERO, overallAttributesData);
            mRatingAttributesList.addAll(ratableAttributesData);
            mRatingTypesAdapter.notifyDataSetChanged();
          });
    }
  }

  /**
   * subscribes for rating attribute mData
   */
  private void subscribeSellerRatingData() {
    mReviewProductViewModel.getSellerAttributesList().observe(this,
        ratableAttributesData -> {
         //mBinding.tvIsRider.setText(mReviewProductViewModel.sellerName);
          mSellerRatingAttributesList.clear();
          mSellerRatingAttributesList.addAll(ratableAttributesData);
          mSellerRatingTypesAdapter.notifyDataSetChanged();
          mBinding.tvRateSeller.setVisibility(mSellerRatingAttributesList.size()>0?View.VISIBLE:View.GONE);
          mBinding.rvRateSeller.setVisibility(mSellerRatingAttributesList.size()>0?View.VISIBLE:View.GONE);
        });
  }

  /**
   * subscribe for rating mData
   */
  private void subscribeRatingData() {
    if (!isSuper){
      mReviewProductViewModel.getUserReviewData().observe(this, userReviewData -> {
        Utilities.printLog("userReviewData" + userReviewData);
        if (userReviewData != null) {
          if (userReviewData.getImage() != null) {
            mReviewImagesList.clear();
            if (userReviewData.getImage().size() <= FOUR) {
              mReviewImagesList.addAll(userReviewData.getImage());
              mReviewImagesList.add(CAMERA_ITEM);
            } else {
              mReviewImagesList.addAll(userReviewData.getImage());
            }
            mReviewProductsAdapter.notifyDataSetChanged();
          }
          if (userReviewData.getReviewDescription() != null) {
            mBinding.etReviewProDesc.setText(userReviewData.getReviewDescription());
          }
          if (userReviewData.getReviewTitle() != null) {
            mBinding.etReviewProTitle.setText(userReviewData.getReviewTitle());
          }
          if (userReviewData.getRating() != null) {
            if (mRatingAttributesList.size() > ZERO) {
              RatableAttributesData ratableAttributesData = mRatingAttributesList.get(ZERO);
              ratableAttributesData.setAttributeRating(
                  Double.parseDouble(userReviewData.getRating()));
              mReviewProductViewModel.attributeId = TRUE;
            }
            mReviewProductViewModel.btnEnabled.set(mReviewProductViewModel.attributeId
                && !Objects.requireNonNull(mBinding.etReviewProTitle.getText()).toString().isEmpty()
                && !Objects.requireNonNull(
                mBinding.etReviewProDesc.getText()).toString().isEmpty());
          }
        }
      });

    }
  }

  /**
   * subscribe for shop now mData
   */
  private void subscribeShopNowData() {
    mReviewProductViewModel.onShopNow().observe(this, shopNow -> {
      if (shopNow) {
        mBinding.includeReviewProPurchase.clShopNow.setVisibility(View.VISIBLE);
      }
    });
  }

  /**
   * check permission camera,read external storage
   */
  private void checkPerMission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ((this.checkSelfPermission(CAMERA)
        != PackageManager.PERMISSION_GRANTED) || (
        checkSelfPermission(READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        || (checkSelfPermission(WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))) {
      requestPermissions(new String[]{CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
          REQUEST_CODE_PERMISSION_MULTIPLE);
    } else {
      selectImagePopUp();
    }
  }

  /**
   * select the image source
   */
  private void selectImagePopUp() {
    final CharSequence[] options = {getString(R.string.allTakePhoto), getString(
        R.string.allChooseFromGallery),
        getString(R.string.allCancel)};
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(getString(R.string.allAddPhoto));
    builder.setItems(options, (dialog, item) -> {
      if (options[item].equals(getString(R.string.allTakePhoto))) {
        takePicFromCamera();
      } else if (options[item].equals(getString(R.string.allChooseFromGallery))) {
        try {
          Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
          photoPickerIntent.setType(IMAGE_SELECT);
          startActivityForResult(photoPickerIntent, GALLERY_PIC);
        } catch (ActivityNotFoundException e) {
          e.printStackTrace();
        }
      } else if (options[item].equals(getString(R.string.allCancel))) {
        dialog.dismiss();
      }
    });
    builder.show();
  }

  /**
   * clear all caches
   */
  private void clearOrCreateDir() {
    try {
      mState = Environment.getExternalStorageState();
      File cropImagesDir;
      File[] cropImagesDirectory;
      if (Environment.MEDIA_MOUNTED.equals(mState)) {
        cropImagesDir = new File(
            String.format("%s/%s%s", Environment.getExternalStorageDirectory(), PARENT_FOLDER,
                IMAGE_DATA));
      } else {
        cropImagesDir = new File(
            String.format("%s/%s%s", this.getFilesDir(), PARENT_FOLDER, IMAGE_DATA));
      }
      if (!cropImagesDir.isDirectory()) {
        cropImagesDir.mkdirs();
      } else {
        cropImagesDirectory = cropImagesDir.listFiles();
        if (cropImagesDirectory.length > ZERO) {
          for (File imagesDirectory : cropImagesDirectory) {
            imagesDirectory.delete();
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * sued to open the camera
   */
  private void takePicFromCamera() {
    setPicPath();
    try {
      Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      intent.putExtra(MediaStore.EXTRA_OUTPUT, mNewProfileImageUri);
      intent.putExtra(RETURN_DATA, TRUE);
      startActivityForResult(intent, CAMERA_PIC);
    } catch (ActivityNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * create the folder for the image which we have selected.
   */
  private void setPicPath() {
    try {
      String takenNewImage = "";
      mState = Environment.getExternalStorageState();
      takenNewImage = String.format("%s%d.png", PARENT_FOLDER, System.nanoTime());
      File newFile1;
      if (Environment.MEDIA_MOUNTED.equals(mState)) {
        newFile1 = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                takenNewImage);
        mNewFile = newFile1;
      } else {
        newFile1 = new File(this.getFilesDir(), takenNewImage);
        mNewFile = newFile1;
      }
      Uri newProfileImageUri1;
      if (Build.VERSION.SDK_INT >= NOUGHT) {
        newProfileImageUri1 = FileProvider.getUriForFile(this,
                String.format("%s.provider", this.getApplicationContext().getPackageName()),
                mNewFile);
      } else {
        newProfileImageUri1 = Uri.fromFile(mNewFile);
      }
      mNewProfileImageUri = newProfileImageUri1;
      Utilities.printLog("RegistrationAct FilePAth in takePicFromCamera()  new: "
              + mNewFile.getPath() + " new profileUri = " + mNewProfileImageUri);
    } catch (ActivityNotFoundException e) {
      Utilities.printLog("RegistrationAct cannot take picture: " + e);
    }
  }

  /**
   * open the crop image activity
   */
  private void startCropImage() {
    /*try {
      Intent intent = new Intent(this, CropImage.class);
      intent.putExtra(CropImage.IMAGE_PATH, mNewFile.getPath());
      intent.putExtra(CropImage.SCALE, TRUE);
      intent.putExtra(CropImage.ASPECT_X, FOUR);
      intent.putExtra(CropImage.ASPECT_Y, FOUR);
      startActivityForResult(intent, CROP_IMAGE);
    } catch (Exception e) {
      e.printStackTrace();
      Toast.makeText(this, "Unable to load this image", Toast.LENGTH_LONG).show();
    }*/
  }

  @Override
  public void onCameraClick() {
    checkPerMission();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Utilities.printLog("gotresult" + requestCode);
    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
        case CAMERA_PIC:
          startCropImage();
          break;
        case GALLERY_PIC:
          setPicPath();
          InputStream inputStream;
          try {
            inputStream = this.getContentResolver().openInputStream(
                Objects.requireNonNull(data.getData()));
            FileOutputStream fileOutputStream = new FileOutputStream(mNewFile);
            assert inputStream != null;
            Utilities.copyStream(inputStream, fileOutputStream);
            fileOutputStream.close();
            inputStream.close();
            mNewProfileImageUri = FileProvider.getUriForFile(
                this,
                String.format("%s%s", getPackageName(), PROVIDER), mNewFile);
            startCropImage();
          } catch (IOException | NullPointerException e) {
            e.printStackTrace();
          }
          break;
        case CROP_IMAGE:
          if (data != null) {
            mFiles.add(mNewFile);
            mReviewImagesList.add(mNewFile.toString());
            mReviewProductsAdapter.updateList(mReviewImagesList);
            if (mReviewImagesList.size() == SIX) {
              mReviewImagesList.remove(mReviewImagesList.size() - ONE);
              mReviewProductsAdapter.notifyItemChanged(mReviewImagesList.size() - ONE);
            }
          }
          break;
        default:
          break;
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
      @NotNull String[] permissions, @NotNull int[] grantResults) {
    switch (requestCode) {
      case REQUEST_CODE_PERMISSION_MULTIPLE: {
        checkPerMission();
      }
    }
  }

  @Override
  public void ratingOrReview(int ratingType, int reviewType, String attributeId, double rating) {
    if (attributeId.isEmpty()) {
      mReviewProductViewModel.attributeId = TRUE;
      mRating = rating;
      Utilities.printLog("exe" + "mRating" + mRating);
    }
    if (mReviewProductViewModel.attributeId
        && !Objects.requireNonNull(mBinding.etReviewProDesc.getText()).toString().isEmpty()
        && !Objects.requireNonNull(mBinding.etReviewProTitle.getText()).toString().isEmpty()) {
      mBinding.tvReviewProSubmit.setEnabled(TRUE);
    }
    mReviewProductViewModel.driverReview = mBinding.etReviewDriver.getText()== null? "":mBinding.etReviewDriver.getText().toString();
    mReviewProductViewModel.callRatingsOrReviewsApi(ratingType, reviewType, mParentProductId,
        attributeId, "",
        "",
        rating,
        null,mReviewProductViewModel.sellerId,mReviewProductViewModel.orderId,mReviewProductViewModel.driverId,mReviewProductViewModel.driverReview);
  }
}
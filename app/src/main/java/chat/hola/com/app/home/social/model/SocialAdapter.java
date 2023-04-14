package chat.hola.com.app.home.social.model;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.OnSwipeTouchListener;
import chat.hola.com.app.Utilities.TagSpannable;
import chat.hola.com.app.Utilities.TextSpannable;
import chat.hola.com.app.Utilities.TimeAgo;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.comment.model.Comment;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.models.Business;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h1>SocialAdapter</h1>
 *
 * @author 3Embed
 * @since 24/2/2018.
 */

public class SocialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR =
      new DecelerateInterpolator();
  private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR =
      new AccelerateInterpolator();
  private String POST_TYPE_REGULAR = "Regular";

  private TypefaceManager typefaceManager;
  private Context mContext;
  private List<Data> dataList;
  private ClickListner clickListner;
  private ConstraintSet set = new ConstraintSet();

  public SocialAdapter(Context mContext, List<Data> data, TypefaceManager typefaceManager) {
    this.dataList = data;
    this.mContext = mContext;
    this.typefaceManager = typefaceManager;
  }

  public void setData(List<Data> data) {
    this.dataList = data;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_social_row, parent, false);
    return new ViewHolder(itemView, typefaceManager, clickListner);
  }

  @Override
  public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
    try {
      ViewHolder viewHolder = (ViewHolder) holder;
      initHolder(viewHolder);
    } catch (Exception ignored) {

    }
  }

  @Override
  public int getItemCount() {
    return dataList.size();
  }

  public void setListener(ClickListner clickListner) {
    this.clickListner = clickListner;
  }

  private void findMatch(SpannableString spanString, Matcher matcher) {
    while (matcher.find()) {
      final String tag = matcher.group(0);
      spanString.setSpan(new TagSpannable(mContext, tag), matcher.start(), matcher.end(),
          Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
  }

  @SuppressLint({ "SetTextI18n", "ClickableViewAccessibility" })
  @SuppressWarnings("TryWithIdenticalCatches")
  private void initHolder(ViewHolder holder) {
    try {
      if (getItemCount() > 0) {
        int position = holder.getAdapterPosition();
        final Data data = dataList.get(position);

        boolean isChannel = data.getChannelId() != null && !data.getChannelId().isEmpty();

        optionMenu(holder, data);

        commentView(holder, data.getComments(), data);

        /* BUSINESS POST START*/
        Business business = data.getBusiness();
        boolean isBusiness =
            business != null && !business.getBusinessPostType().equalsIgnoreCase("regular");
        holder.rlAction.setVisibility(
            isBusiness && !business.getBusinessPostTypeLabel().equalsIgnoreCase(POST_TYPE_REGULAR)
                ? View.VISIBLE : View.GONE);
        if (isBusiness) {
          if (business.getBusinessButtonColor() != null && !business.getBusinessButtonColor()
              .isEmpty()) {
            holder.rlAction.setBackgroundColor(Color.parseColor(business.getBusinessButtonColor()));
          }
          holder.tvActionButton.setText(business.getBusinessButtonText());
          if (business.getBusinessPrice() != null) {
            String currencySymbol = business.getBusinessCurrency();
            holder.tvPrice.setText(currencySymbol + " " + business.getBusinessPrice());
          }
          holder.rlAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              clickListner.onActionButtonClick(business.getBusinessButtonText(),
                  business.getBusinessUrl());
            }
          });

          holder.profileNameTv.setText(business.getBusinessName());
          Glide.with(mContext)
              .load(business.getBusinessProfilePic())
              .asBitmap()
              .centerCrop()
              .signature(new StringSignature(
                  AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
              //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
              //.diskCacheStrategy(DiskCacheStrategy.NONE)
              //    .skipMemoryCache(true)
              .placeholder(R.mipmap.ic_launcher)
              .into(new BitmapImageViewTarget(holder.ivProfilePic) {
                @Override
                protected void setResource(Bitmap resource) {
                  RoundedBitmapDrawable circularBitmapDrawable =
                      RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                  circularBitmapDrawable.setCircular(true);
                  holder.ivProfilePic.setImageDrawable(circularBitmapDrawable);
                }
              });
        } else {
          holder.profileNameTv.setText(isChannel ? data.getChannelName() : data.getUsername());
          if (!data.getProfilepic().isEmpty()) {
            String pic = isChannel ? data.getChannelImageUrl() : data.getProfilepic();
            Glide.with(mContext)
                .load(pic)
                .asBitmap()
                .centerCrop()
                .signature(new StringSignature(
                    AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //    .skipMemoryCache(true)
                .placeholder(R.mipmap.ic_launcher)
                .into(new BitmapImageViewTarget(holder.ivProfilePic) {
                  @Override
                  protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.ivProfilePic.setImageDrawable(circularBitmapDrawable);
                  }
                });
          }
        }
        /* BUSINESS POST END*/

        holder.ivStarBadge.setVisibility(data.isStar() ? View.VISIBLE : View.GONE);
        if (!data.getTitle().isEmpty()) {
          SpannableString spanString = new SpannableString(data.getTitle());
          Matcher matcher = Pattern.compile("#([A-Za-z0-9_-]+)").matcher(spanString);
          findMatch(spanString, matcher);
          Matcher userMatcher = Pattern.compile("@([A-Za-z0-9_-]+)").matcher(spanString);
          findMatch(spanString, userMatcher);
          holder.rlTitle.setVisibility(View.VISIBLE);
          holder.tvTitle.setText(spanString);
          holder.tvTitle.setMovementMethod(LinkMovementMethod.getInstance());

          //                    final boolean[] isExpanded = {false};
          //                    holder.tvViewMore.setVisibility(data.getTitle().length() > 10 ? View.VISIBLE : View.GONE);
          //                    holder.tvViewMore.setOnClickListener(new View.OnClickListener() {
          //                        @Override
          //                        public void onClick(View v) {
          //                            holder.tvTitle.setMaxLines(10);
          //                            isExpanded[0] = !isExpanded[0];
          //                        }
          //                    });
        } else {
          holder.rlTitle.setVisibility(View.GONE);
        }

        if (data.getPlace() != null && !data.getPlace().trim().equals("")) {
          holder.tvLocation.setVisibility(View.VISIBLE);
          holder.tvLocation.setText(data.getPlace());
        } else {
          holder.tvLocation.setVisibility(View.GONE);
        }

        holder.postTimeTv.setText(TimeAgo.getTimeAgo(Long.valueOf(data.getTimeStamp())));
        holder.tvLikeCount.setText(
            data.getLikesCount() + mContext.getResources().getString(R.string.people1));
        holder.tvViewCount.setText(data.getDistinctViews());
        holder.ibLike.setImageDrawable(
            mContext.getDrawable(data.isLiked() ? R.drawable.ic_liked : R.drawable.ic_like_black));

        holder.ibComment.setOnClickListener(v -> clickListner.viewAllComments(data.getPostId()));
        holder.ibLike.setOnClickListener(v -> {
          clickListner.onLikeClicked(position, !data.isLiked());
        });

        holder.tvLikeCount.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            clickListner.onLikerClick(data);
          }
        });

        holder.tvViewCount.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            clickListner.onViewClick(data);
          }
        });

        holder.tvView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            clickListner.onViewClick(data);
          }
        });

        holder.ivMedia.setOnTouchListener(new OnSwipeTouchListener(mContext) {

          @Override
          public void onSingleTap() {
            super.onSingleTap();
            clickListner.onItemClick(position, holder.ivMedia);
          }

          @Override
          public void onDoubleTaps() {
            super.onDoubleTaps();
            animatePhotoLike(holder);
          }
        });
        if (!TextUtils.isEmpty(data.getImageUrl1())) {
          holder.ivVideoCam.setVisibility(data.getMediaType1() == 0 ? View.GONE : View.VISIBLE);
          if (data.getMediaType1() == 1) {
            //video

            try {
              GlideDrawableImageViewTarget imageViewTarget =
                  new GlideDrawableImageViewTarget(holder.ivMedia);
              DrawableRequestBuilder<String> thumbnailRequest = Glide.with(mContext)
                  .load(
                      Utilities.getModifiedThumbnailLink(data.getThumbnailUrl1()))
                  .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                  .fitCenter()
                  .override((int) Math.round(Double.parseDouble(data.getImageUrl1Width())),
                      (int) Math.round(Double.parseDouble(data.getImageUrl1Height())));

              Glide.with(mContext)
                  .load(Utilities.getModifiedImageLink(
                      data.getImageUrl1()))//.replace("upload/", "upload/vs_20,e_loop/")    .replace("mp4", "gif").replace(".mov", ".gif"))
                  .thumbnail(thumbnailRequest)
                  .dontAnimate()
                  .fitCenter()
                  .placeholder(ContextCompat.getDrawable(mContext, R.drawable.ic_default))
                  //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                  .into(imageViewTarget);
            } catch (IllegalArgumentException e) {
              e.printStackTrace();
            } catch (NullPointerException e) {
              e.printStackTrace();
            }
          } else {
            //image

            try {
              DrawableRequestBuilder<String> thumbnailRequest = Glide.with(mContext)
                  .load(Utilities.getModifiedThumbnailLink(
                      data.getThumbnailUrl1()))
                  .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                  .fitCenter()
                  .override(Integer.parseInt(
                      String.valueOf(Math.round(Float.parseFloat(data.getImageUrl1Width())))),
                      Integer.parseInt(
                          String.valueOf(Math.round(Float.parseFloat(data.getImageUrl1Height())))));

              //                            Glide.with(mContext).load(data.getImageUrl1().replace("upload", "upload/t_media_lib_thumb"))
              //                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
              //                                    .dontAnimate()
              //                                    .placeholder(mContext.getResources().getDrawable(R.drawable.ic_default))
              //                                    .into(holder.ivMedia);

              Glide.with(mContext)
                  .load(Utilities.getModifiedImageLink(data.getImageUrl1()))
                  .dontAnimate()
                  .fitCenter()
                  .thumbnail(thumbnailRequest)
                  .placeholder(ContextCompat.getDrawable(mContext, R.drawable.ic_default))
                  //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                  .into(holder.ivMedia);
            } catch (IllegalArgumentException e) {
              e.printStackTrace();
            } catch (NullPointerException e) {
              e.printStackTrace();
            }
          }
        }
        holder.ibSaved.setImageDrawable(mContext.getDrawable(
            data.getBookMarked() ? R.drawable.ic_saved : R.drawable.ic_unsaved_black));

        holder.ibSaved.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (data.getBookMarked()) {
              holder.ibSaved.setImageDrawable(mContext.getDrawable(R.drawable.ic_unsaved_black));
              holder.rL_save.setVisibility(View.GONE);
            } else {
              holder.ibSaved.setImageDrawable(mContext.getDrawable(R.drawable.ic_saved));
              holder.rL_save.setVisibility(View.VISIBLE);
              new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                  holder.rL_save.setVisibility(View.GONE);
                }
              }, 4000);
            }
            clickListner.savedClick(position, data.getBookMarked());
          }
        });

        holder.ibSaved.setOnLongClickListener(new View.OnLongClickListener() {
          @Override
          public boolean onLongClick(View v) {
            if (data.getBookMarked()) {
              holder.rL_save.setVisibility(View.VISIBLE);
              new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                  holder.rL_save.setVisibility(View.GONE);
                }
              }, 4000);
              clickListner.savedLongCick(position, data.getBookMarked());
              return true;
            } else {
              return false;
            }
          }
        });

        holder.tV_savedView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            holder.rL_save.setVisibility(View.GONE);
            clickListner.savedViewClick(position, data);
          }
        });

        holder.tV_saveTo.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            clickListner.onSaveToCollectionClick(position, data);
          }
        });
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void optionMenu(ViewHolder holder, Data data) {

    holder.tvOptions.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //creating a popup menu
        PopupMenu popup = new PopupMenu(mContext, holder.tvOptions);
        //inflating menu from xml resource
        popup.inflate(
            AppController.getInstance().getUserId().equals(data.getUserId()) ? R.menu.my_option
                : R.menu.others_option);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
              case R.id.edit:
                clickListner.onEdit(data);
                return true;
              case R.id.delete:
                clickListner.onDelete(data);
                return true;
              case R.id.report:
                clickListner.onReport(data);
                return true;
              default:
                return false;
            }
          }
        });
        //displaying the popup
        popup.show();
      }
    });
  }

  private void animatePhotoLike(ViewHolder holder) {
    holder.vBgLike.setVisibility(View.VISIBLE);
    holder.ivLikeIt.setVisibility(View.VISIBLE);

    holder.vBgLike.setScaleY(0.1f);
    holder.vBgLike.setScaleX(0.1f);
    holder.vBgLike.setAlpha(1f);
    holder.ivLikeIt.setScaleY(0.1f);
    holder.ivLikeIt.setScaleX(0.1f);

    AnimatorSet animatorSet = new AnimatorSet();

    ObjectAnimator bgScaleYAnim = ObjectAnimator.ofFloat(holder.vBgLike, "scaleY", 0.1f, 1f);
    bgScaleYAnim.setDuration(200);
    bgScaleYAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
    ObjectAnimator bgScaleXAnim = ObjectAnimator.ofFloat(holder.vBgLike, "scaleX", 0.1f, 1f);
    bgScaleXAnim.setDuration(200);
    bgScaleXAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
    ObjectAnimator bgAlphaAnim = ObjectAnimator.ofFloat(holder.vBgLike, "alpha", 1f, 0f);
    bgAlphaAnim.setDuration(200);
    bgAlphaAnim.setStartDelay(150);
    bgAlphaAnim.setInterpolator(DECCELERATE_INTERPOLATOR);

    ObjectAnimator imgScaleUpYAnim = ObjectAnimator.ofFloat(holder.ivLikeIt, "scaleY", 0.1f, 1f);
    imgScaleUpYAnim.setDuration(300);
    imgScaleUpYAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
    ObjectAnimator imgScaleUpXAnim = ObjectAnimator.ofFloat(holder.ivLikeIt, "scaleX", 0.1f, 1f);
    imgScaleUpXAnim.setDuration(300);
    imgScaleUpXAnim.setInterpolator(DECCELERATE_INTERPOLATOR);

    ObjectAnimator imgScaleDownYAnim = ObjectAnimator.ofFloat(holder.ivLikeIt, "scaleY", 1f, 0f);
    imgScaleDownYAnim.setDuration(300);
    imgScaleDownYAnim.setInterpolator(ACCELERATE_INTERPOLATOR);
    ObjectAnimator imgScaleDownXAnim = ObjectAnimator.ofFloat(holder.ivLikeIt, "scaleX", 1f, 0f);
    imgScaleDownXAnim.setDuration(300);
    imgScaleDownXAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

    animatorSet.playTogether(bgScaleYAnim, bgScaleXAnim, bgAlphaAnim, imgScaleUpYAnim,
        imgScaleUpXAnim);
    animatorSet.play(imgScaleDownYAnim).with(imgScaleDownXAnim).after(imgScaleUpYAnim);

    animatorSet.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        resetLikeAnimationState(holder);
      }
    });
    animatorSet.start();
    if (!dataList.get(holder.getAdapterPosition()).isLiked()) {
      clickListner.onLikeClicked(holder.getAdapterPosition(), true);
    }
  }

  private void resetLikeAnimationState(ViewHolder holder) {
    holder.vBgLike.setVisibility(View.INVISIBLE);
    holder.ivLikeIt.setVisibility(View.INVISIBLE);
  }

  private void commentView(ViewHolder holder, List<Comment> comments, Data data) {
    holder.llComments.removeAllViews();
    if (!comments.isEmpty()) {
      holder.postTimeTv.setPadding(0, 0, 0, 0);
      holder.llComments.setVisibility(View.VISIBLE);

      for (int i = 0; i < comments.size(); i++) {
        if (i < 2) {
          Comment comment = comments.get(i);

          LinearLayout linearLayout = new LinearLayout(mContext);
          linearLayout.setOrientation(LinearLayout.HORIZONTAL);

          TextView cmnt = new TextView(mContext);
          cmnt.setTypeface(typefaceManager.getRegularFont());
          cmnt.setTextColor(mContext.getResources().getColor(R.color.star_grey));
          cmnt.setPadding(3, 3, 3, 3);

          SpannableString spanString =
              new SpannableString(comment.getCommentedBy() + " " + comment.getComment());
          Matcher matcher = Pattern.compile("#([A-Za-z0-9_-]+)").matcher(spanString);
          findMatch(spanString, matcher);
          Matcher userMatcher = Pattern.compile("@([A-Za-z0-9_-]+)").matcher(spanString);
          findMatch(spanString, userMatcher);

          ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
              clickListner.onUserClick(comment.getCommentedByUserId());
            }

            @Override
            public void updateDrawState(TextPaint ds) {
              super.updateDrawState(ds);
              ds.setUnderlineText(false);
            }
          };
          spanString.setSpan(clickableSpan, 0, comment.getCommentedBy().length() + 1,
              Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

          cmnt.setText(spanString);
          cmnt.setMovementMethod(LinkMovementMethod.getInstance());
          cmnt.setHighlightColor(Color.BLACK);

          linearLayout.addView(cmnt);

          holder.llComments.addView(linearLayout);
        } else {
          break;
        }
      }

      if (comments.size() > 2) {
        TextView textView = new TextView(mContext);
        textView.setText(R.string.ViewAll+ data.getCommentCount() + R.string.comments);
        textView.setPadding(3, 6, 3, 5);
        textView.setTextColor(mContext.getResources().getColor(R.color.star_grey));
        textView.setOnClickListener(v -> clickListner.viewAllComments(data.getPostId()));

        holder.llComments.addView(textView);
      }
    } else {
      holder.llComments.setVisibility(View.GONE);
      holder.postTimeTv.setPadding(0, 5, 0, 0);
    }
  }

  public static void makeTextViewResizable(final TextView tv, final int maxLine,
      final String expandText, final boolean viewMore) {

    if (tv.getTag() == null) {
      tv.setTag(tv.getText());
    }
    ViewTreeObserver vto = tv.getViewTreeObserver();
    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

      @SuppressWarnings("deprecation")
      @Override
      public void onGlobalLayout() {
        String text;
        int lineEndIndex;
        ViewTreeObserver obs = tv.getViewTreeObserver();
        obs.removeGlobalOnLayoutListener(this);
        if (maxLine == 0) {
          lineEndIndex = tv.getLayout().getLineEnd(0);
          text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1)
              + " "
              + expandText;
        } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
          lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
          text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1)
              + " "
              + expandText;
        } else {
          lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
          text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
        }
        tv.setText(text);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setText(addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv,
            lineEndIndex, expandText, viewMore), TextView.BufferType.SPANNABLE);
      }
    });
  }

  private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned,
      final TextView tv, final int maxLine, final String spanableText, final boolean viewMore) {
    String str = strSpanned.toString();
    SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

    if (str.contains(spanableText)) {
      ssb.setSpan(new TextSpannable(false) {
        @Override
        public void onClick(View widget) {
          tv.setLayoutParams(tv.getLayoutParams());
          tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
          tv.invalidate();
          if (viewMore) {
            makeTextViewResizable(tv, -1, "View Less", false);
          } else {
            makeTextViewResizable(tv, 3, "View More", true);
          }
        }
      }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);
    }
    return ssb;
  }
}
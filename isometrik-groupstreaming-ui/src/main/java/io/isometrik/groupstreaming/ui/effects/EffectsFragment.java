package io.isometrik.groupstreaming.ui.effects;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.R2;
import io.isometrik.groupstreaming.ui.utils.RecyclerItemClickListener;
import io.isometrik.gs.rtcengine.ar.AREffect;
import io.isometrik.gs.rtcengine.ar.AROperations;
import io.isometrik.gs.rtcengine.ar.DownloadZipResult;
import io.isometrik.gs.rtcengine.ar.FiltersConfig;
import io.isometrik.gs.rtcengine.ar.ZipUtils;
import io.isometrik.gs.rtcengine.voice.VoiceEffect;
import io.isometrik.gs.rtcengine.voice.VoiceOperations;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * Effects bottomsheet dialog fragment to allow publisher to apply voice{@link VoiceEffect} or AR
 * filters{@link AREffect}.
 *
 * @see AREffect
 * @see VoiceEffect
 */
public class EffectsFragment extends BottomSheetDialogFragment implements DownloadZipResult {

  public static final String TAG = "EffectsFragment";
  private Activity activity;
  private View view;

  @BindView(R2.id.rvFilters)
  RecyclerView arFiltersList;
  @BindView(R2.id.rvVoiceFilters)
  RecyclerView voiceFiltersList;

  @BindView(R2.id.tabLayoutFilters)
  TabLayout tabLayoutFilters;
  @BindView(R2.id.tvClearArFilters)
  TextView tvClearArFilters;

  @BindView(R2.id.rlDownload)
  RelativeLayout rlDownloadFilters;
  private ArrayList<AREffect> arFilterItems;
  private ArFilterAdapter arFilterAdapter;
  private AROperations arOperations;

  private ArrayList<VoiceEffect> voiceFilterItems;
  private VoiceFilterAdapter voiceFilterAdapter;
  private VoiceOperations voiceOperations;

  private int selectedArTabPosition;
  private String selectedSlot = AROperations.SLOT_MASKS;
  private boolean downloadRequired = FiltersConfig.isDownloadRequired();
  private boolean filtersDownloadedAlready;
  private AlertDialog alertDialogDownloadFilters;
  private ProgressDialog dialog;
  private boolean showAudioEffects;

  public EffectsFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (view == null) {

      view = inflater.inflate(R.layout.ism_bottomsheet_effects, container, false);
    } else {

      if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
    }
    ButterKnife.bind(this, view);
    filtersDownloadedAlready = FiltersConfig.isFiltersDownloadedAlready();
    downloadRequired = FiltersConfig.isDownloadRequired();
    if (dialog == null) {
      dialog = new

          ProgressDialog(activity);
      dialog.setCancelable(false);
      arOperations = new AROperations(IsometrikUiSdk.getInstance().getIsometrik());
      voiceOperations = IsometrikUiSdk.getInstance().getIsometrik().getVoiceOperations();
      tabLayoutFilters.addTab(tabLayoutFilters.newTab()
          .setText(getString(R.string.ism_masks))
          .setIcon(R.drawable.ism_ic_ar_mask));
      tabLayoutFilters.addTab(tabLayoutFilters.newTab()
          .setText(getString(R.string.ism_effects))
          .setIcon(R.drawable.ism_ic_ar_effect));
      tabLayoutFilters.addTab(tabLayoutFilters.newTab()
          .setText(getString(R.string.ism_filters))
          .setIcon(R.drawable.ism_ic_ar_filter));

      if (showAudioEffects) {
        tabLayoutFilters.addTab(tabLayoutFilters.newTab()
            .setText(getString(R.string.ism_voice_change))
            .setIcon(R.drawable.ism_ic_voice_change));
        tabLayoutFilters.addTab(tabLayoutFilters.newTab()
            .setText(getString(R.string.ism_reverberation))
            .setIcon(R.drawable.ism_ic_reverberation));
      }

      arFiltersList.setLayoutManager(
          new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));

      arFilterItems = new ArrayList<>();

      arFilterItems.addAll(arOperations.getMasks());

      arFilterAdapter = new ArFilterAdapter(activity, arFilterItems);

      arFiltersList.setAdapter(arFilterAdapter);

      voiceFiltersList.setLayoutManager(
          new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));

      voiceFilterItems = new ArrayList<>();

      voiceFilterItems.addAll(voiceOperations.getVoiceChangers());

      voiceFilterAdapter = new VoiceFilterAdapter(activity, voiceFilterItems);

      voiceFiltersList.setAdapter(voiceFilterAdapter);
      if (downloadRequired) {
        if (filtersDownloadedAlready) {
          rlDownloadFilters.setVisibility(View.GONE);
        } else {
          rlDownloadFilters.setVisibility(View.VISIBLE);
        }

        rlDownloadFilters.setOnClickListener(v -> {

          AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
          @SuppressLint("InflateParams")
          final View vDownloadFilters =
              (activity).getLayoutInflater().inflate(R.layout.ism_dialog_download_filters, null);

          TextView tvDownloadFilters = vDownloadFilters.findViewById(R.id.tvDownload);
          TextView tvSize = vDownloadFilters.findViewById(R.id.tvSize);

          TextView tvCancel = vDownloadFilters.findViewById(R.id.tvCancel);
          tvSize.setText(FiltersConfig.DEEPAR_SIZE);
          tvDownloadFilters.setText(getString(R.string.ism_download_filters));
          tvDownloadFilters.setEnabled(true);
          tvCancel.setVisibility(View.VISIBLE);
          tvDownloadFilters.setOnClickListener(view -> {
            tvDownloadFilters.setEnabled(false);
            tvDownloadFilters.setText(getString(R.string.ism_downloading_filters));
            tvCancel.setVisibility(View.GONE);
            ZipUtils.downloadEffects(0, this, vDownloadFilters.findViewById(R.id.pbDownload));
          });

          alertDialog.setView(vDownloadFilters);
          alertDialog.setCancelable(false);
          alertDialogDownloadFilters = alertDialog.create();
          if (!activity.isFinishing()) alertDialogDownloadFilters.show();
          tvCancel.setOnClickListener(view -> alertDialogDownloadFilters.dismiss());
        });
      } else {
        rlDownloadFilters.setVisibility(View.GONE);
      }
      arFiltersList.addOnItemTouchListener(new RecyclerItemClickListener(activity, arFiltersList,
          new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

              if (position >= 0) {
                AREffect filterItem = arFilterItems.get(position);

                if (filterItem != null) {

                  if (downloadRequired) {

                    if (filtersDownloadedAlready) {
                      if (filterItem.isSelected()) {

                        deselectCurrentArFilter(position);
                      } else {

                        selectCurrentArFilter(position);
                      }
                    } else {
                      Toast.makeText(activity, getString(R.string.filters_download_required),
                          Toast.LENGTH_SHORT).show();
                    }
                  } else {
                    if (filterItem.isSelected()) {

                      deselectCurrentArFilter(position);
                    } else {

                      selectCurrentArFilter(position);
                    }
                  }
                }
              }
            }

            @Override
            public void onItemLongClick(View view, final int position) {

            }
          }));

      voiceFiltersList.addOnItemTouchListener(
          new RecyclerItemClickListener(activity, voiceFiltersList,
              new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, final int position) {
                  if (position >= 0) {
                    VoiceEffect filterItem = voiceFilterItems.get(position);

                    if (filterItem != null) {
                      if (filterItem.isSelected()) {

                        deselectCurrentVoiceFilter(position, filterItem.getEffectType());
                      } else {

                        selectCurrentVoiceFilter(position, filterItem.getEffectType());
                      }
                    }
                  }
                }

                @Override
                public void onItemLongClick(View view, final int position) {

                }
              }));

      tabLayoutFilters.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
          selectedArTabPosition = tabLayoutFilters.getSelectedTabPosition();
          if (tab.getIcon() != null) {
            tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
          }
          switch (selectedArTabPosition) {

            case 0: {
              //Masks selected
              //tvClearArFilters.setVisibility(View.VISIBLE);
              arFiltersList.setVisibility(View.VISIBLE);
              voiceFiltersList.setVisibility(View.GONE);
              selectedSlot = AROperations.SLOT_MASKS;
              arFilterItems.clear();
              arFilterItems.addAll(arOperations.getMasks());
              arFilterAdapter.notifyDataSetChanged();
              break;
            }
            case 1: {
              //Effects selected
              //tvClearArFilters.setVisibility(View.VISIBLE);
              arFiltersList.setVisibility(View.VISIBLE);
              voiceFiltersList.setVisibility(View.GONE);
              selectedSlot = AROperations.SLOT_EFFECTS;
              arFilterItems.clear();
              arFilterItems.addAll(arOperations.getEffects());
              arFilterAdapter.notifyDataSetChanged();
              break;
            }
            case 2: {
              //Filters selected
              //tvClearArFilters.setVisibility(View.VISIBLE);
              arFiltersList.setVisibility(View.VISIBLE);
              voiceFiltersList.setVisibility(View.GONE);
              selectedSlot = AROperations.SLOT_FILTERS;
              arFilterItems.clear();
              arFilterItems.addAll(arOperations.getFilters());
              arFilterAdapter.notifyDataSetChanged();
              break;
            }

            case 3: {
              //Voice changer selected
              //tvClearArFilters.setVisibility(View.INVISIBLE);
              arFiltersList.setVisibility(View.GONE);
              voiceFiltersList.setVisibility(View.VISIBLE);
              voiceFilterItems.clear();
              voiceFilterItems.addAll(voiceOperations.getVoiceChangers());
              voiceFilterAdapter.notifyDataSetChanged();
              break;
            }

            case 4: {
              //Reverberation selected
              //tvClearArFilters.setVisibility(View.INVISIBLE);
              arFiltersList.setVisibility(View.GONE);
              voiceFiltersList.setVisibility(View.VISIBLE);
              voiceFilterItems.clear();
              voiceFilterItems.addAll(voiceOperations.getReverberations());
              voiceFilterAdapter.notifyDataSetChanged();
              break;
            }
          }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
          if (tab.getIcon() != null) {
            tab.getIcon()
                .setColorFilter(ContextCompat.getColor(activity, R.color.ism_grey),
                    PorterDuff.Mode.SRC_IN);
          }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
      });

      try {
        tabLayoutFilters.getTabAt(0)
            .getIcon()
            .setColorFilter(ContextCompat.getColor(activity, R.color.ism_white),
                PorterDuff.Mode.SRC_IN);
      } catch (NullPointerException ignore) {
      }
      try {
        tabLayoutFilters.getTabAt(0).select();
      } catch (NullPointerException ignore) {
      }
    } else {
      //Log.d("log1",filtersDownloadedAlready+" "+downloadRequired);

      if (downloadRequired) {
        if (filtersDownloadedAlready) {
          rlDownloadFilters.setVisibility(View.GONE);
        } else {
          rlDownloadFilters.setVisibility(View.VISIBLE);
        }
      } else {
        rlDownloadFilters.setVisibility(View.GONE);
      }
    }
    return view;
  }

  @OnClick(R2.id.tvClearArFilters)
  public void clearArFilters() {
    if (selectedArTabPosition == 0 || selectedArTabPosition == 1 || selectedArTabPosition == 2) {
      if (downloadRequired) {
        if (filtersDownloadedAlready) {
          clearAllArFilters();
        } else {
          Toast.makeText(activity, getString(R.string.filters_download_required),
              Toast.LENGTH_SHORT).show();
        }
      } else {
        clearAllArFilters();
      }
    } else {
      clearAllVoiceFilters();
    }
  }

  private void deselectCurrentArFilter(int position) {

    arOperations.applyFilter(selectedSlot, "none");

    AREffect arEffect = arFilterItems.get(position);
    arEffect.setSelected(false);

    arFilterItems.set(position, arEffect);
    arFilterAdapter.notifyItemChanged(position);

    updateArFiltersSelectedStatus();
  }

  private void selectCurrentArFilter(int position) {
    arOperations.applyFilter(selectedSlot, arFilterItems.get(position).getPath());

    AREffect arEffect;
    for (int i = 0; i < arFilterItems.size(); i++) {

      arEffect = arFilterItems.get(i);
      if (position == i) {
        arEffect.setSelected(true);
      } else {
        arEffect.setSelected(false);
      }
      arFilterItems.set(i, arEffect);
    }
    arFilterAdapter.notifyDataSetChanged();
    updateArFiltersSelectedStatus();
  }

  private void deselectCurrentVoiceFilter(int position, String effectType) {

    voiceOperations.applyFilter(0, effectType);

    VoiceEffect voiceEffect = voiceFilterItems.get(position);
    voiceEffect.setSelected(false);

    voiceFilterItems.set(position, voiceEffect);
    voiceFilterAdapter.notifyItemChanged(position);

    updateVoiceFiltersSelectedStatus();
  }

  private void selectCurrentVoiceFilter(int position, String effectType) {

    voiceOperations.applyFilter(voiceFilterItems.get(position).getEffectNameInternal(), effectType);

    VoiceEffect voiceEffect;
    for (int i = 0; i < voiceFilterItems.size(); i++) {

      voiceEffect = voiceFilterItems.get(i);
      if (position == i) {
        voiceEffect.setSelected(true);
      } else {
        voiceEffect.setSelected(false);
      }
      voiceFilterItems.set(i, voiceEffect);
    }
    voiceFilterAdapter.notifyDataSetChanged();
    updateVoiceFiltersSelectedStatus();
  }

  private void updateArFiltersSelectedStatus() {
    switch (selectedArTabPosition) {
      case 0:
        //Masks selected
        arOperations.setMasks(arFilterItems);
        break;
      case 1:
        //Effects selected
        arOperations.setEffects(arFilterItems);
        break;
      case 2:
        //Filters selected
        arOperations.setFilters(arFilterItems);
        break;
    }
  }

  private void updateVoiceFiltersSelectedStatus() {
    switch (selectedArTabPosition) {
      case 3:
        //Voice changer selected
        voiceOperations.setVoiceChangers(voiceFilterItems);
        break;
      case 4:
        //Reverberation selected
        voiceOperations.setReverberations(voiceFilterItems);
        break;
    }
  }

  private void clearAllArFilters() {
    arOperations.clearAllFilters();

    AREffect arEffect;
    for (int i = 0; i < arFilterItems.size(); i++) {

      arEffect = arFilterItems.get(i);
      arEffect.setSelected(false);

      arFilterItems.set(i, arEffect);
    }

    arFilterAdapter.notifyDataSetChanged();
    updateArFiltersSelectedStatus();
    ArrayList<AREffect> arEffects;
    ArrayList<AREffect> values;
    for (int i = 0; i < 3; i++) {
      if (i != selectedArTabPosition) {

        switch (i) {
          case 0:
            arEffects = arOperations.getMasks();
            values = new ArrayList<>();

            for (int j = 0; j < arEffects.size(); j++) {

              arEffect = arEffects.get(j);
              arEffect.setSelected(false);

              values.add(arEffect);
            }
            arOperations.setMasks(values);
            break;
          case 1:
            arEffects = arOperations.getEffects();
            values = new ArrayList<>();

            for (int j = 0; j < arEffects.size(); j++) {

              arEffect = arEffects.get(j);
              arEffect.setSelected(false);

              values.add(arEffect);
            }
            arOperations.setEffects(values);
            break;
          case 2:
            arEffects = arOperations.getFilters();
            values = new ArrayList<>();

            for (int j = 0; j < arEffects.size(); j++) {

              arEffect = arEffects.get(j);
              arEffect.setSelected(false);

              values.add(arEffect);
            }
            arOperations.setFilters(values);
            break;
        }
      }
    }
  }

  private void clearAllVoiceFilters() {
    voiceOperations.clearAllFilters();

    VoiceEffect voiceEffect;
    for (int i = 0; i < voiceFilterItems.size(); i++) {

      voiceEffect = voiceFilterItems.get(i);
      voiceEffect.setSelected(false);

      voiceFilterItems.set(i, voiceEffect);
    }

    voiceFilterAdapter.notifyDataSetChanged();
    updateVoiceFiltersSelectedStatus();
    ArrayList<VoiceEffect> voiceEffects;
    ArrayList<VoiceEffect> values;
    for (int i = 3; i < 5; i++) {
      if (i != selectedArTabPosition) {

        switch (i) {
          case 3:
            voiceEffects = voiceOperations.getVoiceChangers();
            values = new ArrayList<>();

            for (int j = 0; j < voiceEffects.size(); j++) {

              voiceEffect = voiceEffects.get(j);
              voiceEffect.setSelected(false);

              values.add(voiceEffect);
            }
            voiceOperations.setVoiceChangers(values);
            break;
          case 4:
            voiceEffects = voiceOperations.getReverberations();
            values = new ArrayList<>();

            for (int j = 0; j < voiceEffects.size(); j++) {

              voiceEffect = voiceEffects.get(j);
              voiceEffect.setSelected(false);

              values.add(voiceEffect);
            }
            voiceOperations.setReverberations(values);
            break;
        }
      }
    }
  }

  /**
   * To allow filters download on the fly
   */
  @Override
  public void downloadResult(String result, String filePath) {
    if (alertDialogDownloadFilters != null && alertDialogDownloadFilters.isShowing()) {
      try {
        alertDialogDownloadFilters.dismiss();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (filePath != null) {

      dialog.setMessage(getString(R.string.preparing_filters));
      if (activity != null) {
        if (!activity.isFinishing()) {
          activity.runOnUiThread(() -> dialog.show());
        }
      }
      ZipUtils.extractZip(filePath, 0, this);
    } else {
      if (activity != null) {
        activity.runOnUiThread(() -> Toast.makeText(activity, result, Toast.LENGTH_SHORT).show());
      }
    }
  }

  @Override
  public void zipExtractResult(String result) {

    if (dialog != null && dialog.isShowing()) {
      dialog.dismiss();
    }

    if (result == null) {
      filtersDownloadedAlready = true;

      if (activity != null) {
        activity.runOnUiThread(() -> {
          rlDownloadFilters.setVisibility(View.GONE);
          Toast.makeText(activity, R.string.filters_prepared, Toast.LENGTH_SHORT).show();
        });
      }
    } else {

      if (activity != null) {
        activity.runOnUiThread(
            () -> Toast.makeText(activity, R.string.filters_prepare_failed, Toast.LENGTH_SHORT)
                .show());
      }
    }
  }

  @Override
  public void onAttach(@NotNull Context context) {
    super.onAttach(context);
    activity = getActivity();
  }

  @Override
  public void onDetach() {
    super.onDetach();
    activity = null;
  }

  public void updateParameters(boolean showAudioEffects) {
    this.showAudioEffects = showAudioEffects;
  }
}

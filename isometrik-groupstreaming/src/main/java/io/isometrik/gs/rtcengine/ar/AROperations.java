package io.isometrik.gs.rtcengine.ar;

import io.isometrik.gs.Isometrik;
import java.util.ArrayList;

/**
 * The class to apply AR masks, filters or effects.
 */
public class AROperations {

  public final static String SLOT_MASKS = "masks";

  public final static String SLOT_EFFECTS = "effects";

  public final static String SLOT_FILTERS = "filters";

  private ArrayList<AREffect> masks;
  private ArrayList<AREffect> effects;
  private ArrayList<AREffect> filters;

  private Isometrik isometrik;

  /**
   * Instantiates a new AROperations instance.
   *
   * @param isometrik the isometrik instance
   * @see io.isometrik.gs.Isometrik
   */
  public AROperations(Isometrik isometrik) {

    masks = new ArrayList<>();
    masks.add(new AREffect(MasksEnum.None.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.None.getValue()));
    masks.add(new AREffect(MasksEnum.Aviators.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.Aviators.getValue()));
    masks.add(new AREffect(MasksEnum.Koala.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.Koala.getValue()));
    masks.add(new AREffect(MasksEnum.Lion.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.Lion.getValue()));
    masks.add(new AREffect(MasksEnum.Dalmatian.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.Dalmatian.getValue()));
    masks.add(new AREffect(MasksEnum.Flowers.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.Flowers.getValue()));
    masks.add(new AREffect(MasksEnum.TeddyCigar.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.TeddyCigar.getValue()));
    masks.add(new AREffect(MasksEnum.Pug.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.Pug.getValue()));
    masks.add(new AREffect(MasksEnum.MudMask.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.MudMask.getValue()));
    masks.add(new AREffect(MasksEnum.Slash.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.Slash.getValue()));
    masks.add(new AREffect(MasksEnum.Beard.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.Beard.getValue()));
    masks.add(new AREffect(MasksEnum.Frankenstein.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.Frankenstein.getValue()));
    masks.add(new AREffect(MasksEnum.Alien.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.Alien.getValue()));

    masks.add(new AREffect(MasksEnum.Topology.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.Topology.getValue()));
    masks.add(new AREffect(MasksEnum.TinySunglasses.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.TinySunglasses.getValue()));
    masks.add(new AREffect(MasksEnum.TapeFace.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.TapeFace.getValue()));
    masks.add(new AREffect(MasksEnum.Scuba.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.Scuba.getValue()));
    masks.add(new AREffect(MasksEnum.Pumpkin.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.Pumpkin.getValue()));
    masks.add(new AREffect(MasksEnum.FlowerCrown.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.FlowerCrown.getValue()));
    masks.add(new AREffect(MasksEnum.FairyLights.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.FairyLights.getValue()));

    masks.add(new AREffect(MasksEnum.Kanye.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.Kanye.getValue()));
    masks.add(new AREffect(MasksEnum.Obama.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.Obama.getValue()));

    masks.add(new AREffect(MasksEnum.SleepingMask.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.SleepingMask.getValue()));
    masks.add(new AREffect(MasksEnum.TripleFace.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.TripleFace.getValue()));
    masks.add(new AREffect(MasksEnum.SmallFace.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.SmallFace.getValue()));
    masks.add(new AREffect(MasksEnum.Fatify.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.Fatify.getValue()));
    masks.add(new AREffect(MasksEnum.BigMouth.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.BigMouth.getValue()));
    masks.add(new AREffect(MasksEnum.TwistedFace.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.TwistedFace.getValue()));
    masks.add(new AREffect(MasksEnum.GrumpyCat.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.GrumpyCat.getValue()));
    masks.add(new AREffect(MasksEnum.ManlyFace.getValue(), AREffect.EffectTypeMask,
        MasksNameEnum.ManlyFace.getValue()));

    effects = new ArrayList<>();
    effects.add(new AREffect(EffectsEnum.None.getValue(), AREffect.EffectTypeAction,
        EffectsNameEnum.None.getValue()));
    effects.add(new AREffect(EffectsEnum.PlasticOcean.getValue(), AREffect.EffectTypeAction,
        EffectsNameEnum.PlasticOcean.getValue()));
    effects.add(new AREffect(EffectsEnum.Rain.getValue(), AREffect.EffectTypeAction,
        EffectsNameEnum.Rain.getValue()));
    effects.add(new AREffect(EffectsEnum.Blizzard.getValue(), AREffect.EffectTypeAction,
        EffectsNameEnum.Blizzard.getValue()));
    //effects.add(new AREffect(EffectsEnum.Heart.getValue(), AREffect.EffectTypeAction,
    //    EffectsNameEnum.Heart.getValue()));
    //effects.add(new AREffect(EffectsEnum.Fire.getValue(), AREffect.EffectTypeAction,
    //    EffectsNameEnum.Fire.getValue()));
    effects.add(new AREffect(EffectsEnum.HairSegmentation.getValue(), AREffect.EffectTypeAction,
        EffectsNameEnum.HairSegmentation.getValue()));

    filters = new ArrayList<>();
    filters.add(new AREffect(FiltersEnum.None.getValue(), AREffect.EffectTypeFilter,
        FiltersNameEnum.None.getValue()));
    filters.add(new AREffect(FiltersEnum.FilmColorPerfection.getValue(), AREffect.EffectTypeFilter,
        FiltersNameEnum.FilmColorPerfection.getValue()));
    filters.add(new AREffect(FiltersEnum.Tv80.getValue(), AREffect.EffectTypeFilter,
        FiltersNameEnum.Tv80.getValue()));
    filters.add(new AREffect(FiltersEnum.DrawingManga.getValue(), AREffect.EffectTypeFilter,
        FiltersNameEnum.DrawingManga.getValue()));
    filters.add(new AREffect(FiltersEnum.Sepia.getValue(), AREffect.EffectTypeFilter,
        FiltersNameEnum.Sepia.getValue()));
    filters.add(new AREffect(FiltersEnum.BleachByPass.getValue(), AREffect.EffectTypeFilter,
        FiltersNameEnum.BleachByPass.getValue()));
    filters.add(new AREffect(FiltersEnum.RealVhs.getValue(), AREffect.EffectTypeFilter,
        FiltersNameEnum.RealVhs.getValue()));
    filters.add(new AREffect(FiltersEnum.FxDrunk.getValue(), AREffect.EffectTypeFilter,
        FiltersNameEnum.FxDrunk.getValue()));
    filters.add(
        new AREffect(FiltersEnum.BackgroundSegmentation.getValue(), AREffect.EffectTypeFilter,
            FiltersNameEnum.BackgroundSegmentation.getValue()));

    this.isometrik = isometrik;
  }

  /**
   * Gets masks.
   *
   * @return the list of masks
   * @see io.isometrik.gs.rtcengine.ar.AREffect
   */
  public ArrayList<AREffect> getMasks() {
    return masks;
  }

  /**
   * Gets effects.
   *
   * @return the list of effects
   * @see io.isometrik.gs.rtcengine.ar.AREffect
   */
  public ArrayList<AREffect> getEffects() {
    return effects;
  }

  /**
   * Gets filters.
   *
   * @return the list of filters
   * @see io.isometrik.gs.rtcengine.ar.AREffect
   */
  public ArrayList<AREffect> getFilters() {
    return filters;
  }

  /**
   * Apply filter.
   *
   * @param slot the slot of effects,filters or masks
   * @param path the path of the effect to apply
   */
  public void applyFilter(String slot, String path) {
    if (isometrik.isARFiltersEnabled()) {

      isometrik.getAREngine().switchEffect(slot, path);
    }
  }

  /**
   * Clear all filters to remove all of the active effects from the video feed at once.
   */
  public void clearAllFilters() {
    applyFilter("masks", "none");
    applyFilter("effects", "none");
    applyFilter("filters", "none");
  }

  public void setMasks(ArrayList<AREffect> masks) {
    this.masks.clear();
    this.masks.addAll(masks);
  }

  public void setEffects(ArrayList<AREffect> effects) {
    this.effects.clear();
    this.effects.addAll(effects);
  }

  public void setFilters(ArrayList<AREffect> filters) {
    this.filters.clear();
    this.filters.addAll(filters);
  }
}

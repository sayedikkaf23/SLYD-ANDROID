package chat.hola.com.app.DublyCamera.deepar.ar;

import chat.hola.com.app.DublyCamera.utilities.FiltersConfig;

/**
 * The class to wrap the detail of the AR effect which includes its type, name, internal name and
 * path.
 */
public class AREffect {

  static final String EffectTypeMask = "mask";

  static final String EffectTypeAction = "effect";

  static final String EffectTypeFilter = "filter";

  private String effectNameInternal;
  private String effectType;
  private String effectName;
  private boolean selected;

  /**
   * Instantiates a new Ar effect.
   *
   * @param effectNameInternal the effect name internal
   * @param effectType the effect type
   * @param effectName the effect name
   */
  AREffect(String effectNameInternal, String effectType, String effectName) {
    this.effectNameInternal = effectNameInternal;
    this.effectType = effectType;
    this.effectName = effectName;
  }

  /**
   * Gets effect name internal.
   *
   * @return the effect name internal
   */
  public String getEffectNameInternal() {
    return effectNameInternal;
  }

  /**
   * Sets effect name internal.
   *
   * @param effectNameInternal the effect name internal
   */
  public void setEffectNameInternal(String effectNameInternal) {
    this.effectNameInternal = effectNameInternal;
  }

  /**
   * Gets effect name.
   *
   * @return the name of the ar effect
   */
  public String getEffectName() {
    return effectName;
  }

  /**
   * Gets effect type.
   *
   * @return the type of the ar effect
   */
  public String getEffectType() {
    return effectType;
  }

  /**
   * Gets path.
   *
   * @return the path of the ar effect
   */
  public String getPath() {
    if (effectNameInternal.equals("none")) {
      return null;
    }
    if (FiltersConfig.DOWNLOAD_REQUIRED) {
      return FiltersConfig.DEEPAR_DIRECTORY + "deepar/"+ effectNameInternal;
    } else {
      return "file:///android_asset/deepar/" + effectNameInternal;
    }
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }
}

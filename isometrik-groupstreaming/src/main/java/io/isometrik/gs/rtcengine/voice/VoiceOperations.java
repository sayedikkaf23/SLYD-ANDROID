package io.isometrik.gs.rtcengine.voice;

import io.agora.rtc.RtcEngine;
import java.util.ArrayList;

/**
 * The class contains code to add voice change and reverberation effects.
 */
public class VoiceOperations {

  private ArrayList<VoiceEffect> voiceChangers;
  private ArrayList<VoiceEffect> reverberations;

  private RtcEngine mRtcEngine;

  public VoiceOperations(RtcEngine mRtcEngine) {
    this.mRtcEngine = mRtcEngine;

    voiceChangers = new ArrayList<>();
    voiceChangers.add(new VoiceEffect(VoiceChangerEnum.VOICE_CHANGER_OFF.getValue(),
        VoiceEffect.EffectTypeVoiceChange, VoiceChangeNamesEnum.VOICE_CHANGE_OFF.getValue()));
    voiceChangers.add(new VoiceEffect(VoiceChangerEnum.VOICE_CHANGER_OLDMAN.getValue(),
        VoiceEffect.EffectTypeVoiceChange, VoiceChangeNamesEnum.VOICE_CHANGE_OLDMAN.getValue()));
    voiceChangers.add(new VoiceEffect(VoiceChangerEnum.VOICE_CHANGER_BABYBOY.getValue(),
        VoiceEffect.EffectTypeVoiceChange, VoiceChangeNamesEnum.VOICE_CHANGE_BABYBOY.getValue()));
    voiceChangers.add(new VoiceEffect(VoiceChangerEnum.VOICE_CHANGER_BABYGIRL.getValue(),
        VoiceEffect.EffectTypeVoiceChange, VoiceChangeNamesEnum.VOICE_CHANGE_BABYGIRL.getValue()));
    voiceChangers.add(new VoiceEffect(VoiceChangerEnum.VOICE_CHANGER_ZHUBAJIE.getValue(),
        VoiceEffect.EffectTypeVoiceChange, VoiceChangeNamesEnum.VOICE_CHANGE_ZHUBAJIE.getValue()));
    voiceChangers.add(new VoiceEffect(VoiceChangerEnum.VOICE_CHANGER_ETHEREAL.getValue(),
        VoiceEffect.EffectTypeVoiceChange, VoiceChangeNamesEnum.VOICE_CHANGE_ETHEREAL.getValue()));
    voiceChangers.add(new VoiceEffect(VoiceChangerEnum.VOICE_CHANGER_HULK.getValue(),
        VoiceEffect.EffectTypeVoiceChange, VoiceChangeNamesEnum.VOICE_CHANGE_HULK.getValue()));

    reverberations = new ArrayList<>();
    reverberations.add(new VoiceEffect(VoiceReverberationEnum.AUDIO_REVERB_OFF.getValue(),
        VoiceEffect.EffectTypeReverberation,
        VoiceReverberationNamesEnum.VOICE_REVERBERATION_OFF.getValue()));
    reverberations.add(new VoiceEffect(VoiceReverberationEnum.AUDIO_REVERB_POPULAR.getValue(),
        VoiceEffect.EffectTypeReverberation,
        VoiceReverberationNamesEnum.VOICE_REVERBERATION_POPMUSIC.getValue()));
    reverberations.add(new VoiceEffect(VoiceReverberationEnum.AUDIO_REVERB_RNB.getValue(),
        VoiceEffect.EffectTypeReverberation,
        VoiceReverberationNamesEnum.VOICE_REVERBERATION_RANDB.getValue()));
    reverberations.add(new VoiceEffect(VoiceReverberationEnum.AUDIO_REVERB_ROCK.getValue(),
        VoiceEffect.EffectTypeReverberation,
        VoiceReverberationNamesEnum.VOICE_REVERBERATION_ROCKMUSIC.getValue()));
    reverberations.add(new VoiceEffect(VoiceReverberationEnum.AUDIO_REVERB_HIPHOP.getValue(),
        VoiceEffect.EffectTypeReverberation,
        VoiceReverberationNamesEnum.VOICE_REVERBERATION_HIPHOP.getValue()));
    reverberations.add(new VoiceEffect(VoiceReverberationEnum.AUDIO_REVERB_VOCAL_CONCERT.getValue(),
        VoiceEffect.EffectTypeReverberation,
        VoiceReverberationNamesEnum.VOICE_REVERBERATION_VOCAL_CONCERT.getValue()));
    reverberations.add(new VoiceEffect(VoiceReverberationEnum.AUDIO_REVERB_KTV.getValue(),
        VoiceEffect.EffectTypeReverberation,
        VoiceReverberationNamesEnum.VOICE_REVERBERATION_KARAOKE.getValue()));
    reverberations.add(new VoiceEffect(VoiceReverberationEnum.AUDIO_REVERB_STUDIO.getValue(),
        VoiceEffect.EffectTypeReverberation,
        VoiceReverberationNamesEnum.VOICE_REVERBERATION_STUDIO.getValue()));
  }

  /**
   * Gets voiceChangers.
   *
   * @return the list of voiceChangers
   * @see io.isometrik.gs.rtcengine.voice.VoiceEffect
   */
  public ArrayList<VoiceEffect> getVoiceChangers() {
    return voiceChangers;
  }

  /**
   * Gets reverberations.
   *
   * @return the list of reverberations
   * @see io.isometrik.gs.rtcengine.voice.VoiceEffect
   */
  public ArrayList<VoiceEffect> getReverberations() {
    return reverberations;
  }

  /**
   * Voice changer.
   *
   * @param voiceChange the value to change the local voice
   */
  public void voiceChanger(int voiceChange) {
    //mRtcEngine.setLocalVoiceReverbPreset(VoiceReverberationEnum.AUDIO_REVERB_OFF.getValue());
    switch (voiceChange) {

      case 1:
        mRtcEngine.setLocalVoiceChanger(VoiceChangerEnum.VOICE_CHANGER_OLDMAN.getValue());
        break;
      case 2:
        mRtcEngine.setLocalVoiceChanger(VoiceChangerEnum.VOICE_CHANGER_BABYBOY.getValue());
        break;
      case 3:
        mRtcEngine.setLocalVoiceChanger(VoiceChangerEnum.VOICE_CHANGER_BABYGIRL.getValue());
        break;
      case 4:
        mRtcEngine.setLocalVoiceChanger(VoiceChangerEnum.VOICE_CHANGER_ZHUBAJIE.getValue());
        break;
      case 5:
        mRtcEngine.setLocalVoiceChanger(VoiceChangerEnum.VOICE_CHANGER_ETHEREAL.getValue());
        break;
      case 6:
        mRtcEngine.setLocalVoiceChanger(VoiceChangerEnum.VOICE_CHANGER_HULK.getValue());
        break;

      default:
        mRtcEngine.setLocalVoiceChanger(VoiceChangerEnum.VOICE_CHANGER_OFF.getValue());
    }
  }

  /**
   * Reverberation changer.
   *
   * @param reverbChange the value to change the local reverberation
   */
  public void reverberationChanger(int reverbChange) {

    //mRtcEngine.setLocalVoiceChanger(VoiceChangerEnum.VOICE_CHANGER_OFF.getValue());
    if (reverbChange != 0) {
      mRtcEngine.setLocalVoiceChanger(VoiceReverberationEnum.AUDIO_REVERB_OFF.getValue());
    }
    switch (reverbChange) {

      case 1:
        mRtcEngine.setLocalVoiceReverbPreset(
            VoiceReverberationEnum.AUDIO_REVERB_POPULAR.getValue());
        break;
      case 2:
        mRtcEngine.setLocalVoiceReverbPreset(VoiceReverberationEnum.AUDIO_REVERB_RNB.getValue());
        break;
      case 3:
        mRtcEngine.setLocalVoiceReverbPreset(VoiceReverberationEnum.AUDIO_REVERB_ROCK.getValue());
        break;
      case 4:
        mRtcEngine.setLocalVoiceReverbPreset(VoiceReverberationEnum.AUDIO_REVERB_HIPHOP.getValue());
        break;
      case 5:
        mRtcEngine.setLocalVoiceReverbPreset(
            VoiceReverberationEnum.AUDIO_REVERB_VOCAL_CONCERT.getValue());
        break;
      case 6:
        mRtcEngine.setLocalVoiceReverbPreset(VoiceReverberationEnum.AUDIO_REVERB_KTV.getValue());
        break;
      case 7:
        mRtcEngine.setLocalVoiceReverbPreset(VoiceReverberationEnum.AUDIO_REVERB_STUDIO.getValue());
        break;

      default:

        mRtcEngine.setLocalVoiceReverbPreset(VoiceReverberationEnum.AUDIO_REVERB_OFF.getValue());
    }
  }

  public void setVoiceChangers(ArrayList<VoiceEffect> voiceChangers) {
    this.voiceChangers.clear();
    this.voiceChangers.addAll(voiceChangers);
  }

  public void setReverberations(ArrayList<VoiceEffect> reverberations) {
    this.reverberations.clear();
    this.reverberations.addAll(reverberations);
  }

  public void applyFilter(int effect, String effectType) {
    if (effectType.equals(VoiceEffect.EffectTypeVoiceChange)) {
      //voice change requested
      mRtcEngine.setLocalVoiceChanger(effect);
    } else if (effectType.equals(VoiceEffect.EffectTypeReverberation)) {
      //reverberation requested
      mRtcEngine.setLocalVoiceReverbPreset(effect);
    }
  }

  public void clearAllFilters() {
    applyFilter(0, VoiceEffect.EffectTypeVoiceChange);
    applyFilter(0, VoiceEffect.EffectTypeReverberation);
  }
}

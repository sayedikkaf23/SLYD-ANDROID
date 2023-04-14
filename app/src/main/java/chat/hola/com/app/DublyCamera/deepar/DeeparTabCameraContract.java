package chat.hola.com.app.DublyCamera.deepar;

import chat.hola.com.app.DublyCamera.deepar.ar.AROperations;

public interface DeeparTabCameraContract {

  interface Presenter {
    void startLiveBroadcastApi(String streamId, String streamType, String thumbnail,
        String streamName);

    /**
     * Applies a deepar filter
     *
     * @param slot the active filter slot
     * @param path of the filter to be applied
     */
    void applyFilter(String slot, String path);

    /**
     * Toggles state of beauty filter
     *
     * @param beautificationApplied whether to enable or disable beauty filter
     */
    void applyBeautifyOptions(boolean beautificationApplied);

    /**
     * Clear filters.
     */
    void clearFilters();

    void initialize(AROperations arOperations);
  }

  interface View {

    void onError(String message);

    void showAlert(String message);

    void onSuccess(String streamId);
  }
}
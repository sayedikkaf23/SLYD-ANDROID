package chat.hola.com.app.ecom.pdp;

import com.kotlintestgradle.model.ecom.pdp.VariantsData;
import java.util.ArrayList;

/**
 * interface onclick listener for the pdp variants.
 */
public interface PdpVariantsClickListener {
  void onVariantClick(ArrayList<VariantsData> variantsData, String unitId);
}


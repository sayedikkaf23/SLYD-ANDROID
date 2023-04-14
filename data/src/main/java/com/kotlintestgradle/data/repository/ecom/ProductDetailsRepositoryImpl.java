package com.kotlintestgradle.data.repository.ecom;

import static com.kotlintestgradle.data.utils.DataConstants.ECOM_TYPE;

import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.PdpMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.ecom.cart.PdpUseCase;
import com.kotlintestgradle.remote.model.request.ecom.PdpRequest;
import com.kotlintestgradle.remote.model.response.ecom.pdp.ProductDetails;
import com.kotlintestgradle.repository.ProductDetailsRepository;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import javax.inject.Inject;

public class ProductDetailsRepositoryImpl extends BaseRepository implements
    ProductDetailsRepository {
  private DataSource dataSource;
  private PreferenceManager mPreferenceManager;
  private PdpMapper mPdpMapper = new PdpMapper();

  @Inject
  public ProductDetailsRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    mPreferenceManager = dataSource.preference();
    this.dataSource = dataSource;
  }

  @Override
  public Single<PdpUseCase.ResponseValues> getPdp(String productId, String parentProductId,
      double lat, double longitude,String ipAddress, String city , String country) {
      return dataSource.api().handler().getProductDetails(mPreferenceManager.getAuthToken(),lat,longitude,
              ipAddress,city,country, new PdpRequest(productId, parentProductId)).flatMap(
              new Function<ProductDetails, SingleSource<? extends PdpUseCase.ResponseValues>>() {
                @Override
                public SingleSource<? extends PdpUseCase.ResponseValues> apply(ProductDetails productDetails)
                        throws Exception {
                  return Single.just(new PdpUseCase.ResponseValues(
                          mPdpMapper.map(productDetails, ECOM_TYPE)));
                }
              });
  }
}

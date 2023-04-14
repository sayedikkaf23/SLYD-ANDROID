package com.kotlintestgradle.data.mapper;

import static com.kotlintestgradle.data.utils.DataConstants.FIVE_STAR;
import static com.kotlintestgradle.data.utils.DataConstants.FOUR_STAR;
import static com.kotlintestgradle.data.utils.DataConstants.ONE_STAR;
import static com.kotlintestgradle.data.utils.DataConstants.PHARMACY_FLOW;
import static com.kotlintestgradle.data.utils.DataConstants.THREE_STAR;
import static com.kotlintestgradle.data.utils.DataConstants.TWO_STAR;

import com.kotlintestgradle.data.utils.DataUtils;
import com.kotlintestgradle.model.ecom.common.ImageData;
import com.kotlintestgradle.model.ecom.pdp.AttributeRatingData;
import com.kotlintestgradle.model.ecom.pdp.AttributesData;
import com.kotlintestgradle.model.ecom.pdp.ColorsData;
import com.kotlintestgradle.model.ecom.pdp.InnerAttributesData;
import com.kotlintestgradle.model.ecom.pdp.LinkToUnitData;
import com.kotlintestgradle.model.ecom.pdp.PdpOfferData;
import com.kotlintestgradle.model.ecom.pdp.ProductData;
import com.kotlintestgradle.model.ecom.pdp.ProductDataModel;
import com.kotlintestgradle.model.ecom.pdp.ProductModel;
import com.kotlintestgradle.model.ecom.pdp.Rating;
import com.kotlintestgradle.model.ecom.pdp.ReviewData;
import com.kotlintestgradle.model.ecom.pdp.SizeChartData;
import com.kotlintestgradle.model.ecom.pdp.SizeData;
import com.kotlintestgradle.model.ecom.pdp.UserReviewData;
import com.kotlintestgradle.model.ecom.pdp.VariantsData;
import com.kotlintestgradle.remote.model.response.ecom.common.BasicSizeDetails;
import com.kotlintestgradle.remote.model.response.ecom.common.SizeChart;
import com.kotlintestgradle.remote.model.response.ecom.pdp.AttributeRating;
import com.kotlintestgradle.remote.model.response.ecom.pdp.AttributesDetails;
import com.kotlintestgradle.remote.model.response.ecom.pdp.ColorsDetails;
import com.kotlintestgradle.remote.model.response.ecom.pdp.InnerAttributesDetails;
import com.kotlintestgradle.remote.model.response.ecom.pdp.LinkToUnit;
import com.kotlintestgradle.remote.model.response.ecom.pdp.PdpOfferDetails;
import com.kotlintestgradle.remote.model.response.ecom.pdp.ProductDetails;
import com.kotlintestgradle.remote.model.response.ecom.pdp.ProductModelDetails;
import com.kotlintestgradle.remote.model.response.ecom.pdp.ReviewDetails;
import com.kotlintestgradle.remote.model.response.ecom.pdp.SizeDetails;
import com.kotlintestgradle.remote.model.response.ecom.pdp.UserReviewDetails;
import com.kotlintestgradle.remote.model.response.ecom.pdp.VariantDetails;
import com.kotlintestgradle.remote.model.response.ecom.pdp.substitute.DataItems;
import java.util.ArrayList;
import java.util.List;

public class PdpMapper {
  public ProductDataModel map(ProductDetails productDetails, int storeType) {
    ReviewDetails reviewDetails = productDetails.getReview();
    ReviewData reviewData = new ReviewData(
        reviewDetails.getFourStartRating(), reviewDetails.getPenCount(),
        reviewDetails.getTwoStarRating(),
        reviewDetails.getTotalNoOfRatings(), reviewDetails.getThreeStarRating(),
        reviewDetails.getTotalStarRating(), reviewDetails.getFiveStarRating(),
        reviewDetails.getOneStarRating(),
        reviewDetails.getTotalNoOfReviews(),
        convertToAttributeRatingData(reviewDetails.getAttributeRating()),
        convertToReviewData(reviewDetails.getUserReviews()), reviewDetails.getImages(),
        getReviewList(reviewDetails)
    );
    ProductData productData = null;
    if (productDetails.getProductData() != null) {
      productData = new ProductData(
          convertToProductModel(productDetails.getProductData().getData(), storeType),
          productDetails.getProductData().getMessage());
    }
    return new ProductDataModel(reviewData,
        productData);
  }

  private ArrayList<Rating> getReviewList(ReviewDetails reviewDetails) {
    ArrayList<Rating> reviews = new ArrayList<>();
    reviews.add(
        new Rating(FIVE_STAR, reviewDetails.getFiveStarRating(),
            reviewDetails.getTotalNoOfRatings()));
    reviews.add(
        new Rating(FOUR_STAR, reviewDetails.getFourStartRating(),
            reviewDetails.getTotalNoOfRatings()));
    reviews.add(
        new Rating(THREE_STAR, reviewDetails.getThreeStarRating(),
            reviewDetails.getTotalNoOfRatings()));
    reviews.add(
        new Rating(TWO_STAR, reviewDetails.getTwoStarRating(),
            reviewDetails.getTotalNoOfRatings()));
    reviews.add(
        new Rating(ONE_STAR, reviewDetails.getOneStarRating(),
            reviewDetails.getTotalNoOfRatings()));
    return reviews;
  }

  private ArrayList<UserReviewData> convertToReviewData(
      ArrayList<UserReviewDetails> reviewDetails) {
    ArrayList<UserReviewData> userReviewList = new ArrayList<>();
    if (!DataUtils.isEmptyArray(reviewDetails)) {
      for (UserReviewDetails userReviewDetails : reviewDetails) {
        UserReviewData userReviewData = new UserReviewData(userReviewDetails.getImages(),
            userReviewDetails.getRating(),
            userReviewDetails.getName(),
            userReviewDetails.getReviewDesc(), userReviewDetails.getReviewId(),
            userReviewDetails.getTimestamp(),
            userReviewDetails.getReviewTitle(), userReviewDetails.getLikes(),
            userReviewDetails.getDisLikes(), userReviewDetails.isUserLikes(),
            userReviewDetails.isUserdisLikes());
        userReviewList.add(userReviewData);
      }
    }
    return userReviewList;
  }

  private ArrayList<AttributeRatingData> convertToAttributeRatingData(
      ArrayList<AttributeRating> attributesData) {
    ArrayList<AttributeRatingData> userAttributesList = new ArrayList<>();
    if (!DataUtils.isEmptyArray(attributesData)) {
      for (AttributeRating userReviewDetails : attributesData) {
        AttributeRatingData userReviewData = new AttributeRatingData(
            userReviewDetails.getAttributeId(), userReviewDetails.getRating(),
            userReviewDetails.getAttributeName(),
            userReviewDetails.getTotalStarRating());
        userAttributesList.add(userReviewData);
      }
    }
    return userAttributesList;
  }

  private ArrayList<ProductModel> convertToProductModel(
      ArrayList<ProductModelDetails> productDataList, int storeType) {
    ArrayList<ProductModel> productModels = new ArrayList<>();
    if (!DataUtils.isEmptyArray(productDataList)) {
      for (ProductModelDetails details : productDataList) {
        ProductModel model;
        if (storeType == PHARMACY_FLOW) {
          model = new ProductModel(convertToVariantData(details.getVariants()),
              convertToOfferListData(details.getOffers()),
              convertToOfferData(details.getAllOffers()),
              details.getBrandName(), CommonMapper.convertToImageData(details.getImages()),
              details.getParentProductId(),
              details.getChildProductId(), details.getSubCatName(), details.getCurrencySymbol(),
              details.getProductName(), convertToColorData(details.getColors()),
              details.getSubSubCatName(),
              details.getDetailDesc(), details.getHighlight(),
              convertToMiniSizeData(details.getSizes()), details.getIsPrimary(),
              details.getCatName(), CommonMapper.convertSupplierData(details.getSupplier()),
              details.getUnitId(),
              details.getCurrency(), details.getShortDesc(),
              convertToAttributeData(details.getAttributes()),
              details.getIsFavourite(),
              CommonMapper.convertToFinalPriceData(details.getFinalPriceList()),
              convertToLinkToUnitData(details.getLinkToUnit()),
              details.getMouUnit(), details.getDiscountType(), details.isOutOfStock(),
              details.getMou(),
              details.getAvailableQuantity(), convertToSizeChartData(details.getSizeChart()),
              details.getSellerCount(), details.isPrescriptionRequired(),
              details.getManufactureName(), details.getMouDataUnit(),
              convertToAttributeData(details.getHtmlAttributes()),details.isShoppingList(), details.getShareLink());
        } else {
          model = new ProductModel(convertToVariantData(details.getVariants()),
              convertToOfferListData(details.getOffers()), convertToOfferData(details.getAllOffers()),
              details.getBrandName(), CommonMapper.convertToImageData(details.getImages()),
              details.getParentProductId(),
              details.getChildProductId(), details.getSubCatName(), details.getCurrencySymbol(),
              details.getProductName(), convertToColorData(details.getColors()),
              details.getSubSubCatName(),
              details.getDetailDesc(), details.getHighlight(),
              convertToMiniSizeData(details.getSizes()), details.getIsPrimary(),
              details.getCatName(), CommonMapper.convertSupplierData(details.getSupplier()),
              details.getUnitId(),
              details.getCurrency(), details.getShortDesc(),
              convertToAttributeData(details.getAttributes()),
              details.getIsFavourite(),
              CommonMapper.convertToFinalPriceData(details.getFinalPriceList()),
              convertToLinkToUnitData(details.getLinkToUnit()),
              details.getMouUnit(), details.getDiscountType(), details.isOutOfStock(),
              details.getMou(),
              details.getAvailableQuantity(), convertToSizeChartData(details.getSizeChart()),
              details.getSellerCount(), details.getShareLink());
        }
        productModels.add(model);
      }
    }
    return productModels;
  }

  private ArrayList<VariantsData> convertToVariantData(ArrayList<VariantDetails> detailsList) {
    ArrayList<VariantsData> dataList = new ArrayList<>();
    if (!DataUtils.isEmptyArray(detailsList)) {
      for (VariantDetails details : detailsList) {
        VariantsData data = new VariantsData(details.getImage(), details.getChildProductId(),
            details.getIsPrimary(), details.getName(), details.getUnitId(), details.getRgb(),
            convertToMiniSizeData(details.getSizeData()));
        dataList.add(data);
      }
    }
    return dataList;
  }

  private ArrayList<SizeData> convertToMiniSizeData(ArrayList<BasicSizeDetails> sizeList) {
    ArrayList<SizeData> sizeData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(sizeList)) {
      for (BasicSizeDetails details : sizeList) {
        sizeData.add(new SizeData(details.getRgb(), details.getImage(),
            details.getSize(), details.isPrimary(), details.getChildProductId(),
            details.getName(), details.getUnitId()));
      }
    }
    return sizeData;
  }

  private ArrayList<LinkToUnitData> convertToLinkToUnitData(ArrayList<LinkToUnit> linkToUnitsList) {
    ArrayList<LinkToUnitData> unitData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(linkToUnitsList)) {
      for (LinkToUnit unit : linkToUnitsList) {
        LinkToUnitData toUnitData = new LinkToUnitData(unit.getImage(), unit.getChildProductId(),
            unit.getIsPrimary(), unit.getName(), unit.getUnitId(),
            convertToSizeData(unit.getSizeData()));
        unitData.add(toUnitData);
      }
    }
    return unitData;
  }

  private ArrayList<SizeChartData> convertToSizeChartData(ArrayList<SizeChart> sizeCharts) {
    ArrayList<SizeChartData> sizeChartData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(sizeCharts)) {
      for (SizeChart sizeChart : sizeCharts) {
        SizeChartData data = new SizeChartData(sizeChart.getSize(), sizeChart.getName());
        sizeChartData.add(data);
      }
    }
    return sizeChartData;
  }

  private ArrayList<SizeData> convertToSizeData(ArrayList<SizeDetails> sizeDetails) {
    ArrayList<SizeData> sizeData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(sizeDetails)) {
      for (SizeDetails details : sizeDetails) {
        SizeData sizeDat = new SizeData(details.getColourId(),
            details.getImage(), details.getSize(), details.getIsPrimary(),
            details.getChildProductId(), details.getName(), details.getUnitId());
        sizeData.add(sizeDat);
      }
    }
    return sizeData;
  }

  private ArrayList<AttributesData> convertToAttributeData(
      ArrayList<AttributesDetails> attributeList) {
    ArrayList<AttributesData> attributesData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(attributeList)) {
      for (AttributesDetails details : attributeList) {
        AttributesData data = new AttributesData(details.getName(),
            convertToInnerAttribute(details.getInnerAttributes()));
        attributesData.add(data);
      }
    }
    return attributesData;
  }

  private ArrayList<InnerAttributesData> convertToInnerAttribute(
      ArrayList<InnerAttributesDetails> innerAttributesList) {
    ArrayList<InnerAttributesData> dataArrayList = new ArrayList<>();
    if (!DataUtils.isEmptyArray(innerAttributesList)) {
      for (InnerAttributesDetails details : innerAttributesList) {
        InnerAttributesData data = new InnerAttributesData(details.getName(),
            details.getValue());
        dataArrayList.add(data);
      }
    }
    return dataArrayList;
  }

  private ArrayList<ColorsData> convertToColorData(ArrayList<ColorsDetails> colorsDetList) {
    ArrayList<ColorsData> colorsData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(colorsDetList)) {
      for (ColorsDetails details : colorsDetList) {
        ColorsData data = new ColorsData(details.getImage(), details.getIsPrimary(),
            details.getChildProductId(), details.getName(), details.getRgb());
        colorsData.add(data);
      }
    }
    return colorsData;
  }

  private PdpOfferData convertToOfferListData(PdpOfferDetails details) {
    ImageData webImage = null;
    if (details.getWebimages() != null) {
      webImage = new ImageData(details.getWebimages().getImage(),
          details.getWebimages().getThumbnail(), details.getWebimages().getMobile());
    }
    ImageData image = null;
    if (details.getImages() != null) {
      image = new ImageData(details.getImages().getImage(), details.getImages().getThumbnail(),
          details.getImages().getMobile());
    }
    return new PdpOfferData(details.getApplicableOnStatus(), image,
        details.getOfferName() != null ? details.getOfferName().getEn() : "",
        details.getEndDateTimeISO(),
        details.getEndDateTime(),
        details.getGlobalClaimCount(), details.getStartDateTimeISO(), webImage,
        details.getStartDateTime(), details.getPerUserLimit(), details.getMinimumPurchaseQty(),
        details.getStatusString(), details.getOfferId(), details.getDiscountType(),
        details.getOfferFor(),
        details.getDiscountValue(), details.getApplicableOn(), details.getStatus(),details.getTermscond()
    );
  }

   ArrayList<PdpOfferData> convertToOfferData(ArrayList<PdpOfferDetails> detailsList) {
    ArrayList<PdpOfferData> dataList = new ArrayList<>();
    if (!DataUtils.isEmptyArray(detailsList)) {
      for (PdpOfferDetails details : detailsList) {
        ImageData webImage = null;
        if (details.getWebimages() != null) {
          webImage = new ImageData(details.getWebimages().getImage(),
              details.getWebimages().getThumbnail(), details.getWebimages().getMobile());
        }
        ImageData image = null;
        if (details.getImages() != null) {
          image = new ImageData(details.getImages().getImage(), details.getImages().getThumbnail(),
              details.getImages().getMobile());
        }
        PdpOfferData pdpOfferData = new PdpOfferData(details.getApplicableOnStatus(), image,
            details.getOfferName() != null ? details.getOfferName().getEn() : "",
            details.getEndDateTimeISO(),
            details.getEndDateTime(),
            details.getGlobalClaimCount(), details.getStartDateTimeISO(), webImage,
            details.getStartDateTime(), details.getPerUserLimit(), details.getMinimumPurchaseQty(),
            details.getStatusString(), details.getOfferId(), details.getDiscountType(),
            details.getOfferFor(),
            details.getDiscountValue(), details.getApplicableOn(), details.getStatus(),details.getTermscond());
        dataList.add(pdpOfferData);
      }
    }
    return dataList;
  }

  PdpOfferData convertToOfferDataObject(PdpOfferDetails detailsList) {

        ImageData webImage = null;
        if (detailsList.getWebimages() != null) {
          webImage = new ImageData(detailsList.getWebimages().getImage(),
                  detailsList.getWebimages().getThumbnail(), detailsList.getWebimages().getMobile());
        }
        ImageData image = null;
        if (detailsList.getImages() != null) {
          image = new ImageData(detailsList.getImages().getImage(), detailsList.getImages().getThumbnail(),
                  detailsList.getImages().getMobile());
        }
        PdpOfferData pdpOfferData = new PdpOfferData(detailsList.getApplicableOnStatus(), image,
                detailsList.getOfferName() != null ? detailsList.getOfferName().getEn() : "",
                detailsList.getEndDateTimeISO(),
                detailsList.getEndDateTime(),
                detailsList.getGlobalClaimCount(), detailsList.getStartDateTimeISO(), webImage,
                detailsList.getStartDateTime(), detailsList.getPerUserLimit(), detailsList.getMinimumPurchaseQty(),
                detailsList.getStatusString(), detailsList.getOfferId(), detailsList.getDiscountType(),
                detailsList.getOfferFor(),
                detailsList.getDiscountValue(), detailsList.getApplicableOn(), detailsList.getStatus(),detailsList.getTermscond());

    return pdpOfferData;
  }
}

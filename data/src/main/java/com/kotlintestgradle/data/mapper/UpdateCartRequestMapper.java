package com.kotlintestgradle.data.mapper;

import com.kotlintestgradle.model.ecom.common.ImageData;
import com.kotlintestgradle.model.ecom.home.CategoryOfferData;
import com.kotlintestgradle.model.ecom.pdp.PdpOfferData;
import com.kotlintestgradle.remote.model.response.ecom.common.ImagesDetails;
import com.kotlintestgradle.remote.model.response.ecom.pdp.OfferName;
import com.kotlintestgradle.remote.model.response.ecom.pdp.PdpOfferDetails;
import org.jetbrains.annotations.NotNull;

public class UpdateCartRequestMapper {

    @NotNull
    public PdpOfferDetails getOffer(PdpOfferData offer) {
        try {
            return new PdpOfferDetails(offer.getApplicableOnStatus(), convertToImages(offer.getImages()),
                    convertToOfferName(offer.getOfferName()), offer.getEndDateTimeISO(), offer.getEndDateTime(), offer.getGlobalClaimCount(),
                    offer.getStartDateTimeISO(), convertToWebImages(offer.getWebimages()), offer.getStartDateTime(), offer.getPerUserLimit(),
                    offer.getMinimumPurchaseQty(), offer.getStatusString(), offer.getOfferId(), offer.getDiscountType(), offer.getOfferFor(),
                    offer.getDiscountValue(), offer.getApplicableOn(), offer.getStatus());
        } catch (NullPointerException e) {
            return new PdpOfferDetails();
        }
    }

    @NotNull
    public PdpOfferDetails getOffer(CategoryOfferData offer) {
        try {
            return new PdpOfferDetails(offer.getApplicableOnStatus(), convertToImages(offer.getImages()),
                    convertToOfferName(offer.getOfferName()), String.valueOf(offer.getEndDateTimeISO()), offer.getEndDateTime(), offer.getGlobalClaimCount(),
                    offer.getStartDateTimeISO(), convertToWebImages(offer.getImages()), offer.getStartDateTime(), offer.getPerUserLimit(),
                    offer.getMinimumPurchaseQty(), offer.getStatusString(), offer.getOfferId(), String.valueOf(offer.getDiscountType()), offer.getOfferFor(),
                    offer.getDiscountValue(), offer.getApplicableOn(), offer.getStatus());
        } catch (NullPointerException e) {
            return new PdpOfferDetails();
        }
    }

    private ImagesDetails convertToImages(ImageData images) {
        return new ImagesDetails(images.getImageText(), images.getImage(), images.getThumbnail(),
                images.getMobile(), images.getDescription(), images.getMedium(), images.getKeyword());
    }

    private OfferName convertToOfferName(String offerName) {
        return new OfferName(offerName);
    }

    private ImagesDetails convertToWebImages(ImageData webimages) {
        return new ImagesDetails(webimages.getImageText(), webimages.getImage(), webimages.getThumbnail(),
                webimages.getMobile(), webimages.getDescription(), webimages.getMedium(), webimages.getKeyword());
    }
}

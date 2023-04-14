package chat.hola.com.app.ecom.review;

/**
 * interface call back listener for the rating and reviews
 */
public interface RatingOrReviewCallBack {
  /**
   * call back method for the rating or reviews
   * @param ratingType rating type whether you are giving rating or reviews
   * @param attributeId attribute id
   * @param rating how much rating you are giving
   */
  void ratingOrReview(int ratingType, int reviewType, String attributeId,
      double rating);
}

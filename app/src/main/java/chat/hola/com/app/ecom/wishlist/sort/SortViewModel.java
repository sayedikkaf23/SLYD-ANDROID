package chat.hola.com.app.ecom.wishlist.sort;

/*
 * Purpose – This class holds Sorting bottomSheet to Activity ViewModel interaction
 * @author 3Embed
 * Created on Nov 25, 2019
 * Modified on
 */
public interface SortViewModel {

  /**
   * This method is using to dispose the previously used single disposable observer
   */
  void setDisposableSingleObserver();

  /**
   * this method is using to apply sort to category, subCategory products
   *
   * @param catName    category Name
   * @param subCatName sub category name
   * @param sortType   sort type o be apply
   */
  void getSubCatFilterProduct(String catName, String subCatName, String subSubCatName, int sortType,
      String page, String symptomName);

  /**
   * this method is using to apply sort to Search products
   *
   * @param searchQuery searched word
   * @param sortType    sort type to be apply
   */
  void getSearchProduct(String searchQuery, int sortType);

  /**
   * This method is using to sort the wish list items
   *
   * @param type type is the position of selected sort item
   */
  void wishLIstSort(int type);
}
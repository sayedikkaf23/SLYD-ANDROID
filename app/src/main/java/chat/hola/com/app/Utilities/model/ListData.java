package chat.hola.com.app.Utilities.model;

/**
 * This class is using as light weight data transfer class
 */
public class ListData {
  private String mShoppingListId;
  private String mShoppingListName;
  private int action;

  public ListData(String shoppingListId, String shoppingListName) {
    mShoppingListId = shoppingListId;
    mShoppingListName = shoppingListName;
  }

  public ListData(String shoppingListId, String shoppingListName, int action) {
    mShoppingListId = shoppingListId;
    mShoppingListName = shoppingListName;
    this.action = action;
  }

  public int getAction() {
    return action;
  }

  public void setAction(int action) {
    this.action = action;
  }

  public String getShoppingListId() {
    return mShoppingListId;
  }

  public void setShoppingListId(String shoppingListId) {
    mShoppingListId = shoppingListId;
  }

  public String getShoppingListName() {
    return mShoppingListName;
  }

  public void setShoppingListName(String shoppingListName) {
    mShoppingListName = shoppingListName;
  }
}

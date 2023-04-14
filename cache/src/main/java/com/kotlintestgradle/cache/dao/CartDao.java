package com.kotlintestgradle.cache.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.kotlintestgradle.cache.entity.UserCart;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface CartDao {
  @Query("SELECT * FROM cart")
  List<UserCart> getAllCartItem();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertItemToCart(UserCart userCart);

  @Query("DELETE FROM cart WHERE productId = :productId")
  void deleteCartItemById(String productId);

  @Query("DELETE FROM cart")
  void clearCart();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertCartItems(ArrayList<UserCart> userCartArrayList);

  @Query("SELECT COUNT(*) FROM cart")
  int getRowCount();

  @Query("SELECT COUNT(*) FROM cart WHERE storeType = :storeType")
  int getRowCount(int storeType);

  @Query("SELECT COUNT(*) FROM cart WHERE storeType = :storeType AND productId = :productId")
  int getTotalProductCount(int storeType, String productId);

  @Query("SELECT COUNT(*) FROM cart WHERE productId = :productId")
  int getProductCount(String productId);

  @Update
  void update(UserCart userCart);

  @Query("SELECT quantity FROM cart WHERE productId = :productId")
  int getProductById(String productId);

}

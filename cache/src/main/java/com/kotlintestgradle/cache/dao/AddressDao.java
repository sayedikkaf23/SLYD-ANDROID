package com.kotlintestgradle.cache.dao;

import static com.kotlintestgradle.cache.DbConstants.DEFAULT_ADDRESS_VAL;
import static com.kotlintestgradle.cache.DbConstants.NON_DEFAULT_ADDRESS_VAL;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.kotlintestgradle.cache.entity.UserAddress;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface AddressDao {

  @Query("SELECT * FROM address")
  List<UserAddress> getAllAddress();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAddress(UserAddress userAddress);

  @Query("DELETE FROM address WHERE addressId = :addressId")
  void deleteByAddressId(String addressId);

  @Query("DELETE FROM address")
  void deleteAllAddresses();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAddresses(ArrayList<UserAddress> userAddress);

  @Query("SELECT * FROM address WHERE IsDefault=" + DEFAULT_ADDRESS_VAL)
  UserAddress getDefaultAddress();

  @Query("UPDATE address SET IsDefault=" + DEFAULT_ADDRESS_VAL + " WHERE addressId = :addressId")
  void makeAddressDefault(String addressId);

  @Query(
      "UPDATE address SET IsDefault=" + NON_DEFAULT_ADDRESS_VAL + " WHERE IsDefault ="
          + DEFAULT_ADDRESS_VAL)
  void makeAddressAsNonDefault();

  @Update
  void update(UserAddress userAddress);

  @Query("SELECT COUNT(*) FROM address")
  int getRowCount();

}

package com.kotlintestgradle.cache.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.kotlintestgradle.cache.entity.RecentAddress;
import java.util.List;

@Dao
public interface RecentAddressDao {

  @Query("SELECT * FROM recent_address")
  List<RecentAddress> getAllRecentAddress();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAddress(RecentAddress userCart);
}

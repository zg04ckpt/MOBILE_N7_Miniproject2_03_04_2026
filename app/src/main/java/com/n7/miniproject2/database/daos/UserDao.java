package com.n7.miniproject2.database.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.n7.miniproject2.entities.User;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("SELECT Users.id, Users.username, Users.avatarUrl FROM Users WHERE username = :username AND password = :password")
    User getUser(String username, String password);
}
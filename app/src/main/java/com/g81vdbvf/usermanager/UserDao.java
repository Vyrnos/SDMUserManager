package com.g81vdbvf.usermanager;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;


@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAll();

    /*@Query("SELECT * FROM user where gender LIKE :gender")
    User findByGender(String gender);

    @Query("SELECT * FROM user where nationality LIKE :nationality")
    User findByNationality(String nationality);

    @Query("SELECT COUNT(*) from user")
    int countUsers();*/

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);

    @Update
    void update(User user);

    @Query("DELETE FROM user WHERE 1=1")
    void deleteAll();
}

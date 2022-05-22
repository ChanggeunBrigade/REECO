package com.example.reeco;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ServerDao {
    @Insert
    void insertAll(Server... servers);

    @Insert
    void insert(Server server);

    @Update
    void update(Server server);

    @Delete
    void delete(Server server);

    @Query("SELECT * FROM servers")
    List<Server> getServers();

    @Query("SELECT * FROM servers where name = :name")
    List<Server> findServersByName(String name);
}

package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task")
    List<Task> getAll();

    @Query("SELECT * FROM task WHERE id IN (:taskIds)")
    List<Task> loadAllByIds(int[] taskIds);

    @Query("SELECT * FROM task WHERE id LIKE :id LIMIT 1")
    Task findById(String id);

    @Query("SELECT * FROM task WHERE title LIKE :title LIMIT 1")
    Task findByTitle(String title);

    @Query("SELECT * FROM task WHERE state LIKE :state LIMIT 1")
    Task findByState(String state);

    @Insert
    void saveTask(Task task);
//    void insertAll(Task... tasks);

    @Delete
    void delete(Task task);
}

package com.rubyumbra.todolist.repository

import com.rubyumbra.todolist.model.TodoTask
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface TodoRepository : JpaRepository<TodoTask, Int> {
    @Modifying
    @Transactional
    @Query("update TodoTask set isCompleted = :isCompleted where id = :id")
    fun updateStatus(@Param("id") id: Int,
                     @Param("isCompleted") isCompleted: Boolean)

    @Modifying
    @Transactional
    @Query("delete from TodoTask where listId = :listId")
    fun deleteTasks(@Param("listId") listId: Int)
}

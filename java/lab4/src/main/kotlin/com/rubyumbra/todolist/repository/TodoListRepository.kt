package com.rubyumbra.todolist.repository

import com.rubyumbra.todolist.model.TodoList
import org.springframework.data.jpa.repository.JpaRepository

interface TodoListRepository : JpaRepository<TodoList, Int>

package com.rubyumbra.todolist.model

import javax.persistence.*

@Entity
@Table(name = "lists")
class TodoList(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int = 0,

        @Column(name = "title")
        val title: String = "",

        @OneToMany @JoinColumn(name = "list_id") @OrderBy("id")
        val todos: MutableList<TodoTask> = mutableListOf()
)

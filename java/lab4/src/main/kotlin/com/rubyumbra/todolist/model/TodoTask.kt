package com.rubyumbra.todolist.model

import javax.persistence.*

@Entity
@Table(name = "todos")
class TodoTask(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int = 0,

        @Column(name = "list_id")
        val listId: Int = 0,

        @Column(name = "description")
        val description: String = "",

        @Column(name = "is_completed")
        val isCompleted: Boolean = false
)

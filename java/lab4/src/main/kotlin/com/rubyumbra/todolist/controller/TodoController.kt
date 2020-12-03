package com.rubyumbra.todolist.controller

import com.rubyumbra.todolist.model.TodoList
import com.rubyumbra.todolist.model.TodoTask
import com.rubyumbra.todolist.repository.TodoListRepository
import com.rubyumbra.todolist.repository.TodoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class TodoController @Autowired constructor(
        val todoListRepository: TodoListRepository,
        val todoRepository: TodoRepository
) {
    @RequestMapping("/")
    fun root(model: Model): String {
        model["lists"] = todoListRepository.findAll()
        return "index"
    }

    @PostMapping("/createList")
    fun createList(@RequestParam("title") title: String): String {
        todoListRepository.save(TodoList(title = title))
        return "redirect:/"
    }

    @PostMapping("/deleteList")
    fun deleteList(@RequestParam("listId") listId: Int): String {
        todoRepository.deleteTasks(listId)
        todoListRepository.deleteById(listId)
        return "redirect:/"
    }

    @PostMapping("/changeStatus")
    fun changeStatus(@RequestParam("id") id: Int,
                     @RequestParam("status") status: Boolean): String {
        todoRepository.updateStatus(id, status)
        return "redirect:/"
    }

    @PostMapping("/createTask")
    fun createTask(@RequestParam("listId") listId: Int,
                   @RequestParam("description") description: String
    ): String {
        todoRepository.save(TodoTask(listId = listId, description = description))
        return "redirect:/"
    }

    @PostMapping("/deleteTask")
    fun deleteTask(@RequestParam("id") id: Int): String {
        todoRepository.deleteById(id)
        return "redirect:/"
    }
}

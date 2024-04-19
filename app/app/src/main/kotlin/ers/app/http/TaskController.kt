package ers.app.http

import ers.app.domainEntities.Either
import ers.app.service.TaskService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

data class TaskInputModel(val name: String, val userId: Int, val robotId: Int)
data class TaskUpdateInputModel(val status: String)


@RestController
@RequestMapping("/task")
class TaskController(private val taskService: TaskService) {

    //Não deviamos passar o status da task?
    @PostMapping
    fun createTask(@RequestBody task: TaskInputModel): ResponseEntity<*> {
        return when (val res = taskService.createTask(task.name, task.userId, task.robotId)) {
            is Either.Right -> ResponseEntity.status(201).body(res.value)
            is Either.Left -> ResponseEntity.status(409).body(res.value)
        }
    }

    @GetMapping(PathTemplate.ID)
    fun getTaskByID(@PathVariable id: Int): ResponseEntity<*> {
        return when (val res = taskService.getTaskByID(id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> ResponseEntity.badRequest().body(res.value)
        }
    }

    @PostMapping(PathTemplate.UPDATE)
    fun updateTask(@PathVariable id: Int, @RequestBody status: TaskUpdateInputModel): ResponseEntity<*> {
        return when (val res = taskService.updateTask(id, status.status)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> ResponseEntity.badRequest().body(res.value)
        }

    }

    @DeleteMapping(PathTemplate.ID)
    fun deleteTask(@PathVariable id: Int): ResponseEntity<*> {
        return when (val res = taskService.deleteTask(id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> ResponseEntity.badRequest().body(res.value)
        }
    }

    //Fica em que file?
    @GetMapping(PathTemplate.USER_ID)
    fun getTasksByUserId(@PathVariable id: Int, @RequestParam offset: Int, @RequestParam limit: Int): ResponseEntity<*> {
        return when (val res = taskService.getTasksByUserId(offset, limit, id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> ResponseEntity.badRequest().body(res.value)
        }
    }

    //Fica em que file?
    @GetMapping(PathTemplate.ROBOT_TASKS)
    fun getTasksByRobotId(@PathVariable id: Int, @RequestParam offset: Int, @RequestParam limit: Int): ResponseEntity<*> {
        return when (val res = taskService.getTasksByRobotId(offset, limit, id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> ResponseEntity.badRequest().body(res.value)
        }
    }

}
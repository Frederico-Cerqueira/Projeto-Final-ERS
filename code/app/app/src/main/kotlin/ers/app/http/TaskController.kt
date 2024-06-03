package ers.app.http

import ers.app.domainEntities.Either
import ers.app.domainEntities.inputModels.TaskInputModel
import ers.app.domainEntities.inputModels.TaskUpdateInputModel
import ers.app.service.TaskService
import ers.app.utils.Errors
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/task")
class TaskController(private val taskService: TaskService) {

    //Não deviamos passar o status da task?
    @PostMapping
    fun createTask(@RequestBody task: TaskInputModel): ResponseEntity<*> {
        return when (val res = taskService.createTask(task.name, task.userID, task.robotID)) {
            is Either.Right -> ResponseEntity.status(201).body(res.value)
            is Either.Left -> when (res.value) {
                Errors.InvalidInput -> ResponseEntity.badRequest().body(res.value)
                Errors.InputTooLong -> ResponseEntity.status(413).body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @GetMapping(PathTemplate.ID)
    fun getTaskByID(@PathVariable id: Int): ResponseEntity<*> {
        return when (val res = taskService.getTaskByID(id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Errors.TaskNotFound -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @PostMapping(PathTemplate.UPDATE)
    fun updateTask(@PathVariable id: Int, @RequestBody status: TaskUpdateInputModel): ResponseEntity<*> {
        return when (val res = taskService.updateTask(id, status.status)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Errors.InvalidInput -> ResponseEntity.badRequest().body(res.value)
                Errors.TaskNotFound -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }

    }

    @DeleteMapping(PathTemplate.ID)
    fun deleteTask(@PathVariable id: Int): ResponseEntity<*> {
        return when (val res = taskService.deleteTask(id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Errors.TaskNotFound -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @GetMapping(PathTemplate.USER_ID)
    fun getTasksByUserID(
        @PathVariable id: Int,
        @RequestParam offset: Int,
        @RequestParam limit: Int
    ): ResponseEntity<*> {
        return when (val res = taskService.getTasksByUserID(offset, limit, id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Errors.UserNotFound -> ResponseEntity.badRequest().body(res.value)
                Errors.InvalidInput -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @GetMapping(PathTemplate.ROBOT_TASKS)
    fun getTasksByRobotID(
        @PathVariable id: Int,
        @RequestParam offset: Int,
        @RequestParam limit: Int
    ): ResponseEntity<*> {
        return when (val res = taskService.getTasksByRobotID(offset, limit, id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Errors.RobotNotFound -> ResponseEntity.badRequest().body(res.value)
                Errors.InvalidInput -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }
}
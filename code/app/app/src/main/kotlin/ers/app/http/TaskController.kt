package ers.app.http

import ers.app.domainEntities.AuthenticatedUser
import ers.app.domainEntities.inputModels.TaskInputModel
import ers.app.domainEntities.inputModels.TaskUpdateInputModel
import ers.app.service.TaskService
import ers.app.utils.Handler
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/task")
class TaskController(private val taskService: TaskService) {

    //Não deviamos passar o status da task?
    @PostMapping
    fun createTask(@RequestBody task: TaskInputModel, user: AuthenticatedUser): ResponseEntity<*> {
        return Handler().responseHandler(taskService.createTask(task.name, task.userID, task.robotID), 201)
    }

    @GetMapping(PathTemplate.ID)
    fun getTaskByID(@PathVariable id: Int, user: AuthenticatedUser): ResponseEntity<*> {
        return Handler().responseHandler(taskService.getTaskByID(id), 200)
    }

    @PostMapping(PathTemplate.UPDATE)
    fun updateTask(
        @PathVariable id: Int,
        @RequestBody status: TaskUpdateInputModel,
        user: AuthenticatedUser
    ): ResponseEntity<*> {
        return Handler().responseHandler(taskService.updateTask(id, status.status), 200)
    }

    @DeleteMapping(PathTemplate.ID)
    fun deleteTask(@PathVariable id: Int, user: AuthenticatedUser): ResponseEntity<*> {
        return Handler().responseHandler(taskService.deleteTask(id), 200)
    }

    @GetMapping(PathTemplate.USER_ID)
    fun getTasksByUserID(
        @PathVariable id: Int,
        @RequestParam offset: Int,
        @RequestParam limit: Int,
        user: AuthenticatedUser
    ): ResponseEntity<*> {
        return Handler().responseHandler(taskService.getTasksByUserID(offset, limit, id), 200)
    }

    @GetMapping(PathTemplate.ROBOT_TASKS)
    fun getTasksByRobotID(
        @PathVariable id: Int,
        @RequestParam offset: Int,
        @RequestParam limit: Int,
        user: AuthenticatedUser
    ): ResponseEntity<*> {
        return Handler().responseHandler(taskService.getTasksByRobotID(offset, limit, id), 200)
    }

    @GetMapping(PathTemplate.START)
    fun startTask(@PathVariable id: Int, user: AuthenticatedUser): ResponseEntity<*> {
        return Handler().responseHandler(taskService.startTask(id), 200)
    }

    @GetMapping(PathTemplate.STOP)
    fun stopTask(@PathVariable id: Int, user: AuthenticatedUser): ResponseEntity<*> {
        return Handler().responseHandler(taskService.stopTask(id), 200)
    }
}
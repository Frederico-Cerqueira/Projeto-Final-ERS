package ers.app.http

import ers.app.domainEntities.AuthenticatedUser
import ers.app.domainEntities.inputModels.TimeInputModel
import ers.app.domainEntities.inputModels.TimeUpdateDescriptionInputModel
import ers.app.domainEntities.inputModels.TimeUpdateInputModel
import ers.app.service.TimeService
import ers.app.utils.Handler
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/time")
class TimeController(private val timeService: TimeService) {

    @PostMapping(PathTemplate.CREATE_WITH_TASK)
    fun createTime(@PathVariable taskID: Int, @RequestBody time: TimeInputModel, user: AuthenticatedUser): ResponseEntity<*> {
        return Handler().responseHandler(timeService.createTime(taskID, time.startTime, time.endTime, time.weekDay, time.description), 201)
    }

    @GetMapping(PathTemplate.ID)
    fun getTimeByID(@PathVariable id: Int, user: AuthenticatedUser): ResponseEntity<*> {
        return Handler().responseHandler(timeService.getTimeByID(id), 200)
    }

    @PostMapping(PathTemplate.UPDATE)
    fun updateTime(@PathVariable id: Int, @RequestBody time: TimeUpdateInputModel,user: AuthenticatedUser): ResponseEntity<*> {
        return Handler().responseHandler(timeService.updateTime(id, time.startTime, time.endTime, time.weekDay), 200)
    }

    @PostMapping(PathTemplate.UPDATE_DESCRIPTION)
    fun updateTimeDescription(
        @PathVariable id: Int,
        @RequestBody description: TimeUpdateDescriptionInputModel,
        user: AuthenticatedUser
    ): ResponseEntity<*> {
        return Handler().responseHandler(timeService.updateTimeDescription(id, description.description), 200)
    }

    @DeleteMapping(PathTemplate.ID)
    fun deleteTime(@PathVariable id: Int, user: AuthenticatedUser): ResponseEntity<*> {
        return Handler().responseHandler(timeService.deleteTime(id), 200)
    }

    @GetMapping(PathTemplate.TASK_ID)
    fun getTimesByTaskID(
        @PathVariable id: Int,
        @RequestParam offset: Int,
        @RequestParam limit: Int,
        user: AuthenticatedUser
    ): ResponseEntity<*> {
        return Handler().responseHandler(timeService.getTimesByTaskId(offset, limit, id), 200)
    }
}
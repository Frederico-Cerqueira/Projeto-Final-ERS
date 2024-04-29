package ers.app.http

import ers.app.domainEntities.Either
import ers.app.domainEntities.inputModels.TimeInputModel
import ers.app.domainEntities.inputModels.TimeUpdateDescriptionInputModel
import ers.app.domainEntities.inputModels.TimeUpdateInputModel
import ers.app.service.TimeService
import ers.app.utils.Error
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/time")
class TimeController(private val timeService: TimeService) {

    @PostMapping(PathTemplate.CREATE_WITH_TASK)
    fun createTime(@PathVariable taskID: Int, @RequestBody time: TimeInputModel): ResponseEntity<*> {
        return when (val res =
            timeService.createTime(taskID, time.startTime, time.endTime, time.weekDay, time.description)) {
            is Either.Right -> ResponseEntity.status(201).body(res.value)
            is Either.Left -> when (res.value) {
                Error.InvalidInput -> ResponseEntity.badRequest().body(res.value)
                Error.InputTooLong -> ResponseEntity.status(413).body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @GetMapping(PathTemplate.ID)
    fun getTimeByID(@PathVariable id: Int): ResponseEntity<*> {
        return when (val res = timeService.getTimeByID(id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Error.TimeNotFound -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @PostMapping(PathTemplate.UPDATE)
    fun updateTime(@PathVariable id: Int, @RequestBody time: TimeUpdateInputModel): ResponseEntity<*> {
        return when (val res = timeService.updateTime(id, time.startTime, time.endTime, time.weekDay)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Error.InvalidInput -> ResponseEntity.badRequest().body(res.value)
                Error.TimeNotFound -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }

    }

    @PostMapping(PathTemplate.UPDATE_DESCRIPTION)
    fun updateTimeDescription(
        @PathVariable id: Int,
        @RequestBody description: TimeUpdateDescriptionInputModel
    ): ResponseEntity<*> {
        return when (val res = timeService.updateTimeDescription(id, description.description)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Error.InvalidInput -> ResponseEntity.badRequest().body(res.value)
                Error.TimeNotFound -> ResponseEntity.badRequest().body(res.value)
                Error.InputTooLong -> ResponseEntity.status(413).body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @DeleteMapping(PathTemplate.ID)
    fun deleteTime(@PathVariable id: Int): ResponseEntity<*> {
        return when (val res = timeService.deleteTime(id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Error.TimeNotFound -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @GetMapping(PathTemplate.TASK_ID)
    fun getTimesByTaskID(
        @PathVariable id: Int,
        @RequestParam offset: Int,
        @RequestParam limit: Int
    ): ResponseEntity<*> {
        return when (val res = timeService.getTimesByTaskId(offset, limit, id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Error.TaskNotFound -> ResponseEntity.badRequest().body(res.value)
                Error.InvalidInput -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }
}
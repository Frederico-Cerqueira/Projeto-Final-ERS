package ers.app.http

import ers.app.domainEntities.Either
import ers.app.domainEntities.inputModels.RobotInputModel
import ers.app.domainEntities.inputModels.RobotUpdateInputModel
import ers.app.service.RobotService
import ers.app.utils.Error
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/robot")
class RobotController(private val robotService: RobotService) {

    @PostMapping
    fun createRobot(@RequestBody robot: RobotInputModel): ResponseEntity<*> {
        return when (val res = robotService.createRobot(robot.name, robot.characteristics)) {
            is Either.Right -> ResponseEntity.status(201).body(res.value)
            is Either.Left -> when (res.value) {
                Error.InvalidInput -> ResponseEntity.badRequest().body(res.value)
                Error.InputTooLong -> ResponseEntity.status(413).body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @GetMapping(PathTemplate.ID)
    fun getRobotByID(@PathVariable id: Int): ResponseEntity<*> {
        return when (val res = robotService.getRobotByID(id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Error.RobotNotFound -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @PostMapping(PathTemplate.UPDATE)
    fun updateRobotStatus(@PathVariable id: Int, @RequestBody status: RobotUpdateInputModel): ResponseEntity<*> {
        return when (val res = robotService.updateRobotStatus(id, status.status)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Error.InvalidInput -> ResponseEntity.badRequest().body(res.value)
                Error.RobotNotFound -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @DeleteMapping(PathTemplate.ID)
    fun deleteRobot(@PathVariable id: Int): ResponseEntity<*> {
        return when (val res = robotService.deleteRobot(id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Error.RobotNotFound -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @GetMapping(PathTemplate.USER_ID)
    fun getRobotByUserID(
        @PathVariable id: Int,
        @RequestParam offset: Int,
        @RequestParam limit: Int
    ): ResponseEntity<*> {
        return when (val res = robotService.getRobotByUserID(offset, limit, id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Error.UserNotFound -> ResponseEntity.badRequest().body(res.value)
                Error.InvalidInput -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }
}
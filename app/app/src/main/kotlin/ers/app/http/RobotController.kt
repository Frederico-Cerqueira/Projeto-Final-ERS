package ers.app.http

import ers.app.domainEntities.Either
import ers.app.service.RobotService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

data class RobotInputModel(val name: String, val characteristics: String)
data class RobotUpdateInputModel(val status: String)

@RestController
@RequestMapping("/robot")
class RobotController(private val robotService: RobotService) {

    @PostMapping
    fun createRobot(@RequestBody robot: RobotInputModel): ResponseEntity<*> {
        return when (val res = robotService.createRobot(robot.name, robot.characteristics)) {
            is Either.Right -> ResponseEntity.status(201).body(res.value)
            is Either.Left -> ResponseEntity.status(409).body(res.value)
        }
    }

    @GetMapping(PathTemplate.ID)
    fun getRobotById(@PathVariable id: Int): ResponseEntity<*> {
        return when (val res = robotService.getRobotById(id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> ResponseEntity.badRequest().body(res.value)
        }
    }

    @PostMapping(PathTemplate.UPDATE)
    fun updateRobotStatus(@PathVariable id: Int, @RequestBody status: RobotUpdateInputModel): ResponseEntity<*> {
        return when (val res = robotService.updateRobotStatus(id, status.status)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> ResponseEntity.badRequest().body(res.value)
        }
    }

    @DeleteMapping(PathTemplate.ID)
    fun deleteRobot(@PathVariable id: Int): ResponseEntity<*> {
        return when (val res = robotService.deleteRobot(id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> ResponseEntity.badRequest().body(res.value)
        }
    }

    //Fica em que file?
    @GetMapping(PathTemplate.USER_ID)
    fun getRobotByUserId(
        @PathVariable id: Int,
        @RequestParam offset: Int,
        @RequestParam limit: Int
    ): ResponseEntity<*> {
        return when (val res = robotService.getRobotByUserId(offset, limit, id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> ResponseEntity.badRequest().body(res.value)
        }
    }
}
package ers.app.http

import ers.app.domainEntities.AuthenticatedUser
import ers.app.domainEntities.inputModels.RobotInputModel
import ers.app.domainEntities.inputModels.RobotUpdateInputModel
import ers.app.service.RobotService
import ers.app.utils.Handler
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/robot")
class RobotController(private val robotService: RobotService) {

    @PostMapping
    fun createRobot(@RequestBody robot: RobotInputModel, user: AuthenticatedUser): ResponseEntity<*> {
        return Handler().responseHandler(robotService.createRobot(robot.name, robot.characteristics),201)
    }

    @GetMapping
    fun getRobots (@RequestParam offset: Int, @RequestParam limit: Int, user: AuthenticatedUser): ResponseEntity<*> {
        return Handler().responseHandler(robotService.getRobots(offset, limit),200)
    }

    @GetMapping(PathTemplate.ID)
    fun getRobotByID(@PathVariable id: Int, user: AuthenticatedUser): ResponseEntity<*> {
        return Handler().responseHandler(robotService.getRobotByID(id),200)
    }

    @PostMapping(PathTemplate.UPDATE)
    fun updateRobotStatus(@PathVariable id: Int, @RequestBody status: RobotUpdateInputModel, user: AuthenticatedUser): ResponseEntity<*> {
        return Handler().responseHandler(robotService.updateRobotStatus(id, status.status),200)
    }

    @DeleteMapping(PathTemplate.ID)
    fun deleteRobot(@PathVariable id: Int, user: AuthenticatedUser): ResponseEntity<*> {
        return Handler().responseHandler(robotService.deleteRobot(id),200)
    }

    @GetMapping(PathTemplate.USER_ID)
    fun getRobotByUserID(
        @PathVariable id: Int,
        @RequestParam offset: Int,
        @RequestParam limit: Int,
        user: AuthenticatedUser
    ): ResponseEntity<*> {
        return Handler().responseHandler(robotService.getRobotByUserID(offset, limit, id),200)
    }
}
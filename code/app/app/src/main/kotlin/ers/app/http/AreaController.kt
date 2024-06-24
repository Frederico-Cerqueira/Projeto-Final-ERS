package ers.app.http

import ers.app.domainEntities.AuthenticatedUser
import ers.app.domainEntities.inputModels.AreaInputModel
import ers.app.domainEntities.inputModels.AreaUpdateDescriptionInputModel
import ers.app.domainEntities.inputModels.AreaUpdateInputModel
import ers.app.service.AreaService
import ers.app.utils.Handler
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/area")
class AreaController(private val areaService: AreaService) {
    @PostMapping(PathTemplate.CREATE_WITH_TASK)
    fun createArea(@PathVariable taskID: Int, @RequestBody area: AreaInputModel, user: AuthenticatedUser): ResponseEntity<*> {
       return Handler().responseHandler(
           areaService.createArea(area.height, area.width, taskID, area.name, area.description),
           201)
    }

    @GetMapping(PathTemplate.ID)
    fun getAreaByID(@PathVariable id: Int, user: AuthenticatedUser): ResponseEntity<*> {
        return Handler().responseHandler(areaService.getAreaByID(id), 200)
    }

    @PostMapping(PathTemplate.UPDATE)
    fun updateArea(@PathVariable id: Int, @RequestBody area: AreaUpdateInputModel, user: AuthenticatedUser): ResponseEntity<*> {
        return Handler().responseHandler(areaService.updateArea(id, area.height, area.width), 200)
    }

    @PostMapping(PathTemplate.UPDATE_DESCRIPTION)
    fun updateAreaDescription(
        @PathVariable id: Int,
        @RequestBody description: AreaUpdateDescriptionInputModel,
        user: AuthenticatedUser
    ): ResponseEntity<*> {
        return Handler().responseHandler(areaService.updateAreaDescription(id, description.description), 200)
    }

    @DeleteMapping(PathTemplate.ID)
    fun deleteArea(@PathVariable id: Int, user: AuthenticatedUser): ResponseEntity<*> {
        return Handler().responseHandler(areaService.deleteArea(id), 200)
    }

    @GetMapping(PathTemplate.TASK_ID)
    fun getAreasByTaskID(
        @PathVariable id: Int,
        @RequestParam offset: Int,
        @RequestParam limit: Int,
        user: AuthenticatedUser
    ): ResponseEntity<*> {
        return Handler().responseHandler(areaService.getAreasByTaskID(offset, limit, id), 200)
    }
}
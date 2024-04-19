package ers.app.http

import ers.app.domainEntities.Either
import ers.app.service.AreaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

data class AreaInputModel(val height: Int, val width: Int, val name: String, val description: String)
data class AreaUpdateInputModel(val height: Int, val width: Int)
data class AreaUpdateDescriptionInputModel(val description: String)

@RestController
@RequestMapping("/area")
class AreaController(private val areaService: AreaService) {
    @PostMapping(PathTemplate.CREATE_AREA)
    fun createArea(@PathVariable taskID: Int, @RequestBody area: AreaInputModel): ResponseEntity<*> {
        return when (val res = areaService.createArea(area.height, area.width, taskID, area.name, area.description)) {
            is Either.Right -> ResponseEntity.status(201).body(res.value)
            is Either.Left -> ResponseEntity.status(409).body(res.value)
        }
    }
    @GetMapping(PathTemplate.ID)
    fun getAreaById(@PathVariable id: Int): ResponseEntity<*> {
        return when (val res = areaService.getAreaById(id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> ResponseEntity.badRequest().body(res.value)
        }
    }


    @PostMapping(PathTemplate.UPDATE)
    fun updateArea(@PathVariable id: Int, @RequestBody area: AreaUpdateInputModel): ResponseEntity<*> {
        return when (val res = areaService.updateArea(id, area.height, area.width)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> ResponseEntity.badRequest().body(res.value)
        }
    }

    @PostMapping(PathTemplate.UPDATE_DESCRIPTION)
    fun updateAreaDescription(@PathVariable id: Int, @RequestBody description: AreaUpdateDescriptionInputModel): ResponseEntity<*> {
        return when (val res = areaService.updateAreaDescription(id, description.description)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> ResponseEntity.badRequest().body(res.value)
        }
    }

    @DeleteMapping(PathTemplate.ID)
    fun deleteArea(@PathVariable id: Int): ResponseEntity<*> {
        return when (val res = areaService.deleteArea(id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> ResponseEntity.badRequest().body(res.value)
        }
    }

    @GetMapping(PathTemplate.TASK_ID)
    fun getAreasByTaskId(@PathVariable id: Int, @RequestParam offset: Int, @RequestParam limit: Int): ResponseEntity<*> {
        return when (val res = areaService.getAreasByTaskId(offset, limit, id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> ResponseEntity.badRequest().body(res.value)
        }
    }

}
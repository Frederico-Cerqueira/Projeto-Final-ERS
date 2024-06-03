package ers.app.http

import ers.app.domainEntities.Either
import ers.app.domainEntities.inputModels.AreaInputModel
import ers.app.domainEntities.inputModels.AreaUpdateDescriptionInputModel
import ers.app.domainEntities.inputModels.AreaUpdateInputModel
import ers.app.service.AreaService
import ers.app.utils.Errors
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/area")
class AreaController(private val areaService: AreaService) {
    @PostMapping(PathTemplate.CREATE_WITH_TASK)
    fun createArea(@PathVariable taskID: Int, @RequestBody area: AreaInputModel): ResponseEntity<*> {
        return when (val res = areaService.createArea(area.height, area.width, taskID, area.name, area.description)) {
            is Either.Right -> ResponseEntity.status(201).body(res.value)
            is Either.Left -> when (res.value) {
                Errors.InvalidInput -> ResponseEntity.badRequest().body(res.value)
                Errors.InputTooLong -> ResponseEntity.status(413).body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @GetMapping(PathTemplate.ID)
    fun getAreaByID(@PathVariable id: Int): ResponseEntity<*> {
        return when (val res = areaService.getAreaByID(id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Errors.AreaNotFound -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @PostMapping(PathTemplate.UPDATE)
    fun updateArea(@PathVariable id: Int, @RequestBody area: AreaUpdateInputModel): ResponseEntity<*> {
        return when (val res = areaService.updateArea(id, area.height, area.width)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Errors.InvalidInput -> ResponseEntity.badRequest().body(res.value)
                Errors.AreaNotFound -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @PostMapping(PathTemplate.UPDATE_DESCRIPTION)
    fun updateAreaDescription(
        @PathVariable id: Int,
        @RequestBody description: AreaUpdateDescriptionInputModel
    ): ResponseEntity<*> {
        return when (val res = areaService.updateAreaDescription(id, description.description)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Errors.InvalidInput -> ResponseEntity.badRequest().body(res.value)
                Errors.AreaNotFound -> ResponseEntity.badRequest().body(res.value)
                Errors.InputTooLong -> ResponseEntity.status(413).body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @DeleteMapping(PathTemplate.ID)
    fun deleteArea(@PathVariable id: Int): ResponseEntity<*> {
        return when (val res = areaService.deleteArea(id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Errors.AreaNotFound -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @GetMapping(PathTemplate.TASK_ID)
    fun getAreasByTaskID(
        @PathVariable id: Int,
        @RequestParam offset: Int,
        @RequestParam limit: Int
    ): ResponseEntity<*> {
        return when (val res = areaService.getAreasByTaskID(offset, limit, id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Errors.AreaNotFound -> ResponseEntity.badRequest().body(res.value)
                Errors.InvalidInput -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }
}
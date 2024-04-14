package ers.app.repo.data.areaData

import ers.app.repo.dtos.AreaDto
import ers.app.repo.mappers.AreaDtoMapper
import org.jdbi.v3.core.Handle


/**
 * Class that implements the AreaDataI interface and is responsible for the operations related to the area table in the database.
 */
class AreaData (private val handle: Handle) : AreaDataI {

    //PROVAVELMENTE NÃO IRÁ RETORNAR UM DTO MAS UMA AREA NO FUTURO

    /**
     * Function that gets an area by its id.
     * @param id the id of the area.
     * @return the area with the id passed as parameter.
     */
    override fun getAreaById(id: Int): AreaDto? {
        val area = handle.createQuery("SELECT * FROM area WHERE id = :id")
            .bind("id", id)
            .map(AreaDtoMapper())
            .singleOrNull()
        return area
    }

    /**
     * Function that creates an area in the database.
     * @param height the height of the area.
     * @param width the width of the area.
     * @param taskId the id of the task that the area is related to.
     * @return the area created.
     */
    override fun createArea(height : Int, width: Int, taskId : Int): AreaDto? {
        val newArea =
            handle.createUpdate("INSERT INTO area (height, width, taskId) VALUES (:height, :width, :taskId)")
                .bind("height", height)
                .bind("width", width)
                .bind("taskId", taskId)
                .executeAndReturnGeneratedKeys()
                .map(AreaDtoMapper())
                .singleOrNull()
        return newArea
    }

    /**
     * Function that updates the height and width of an area.
     * @param id the id of the area.
     * @param height the new height of the area.
     * @param width the new width of the area.
     * @return the area with the updated height and width.
     */
    override fun updateArea(id: Int, height: Int, width: Int): AreaDto? {
        val area = handle.createUpdate("UPDATE area SET height = :height, width = :width WHERE id = :id")
            .bind("height", height)
            .bind("width", width)
            .bind("id", id)
            .executeAndReturnGeneratedKeys()
            .map(AreaDtoMapper())
            .singleOrNull()
        return area
    }

    /**
     * Function that gets an area by the id of the task that it is related to.
     * @param taskId the id of the task.
     * @return the area related to the task with the id passed as parameter.
     */
    override fun getAreaByTaskId(taskId: Int): AreaDto? {
        val area = handle.createQuery("SELECT * FROM area WHERE taskId = :taskId")
            .bind("taskId", taskId)
            .map(AreaDtoMapper())
            .singleOrNull()
        return area
    }

    /**
     * Function that deletes an area by its id.
     * @param id the id of the area.
     */
    override fun deleteArea(id: Int) {
        handle.createUpdate("DELETE FROM area WHERE id = :id")
            .bind("id", id)
            .execute()
    }
}
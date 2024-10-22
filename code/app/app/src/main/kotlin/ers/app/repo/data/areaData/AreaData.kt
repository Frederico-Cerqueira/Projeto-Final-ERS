package ers.app.repo.data.areaData

import ers.app.repo.dtos.AreaDto
import ers.app.repo.mappers.AreaDtoMapper
import org.jdbi.v3.core.Handle


/**
 * Class that implements the AreaDataI interface and is responsible for the operations related to the area table in the database.
 */
class AreaData (private val handle: Handle) : AreaDataI {

    /**
     * Function that gets an area by its id.
     * @param id the id of the area.
     * @return the area with the id passed as parameter.
     */
    override fun getAreaByID(id: Int): AreaDto? {
        return handle.createQuery("SELECT * FROM area WHERE id = :id")
            .bind("id", id)
            .map(AreaDtoMapper())
            .singleOrNull()
    }

    /**
     * Function that creates an area in the database.
     * @param height the height of the area.
     * @param width the width of the area.
     * @param taskID the id of the task that the area is related to.
     * @param name the name of the area.
     * @param description the description of the area.
     * @return the area created.
     */
    override fun createArea(height: Int, width: Int, taskID: Int, name: String, description: String): AreaDto {
        return handle.createUpdate("INSERT INTO area (height, width, taskId, name, description) VALUES (:height, :width, :taskId, :name, :description)")
            .bind("height", height)
            .bind("width", width)
            .bind("taskId", taskID)
            .bind("name", name)
            .bind("description", description)
            .executeAndReturnGeneratedKeys()
            .map(AreaDtoMapper())
            .first()
    }

    /**
     * Function that updates the height and width of an area.
     * @param id the id of the area.
     * @param height the new height of the area.
     * @param width the new width of the area.
     * @return the area with the updated height and width.
     */
    override fun updateArea(id: Int, height: Int, width: Int): AreaDto {
        return handle.createUpdate("UPDATE area SET height = :height, width = :width WHERE id = :id")
            .bind("height", height)
            .bind("width", width)
            .bind("id", id)
            .executeAndReturnGeneratedKeys()
            .map(AreaDtoMapper())
            .first()
    }

    /**
     * Function that updates the description of an area.
     * @param id the id of the area.
     * @param description the new description of the area.
     * @return the area with the updated description.
     */
    override fun updateAreaDescription(id: Int, description: String): AreaDto {
        return handle.createUpdate("UPDATE area SET description = :description WHERE id = :id")
            .bind("description", description)
            .bind("id", id)
            .executeAndReturnGeneratedKeys()
            .map(AreaDtoMapper())
            .first()
    }

    /**
     * Function that gets a list of areas by the id of the task that they are related to with pagination.
     * @param offset the number of areas to skip.
     * @param limit the maximum number of areas to get.
     * @param taskID the id of the task that the areas are related to.
     * @return a list of areas related to the task.
     */
    override fun getAreasByTaskID(offset: Int, limit: Int, taskID: Int): List<AreaDto> {
        return handle.createQuery("SELECT * FROM area WHERE taskId = :taskId ORDER BY id LIMIT :limit OFFSET :offset")
            .bind("taskId", taskID)
            .bind("limit", limit)
            .bind("offset", offset)
            .map(AreaDtoMapper())
            .list()
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
package ers.app.repo.data.areaData

import ers.app.repo.dtos.AreaDto

/**
 * Interface that defines the operations related to the area table in the database.
 */
interface AreaDataI {
    fun createArea(height : Int, width: Int, taskId : Int, name: String, description: String): AreaDto
    fun getAreaById(id: Int): AreaDto?
    fun updateArea(id: Int, height: Int, width: Int): AreaDto
    fun updateAreaDescription(id: Int, description: String): AreaDto
    fun deleteArea(id: Int)
    fun getAreasByTaskId(offset: Int, limit: Int, taskId: Int): List<AreaDto>
}
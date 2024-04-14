package ers.app.repo.data.areaData

import ers.app.repo.dtos.AreaDto

/**
 * Interface that defines the operations related to the area table in the database.
 */
interface AreaDataI {
    fun getAreaById(id: Int): AreaDto?
    fun createArea(height : Int, width: Int, taskId : Int): AreaDto?
    fun updateArea(id: Int, height: Int, width: Int): AreaDto?
    fun getAreaByTaskId(taskId: Int): AreaDto?
    fun deleteArea(id: Int)
}
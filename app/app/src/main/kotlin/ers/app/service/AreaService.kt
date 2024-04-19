package ers.app.service

import ers.app.domainEntities.*
import ers.app.repo.dtos.AreaDto
import ers.app.repo.transaction.TransactionManager
import ers.app.utils.Error
import org.springframework.stereotype.Component

data class AreaOutputModel(
    val id: Int,
    val height: Int,
    val width: Int,
    val taskID: Int,
    val name: String,
    val description: String
)

data class AreaIDOutputModel(val id: Int)
data class AreasOutputModel(val areas: List<AreaDto>)

@Component
class AreaService(private val transactionManager: TransactionManager) {

    fun createArea(height: Int, width: Int, taskID: Int, name: String, description: String): AreaResult =
        transactionManager.run {
            try {
                val area = it.areaData.createArea(height, width, taskID, name, description)
                success(AreaOutputModel(area.id, area.height, area.width, area.taskId, area.name, area.description))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun getAreaById(id: Int): AreaResult =
        transactionManager.run {
            try {
                val area = it.areaData.getAreaById(id)
                if (area != null) {
                    success(AreaOutputModel(area.id, area.height, area.width, area.taskId, area.name, area.description))
                } else {
                    failure(Error.AreaNotFound)
                }
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun updateArea(id: Int, height: Int, width: Int): AreaResult =
        transactionManager.run {
            try {
                if (it.areaData.getAreaById(id) == null)
                    failure(Error.AreaNotFound)
                val area = it.areaData.updateArea(id, height, width)
                success(AreaOutputModel(area.id, area.height, area.width, area.taskId, area.name, area.description))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun updateAreaDescription(id: Int, description: String): AreaResult =
        transactionManager.run {
            try {
                if (it.areaData.getAreaById(id) == null)
                    failure(Error.AreaNotFound)
                val area = it.areaData.updateAreaDescription(id, description)
                success(AreaOutputModel(area.id, area.height, area.width, area.taskId, area.name, area.description))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun deleteArea(id: Int): AreaIDResult =
        transactionManager.run {
            try {
                if (it.areaData.getAreaById(id) == null)
                    failure(Error.AreaNotFound)
                it.areaData.deleteArea(id)
                success(AreaIDOutputModel(id))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun getAreasByTaskId(offset: Int, limit: Int, taskId: Int): AreasResult =
        transactionManager.run {
            try {
                val user = it.taskData.getTaskById(taskId)
                if (user == null)
                    failure(Error.TaskNotFound)
                val areas = it.areaData.getAreasByTaskId(offset, limit, taskId)
                success(AreasOutputModel(areas))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }
}
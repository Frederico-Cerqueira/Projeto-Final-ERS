package ers.app.service

import ers.app.domainEntities.*
import ers.app.domainEntities.outputModels.AreaIDOutputModel
import ers.app.domainEntities.outputModels.AreaOutputModel
import ers.app.domainEntities.outputModels.AreasOutputModel
import ers.app.repo.transaction.TransactionManager
import ers.app.utils.Error
import org.springframework.stereotype.Component


@Component
class AreaService(private val transactionManager: TransactionManager) {

    fun createArea(height: Int, width: Int, taskID: Int, name: String, description: String): AreaResult =
        transactionManager.run {
            try {
                if (height <= 0 || width <= 0 || name.isEmpty() || description.isEmpty())
                    failure(Error.InvalidInput)
                if (name.length > 255 || description.length > 255)
                    failure(Error.InputTooLong)
                val area = it.areaData.createArea(height, width, taskID, name, description)
                success(AreaOutputModel(area.id, area.height, area.width, area.taskId, area.name, area.description))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun getAreaByID(id: Int): AreaResult =
        transactionManager.run {
            try {
                val area = it.areaData.getAreaById(id)
                if (area != null)
                    success(AreaOutputModel(area.id, area.height, area.width, area.taskId, area.name, area.description))
                else
                    failure(Error.AreaNotFound)
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun updateArea(id: Int, height: Int, width: Int): AreaResult =
        transactionManager.run {
            try {
                if (height <= 0 || width <= 0)
                    failure(Error.InvalidInput)
                it.areaData.getAreaById(id) ?: failure(Error.AreaNotFound)
                val area = it.areaData.updateArea(id, height, width)
                success(AreaOutputModel(area.id, area.height, area.width, area.taskId, area.name, area.description))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun updateAreaDescription(id: Int, description: String): AreaResult =
        transactionManager.run {
            try {
                if (description.isEmpty())
                    failure(Error.InvalidInput)
                if (description.length > 255)
                    failure(Error.InputTooLong)
                it.areaData.getAreaById(id) ?: failure(Error.AreaNotFound)
                val area = it.areaData.updateAreaDescription(id, description)
                success(AreaOutputModel(area.id, area.height, area.width, area.taskId, area.name, area.description))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun deleteArea(id: Int): AreaIDResult =
        transactionManager.run {
            try {
                it.areaData.getAreaById(id) ?: failure(Error.AreaNotFound)
                it.areaData.deleteArea(id)
                success(AreaIDOutputModel(id))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun getAreasByTaskID(offset: Int = 0, limit: Int = 0, taskId: Int): AreasResult =
        transactionManager.run {
            try {
                it.taskData.getTaskById(taskId) ?: failure(Error.TaskNotFound)
                if (offset < 0 || limit < 0)
                    failure(Error.InvalidInput)
                val areas = it.areaData.getAreasByTaskId(offset, limit, taskId)
                success(AreasOutputModel(areas))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }
}
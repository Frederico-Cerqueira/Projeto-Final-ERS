package ers.app.service

import ers.app.domainEntities.*
import ers.app.domainEntities.outputModels.AreaIDOutputModel
import ers.app.domainEntities.outputModels.AreaOutputModel
import ers.app.domainEntities.outputModels.AreasOutputModel
import ers.app.repo.transaction.TransactionManager
import ers.app.utils.Errors
import org.springframework.stereotype.Component


@Component
class AreaService(private val transactionManager: TransactionManager) {

    fun createArea(height: Int, width: Int, taskID: Int, name: String, description: String): AreaResult {
        if (height <= 0 || width <= 0 || name.isEmpty() || description.isEmpty())
            return failure(Errors.InvalidInput)
        if (name.length > 255 || description.length > 255)
            return failure(Errors.InputTooLong)
        return transactionManager.run {
            try {
                val area = it.areaData.createArea(height, width, taskID, name, description)
                success(AreaOutputModel(area.id, area.height, area.width, area.taskId, area.name, area.description))
            } catch (e: Exception) {
                failure(Errors.InternalServerError)
            }
        }
    }

    fun getAreaByID(id: Int): AreaResult =
        transactionManager.run {
            try {
                val area = it.areaData.getAreaByID(id)
                if (area != null)
                    success(AreaOutputModel(area.id, area.height, area.width, area.taskId, area.name, area.description))
                else
                    failure(Errors.AreaNotFound)
            } catch (e: Exception) {
                failure(Errors.InternalServerError)
            }
        }

    fun updateArea(id: Int, height: Int, width: Int): AreaResult {
        if (height <= 0 || width <= 0)
            return failure(Errors.InvalidInput)
        return transactionManager.run {
            try {
                it.areaData.getAreaByID(id) ?: failure(Errors.AreaNotFound)
                val area = it.areaData.updateArea(id, height, width)
                success(AreaOutputModel(area.id, area.height, area.width, area.taskId, area.name, area.description))
            } catch (e: Exception) {
                failure(Errors.InternalServerError)
            }
        }
    }


    fun updateAreaDescription(id: Int, description: String): AreaResult {
        if (description.isEmpty())
            return failure(Errors.InvalidInput)
        if (description.length > 255)
            return failure(Errors.InputTooLong)
        return transactionManager.run {
            try {
                it.areaData.getAreaByID(id) ?: failure(Errors.AreaNotFound)
                val area = it.areaData.updateAreaDescription(id, description)
                success(AreaOutputModel(area.id, area.height, area.width, area.taskId, area.name, area.description))
            } catch (e: Exception) {
                failure(Errors.InternalServerError)
            }
        }
    }


    fun deleteArea(id: Int): AreaIDResult =
        transactionManager.run {
            try {
                it.areaData.getAreaByID(id) ?: failure(Errors.AreaNotFound)
                it.areaData.deleteArea(id)
                success(AreaIDOutputModel(id))
            } catch (e: Exception) {
                failure(Errors.InternalServerError)
            }
        }

    fun getAreasByTaskID(offset: Int = 0, limit: Int = 0, taskId: Int): AreasResult {
        if (offset < 0 || limit < 0)
            return failure(Errors.InvalidInput)
        return transactionManager.run {
            try {
                it.taskData.getTaskByID(taskId) ?: failure(Errors.TaskNotFound)
                val areas = it.areaData.getAreasByTaskID(offset, limit, taskId)
                success(AreasOutputModel(areas))
            } catch (e: Exception) {
                failure(Errors.InternalServerError)
            }
        }
    }

}
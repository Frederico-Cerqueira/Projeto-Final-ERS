package ers.app.service

import ers.app.domainEntities.outputModels.AreaIDOutputModel
import ers.app.domainEntities.outputModels.AreaOutputModel
import ers.app.domainEntities.outputModels.AreasOutputModel
import ers.app.utils.Handler
import ers.app.repo.transaction.TransactionManager
import ers.app.utils.errors.*
import org.springframework.stereotype.Component


@Component
class AreaService(private val transactionManager: TransactionManager) {

    fun createArea(height: Int, width: Int, taskID: Int, name: String, description: String): Result<AreaOutputModel> {
        if (height <= 0 || width <= 0 || name.isEmpty() || description.isEmpty())
            return failure(InvalidInput)
        if (name.length > 255 || description.length > 255)
            return failure(InputTooLong)
        return Handler().servicesHandler {
            transactionManager.run {
                val area = it.areaData.createArea(height, width, taskID, name, description)
                return@run success(
                    AreaOutputModel(
                        area.id,
                        area.height,
                        area.width,
                        area.taskId,
                        area.name,
                        area.description
                    )
                )
            }
        }
    }

    fun getAreaByID(id: Int): Result<AreaOutputModel> =
        Handler().servicesHandler {
            transactionManager.run {
                val area = it.areaData.getAreaByID(id)
                if (area != null)
                    return@run success(
                        AreaOutputModel(
                            area.id,
                            area.height,
                            area.width,
                            area.taskId,
                            area.name,
                            area.description
                        )
                    )
                else
                    return@run failure(AreaNotFound)
            }
        }

    fun updateArea(id: Int, height: Int, width: Int): Result<AreaOutputModel> {
        if (height <= 0 || width <= 0)
            return failure(InvalidInput)
        return Handler().servicesHandler {
            transactionManager.run {
                it.areaData.getAreaByID(id) ?: return@run failure(AreaNotFound)
                val area = it.areaData.updateArea(id, height, width)
                return@run success(
                    AreaOutputModel(
                        area.id,
                        area.height,
                        area.width,
                        area.taskId,
                        area.name,
                        area.description
                    )
                )
            }
        }
    }


    fun updateAreaDescription(id: Int, description: String): Result<AreaOutputModel> {
        if (description.isEmpty())
            return failure(InvalidInput)
        if (description.length > 255)
            return failure(InputTooLong)
        return Handler().servicesHandler {
            transactionManager.run {
                it.areaData.getAreaByID(id) ?: return@run failure(AreaNotFound)
                val area = it.areaData.updateAreaDescription(id, description)
                return@run success(
                    AreaOutputModel(area.id, area.height, area.width, area.taskId, area.name, area.description)
                )
            }
        }
    }


    fun deleteArea(id: Int): Result<AreaIDOutputModel> =
        Handler().servicesHandler {
            transactionManager.run {
                it.areaData.getAreaByID(id) ?: return@run failure(AreaNotFound)
                it.areaData.deleteArea(id)
                return@run success(AreaIDOutputModel(id))
            }
        }

    fun getAreasByTaskID(offset: Int = 0, limit: Int = 0, taskId: Int): Result<AreasOutputModel> {
        if (offset < 0 || limit < 0)
            return failure(InvalidInput)

        return Handler().servicesHandler {
            transactionManager.run {
                it.taskData.getTaskByID(taskId) ?: return@run failure(TaskNotFound)
                val areas = it.areaData.getAreasByTaskID(offset, limit, taskId)
                return@run success(AreasOutputModel(areas))
            }
        }
    }

}
package ers.app.domainEntities.inputModels

data class TaskInputModel(val name: String, val userID: Int, val robotID: Int)
data class TaskUpdateInputModel(val status: String)
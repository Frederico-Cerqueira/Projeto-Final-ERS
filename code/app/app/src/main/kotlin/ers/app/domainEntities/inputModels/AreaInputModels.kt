package ers.app.domainEntities.inputModels

data class AreaInputModel(val height: Int, val width: Int, val name: String, val description: String)
data class AreaUpdateInputModel(val height: Int, val width: Int)
data class AreaUpdateDescriptionInputModel(val description: String)
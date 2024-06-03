package ers.app.domainEntities.inputModels

data class TimeInputModel(val startTime : String, val endTime : String, val weekDay : String, val description : String)
data class TimeUpdateInputModel(val startTime : String, val endTime : String, val weekDay : String)
data class TimeUpdateDescriptionInputModel(val description: String)
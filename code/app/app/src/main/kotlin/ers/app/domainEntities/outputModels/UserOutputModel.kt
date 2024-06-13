package ers.app.domainEntities.outputModels

data class UserOutputModel(val id: Int, val name: String, val email: String, val token: String)
data class LogoutOutputModel(val id: Int, val email: String)

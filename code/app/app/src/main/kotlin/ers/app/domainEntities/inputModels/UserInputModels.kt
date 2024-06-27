package ers.app.domainEntities.inputModels

data class UserInputModel(val name: String, val email: String, val password: String)
data class LoginInputModel(val email: String, val password: String)
data class LogoutInputModel(val id: Int)
data class TokenInputModel(val token: String)
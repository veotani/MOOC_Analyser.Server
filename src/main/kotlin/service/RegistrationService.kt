package service

import repositories.UserRepository
import web.api.model.User

class RegistrationService
{
    private val repository = UserRepository()

    fun registerNewUserAccount(account: User)
    {
        if (repository.isUserExists(account.userName!!))
            throw Exception("User exists")
        if (repository.isEmailExists(account.email!!))
            throw Exception("Email exists")
        account.role = "NEW"
        repository.save(account)
    }
}
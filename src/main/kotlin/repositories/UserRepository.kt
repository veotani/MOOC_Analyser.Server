package repositories

import web.api.model.User
import java.io.FileInputStream
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

class UserRepository
{
    private val dbConfig = Properties()
    private val propertiesFilePath = "properties.xml"
    private var connection: Connection? = null

    init
    {
        val inputStream = FileInputStream(propertiesFilePath)
        dbConfig.loadFromXML(inputStream)
        this.connection = connectToDb()
        if (this.connection == null)
            throw Exception("Couldn't connect to database on ${dbConfig["db.url"]}")
        createTable()
    }

    private fun createTable()
    {
        val query = "CREATE TABLE IF NOT EXISTS user(" +
                              "user_id INT PRIMARY KEY AUTO_INCREMENT, " +
                              "user_name VARCHAR(50) UNIQUE, " +
                              "password VARCHAR(100), " +
                              "role VARCHAR(20), " +
                              "email VARCHAR(100) UNIQUE" +
        ")"
        val statement = this.connection!!.createStatement()
        statement.execute(query)
    }

    private fun connectToDb(): Connection?
    {
        try
        {
            Class.forName(dbConfig["db.driver"].toString())
            val connectionString = dbConfig["db.url"].toString()
            val connectionProperties = getConnectionProperties()
            return DriverManager.getConnection(connectionString, connectionProperties)
        }
        catch (ex: SQLException)
        {
            ex.printStackTrace()
        }
        return null
    }

    private fun getConnectionProperties(): Properties
    {
        val username = dbConfig["db.username"].toString()
        val password = dbConfig["db.password"].toString()
        val connectionProperties = Properties()
        connectionProperties["user"] = username
        connectionProperties["password"] = password
        return connectionProperties
    }

    fun save(user: User)
    {
        val query = "INSERT INTO user(user_name, password, role, email) VALUES(" +
                "\"${user.userName}\", " +
                "\"${user.password}\", " +
                "\"${user.role}\", "     +
                "\"${user.email}\")"
        val statement = this.connection!!.createStatement()
        statement.execute(query)
    }

    fun isUserExists(username: String): Boolean
    {
        val query = "SELECT COUNT(user_name) FROM user WHERE user_name = \"$username\""
        val statement = this.connection!!.createStatement()
        statement.executeQuery(query)
        val resultSet = statement.resultSet
        while (resultSet.next())
            if (resultSet.getString(1).toInt() > 0) return true
        return false
    }

    fun isEmailExists(email: String): Boolean
    {
        val query = "SELECT COUNT(email) FROM user WHERE email = \"$email\""
        val statement = this.connection!!.createStatement()
        statement.executeQuery(query)
        val resultSet = statement.resultSet
        while (resultSet.next())
            if (resultSet.getString(1).toInt() > 0) return true
        return false
    }

    fun isValidUser(username: String, password: String): Boolean
    {
        val query = "SELECT COUNT(user_name) FROM user WHERE user_name = \"$username\" AND password = \"$password\""
        val statement = this.connection!!.createStatement()
        statement.executeQuery(query)
        val resultSet = statement.resultSet
        while (resultSet.next())
            if (resultSet.getString(1).toInt() == 1) return true
        return false
    }

    fun getUserRole(username: String): String
    {
        val query = "SELECT role FROM user WHERE user_name = \"$username\""
        val statement = this.connection!!.createStatement()
        statement.executeQuery(query)
        val resultSet = statement.resultSet
        while (resultSet.next())
            return resultSet.getString(1)
        return ""
    }

    fun getPasswordByUserName(userName: String): String
    {
        var query = "SELECT password FROM user WHERE user_name = \"$userName\""
        var statement = this.connection!!.createStatement()
        statement.executeQuery(query)
        val resultSet = statement.resultSet
        while (resultSet.next())
            return resultSet.getString(1)
        return ""
    }
}
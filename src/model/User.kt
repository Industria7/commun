package model

import dao.Users
import dao.Users.autoIncrement
import dao.Users.primaryKey
import dao.Users.uniqueIndex
import org.jetbrains.exposed.sql.Date
import org.joda.time.DateTime
import java.io.*
import java.util.*

//import kotlinx.serialization.*

//@Serializable
data class User(val date_reg: DateTime,
                val name: String,
                val login: String,
                val pass: String,
                val id: Int) //: Serializable
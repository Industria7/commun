package model

import org.joda.time.DateTime

//@Serializable
data class User(val date_reg: DateTime,
                var name: String,
                val login: String,
                val pass: String,
                val id: Int) //: Serializable
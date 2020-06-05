package model

import org.jetbrains.exposed.sql.Date
import org.joda.time.DateTime

data class Info(val gas : Int,
                val water : Int,
                val elec : Int,
                val tarif : Int,
                val id : Int? = -1,
                val date : DateTime? = DateTime.parse("2020-02-01"))
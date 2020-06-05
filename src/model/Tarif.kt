package model

import dao.Tarifs
import dao.Tarifs.autoIncrement
import dao.Tarifs.primaryKey
import org.jetbrains.exposed.sql.Date
import org.joda.time.DateTime

data class Tarif(val date : DateTime,
                 val gas : Double,
                 val water : Double,
                 val elec1 : Double,
                 val elec2 : Double,
                 val elec3 : Double,
                 val elec_level1 : Int,
                 val elec_level2 : Int,
                 val id : Int? = -1)

//data class defTarif(val date : DateTime = DateTime.parse("2020-02-01"),
//                    val gas : Double = 2.61,
//                    val water : Double = 32.162,
//                    val elec1 : Double = 0.8018,
//                    val elec2 : Double = 1.0904,
//                    val elec3 : Double = 2.6808,
//                    val elec_level1 : Int = 150,
//                    val elec_level2 : Int = 800)
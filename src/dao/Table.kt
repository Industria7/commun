package dao

import org.jetbrains.exposed.sql.*

object Users : Table("Users") {
    val id = integer("id").autoIncrement().primaryKey()
    val date_reg = date("date_reg")
    val name = varchar("name", 128)
    val login = varchar("login", 256)
    val pass = varchar("pass", 64)
}

object Settings : Table("Settings") {
    val userId = integer("userId").primaryKey()
    val tarif = integer("tarif")
    val old_pay_Date = date("old_pay_Date").nullable()
    val adress = varchar("adress", 150).nullable()
    val waterFIO = varchar("waterFIO", 200).nullable()
    val waterLS = varchar("waterLS", 100).nullable()
    val gasFIO = varchar("gasFIO", 200).nullable()
    val gasLS = varchar("gasLS", 100).nullable()
    val elecFIO = varchar("elecFIO", 200).nullable()
    val elecLS = varchar("elecLS", 100).nullable()
}

object Infos : Table("Infos") {
    val id = integer("id").autoIncrement().primaryKey()
    val userId = integer("userId")
    val date = date("date")
    val gas = integer("gas")
    val water = integer("water")
    val elec = integer("elec")
    val tarif = integer("tarif")
}

object Tarifs : Table("Tarifs") {
    val id = integer("id").autoIncrement().primaryKey()
    val userId = integer("userId")
    val date = date("date")
    val gas = double("gas")
    val water = double("water")
    val elec1 = double("elec1")
    val elec2 = double("elec2")
    val elec3 = double("elec3")
    val elec_level1 = integer("elec_level1")
    val elec_level2 = integer("elec_level2")
}
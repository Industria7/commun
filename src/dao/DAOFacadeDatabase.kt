package dao

import org.jetbrains.exposed.dao.*
import model.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SortOrder.*
import org.jetbrains.exposed.sql.transactions.transactionManager
import org.joda.time.*
import java.io.*
import kotlin.io.*
import kotlin.reflect.jvm.internal.impl.util.ValueParameterCountCheck

interface DAOFacade : Closeable {
    fun init()

    fun newUser(user: User): Int
    fun user(login: String, hash: String? = null): User?

    fun setSettings(userId : Int, sett: Setting)
    fun getSettings(userId : Int): Setting?

    fun newTarif(userId : Int, t : Tarif): Int
    fun getTarif(id: Int) : Tarif?
    fun getAllTarifs(userId : Int): List<Tarif>
    fun delTarif(id : Int)

    fun newInfo(userId: Int, i : Info): Int
    fun delInfo(id : Int)
    fun getInfo(id: Int) : Info?
    fun getOldInfo(date: DateTime?) : Info?
    fun getAllInfo(userId: Int): List<Info>
}
//${dir.canonicalFile.absolutePath}
class DAOFacadeDatabase(val db: Database = Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")): DAOFacade {
    constructor(dir: File) : this(Database.connect("jdbc:h2:file:~/info.db", driver = "org.h2.Driver"))

    override fun init() {
        // Create the used tables
        transaction {//db.transaction
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(Users, Settings, Infos, Tarifs)
        }
    }

    override fun user(login: String, hash: String?) = transaction{
        addLogger(StdOutSqlLogger)
        Users.select {Users.login eq login}
                .map {
                    User(it[Users.date_reg], it[Users.name], it[Users.login], it[Users.pass], it[Users.id])
                }
                .firstOrNull()
    }

    override fun newUser(user: User) = transaction {
        addLogger(StdOutSqlLogger)
        Users.insert {
            it[date_reg] = user.date_reg
            it[name] = user.name
            it[login] = user.login
            it[pass] = user.pass
        }[Users.id]
    }

    override fun setSettings(userId: Int, sett: Setting) {
        transaction {
            addLogger(StdOutSqlLogger)
            val oldSetting = getSettings(userId)
            println("$oldSetting")
            if (oldSetting == null){
                Settings.insert {
                    it[Settings.userId] = userId
                    it[Settings.tarif] = sett.tarif
                    if(sett.old_pay_Date != null) it[Settings.old_pay_Date] = sett.old_pay_Date
                    if(sett.adress != null) it[Settings.adress] = sett.adress
                    if(sett.waterFIO != null) it[Settings.waterFIO] = sett.waterFIO
                    if(sett.waterLS != null) it[Settings.waterLS] = sett.waterLS
                    if(sett.gasFIO != null) it[Settings.gasFIO] = sett.gasFIO
                    if(sett.gasLS != null) it[Settings.gasLS] = sett.gasLS
                    if(sett.elecFIO != null) it[Settings.elecFIO] = sett.elecFIO
                    if(sett.elecLS != null) it[Settings.elecLS] = sett.elecLS
                }
            }else{
                Settings.update ({Settings.userId eq userId }){
                    it[Settings.userId] = userId
                    if(sett.tarif != oldSetting.tarif) it[Settings.tarif] = sett.tarif
                    if(sett.old_pay_Date != null)
                        if(sett.old_pay_Date != oldSetting.old_pay_Date) it[Settings.old_pay_Date] = sett.old_pay_Date
                    if(sett.adress != null)
                        if(sett.adress != oldSetting.adress) it[Settings.adress] = sett.adress
                    if(sett.waterFIO != null)
                        if(sett.waterFIO != oldSetting.waterFIO) it[Settings.waterFIO] = sett.waterFIO
                    if(sett.waterLS != null)
                        if(sett.waterLS != oldSetting.waterLS) it[Settings.waterLS] = sett.waterLS
                    if(sett.gasFIO != null)
                        if(sett.gasFIO != oldSetting.gasFIO) it[Settings.gasFIO] = sett.gasFIO
                    if(sett.gasLS != null)
                        if(sett.gasLS != oldSetting.gasLS) it[Settings.gasLS] = sett.gasLS
                    if(sett.elecFIO != null)
                        if(sett.elecFIO != oldSetting.elecFIO) it[Settings.elecFIO] = sett.elecFIO
                    if(sett.elecLS != null)
                        if(sett.elecLS != oldSetting.elecLS) it[Settings.elecLS] = sett.elecLS
                }
            }

        }
    }

    override fun getSettings(userId: Int) = transaction {
        addLogger(StdOutSqlLogger)
        Settings.select{Settings.userId eq userId}.map {
            Setting(it[Settings.tarif],
                    it[Settings.old_pay_Date],
                    it[Settings.adress],
                    it[Settings.waterFIO],
                    it[Settings.waterLS],
                    it[Settings.gasFIO],
                    it[Settings.gasLS],
                    it[Settings.elecFIO],
                    it[Settings.elecFIO])
        }.singleOrNull()
    }

    override fun newTarif(userId : Int, t: Tarif) = transaction {
        addLogger(StdOutSqlLogger)
        Tarifs.insert {
            it[Tarifs.userId] = userId
            it[Tarifs.date] = t.date
            it[Tarifs.gas] = t.gas
            it[Tarifs.water] = t.water
            it[Tarifs.elec1] = t.elec1
            it[Tarifs.elec2] = t.elec2
            it[Tarifs.elec3] = t.elec3
            it[Tarifs.elec_level1] = t.elec_level1
            it[Tarifs.elec_level2] = t.elec_level2
        }[Tarifs.id]
    }

    override fun getTarif(id: Int) = transaction {
        addLogger(StdOutSqlLogger)
        Tarifs.select {Tarifs.id eq id}
                .map {
                    Tarif(it[Tarifs.date],
                            it[Tarifs.gas],
                            it[Tarifs.water],
                            it[Tarifs.elec1],
                            it[Tarifs.elec2],
                            it[Tarifs.elec3],
                            it[Tarifs.elec_level1],
                            it[Tarifs.elec_level2],
                            it[Tarifs.id])
                }.singleOrNull()
    }

    override fun getAllTarifs(userId: Int) = transaction {
        addLogger(StdOutSqlLogger)
        Tarifs.select {Tarifs.userId eq userId}
                .map {
                    Tarif(it[Tarifs.date],
                            it[Tarifs.gas],
                            it[Tarifs.water],
                            it[Tarifs.elec1],
                            it[Tarifs.elec2],
                            it[Tarifs.elec3],
                            it[Tarifs.elec_level1],
                            it[Tarifs.elec_level2],
                            it[Tarifs.id])
                }
    }

    override fun delTarif(id: Int) {
        transaction {
            addLogger(StdOutSqlLogger)
            Tarifs.deleteWhere {
                Tarifs.id eq id
            }
        }
    }

    override fun newInfo(userId: Int, i: Info) = transaction {
        addLogger(StdOutSqlLogger)
        Infos.insert{
            it[Infos.userId] = userId
            it[Infos.gas] = i.gas
            it[Infos.water] = i.water
            it[Infos.elec] = i.elec
            it[Infos.tarif] = i.tarif
            if(i.date != null) {
                it[Infos.date] = i.date
            }else{
                it[dao.Infos.date] = DateTime.now()
            }
        }[Infos.id]
    }

    override fun delInfo(id: Int) = transaction {
        addLogger(StdOutSqlLogger)
        Infos.deleteWhere {
            Infos.id eq id
        }
        Unit
    }

    override fun getInfo(id: Int) = transaction {
        addLogger(StdOutSqlLogger)
        Infos.select{Infos.id eq id}
                .map {Info(it[Infos.gas],
                           it[Infos.water],
                           it[Infos.elec],
                           it[Infos.tarif],
                           it[Infos.id],
                           it[Infos.date])
        }.singleOrNull()
    }

    override fun getOldInfo(date: DateTime?) = transaction {
        addLogger(StdOutSqlLogger)
        if(date == null) return@transaction null
        Infos.select{Infos.date eq date}
                .map {Info(it[Infos.gas],
                           it[Infos.water],
                           it[Infos.elec],
                           it[Infos.tarif],
                           it[Infos.id],
                           it[Infos.date])
        }.singleOrNull()
    }



    override fun getAllInfo(userId: Int) = transaction {
        addLogger(StdOutSqlLogger)
        Infos.select{Tarifs.userId eq userId}
                .map {
                    Info(it[Infos.gas],
                        it[Infos.water],
                        it[Infos.elec],
                        it[Infos.tarif],
                        it[Infos.id],
                        it[Infos.date])
                }
    }

    override fun close() {
    }
}

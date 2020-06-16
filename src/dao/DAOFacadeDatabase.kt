package dao

import model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.*
import java.io.*
import kotlin.io.*

interface DAOFacade : Closeable {
    fun init()

    fun newUser(user: User): Int
    fun user(login: String, hash: String? = null): User?

    fun setName(userId: Int, name : String)

    fun setSettings(userId : Int, setting: Setting, oldSetting : Setting? = null)
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

    override fun setName(userId: Int, name : String){
        transaction{
            addLogger(StdOutSqlLogger)
            Users.update ({Users.id eq userId }){
                if(name != "") it[Users.name] = name
            }

        }
    }

    override fun setSettings(userId: Int, setting: Setting, oldSetting : Setting?) {
        transaction {
            addLogger(StdOutSqlLogger)
//            val oldSetting = getSettings(userId)
            println("$oldSetting")
            if (oldSetting == null){
                Settings.insert {
                    it[Settings.userId] = userId
                    it[Settings.tarif] = setting.tarif
                    if(setting.old_pay_Date != null) it[Settings.old_pay_Date] = setting.old_pay_Date
                    if(setting.adress != null) it[Settings.adress] = setting.adress
                    if(setting.waterFIO != null) it[Settings.waterFIO] = setting.waterFIO
                    if(setting.waterLS != null) it[Settings.waterLS] = setting.waterLS
                    if(setting.gasFIO != null) it[Settings.gasFIO] = setting.gasFIO
                    if(setting.gasLS != null) it[Settings.gasLS] = setting.gasLS
                    if(setting.elecFIO != null) it[Settings.elecFIO] = setting.elecFIO
                    if(setting.elecLS != null) it[Settings.elecLS] = setting.elecLS
                }
            }else{
                Settings.update ({Settings.userId eq userId }){
                    it[Settings.userId] = userId
                    if(setting.tarif != oldSetting.tarif) it[Settings.tarif] = setting.tarif
                    if(setting.old_pay_Date != null)
                        if(setting.old_pay_Date != oldSetting.old_pay_Date) it[Settings.old_pay_Date] = setting.old_pay_Date
                    if(setting.adress != null)
                        if(setting.adress != oldSetting.adress) it[Settings.adress] = setting.adress
                    if(setting.waterFIO != null)
                        if(setting.waterFIO != oldSetting.waterFIO) it[Settings.waterFIO] = setting.waterFIO
                    if(setting.waterLS != null)
                        if(setting.waterLS != oldSetting.waterLS) it[Settings.waterLS] = setting.waterLS
                    if(setting.gasFIO != null)
                        if(setting.gasFIO != oldSetting.gasFIO) it[Settings.gasFIO] = setting.gasFIO
                    if(setting.gasLS != null)
                        if(setting.gasLS != oldSetting.gasLS) it[Settings.gasLS] = setting.gasLS
                    if(setting.elecFIO != null)
                        if(setting.elecFIO != oldSetting.elecFIO) it[Settings.elecFIO] = setting.elecFIO
                    if(setting.elecLS != null)
                        if(setting.elecLS != oldSetting.elecLS) it[Settings.elecLS] = setting.elecLS
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

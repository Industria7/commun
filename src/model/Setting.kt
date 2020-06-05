package model

import org.jetbrains.exposed.sql.Date
import org.joda.time.DateTime

data class Setting(
        val tarif : Int,
        val old_pay_Date: DateTime? = null,
        val adress : String? = null,
        val waterFIO : String? = null,
        val waterLS : String? = null,
        val gasFIO : String? = null,
        val gasLS : String? = null,
        val elecFIO : String? = null,
        val elecLS : String? = null)
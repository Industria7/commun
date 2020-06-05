import model.*
import dao.*
import org.joda.time.DateTime

data class resCalc(val gas: Double,val water: Double,val elec: Double)

fun calculate(tarif: Tarif, info : Info) = resCalc(
        tarif.gas * info.gas,
        tarif.water * info.water,
        when{
        info.elec > tarif.elec_level2 -> tarif.elec_level1 * tarif.elec1 + (tarif.elec_level2 - tarif.elec_level1)*tarif.elec2 + (info.elec-tarif.elec_level2)*tarif.elec3
        info.elec in (tarif.elec_level1 .. tarif.elec_level2) -> tarif.elec_level1 * tarif.elec1 + (info.elec-tarif.elec_level1)*tarif.elec2
        else -> tarif.elec1 * info.elec
            }
    )




fun test(){
    val tar = Tarif(DateTime.parse("2020-02-01"),
            2.61,
            32.162,
            0.8018,
            1.0904,
            2.6808,
            150,
            800)

    println( calculate(tar, Info(250,2,220,0) ) )
    println( calculate(tar, Info(200,2,200,0) ) )
    println( calculate(tar, Info(260,3,240,0) ) )
    println( calculate(tar, Info(100,3,250,0) ) )
    println( calculate(tar, Info(400,10,950,0) ) )
    println( calculate(tar, Info(10,1,100,0) ) )
    println( calculate(tar, Info(10,1,150,0) ) )
    println( calculate(tar, Info(350,5,1000,0) ) )
    println( calculate(tar, Info(10,1,801,0) ) )
}

/*
num*pay.voda
num*pay.gaz

if num> 600 then
              Result:= Ceil(100 * pay.elekt1 + 500*pay.elekt2 + (num-600)*pay.elekt3)
               else
                if num>100 then
              Result:= Ceil(100 * pay.elekt1 + (num-100)*pay.elekt2)
                  else
              Result:= Ceil(num*pay.elekt1);

 */

/*
* fun calculate(tarif: Tarif, info : Info): resCalc = {
    val gas = tarif.gas * info.gas
    val water = tarif.water * info.water
    val elec : Double = when{
        info.elec > tarif.elec_level2 -> tarif.elec_level1 * tarif.elec1 + (info.elec-tarif.elec_level1)*tarif.elec2 + (info.elec-tarif.elec_level2)*tarif.elec3
        info.elec in (tarif.elec_level1 .. tarif.elec_level2) -> tarif.elec_level1 * tarif.elec1 + (info.elec-tarif.elec_level1)*tarif.elec2
        else -> tarif.elec1 * info.elec
            }
    resCalc(gas, water, elec)
}
*
* */
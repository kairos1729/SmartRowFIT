
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.FileReader


data class SmartRowData(
    val strokes: Int = 0,
    val seconds: Int = 0,
    val distance_m: Int = 0,
    val work_J: Double = 0.0,
    val actualPower_W: Int = 0,
    val averagePower_W: Double = 0.0,
    val actualSplit_s: Int = 0,
    val averageSplit_s: Int = 0,
    val strokeRate_spm: Double = 0.0,
    val heartRate_bpm: Int = 0
)


const val FILE_ID_DEFINITION =
    "Definition,0,file_id,type,1,,manufacturer,1,,product,1,,serial_number,1,,time_created,1,,number,1,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"
const val FILE_ID_DATA =
    """Data,0,file_id,type,4,,manufacturer,118,,product,0,,time_created,0,,number,0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"""

const val DEVICE_INFO_DEFINITION =
    """Definition,1,device_info,timestamp,1,,device_index,1,,device_type,1,,manufacturer,1,,serial_number,1,,product,1,,software_version,1,,hardware_version,1,,cum_operating_time,1,,battery_voltage,1,,battery_status,1,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"""
const val DEVICE_INFO_DATA =
    """Data,1,device_info,timestamp,0,s,device_index,0,,device_type,0,,manufacturer,118,,product,0,,software_version,1,,hardware_version,0,,cum_operating_time,0,s,battery_voltage,0,V,battery_status,0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"""

data class FitRecord(
    val timestamp_s: Int,
    val heartRate_bpm: Int,
    val cadence_rpm: Double,
    val distance_m: Int,
    val speed_m_per_s: Double,
    val power_W: Int
) {

    val data =
        "Data,6,record,timestamp,$timestamp_s,s,heart_rate,$heartRate_bpm,bpm," +
                "cadence,$cadence_rpm,rpm,distance,$distance_m,m,speed," +
                "${String.format("%.3f", speed_m_per_s)},m/s,power,$power_W,watts,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"

    companion object {
        const val definition = "Definition,6,record,timestamp,1,,heart_rate,1,,cadence,1,,distance,1,,speed,1,,power,1,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"
    }
}


fun main() {
    readTcxSR()
//    readCsvSR()
}



@JsonIgnoreProperties(ignoreUnknown = true)
data class Trackpoint(
    @JsonProperty("Time") val time_utc: String,
    @JsonProperty("DistanceMeters") val distance_m: Int,
    @JsonProperty("Cadence") val cadence_rpm: Double,
    @JsonProperty("HeartRateBpm") val heartRate_bpm: HeartRateBpm,
    @JsonProperty("Extensions") val extensions: Extensions,
)
data class HeartRateBpm(@JsonProperty("Value") val value: Int)
data class Extensions(@JsonProperty("TPX") val tpx: TPX)
data class TPX(@JsonProperty("Watts") val watts: Int)

private fun readTcxSR() {
    tcxTpx()
    tcxExtensions()
    tcxTrackpoint()
}

private fun tcxTrackpoint() {
    val xs = """
        <x>
      <Time>2021-04-09T23:55:47Z</Time>
      <DistanceMeters>5507</DistanceMeters>
      <SensorState>Present</SensorState>
      <Cadence>31.7</Cadence>
      <HeartRateBpm>
       <Value>142</Value>
      </HeartRateBpm>
      <Extensions>
       <TPX xmlns="http://www.garmin.com/xmlschemas/ActivityExtension/v2">
        <Watts>197</Watts>
       </TPX>
      </Extensions>        
        </x>
    """
    val x = readXmlString<Trackpoint>(xs)
    println("ext=$x")
}

private fun tcxExtensions() {
    val xs = "<x><TPX><Watts>120</Watts></TPX></x>"
    val x = readXmlString<Extensions>(xs)
    println("ext=$x")
}

private fun tcxTpx() {
    val xs = "<x><Watts>101</Watts></x>"
    val x = readXmlString<TPX>(xs)
    println("txp=$x")
}

private fun readCsvSR() {
    val smartRowDataList =
        readCsvFile<SmartRowData>("/Users/dominic.godwin/Developer/FitSDKRelease_21.53.00/dominic/2021-04-09T232928_5946m.csv")

    //println(smartRowDataList.joinToString("\n"))

    val fitRecords = smartRowDataList.map(SmartRowData::toFitRecord)


    println("""Type,Local Number,Message,Field 1,Value 1,Units 1,Field 2,Value 2,Units 2,Field 3,Value 3,Units 3,Field 4,Value 4,Units 4,Field 5,Value 5,Units 5,Field 6,Value 6,Units 6,Field 7,Value 7,Units 7,Field 8,Value 8,Units 8,Field 9,Value 9,Units 9,Field 10,Value 10,Units 10,Field 11,Value 11,Units 11,Field 12,Value 12,Units 12,Field 13,Value 13,Units 13,Field 14,Value 14,Units 14,Field 15,Value 15,Units 15,Field 16,Value 16,Units 16,Field 17,Value 17,Units 17,Field 18,Value 18,Units 18,Field 19,Value 19,Units 19,Field 20,Value 20,Units 20,Field 21,Value 21,Units 21,Field 22,Value 22,Units 22,Field 23,Value 23,Units 23,Field 24,Value 24,Units 24""")

    println(FILE_ID_DEFINITION)
    println(FILE_ID_DATA)
    println(DEVICE_INFO_DEFINITION)
    println(DEVICE_INFO_DATA)


    println("""Definition,2,activity,timestamp,1,,total_timer_time,1,,num_sessions,1,,type,1,,event,1,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,""")
    println("""Data,2,activity,timestamp,0,,total_timer_time,0.001,s,num_sessions,0,,type,3,,event,1,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,""")

    println("""Definition,3,session,timestamp,1,,event,1,,event_type,1,,start_time,1,,sport,1,,sub_sport,1,,total_elapsed_time,1,,total_timer_time,1,,total_distance,1,,total_cycles,1,,total_calories,1,,avg_speed,1,,max_speed,1,,avg_heart_rate,1,,max_heart_rate,1,,avg_cadence,1,,max_cadence,1,,avg_power,1,,max_power,1,,first_lap_index,1,,message_index,1,,,,,,,,,,""")
    println("""Data,3,session,timestamp,0,s,event,0,,event_type,0,,start_time,0,,sport,4,,sub_sport,14,,total_elapsed_time,264.1,s,total_timer_time,264.1,s,total_distance,1147.99,m,total_cycles,130,cycles,total_calories,80,kcal,avg_speed,4.346,m/s,max_speed,4.587,m/s,avg_heart_rate,121,bpm,max_heart_rate,151,bpm,avg_cadence,27,rpm,max_cadence,92,rpm,avg_power,230,watts,max_power,270,watts,first_lap_index,0,,message_index,0,,,,,,,,,,""")

    println("""Definition,4,event,timestamp,1,,event,1,,event_type,1,,data,1,,event_group,1,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,""")
    println("""Data,4,event,timestamp,0,s,event,0,,event_type,0,,timer_trigger,0,,event_group,0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,""")

    println(FitRecord.definition)

    fitRecords.forEach {
        println(it.data)
    }

    println("""Data,4,event,timestamp,247,s,event,0,,event_type,0,,timer_trigger,0,,event_group,0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,""")

    val work = smartRowDataList.sumByDouble { it.work_J }
    val time = smartRowDataList.last().seconds - smartRowDataList.first().seconds
    val averagePower = work / time

    println("work=$work, time=$time, average power = $averagePower")
}

fun SmartRowData.toFitRecord() = FitRecord(
    seconds,
    heartRate_bpm,
    strokeRate_spm,
    distance_m,
    500.0 / actualSplit_s,
    actualPower_W
)


val csvMapper = CsvMapper().apply {
    registerModule(KotlinModule())
}

inline fun <reified T> readCsvFile(fileName: String): List<T> {
    FileReader(fileName).use { reader ->
        return csvMapper
            .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
            .readerFor(T::class.java)
            .with(
                csvMapper
                    .schemaFor(T::class.java)
                    .withSkipFirstDataRow(true)
                    .withNullValue(" ")
            )
            .readValues<T>(reader)
            .readAll()
            .toList()
    }
}

val xmlMapper = XmlMapper().apply {
    registerKotlinModule()
}

inline fun <reified T> readXmlFile(fileName: String): T =
    FileReader(fileName).use { reader ->
        xmlMapper.readValue(reader, T::class.java)
    }

inline fun <reified T> readXmlString(xml: String): T =
        xmlMapper.readValue(xml, T::class.java)

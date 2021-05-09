import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.FileReader
import java.time.ZonedDateTime
import kotlin.math.roundToInt


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
                "${
                    String.format(
                        "%.3f",
                        speed_m_per_s
                    )
                },m/s,power,$power_W,watts,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"

    companion object {
        const val definition =
            "Definition,6,record,timestamp,1,,heart_rate,1,,cadence,1,,distance,1,,speed,1,,power,1,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"
    }
}


fun main() {
    readTcxAndCsvSR()
//    convertSRToFitCsv()
}


@JsonIgnoreProperties(ignoreUnknown = true)
data class TrainingCenterDatabase(
    @JsonProperty("Activities") val activities: List<Activity>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Activity(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("Lap") val laps: List<Lap>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Lap(
    @JacksonXmlProperty(isAttribute = true, localName = "StartTime") val startTime_utc: String,
    @JsonProperty("TotalTimeSeconds") val totalTime_s: Double,
    @JsonProperty("DistanceMeters") val distance_m: Int,
    @JsonProperty("Calories") val calories: Int,
    @JsonProperty("Track") val track: List<Trackpoint>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Trackpoint(
    @JsonProperty("Time") val time_utc: String,
    @JsonProperty("DistanceMeters") val distance_m: Int,
    @JsonProperty("Cadence") val cadence_rpm: Double,
    @JsonProperty("HeartRateBpm") val heartRate_bpm: HeartRateBpm,
    @JsonProperty("Extensions") val extensions: Extensions,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class HeartRateBpm(@JsonProperty("Value") val value: Int)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Extensions(@JsonProperty("TPX") val tpx: TPX)

@JsonIgnoreProperties(ignoreUnknown = true)
data class TPX(@JsonProperty("Watts") val watts: Int)

const val GARMIN_EPOCH_OFFSET = 631065600

private fun readTcxAndCsvSR() {
    val tcx =
        readXmlFile<TrainingCenterDatabase>("/Users/dominic.godwin/Developer/FitSDKRelease_21.53.00/dominic/SR1.tcx")

    val csvRows =
        readCsvFile<SmartRowData>("/Users/dominic.godwin/Developer/FitSDKRelease_21.53.00/dominic/SR1.csv")

    val timestamps_utc = listOf(tcx.mapLaps { startTime_utc }.first()) + tcx.mapTrackpoints { time_utc }

    val timestamps = timestamps_utc.map { ZonedDateTime.parse(it).toEpochSecond() - GARMIN_EPOCH_OFFSET }
    val last = timestamps.last() + tcx.activities.last().laps.last().totalTime_s.toLong()
    val allTimestamps = timestamps + listOf(last)


    println(allTimestamps.count())
    println(allTimestamps)
    println(allTimestamps.last() - allTimestamps.first())

    val lapTimes = tcx.mapLaps { totalTime_s }
    val totalTime = lapTimes.sum()

    println(lapTimes)
    println(totalTime)
    println("${totalTime.roundToInt() / 60}:${totalTime.roundToInt() % 60}")

    val distances = tcx.mapTrackpoints { distance_m }

    println(distances)
    println(distances.last())

    val lapDistances = tcx.mapLaps { distance_m }

    val totalDistance = lapDistances.sum()
    println(lapDistances)
    println(totalDistance)


    val calories = tcx.mapLaps { calories }
    println(calories)
    println(calories.sum())

    val work = csvRows.sumByDouble { it.work_J }
    val time_s = csvRows.last().seconds - csvRows.first().seconds
    val averagePower = work / time_s

    println("csv: work=$work, time=$time_s, average power from work = $averagePower")
    val averagepowerW = csvRows.last().averagePower_W
    println("csv: average power = $averagepowerW")
    println("csv: peak power = ${csvRows.maxOf(SmartRowData::actualPower_W)}")

    val csvSeconds = csvRows.map { it.seconds }
    val intervals = csvSeconds.zipWithNext { a, b -> b - a }

    println("csv: $csvSeconds")
    println("csv: $intervals")

    val heartRatesCsv = csvRows.map { it.heartRate_bpm }.drop(1)
    val heartBeatsSixtieths = heartRatesCsv.zip(intervals) { r, i -> r * i }
    val totalHeartBeatsSixtieths = heartBeatsSixtieths.sum()
    val aveHeartRateBpm = totalHeartBeatsSixtieths / time_s

    println(heartRatesCsv)
    println(heartBeatsSixtieths)
    println(totalHeartBeatsSixtieths)
    println(aveHeartRateBpm)

    val tcxintervals = allTimestamps.zipWithNext { a, b -> b - a }
    println(tcxintervals)
    val tcxheartrates = tcx.mapTrackpoints { heartRate_bpm.value }
    val tcxheartBeatsSixtieths = tcxheartrates.zip(tcxintervals) { r, i -> r * i }
    val tcxtotalHeartBeatsSixtieths = tcxheartBeatsSixtieths.sum()
    val tcxaveHeartRateBpm = tcxtotalHeartBeatsSixtieths / time_s

    println(tcxheartrates)
    println(tcxheartBeatsSixtieths)
    println(tcxtotalHeartBeatsSixtieths)
    println(tcxaveHeartRateBpm)

    val workPerSixtiethHB = work / totalHeartBeatsSixtieths
    println(workPerSixtiethHB)

//    txcTest()
}

private fun <T> TrainingCenterDatabase.mapTrackpoints(block: Trackpoint.() -> T) =
    mapLaps { track.map(block) }.flatten()


private fun <T> TrainingCenterDatabase.mapLaps(block: Lap.() -> T) =
    activities.flatMap { it.laps.map(block) }

private fun txcTest() {
    tcxTpx()
    tcxExtensions()
    tcxTrackpoint()
    tcxLap()
    tcxActivity()
    tcxTrainingCenterDatabase()
}

private fun tcxTrainingCenterDatabase() {
    val xs = """
    <x>
    <Activities>
     <Activity>
   <Lap StartTime="2000-00-00T00:00:00Z">
    <TotalTimeSeconds>136.04</TotalTimeSeconds>
    <DistanceMeters>500</DistanceMeters>
    <Calories>20</Calories>
    <Intensity>Active</Intensity>
    <TriggerMethod>Distance</TriggerMethod>
    <Track>
     <Trackpoint>
      <Time>2021-04-09T23:29:31Z</Time>
      <DistanceMeters>8</DistanceMeters>
      <SensorState>Present</SensorState>
      <Cadence>0</Cadence>
      <HeartRateBpm>
       <Value>85</Value>
      </HeartRateBpm>
      <Extensions>
       <TPX xmlns="http://www.garmin.com/xmlschemas/ActivityExtension/v2">
        <Watts>0</Watts>
       </TPX>
      </Extensions>
     </Trackpoint>
     </Track>
     </Lap>
   <Lap StartTime="2022-02-02T22:22:22Z">
    <TotalTimeSeconds>136.04</TotalTimeSeconds>
    <DistanceMeters>500</DistanceMeters>
    <Calories>20</Calories>
    <Intensity>Active</Intensity>
    <TriggerMethod>Distance</TriggerMethod>
    <Track>
     <Trackpoint>
      <Time>2021-04-09T23:29:31Z</Time>
      <DistanceMeters>8</DistanceMeters>
      <SensorState>Present</SensorState>
      <Cadence>0</Cadence>
      <HeartRateBpm>
       <Value>85</Value>
      </HeartRateBpm>
      <Extensions>
       <TPX xmlns="http://www.garmin.com/xmlschemas/ActivityExtension/v2">
        <Watts>0</Watts>
       </TPX>
      </Extensions>
     </Trackpoint>
     </Track>
     </Lap>
     </Activity>
     <Activity>
   <Lap StartTime="2021-04-09T23:29:28Z">
    <TotalTimeSeconds>136.04</TotalTimeSeconds>
    <DistanceMeters>500</DistanceMeters>
    <Calories>20</Calories>
    <Intensity>Active</Intensity>
    <TriggerMethod>Distance</TriggerMethod>
    <Track>
     <Trackpoint>
      <Time>2021-04-09T23:29:31Z</Time>
      <DistanceMeters>8</DistanceMeters>
      <SensorState>Present</SensorState>
      <Cadence>0</Cadence>
      <HeartRateBpm>
       <Value>85</Value>
      </HeartRateBpm>
      <Extensions>
       <TPX xmlns="http://www.garmin.com/xmlschemas/ActivityExtension/v2">
        <Watts>0</Watts>
       </TPX>
      </Extensions>
     </Trackpoint>
     </Track>
     </Lap>
   <Lap StartTime="2022-02-02T22:22:22Z">
    <TotalTimeSeconds>136.04</TotalTimeSeconds>
    <DistanceMeters>500</DistanceMeters>
    <Calories>20</Calories>
    <Intensity>Active</Intensity>
    <TriggerMethod>Distance</TriggerMethod>
    <Track>
     <Trackpoint>
      <Time>2021-04-09T23:29:31Z</Time>
      <DistanceMeters>8</DistanceMeters>
      <SensorState>Present</SensorState>
      <Cadence>0</Cadence>
      <HeartRateBpm>
       <Value>85</Value>
      </HeartRateBpm>
      <Extensions>
       <TPX xmlns="http://www.garmin.com/xmlschemas/ActivityExtension/v2">
        <Watts>0</Watts>
       </TPX>
      </Extensions>
     </Trackpoint>
     </Track>
     </Lap>
     </Activity>
         </Activities>

        </x>
    """
    val x = readXmlString<TrainingCenterDatabase>(xs)
    println("activity=$x")
}

private fun tcxActivity() {
    val xs = """
    <x>
   <Lap StartTime="2021-04-09T23:29:28Z">
    <TotalTimeSeconds>136.04</TotalTimeSeconds>
    <DistanceMeters>500</DistanceMeters>
    <Calories>20</Calories>
    <Intensity>Active</Intensity>
    <TriggerMethod>Distance</TriggerMethod>
    <Track>
     <Trackpoint>
      <Time>2021-04-09T23:29:31Z</Time>
      <DistanceMeters>8</DistanceMeters>
      <SensorState>Present</SensorState>
      <Cadence>0</Cadence>
      <HeartRateBpm>
       <Value>85</Value>
      </HeartRateBpm>
      <Extensions>
       <TPX xmlns="http://www.garmin.com/xmlschemas/ActivityExtension/v2">
        <Watts>0</Watts>
       </TPX>
      </Extensions>
     </Trackpoint>
     </Track>
     </Lap>
   <Lap StartTime="2022-02-02T22:22:22Z">
    <TotalTimeSeconds>136.04</TotalTimeSeconds>
    <DistanceMeters>500</DistanceMeters>
    <Calories>20</Calories>
    <Intensity>Active</Intensity>
    <TriggerMethod>Distance</TriggerMethod>
    <Track>
     <Trackpoint>
      <Time>2021-04-09T23:29:31Z</Time>
      <DistanceMeters>8</DistanceMeters>
      <SensorState>Present</SensorState>
      <Cadence>0</Cadence>
      <HeartRateBpm>
       <Value>85</Value>
      </HeartRateBpm>
      <Extensions>
       <TPX xmlns="http://www.garmin.com/xmlschemas/ActivityExtension/v2">
        <Watts>0</Watts>
       </TPX>
      </Extensions>
     </Trackpoint>
     </Track>
     </Lap>
        </x>
    """
    val x = readXmlString<Activity>(xs)
    println("activity=$x")
}

private fun tcxLap() {
    val xs = """
   <Lap StartTime="2021-04-09T23:29:28Z">
    <TotalTimeSeconds>136.04</TotalTimeSeconds>
    <DistanceMeters>500</DistanceMeters>
    <Calories>20</Calories>
    <Intensity>Active</Intensity>
    <TriggerMethod>Distance</TriggerMethod>
    <Track>
     <Trackpoint>
      <Time>2021-04-09T23:29:31Z</Time>
      <DistanceMeters>8</DistanceMeters>
      <SensorState>Present</SensorState>
      <Cadence>0</Cadence>
      <HeartRateBpm>
       <Value>85</Value>
      </HeartRateBpm>
      <Extensions>
       <TPX xmlns="http://www.garmin.com/xmlschemas/ActivityExtension/v2">
        <Watts>0</Watts>
       </TPX>
      </Extensions>
     </Trackpoint>
     <Trackpoint>
      <Time>2021-04-09T23:29:34Z</Time>
      <DistanceMeters>18</DistanceMeters>
      <SensorState>Present</SensorState>
      <Cadence>20.5</Cadence>
      <HeartRateBpm>
       <Value>84</Value>
      </HeartRateBpm>
      <Extensions>
       <TPX xmlns="http://www.garmin.com/xmlschemas/ActivityExtension/v2">
        <Watts>119</Watts>
       </TPX>
      </Extensions>
     </Trackpoint>
     </Track>
     </Lap>
    """
    val x = readXmlString<Lap>(xs)
    println("lap=$x")
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
    println("trackpoint=$x")
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

private fun convertSRToFitCsv() {
    val smartRowDataList =
        readCsvFile<SmartRowData>("/Users/dominic.godwin/Developer/FitSDKRelease_21.53.00/dominic/2021-04-09T232928_5946m.csv")

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

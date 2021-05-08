import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
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


// timestamp,988241857,s,heart_rate,87,bpm,cadence,0,rpm,distance,4,m,speed,2.016,m/s,power,23,watts
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
                "${String.format("%.3f", speed_m_per_s)},m/s,power,$power_W,watts"

    companion object {
        const val definition = "Definition,6,record,timestamp,1,,heart_rate,1,,cadence,1,,distance,1,,speed,1,,power,1,"
    }
}


fun main() {
    println("hello")

    val smartRowDataList =
        readCsvFile<SmartRowData>("/Users/dominic.godwin/Developer/FitSDKRelease_21.53.00/dominic/SR1.csv")

    println(smartRowDataList.joinToString("\n"))

    val fitRecords = smartRowDataList.map(SmartRowData::toFitRecord)

    println(FitRecord.definition)

    fitRecords.forEach {
        println(it.data)
    }

    println("bye")
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

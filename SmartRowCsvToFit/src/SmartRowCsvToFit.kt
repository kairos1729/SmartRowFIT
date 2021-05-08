import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.cfg.CoercionConfig
import com.fasterxml.jackson.databind.cfg.CoercionConfigs
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
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


fun main() {
    println("hello")

    val x = readCsvFile<SmartRowData>("/Users/dominic.godwin/Developer/FitSDKRelease_21.53.00/dominic/SR1.csv")

    println(x.joinToString("\n"))

    println("bye")
}


val csvMapper = CsvMapper().apply {
    registerModule(KotlinModule())
}

inline fun <reified T> readCsvFile(fileName: String): List<T> {
    FileReader(fileName).use { reader ->
        return csvMapper
            .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
            .readerFor(T::class.java)
            .with(csvMapper.schemaFor(T::class.java).withSkipFirstDataRow(true).withNullValue(" "))
            .readValues<T>(reader)
            .readAll()
            .toList()
    }
}

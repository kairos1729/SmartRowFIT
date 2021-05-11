import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.FileReader

val csvMapper = CsvMapper().apply {
    registerModule(KotlinModule())
    disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
}

inline fun <reified T> readCsvFile(fileName: String): List<T> {
    FileReader(fileName).use { reader ->
        return csvMapper
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

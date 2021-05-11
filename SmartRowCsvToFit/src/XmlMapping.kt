import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.FileReader

val xmlMapper = XmlMapper().apply {
    registerKotlinModule()
}

inline fun <reified T> readXmlFile(fileName: String): T =
    FileReader(fileName).use { reader ->
        xmlMapper.readValue(reader, T::class.java)
    }

inline fun <reified T> readXmlString(xml: String): T =
    xmlMapper.readValue(xml, T::class.java)

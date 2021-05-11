import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class TrainingCenterDatabase(
    @JsonProperty("Activities") val activities: List<Activity>
) {
    companion object {
        fun from(tcxFileName: String) = readXmlFile<TrainingCenterDatabase>(tcxFileName)
    }
}

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

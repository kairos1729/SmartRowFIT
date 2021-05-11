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

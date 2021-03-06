data class FitActivity(
    val timestamp_garminEpochSeconds: Long,
    val totalTimerTime_s: Double,
    val numSessions: Int,
    val type: Int,
    val event: Int
) {
    val data =
        "Data,2,activity,timestamp,$timestamp_garminEpochSeconds,,total_timer_time,$totalTimerTime_s,s," +
                "num_sessions,n$numSessions,,type,$type,,event,$event,,,,,,,,,,,,,,,,,,,,,,,"

    companion object {
        const val definition =
            "Definition,2,activity,timestamp,1,,total_timer_time,1,,num_sessions,1,,type,1,,event,1,,,,,,,,,,,,,,,,,,,,,,,"
    }
}

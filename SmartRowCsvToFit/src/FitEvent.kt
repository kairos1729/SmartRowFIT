data class FitEvent(
    val timestamp_garminEpochSeconds: Int,
    val event: Int,
    val eventType: Int,
    val timerTrigger: Int,
    val eventGroup: Int,
) {
    val data =
        "Data,4,event,timestamp,$timestamp_garminEpochSeconds,s,event,$event,," +
                "event_type,$eventType,," +
                "timer_trigger,$timerTrigger,," +
                "event_group,$eventGroup,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"

    companion object {
        const val definition =
            "Definition,4,event,timestamp,1,,event,1,,event_type,1,," +
                    "data,1,,event_group,1,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"
    }
}

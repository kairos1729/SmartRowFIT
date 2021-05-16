data class FitSession(
    val timestamp_garminEpochSeconds: Int,
    val startTime_garminEpochSeconds: Int,
    val sport: Int,
    val subSport: Int,
    val totalElapsedTime_s: Double,
    val totalTimerTime_s: Double,
    val totalDistance_s: Double,
    val totalCycles: Int,
    val totalCalories_kCal: Int,
    val averageSpeed_mPerSec: Double,
    val maxSpeed_mPerSec: Double,
    val averageHeartRate_bpm: Int,
    val maxHeartRate_bpm: Int,
    val averageCadence_rpm: Int,
    val maxCadence_rpm: Int,
    val averagePower_watts: Int,
    val maxPower_watts: Int,
    val firstLapIndex: Int,
    val numLaps: Int,
    val messageIndex: Int,
    val enhancedAverageSpeed_mPerSec: Double,
    val enhancedMaxSpeed_mPerSec: Double
) {
    val data =
        "Data,3,session,timestamp,\"988241855\",s,event,\"0\",,event_type,\"0\",,start_time,\"988241855\",," +
                "sport,\"4\",,sub_sport,\"14\",,total_elapsed_time,\"264.1\",s,total_timer_time,\"264.1\",s," +
                "total_distance,\"1147.99\",m,total_cycles,\"130\",cycles,total_calories,\"80\",kcal," +
                "avg_speed,\"4.346\",m/s,max_speed,\"4.587\",m/s,avg_heart_rate,\"121\",bpm,max_heart_rate,\"151\"," +
                "bpm,avg_cadence,\"27\",rpm,max_cadence,\"92\",rpm,avg_power,\"230\",watts,max_power,\"270\",watts," +
                "first_lap_index,\"0\",,num_laps,\"3\",,message_index,\"0\",,enhanced_avg_speed,\"4.346\",m/s," +
                "enhanced_max_speed,\"4.587\",m/s,"

    companion object {
        const val definition =
            "Definition,3,session,timestamp,1,,event,1,,event_type,1,,start_time,1,,sport,1,,sub_sport,1,," +
                    "total_elapsed_time,1,,total_timer_time,1,,total_distance,1,,total_cycles,1,," +
                    "total_calories,1,,avg_speed,1,,max_speed,1,,avg_heart_rate,1,,max_heart_rate,1,," +
                    "avg_cadence,1,,max_cadence,1,,avg_power,1,,max_power,1,,first_lap_index,1,," +
                    "num_laps,1,,message_index,1,,"
    }
}

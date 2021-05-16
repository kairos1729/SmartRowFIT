data class FitSession(
    val timestamp_garminEpochSeconds: Int,
    val event: Int,
    val eventType: Int,
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
        "Data,3,session,timestamp,$timestamp_garminEpochSeconds,s,event,$event,,event_type,$eventType,," +
                "start_time,$startTime_garminEpochSeconds,," +
                "sport,$sport,,sub_sport,$subSport,,total_elapsed_time,$totalElapsedTime_s,s," +
                "total_timer_time,$totalTimerTime_s,s," +
                "total_distance,$totalDistance_s,m,total_cycles,$totalCycles,cycles," +
                "total_calories,$totalCalories_kCal,kcal," +
                "avg_speed,$averageSpeed_mPerSec,m/s,max_speed,$maxSpeed_mPerSec,m/s," +
                "avg_heart_rate,$averageHeartRate_bpm,bpm,max_heart_rate,$maxHeartRate_bpm," +
                "bpm,avg_cadence,$averageCadence_rpm,rpm,max_cadence,$maxCadence_rpm,rpm," +
                "avg_power,$averagePower_watts,watts,max_power,$maxPower_watts,watts," +
                "first_lap_index,$firstLapIndex,,num_laps,$numLaps,," +
                "message_index,$messageIndex,,enhanced_avg_speed,$enhancedAverageSpeed_mPerSec,m/s," +
                "enhanced_max_speed,$enhancedMaxSpeed_mPerSec,m/s,"

    companion object {
        const val definition =
            "Definition,3,session,timestamp,1,,event,1,,event_type,1,,start_time,1,,sport,1,,sub_sport,1,," +
                    "total_elapsed_time,1,,total_timer_time,1,,total_distance,1,,total_cycles,1,," +
                    "total_calories,1,,avg_speed,1,,max_speed,1,,avg_heart_rate,1,,max_heart_rate,1,," +
                    "avg_cadence,1,,max_cadence,1,,avg_power,1,,max_power,1,,first_lap_index,1,," +
                    "num_laps,1,,message_index,1,,"
    }
}

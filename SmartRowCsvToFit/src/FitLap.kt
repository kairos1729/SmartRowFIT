data class FitLap(
    val timestamp_garminEpochSeconds: Int,
    val event: Int,
    val eventType: Int,
    val startTime_garminEpochSeconds: Int,
    val sport: Int,
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
    val messageIndex: Int,
    val enhancedAverageSpeed_mPerSec: Double,
    val enhancedMaxSpeed_mPerSec: Double,
    val totalFatCalories_kCal: Int,
    val totalAscent_m: Int,
    val totalDescent_m: Int,
    val intensity: Int,
    val lapTrigger: Int,
) {
    val data =
        "Data,5,lap,timestamp,$timestamp_garminEpochSeconds,s,start_time,$startTime_garminEpochSeconds,," +
                "total_elapsed_time,$totalElapsedTime_s,s," +
                "total_timer_time,$totalTimerTime_s,s,total_distance,$totalDistance_s,m," +
                "total_cycles,$totalCycles,cycles," +
                "message_index,$messageIndex,,total_calories,$totalCalories_kCal,kcal," +
                "total_fat_calories,$totalFatCalories_kCal,kcal," +
                "avg_speed,$averageSpeed_mPerSec,m/s,max_speed,$maxSpeed_mPerSec,m/s," +
                "avg_power,$averagePower_watts,watts," +
                "max_power,$maxPower_watts,watts,total_ascent,$totalAscent_m,m," +
                "total_descent,$totalDescent_m,m," +
                "event,$event,,event_type,$eventType,,avg_heart_rate,$averageHeartRate_bpm,bpm," +
                "max_heart_rate,$maxHeartRate_bpm,bpm,avg_cadence,$averageCadence_rpm," +
                "rpm,max_cadence,$maxCadence_rpm,rpm,intensity,$intensity,,lap_trigger,$lapTrigger,," +
                "sport,$sport,," +
                "enhanced_avg_speed,$enhancedAverageSpeed_mPerSec,m/s," +
                "enhanced_max_speed,$enhancedMaxSpeed_mPerSec,m/s,\n"

    companion object {
        const val definition =
            "Definition,5,lap,timestamp,1,,start_time,1,,total_elapsed_time,1,,total_timer_time,1,," +
                    "total_distance,1,,total_cycles,1,,message_index,1,,total_calories,1,," +
                    "total_fat_calories,1,,avg_speed,1,,max_speed,1,,avg_power,1,," +
                    "max_power,1,,total_ascent,1,,total_descent,1,," +
                    "event,1,,event_type,1,,avg_heart_rate,1,,max_heart_rate,1,,avg_cadence,1,," +
                    "max_cadence,1,,intensity,1,,lap_trigger,1,,sport,1,,"
    }
}

//Definition,5,lap,timestamp,1,,start_time,1,,total_elapsed_time,1,,total_timer_time,1,,total_distance,1,,total_cycles,1,,message_index,1,,total_calories,1,,total_fat_calories,1,,avg_speed,1,,max_speed,1,,avg_power,1,,max_power,1,,total_ascent,1,,total_descent,1,,event,1,,event_type,1,,avg_heart_rate,1,,max_heart_rate,1,,avg_cadence,1,,max_cadence,1,,intensity,1,,lap_trigger,1,,sport,1,,
//Data,5,lap,timestamp,"988242005",s,start_time,"988241855",,total_elapsed_time,"149.4",s,total_timer_time,"149.4",s,total_distance,"499.99",m,total_cycles,"0",cycles,message_index,"0",,total_calories,"0",kcal,total_fat_calories,"0",kcal,avg_speed,"3.346",m/s,max_speed,"0.0",m/s,avg_power,"105",watts,max_power,"0",watts,total_ascent,"0",m,total_descent,"0",m,event,"0",,event_type,"0",,avg_heart_rate,"0",bpm,max_heart_rate,"0",bpm,avg_cadence,"22",rpm,max_cadence,"0",rpm,intensity,"0",,lap_trigger,"0",,sport,"0",,enhanced_avg_speed,"3.346",m/s,enhanced_max_speed,"0.0",m/s,

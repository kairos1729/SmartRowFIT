private const val MANUFACTURER_ID_WATERROWER = 118
private const val SPORT_FITNESS_EQUIPMENT = 4
private const val SUB_SPORT_INDOOR_ROWING = 14

private const val EVENT_TIMER = 0
private const val EVENT_TYPE_START = 0
private const val EVENT_TYPE_STOP = 1

class SmartRowTcxCsvToFitConverter(tcxPath: String, csvPath: String) {
    private val tcx = TrainingCenterDatabase.from(tcxPath)

    private val csvRows = SmartRowCsvData.rowsFrom(csvPath)

    fun convert(): String {
        return ""
    }

    fun header() = FitHeader.definition
    private fun fileIdDefinition() = FitFileId.definition

    private fun fileIdData() = FitFileId(MANUFACTURER_ID_WATERROWER).data
    private fun deviceInfoDefinition() = FitDeviceInfo.definition

    private fun deviceInfoData() = FitDeviceInfo(MANUFACTURER_ID_WATERROWER).data
    private fun activityDefinition() = FitActivity.definition

    private fun activityData() =
        FitActivity(
            timestamp_garminEpochSeconds = tcx.startTime_garminEpochSeconds(),
            totalTimerTime_s = 0.001,
            numSessions = 0,
            type = 3,
            event = 1
        ).data

    private fun sessionDefinition() = FitSession.definition
    private fun sessionData() = FitSession(
        timestamp_garminEpochSeconds = tcx.startTime_garminEpochSeconds().toInt(),
        event = EVENT_TIMER,
        eventType = EVENT_TYPE_START,
        startTime_garminEpochSeconds = tcx.startTime_garminEpochSeconds().toInt(),
        sport = SPORT_FITNESS_EQUIPMENT,
        subSport = SUB_SPORT_INDOOR_ROWING,
        totalElapsedTime_s = tcx.totalElapsedTime_s(),
        totalTimerTime_s = tcx.totalElapsedTime_s(),
        totalDistance_m = tcx.totalDistance_m().toDouble(),
        totalCycles = csvRows.totalNumberOfStrokes(),
        totalCalories_kCal = tcx.totalCalories_kCal(),
        averageSpeed_mPerSec = tcx.averageSpeed_mPerSec(),
        maxSpeed_mPerSec = csvRows.maxSpeed_mPerSec(),
        averageHeartRate_bpm = csvRows.averageHeartRate_bpm(),
        maxHeartRate_bpm = csvRows.maxHeartRate_bpm(),
        averageCadence_rpm = csvRows.averageStrokeRate_spm(),
        maxCadence_rpm = csvRows.maxStrokeRate_spm().toInt(),
        averagePower_watts = csvRows.averagePower_W().toInt(),
        maxPower_watts = csvRows.maxPower_W(),
        firstLapIndex = 0,
        numLaps = tcx.laps.count(),
        messageIndex = 0,
        enhancedAverageSpeed_mPerSec = tcx.averageSpeed_mPerSec(),
        enhancedMaxSpeed_mPerSec = csvRows.maxSpeed_mPerSec()
    )

    private fun eventDefinition() = FitEvent.definition

    private fun startEvent() = FitEvent(
        timestamp_garminEpochSeconds = tcx.startTime_garminEpochSeconds().toInt(),
        event = EVENT_TIMER,
        eventType = EVENT_TYPE_START,
        timerTrigger = 0,
        eventGroup = 0
    )
    
    private fun endEvent() = FitEvent(
        timestamp_garminEpochSeconds =
        tcx.endTime_garminEpochSeconds().toInt(),
        event = EVENT_TIMER,
        eventType = EVENT_TYPE_STOP,
        timerTrigger = 0,
        eventGroup = 0
    )
}


private fun TrainingCenterDatabase.startTime_garminEpochSeconds() =
    laps.first().startTime_garminEpochSecond

private fun TrainingCenterDatabase.endTime_garminEpochSeconds() =
    startTime_garminEpochSeconds() + totalElapsedTime_s()

private val TrainingCenterDatabase.laps get() = activities.first().laps

private fun TrainingCenterDatabase.totalElapsedTime_s() =
    laps.sumOf { it.totalTime_s }

private fun TrainingCenterDatabase.totalDistance_m() =
    laps.sumOf { it.distance_m }

private fun TrainingCenterDatabase.averageSpeed_mPerSec() =
    totalDistance_m() / totalElapsedTime_s()

private fun TrainingCenterDatabase.totalCalories_kCal() =
    laps.sumOf { it.calories }

private fun List<SmartRowCsvData>.totalNumberOfStrokes() =
    last().strokes

private fun List<SmartRowCsvData>.minSplit_s() =
    minOf { it.actualSplit_s }

private fun List<SmartRowCsvData>.maxSpeed_mPerSec() =
    500.0 / minSplit_s().toDouble()

private fun List<SmartRowCsvData>.measurementTimes_s() =
    map { it.seconds }

private fun List<SmartRowCsvData>.intervalsBetweenMeasurements_s() =
    measurementTimes_s().zipWithNext { time, nextTime -> nextTime - time }

private fun List<SmartRowCsvData>.heartRates_bpm() =
    drop(1).map { it.heartRate_bpm }

private fun List<SmartRowCsvData>.numberOfHeartBeatsPerIntervalPerMinute() =
    heartRates_bpm().zip(intervalsBetweenMeasurements_s()) { bpm, seconds -> bpm * seconds }

private fun List<SmartRowCsvData>.numberOfHeartBeatsPerIntervalPerMinuteSum() =
    numberOfHeartBeatsPerIntervalPerMinute().sum()

private fun List<SmartRowCsvData>.intervalsSum_s() =
    last().seconds - first().seconds

private fun List<SmartRowCsvData>.averageHeartRate_bpm() =
    numberOfHeartBeatsPerIntervalPerMinuteSum() / intervalsSum_s()

private fun List<SmartRowCsvData>.maxHeartRate_bpm() =
    heartRates_bpm().maxOrNull() ?: 0

private fun List<SmartRowCsvData>.averageStrokeRate_spm() =
    60 * totalNumberOfStrokes() / last().seconds

private fun List<SmartRowCsvData>.maxStrokeRate_spm() =
    this.maxOf { it.strokeRate_spm }

private fun List<SmartRowCsvData>.averagePower_W() = last().averagePower_W

private fun List<SmartRowCsvData>.maxPower_W() = maxOf { it.actualPower_W }







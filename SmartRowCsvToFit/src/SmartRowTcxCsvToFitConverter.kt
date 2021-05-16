private const val MANUFACTURER_ID_WATERROWER = 118
private const val SPORT_FITNESS_EQUIPMENT = 4
private const val SUB_SPORT_INDOOR_ROWING = 14

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
tcx.startTime_garminEpochSeconds(),
        0,
        0,
        tcx.startTime_garminEpochSeconds(),
        SPORT_FITNESS_EQUIPMENT,
        SUB_SPORT_INDOOR_ROWING,
    )


    @Suppress("FunctionName")
    private fun TrainingCenterDatabase.startTime_garminEpochSeconds() =
        activities.first().laps.first().startTime_garminEpochSecond
}

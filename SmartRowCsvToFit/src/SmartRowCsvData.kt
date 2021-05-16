data class SmartRowCsvData(
    val strokes: Int = 0,
    val seconds: Int = 0,
    val distance_m: Int = 0,
    val work_J: Double = 0.0,
    val actualPower_W: Int = 0,
    val averagePower_W: Double = 0.0,
    val actualSplit_s: Int = 0,
    val averageSplit_s: Int = 0,
    val strokeRate_spm: Double = 0.0,
    val heartRate_bpm: Int = 0
) {
    companion object {
        fun rowsFrom(csvFileName: String) = readCsvFile<SmartRowCsvData>(csvFileName)
    }
}


import kotlin.math.roundToInt


fun main() {
//    val path = Paths.get("").toAbsolutePath().toString()
//    println(path)
//    val sa = arrayOf("")
//    val declaredConstructor = CSVTool::class.java.getDeclaredConstructor()
//    declaredConstructor.isAccessible = true
//    val x = declaredConstructor.newInstance()
//    val method: Method = CSVTool::class.java.getDeclaredMethod("run", sa.javaClass)
//    method.isAccessible = true
//    method.invoke(x, arrayOf("a","-b", "dominic/blah.fit", "dominic/coocoo.csv", "-u", "-re"))
    readTcxAndCsvSR()
    convertSRToFitCsv()
}




private fun readTcxAndCsvSR() {
    val tcx =
        TrainingCenterDatabase.from("/Users/dominic.godwin/Developer/FitSDKRelease_21.53.00/dominic/SR1.tcx")

    val csvRows =
        SmartRowData.rowsFrom("/Users/dominic.godwin/Developer/FitSDKRelease_21.53.00/dominic/SR1.csv")

    val timestamps = listOf(tcx.mapLaps { startTime_garminEpochSecond }.first()) + tcx.mapTrackpoints { time_garminEpochSecond }

    val last = timestamps.last() + tcx.activities.last().laps.last().totalTime_s.toLong()
    val allTimestamps = timestamps + listOf(last)


    println(allTimestamps.count())
    println(allTimestamps)
    println(allTimestamps.last() - allTimestamps.first())

    val lapTimes = tcx.mapLaps { totalTime_s }
    val totalTime = lapTimes.sum()

    println(lapTimes)
    println(totalTime)
    println("${totalTime.roundToInt() / 60}:${totalTime.roundToInt() % 60}")

    val distances = tcx.mapTrackpoints { distance_m }

    println(distances)
    println(distances.last())

    val lapDistances = tcx.mapLaps { distance_m }

    val totalDistance = lapDistances.sum()
    println(lapDistances)
    println(totalDistance)


    val calories = tcx.mapLaps { calories }
    println(calories)
    println(calories.sum())

    val work = csvRows.sumByDouble { it.work_J }
    val time_s = csvRows.last().seconds - csvRows.first().seconds
    val averagePower = work / time_s

    println("csv: work=$work, time=$time_s, average power from work = $averagePower")
    val averagepowerW = csvRows.last().averagePower_W
    println("csv: average power = $averagepowerW")
    println("csv: peak power = ${csvRows.maxOf(SmartRowData::actualPower_W)}")

    val csvSeconds = csvRows.map { it.seconds }
    val intervals = csvSeconds.zipWithNext { a, b -> b - a }

    println("csv: $csvSeconds")
    println("csv: $intervals")

    val heartRatesCsv = csvRows.map { it.heartRate_bpm }.drop(1)
    val heartBeatsSixtieths = heartRatesCsv.zip(intervals) { r, i -> r * i }
    val totalHeartBeatsSixtieths = heartBeatsSixtieths.sum()
    val aveHeartRateBpm = totalHeartBeatsSixtieths / time_s

    println(heartRatesCsv)
    println(heartBeatsSixtieths)
    println(totalHeartBeatsSixtieths)
    println(aveHeartRateBpm)

    val tcxintervals = allTimestamps.zipWithNext { a, b -> b - a }
    println(tcxintervals)
    val tcxheartrates = tcx.mapTrackpoints { heartRate_bpm.value }
    val tcxheartBeatsSixtieths = tcxheartrates.zip(tcxintervals) { r, i -> r * i }
    val tcxtotalHeartBeatsSixtieths = tcxheartBeatsSixtieths.sum()
    val tcxaveHeartRateBpm = tcxtotalHeartBeatsSixtieths / time_s

    println(tcxheartrates)
    println(tcxheartBeatsSixtieths)
    println(tcxtotalHeartBeatsSixtieths)
    println(tcxaveHeartRateBpm)

    val workPerSixtiethHB = work / totalHeartBeatsSixtieths
    println(workPerSixtiethHB)

//    txcTest()
}

private fun txcTest() {
    tcxTpx()
    tcxExtensions()
    tcxTrackpoint()
    tcxLap()
    tcxActivity()
    tcxTrainingCenterDatabase()
}

private fun tcxTrainingCenterDatabase() {
    val xs = """
    <x>
    <Activities>
     <Activity>
   <Lap StartTime="2000-00-00T00:00:00Z">
    <TotalTimeSeconds>136.04</TotalTimeSeconds>
    <DistanceMeters>500</DistanceMeters>
    <Calories>20</Calories>
    <Intensity>Active</Intensity>
    <TriggerMethod>Distance</TriggerMethod>
    <Track>
     <Trackpoint>
      <Time>2021-04-09T23:29:31Z</Time>
      <DistanceMeters>8</DistanceMeters>
      <SensorState>Present</SensorState>
      <Cadence>0</Cadence>
      <HeartRateBpm>
       <Value>85</Value>
      </HeartRateBpm>
      <Extensions>
       <TPX xmlns="http://www.garmin.com/xmlschemas/ActivityExtension/v2">
        <Watts>0</Watts>
       </TPX>
      </Extensions>
     </Trackpoint>
     </Track>
     </Lap>
   <Lap StartTime="2022-02-02T22:22:22Z">
    <TotalTimeSeconds>136.04</TotalTimeSeconds>
    <DistanceMeters>500</DistanceMeters>
    <Calories>20</Calories>
    <Intensity>Active</Intensity>
    <TriggerMethod>Distance</TriggerMethod>
    <Track>
     <Trackpoint>
      <Time>2021-04-09T23:29:31Z</Time>
      <DistanceMeters>8</DistanceMeters>
      <SensorState>Present</SensorState>
      <Cadence>0</Cadence>
      <HeartRateBpm>
       <Value>85</Value>
      </HeartRateBpm>
      <Extensions>
       <TPX xmlns="http://www.garmin.com/xmlschemas/ActivityExtension/v2">
        <Watts>0</Watts>
       </TPX>
      </Extensions>
     </Trackpoint>
     </Track>
     </Lap>
     </Activity>
     <Activity>
   <Lap StartTime="2021-04-09T23:29:28Z">
    <TotalTimeSeconds>136.04</TotalTimeSeconds>
    <DistanceMeters>500</DistanceMeters>
    <Calories>20</Calories>
    <Intensity>Active</Intensity>
    <TriggerMethod>Distance</TriggerMethod>
    <Track>
     <Trackpoint>
      <Time>2021-04-09T23:29:31Z</Time>
      <DistanceMeters>8</DistanceMeters>
      <SensorState>Present</SensorState>
      <Cadence>0</Cadence>
      <HeartRateBpm>
       <Value>85</Value>
      </HeartRateBpm>
      <Extensions>
       <TPX xmlns="http://www.garmin.com/xmlschemas/ActivityExtension/v2">
        <Watts>0</Watts>
       </TPX>
      </Extensions>
     </Trackpoint>
     </Track>
     </Lap>
   <Lap StartTime="2022-02-02T22:22:22Z">
    <TotalTimeSeconds>136.04</TotalTimeSeconds>
    <DistanceMeters>500</DistanceMeters>
    <Calories>20</Calories>
    <Intensity>Active</Intensity>
    <TriggerMethod>Distance</TriggerMethod>
    <Track>
     <Trackpoint>
      <Time>2021-04-09T23:29:31Z</Time>
      <DistanceMeters>8</DistanceMeters>
      <SensorState>Present</SensorState>
      <Cadence>0</Cadence>
      <HeartRateBpm>
       <Value>85</Value>
      </HeartRateBpm>
      <Extensions>
       <TPX xmlns="http://www.garmin.com/xmlschemas/ActivityExtension/v2">
        <Watts>0</Watts>
       </TPX>
      </Extensions>
     </Trackpoint>
     </Track>
     </Lap>
     </Activity>
         </Activities>

        </x>
    """
    val x = readXmlString<TrainingCenterDatabase>(xs)
    println("activity=$x")
}

private fun tcxActivity() {
    val xs = """
    <x>
   <Lap StartTime="2021-04-09T23:29:28Z">
    <TotalTimeSeconds>136.04</TotalTimeSeconds>
    <DistanceMeters>500</DistanceMeters>
    <Calories>20</Calories>
    <Intensity>Active</Intensity>
    <TriggerMethod>Distance</TriggerMethod>
    <Track>
     <Trackpoint>
      <Time>2021-04-09T23:29:31Z</Time>
      <DistanceMeters>8</DistanceMeters>
      <SensorState>Present</SensorState>
      <Cadence>0</Cadence>
      <HeartRateBpm>
       <Value>85</Value>
      </HeartRateBpm>
      <Extensions>
       <TPX xmlns="http://www.garmin.com/xmlschemas/ActivityExtension/v2">
        <Watts>0</Watts>
       </TPX>
      </Extensions>
     </Trackpoint>
     </Track>
     </Lap>
   <Lap StartTime="2022-02-02T22:22:22Z">
    <TotalTimeSeconds>136.04</TotalTimeSeconds>
    <DistanceMeters>500</DistanceMeters>
    <Calories>20</Calories>
    <Intensity>Active</Intensity>
    <TriggerMethod>Distance</TriggerMethod>
    <Track>
     <Trackpoint>
      <Time>2021-04-09T23:29:31Z</Time>
      <DistanceMeters>8</DistanceMeters>
      <SensorState>Present</SensorState>
      <Cadence>0</Cadence>
      <HeartRateBpm>
       <Value>85</Value>
      </HeartRateBpm>
      <Extensions>
       <TPX xmlns="http://www.garmin.com/xmlschemas/ActivityExtension/v2">
        <Watts>0</Watts>
       </TPX>
      </Extensions>
     </Trackpoint>
     </Track>
     </Lap>
        </x>
    """
    val x = readXmlString<Activity>(xs)
    println("activity=$x")
}

private fun tcxLap() {
    val xs = """
   <Lap StartTime="2021-04-09T23:29:28Z">
    <TotalTimeSeconds>136.04</TotalTimeSeconds>
    <DistanceMeters>500</DistanceMeters>
    <Calories>20</Calories>
    <Intensity>Active</Intensity>
    <TriggerMethod>Distance</TriggerMethod>
    <Track>
     <Trackpoint>
      <Time>2021-04-09T23:29:31Z</Time>
      <DistanceMeters>8</DistanceMeters>
      <SensorState>Present</SensorState>
      <Cadence>0</Cadence>
      <HeartRateBpm>
       <Value>85</Value>
      </HeartRateBpm>
      <Extensions>
       <TPX xmlns="http://www.garmin.com/xmlschemas/ActivityExtension/v2">
        <Watts>0</Watts>
       </TPX>
      </Extensions>
     </Trackpoint>
     <Trackpoint>
      <Time>2021-04-09T23:29:34Z</Time>
      <DistanceMeters>18</DistanceMeters>
      <SensorState>Present</SensorState>
      <Cadence>20.5</Cadence>
      <HeartRateBpm>
       <Value>84</Value>
      </HeartRateBpm>
      <Extensions>
       <TPX xmlns="http://www.garmin.com/xmlschemas/ActivityExtension/v2">
        <Watts>119</Watts>
       </TPX>
      </Extensions>
     </Trackpoint>
     </Track>
     </Lap>
    """
    val x = readXmlString<Lap>(xs)
    println("lap=$x")
}

private fun tcxTrackpoint() {
    val xs = """
        <x>
      <Time>2021-04-09T23:55:47Z</Time>
      <DistanceMeters>5507</DistanceMeters>
      <SensorState>Present</SensorState>
      <Cadence>31.7</Cadence>
      <HeartRateBpm>
       <Value>142</Value>
      </HeartRateBpm>
      <Extensions>
       <TPX xmlns="http://www.garmin.com/xmlschemas/ActivityExtension/v2">
        <Watts>197</Watts>
       </TPX>
      </Extensions>        
        </x>
    """
    val x = readXmlString<Trackpoint>(xs)
    println("trackpoint=$x")
}

private fun tcxExtensions() {
    val xs = "<x><TPX><Watts>120</Watts></TPX></x>"
    val x = readXmlString<Extensions>(xs)
    println("ext=$x")
}

private fun tcxTpx() {
    val xs = "<x><Watts>101</Watts></x>"
    val x = readXmlString<TPX>(xs)
    println("txp=$x")
}

private fun convertSRToFitCsv() {
    val smartRowDataList =
        readCsvFile<SmartRowData>("/Users/dominic.godwin/Developer/FitSDKRelease_21.53.00/dominic/2021-04-09T232928_5946m.csv")

    val fitRecords = smartRowDataList.map(SmartRowData::toFitRecord)


    println("""Type,Local Number,Message,Field 1,Value 1,Units 1,Field 2,Value 2,Units 2,Field 3,Value 3,Units 3,Field 4,Value 4,Units 4,Field 5,Value 5,Units 5,Field 6,Value 6,Units 6,Field 7,Value 7,Units 7,Field 8,Value 8,Units 8,Field 9,Value 9,Units 9,Field 10,Value 10,Units 10,Field 11,Value 11,Units 11,Field 12,Value 12,Units 12,Field 13,Value 13,Units 13,Field 14,Value 14,Units 14,Field 15,Value 15,Units 15,Field 16,Value 16,Units 16,Field 17,Value 17,Units 17,Field 18,Value 18,Units 18,Field 19,Value 19,Units 19,Field 20,Value 20,Units 20,Field 21,Value 21,Units 21,Field 22,Value 22,Units 22,Field 23,Value 23,Units 23,Field 24,Value 24,Units 24""")

    println(FitDeviceInfo.definition)
    println(FitDeviceInfo.data)
    println(FitFileId.definition)
    println(FitFileId.data)


    println("""Definition,2,activity,timestamp,1,,total_timer_time,1,,num_sessions,1,,type,1,,event,1,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,""")
    println("""Data,2,activity,timestamp,0,,total_timer_time,0.001,s,num_sessions,0,,type,3,,event,1,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,""")

    println("""Definition,3,session,timestamp,1,,event,1,,event_type,1,,start_time,1,,sport,1,,sub_sport,1,,total_elapsed_time,1,,total_timer_time,1,,total_distance,1,,total_cycles,1,,total_calories,1,,avg_speed,1,,max_speed,1,,avg_heart_rate,1,,max_heart_rate,1,,avg_cadence,1,,max_cadence,1,,avg_power,1,,max_power,1,,first_lap_index,1,,message_index,1,,,,,,,,,,""")
    println("""Data,3,session,timestamp,0,s,event,0,,event_type,0,,start_time,0,,sport,4,,sub_sport,14,,total_elapsed_time,264.1,s,total_timer_time,264.1,s,total_distance,1147.99,m,total_cycles,130,cycles,total_calories,80,kcal,avg_speed,4.346,m/s,max_speed,4.587,m/s,avg_heart_rate,121,bpm,max_heart_rate,151,bpm,avg_cadence,27,rpm,max_cadence,92,rpm,avg_power,230,watts,max_power,270,watts,first_lap_index,0,,message_index,0,,,,,,,,,,""")

    println("""Definition,4,event,timestamp,1,,event,1,,event_type,1,,data,1,,event_group,1,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,""")
    println("""Data,4,event,timestamp,0,s,event,0,,event_type,0,,timer_trigger,0,,event_group,0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,""")

    println(FitRecord.definition)

    fitRecords.forEach {
        println(it.data)
    }

    println("""Data,4,event,timestamp,247,s,event,0,,event_type,0,,timer_trigger,0,,event_group,0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,""")
}

fun SmartRowData.toFitRecord() = FitRecord(
    timestamp_s = seconds,
    heartRate_bpm = heartRate_bpm,
    cadence_rpm = strokeRate_spm,
    distance_m = distance_m,
    speed_m_per_s = 500.0 / actualSplit_s,
    power_W = actualPower_W
)



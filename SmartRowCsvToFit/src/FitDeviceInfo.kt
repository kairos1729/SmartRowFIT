data class FitDeviceInfo(val manufacturer: Int) {
    val data =
        "Data,1,device_info,timestamp,0,s,device_index,0,,device_type,0,,$manufacturer,118,," +
                "product,0,,software_version,1,,hardware_version,0,," +
                "cum_operating_time,0,s," +
                "battery_voltage,0,V,battery_status,0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"

    companion object {
        const val definition =
            "Definition,1,device_info,timestamp,1,,device_index,1,,device_type,1,,manufacturer,1,," +
                    "serial_number,1,,product,1,,software_version,1,,hardware_version,1,," +
                    "cum_operating_time,1,,battery_voltage,1,," +
                    "battery_status,1,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"
    }
}

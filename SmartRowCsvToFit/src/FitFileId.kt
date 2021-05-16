data class FitFileId(val manufacturer: Int) {
    val data =
        "Data,0,file_id,type,4,,manufacturer,$manufacturer,,product,0,,time_created,0,," +
                "number,0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"

    companion object {
        const val definition =
            "Definition,0,file_id,type,1,,manufacturer,1,,product,1,,serial_number,1,," +
                    "time_created,1,,number,1,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"
    }
}

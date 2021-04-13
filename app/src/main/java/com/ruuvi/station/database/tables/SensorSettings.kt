package com.ruuvi.station.database.tables

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import com.ruuvi.station.database.LocalDatabase
import java.util.*

@Table (
    name = "SensorSettings",
    database = LocalDatabase::class,
    useBooleanGetterSetters = false
)
data class SensorSettings(
    @Column
    @PrimaryKey
    var id: String = "",
    @Column
    var humidityOffset: Double? = null,
    @Column
    var humidityOffsetDate: Date? = null,
    @Column
    var temperatureOffset: Double? = null,
    @Column
    var temperatureOffsetDate: Date? = null,
    @Column
    var pressureOffset: Double? = null,
    @Column
    var pressureOffsetDate: Date? = null
): BaseModel() {
    fun calibrateSensor(sensor: RuuviTagEntity) {
        sensor.temperature += (temperatureOffset ?: 0.0)
        sensor.temperatureOffset = temperatureOffset ?: 0.0

        sensor.pressure?.let { pressure ->
            sensor.pressure = pressure + (pressureOffset ?: 0.0)
        }
        sensor.pressureOffset = pressureOffset ?: 0.0

        sensor.humidity?.let { humidity ->
            sensor.humidity = humidity + (humidityOffset ?: 0.0)
        }
        sensor.humidityOffset = humidityOffset ?: 0.0
    }
}
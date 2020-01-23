package com.ruuvi.station.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.ruuvi.station.bluetooth.interfaces.IRuuviTag;
import com.ruuvi.station.database.LocalDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by elias on 15.9.2017.
 */

@Table(database = LocalDatabase.class)
public class TagSensorReading extends BaseModel {
    @PrimaryKey(autoincrement = true)
    @Column
    public int id;
    @Column
    public String ruuviTagId;
    @Column
    public Date createdAt;
    @Column
    public double temperature;
    @Column
    public double humidity;
    @Column
    public double pressure;
    @Column
    public int rssi;
    @Column
    public double accelX;
    @Column
    public double accelY;
    @Column
    public double accelZ;
    @Column
    public double voltage;
    @Column
    public int dataFormat;
    @Column
    public double txPower;
    @Column
    public int movementCounter;
    @Column
    public int measurementSequenceNumber;

    public TagSensorReading() {
    }

    public TagSensorReading(RuuviTagEntity tag) {
        this.ruuviTagId = tag.getId();
        this.temperature = tag.getTemperature();
        this.humidity = tag.getHumidity();
        this.pressure = tag.getPressure();
        this.rssi = tag.getRssi();
        this.accelX = tag.getAccelX();
        this.accelY = tag.getAccelY();
        this.accelZ = tag.getAccelZ();
        this.voltage = tag.getVoltage();
        this.dataFormat = tag.getDataFormat();
        this.txPower = tag.getTxPower();
        this.movementCounter = tag.getMovementCounter();
        this.measurementSequenceNumber = tag.getMeasurementSequenceNumber();
        this.createdAt = new Date();
    }

    public static List<TagSensorReading> getForTag(String id) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR, -24);
        return SQLite.select()
                .from(TagSensorReading.class)
                .where(TagSensorReading_Table.ruuviTagId.eq(id))
                .and(TagSensorReading_Table.createdAt.greaterThan(cal.getTime()))
                .orderBy(TagSensorReading_Table.createdAt, true)
                .queryList();
    }

    public static List<TagSensorReading> getLatestForTag(String id, int limit) {
        return SQLite.select()
                .from(TagSensorReading.class)
                .where(TagSensorReading_Table.ruuviTagId.eq(id))
                .orderBy(TagSensorReading_Table.id, false)
                .limit(limit)
                .queryList();
    }

    public static void removeOlderThan(int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR, -hours);
        SQLite.delete()
                .from(TagSensorReading.class)
                .where(TagSensorReading_Table.createdAt.lessThan(cal.getTime()))
                .async()
                .execute();
    }

    public static long countAll() {
        return SQLite.selectCountOf().from(TagSensorReading.class).count();
    }
}

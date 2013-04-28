package com.example.ExpLoc.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.*;
import android.os.Bundle;
import android.os.IBinder;

public class LocationService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private LocationManager locationManager;
    private NotificationManager notificationManager;

    private Sensor gravitySensor;
    private Sensor gyroscopeSensor;
    private Sensor accelerometerSensor;
    private Sensor orientationSensor;
    private Sensor proximitySensor;
    private Sensor rotationVectorSensor;
    private Sensor magneticSensor;
    private Sensor pressureSensor;
    private Sensor linearAccelerationSensor;

    private volatile float speedX;
    private volatile float speedY;

    private volatile Location currentLocation;
    private volatile float gravityX;
    private volatile float gravityY;
    private volatile float gravityZ;
    private volatile float gyroscopeX;
    private volatile float gyroscopeY;
    private volatile float gyroscopeZ;
    private volatile float accelerometerX;
    private volatile float accelerometerY;
    private volatile float accelerometerZ;
    private volatile float orientationX;
    private volatile float orientationY;
    private volatile float orientationZ;
    private volatile float proximityX;
    private volatile float proximityY;
    private volatile float proximityZ;
    private volatile float rotationVectorX;
    private volatile float rotationVectorY;
    private volatile float rotationVectorZ;
    private volatile float magneticX;
    private volatile float magneticY;
    private volatile float magneticZ;
    private volatile float pressureX;
    private volatile float pressureY;
    private volatile float pressureZ;

    private float timestamp;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        LocationListener locationListener = initLocationListener();
        GpsStatus.Listener listener = createListener();

        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        locationManager.addGpsStatusListener(listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_GAME);

        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME);

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);

        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_GAME);

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_GAME);

        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_GAME);

        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_GAME);

        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_GAME);

        linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(this, linearAccelerationSensor, SensorManager.SENSOR_DELAY_GAME);

        Notification notification = new Notification();
        notification.tickerText = "i-Asigutare";
        notification.defaults = Notification.DEFAULT_ALL;
        notificationManager.notify(0, notification);


    }

    private GpsStatus.Listener createListener() {
        return new GpsStatus.Listener() {
            @Override
            public void onGpsStatusChanged(int i) {
//                while (i != GpsStatus.GPS_EVENT_STARTED) {
//                    try {
//                        Thread.sleep(3600);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                    }
//                }
            }
        };
    }

    private LocationListener initLocationListener() {
        return new LocationListener() {
            public void onLocationChanged(Location location) {
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
    }

    private void makeUseOfNewLocation(Location location) {
        currentLocation = location;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        synchronized (this) {

            setValueGyroscope(sensorEvent);


            if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

                speedX = sensorEvent.values[0];
                speedY = sensorEvent.values[1];

                if ((speedX > 1 || speedX < -1) || (speedY > 1 || speedY < -1)) {

                    System.out.println(currentLocation.toString());

                }
            }
        }
    }

    private float getAltitude(float pressure) {
        return SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressure);
    }

    private void setValueGyroscope(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroscopeX = sensorEvent.values[0];
            gyroscopeY = sensorEvent.values[1];
            gyroscopeZ = sensorEvent.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this, accelerometerSensor);
        sensorManager.unregisterListener(this, gravitySensor);
        sensorManager.unregisterListener(this, gyroscopeSensor);
        sensorManager.unregisterListener(this, magneticSensor);
        sensorManager.unregisterListener(this, orientationSensor);
        sensorManager.unregisterListener(this, pressureSensor);
        sensorManager.unregisterListener(this, proximitySensor);
        sensorManager.unregisterListener(this, linearAccelerationSensor);
        sensorManager.unregisterListener(this, rotationVectorSensor);
    }
}

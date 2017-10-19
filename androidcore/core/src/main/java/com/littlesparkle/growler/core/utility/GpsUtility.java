package com.littlesparkle.growler.core.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;


/*
 * Copyright (C) 2016-2016, The Little-Sparkle Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS-IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class GpsUtility {
    private LocationManager locationManager;
    public static final int GET_GPS_PERMISSION = 2654;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 5124;

    public void isGpsUsable(Activity activity) {
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            activity.startActivityForResult(intent, GET_GPS_PERMISSION);
            return;
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        locationManager.addGpsStatusListener(listener);
    }

    public void isGpsUsable(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.addGpsStatusListener(listener);
    }


    private GpsStatus.Listener listener = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int event) {
            switch (event) {
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    EventBus.getDefault().post(new GpsStatusEntity(0, GpsStatus.GPS_EVENT_FIRST_FIX));
                    break;
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    //获取当前状态
                    GpsStatus gpsStatus = locationManager.getGpsStatus(null);
                    //获取卫星颗数的默认最大值
                    int maxSatellites = gpsStatus.getMaxSatellites();
                    //创建一个迭代器保存所有卫星
                    Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                    int count = 0;
                    while (iters.hasNext() && count <= maxSatellites) {
                        GpsSatellite s = iters.next();
                        if (s.usedInFix()) {
                            count++;
                        }
                    }
                    EventBus.getDefault().post(new GpsStatusEntity(count, GpsStatus.GPS_EVENT_SATELLITE_STATUS));
                    break;
                case GpsStatus.GPS_EVENT_STARTED:
                    EventBus.getDefault().post(new GpsStatusEntity(0, GpsStatus.GPS_EVENT_STARTED));
                    break;
                case GpsStatus.GPS_EVENT_STOPPED:
                    EventBus.getDefault().post(new GpsStatusEntity(0, GpsStatus.GPS_EVENT_STOPPED));
                    break;
            }
        }
    };

    public class GpsStatusEntity {
        private int count;
        private int status;

        public GpsStatusEntity(int count, int status) {
            this.count = count;
            this.status = status;
        }

        public int getCount() {
            return count;
        }

        public int getStatus() {
            return status;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}

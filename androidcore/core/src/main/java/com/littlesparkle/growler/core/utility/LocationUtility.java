package com.littlesparkle.growler.core.utility;

import android.content.Context;
import android.location.LocationManager;

import java.util.List;

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
public class LocationUtility {

    public static boolean hasGPSDevice(Context context) {
        LocationManager locationManager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            List<String> providers = locationManager.getAllProviders();
            if (providers != null) {
                return providers.contains(LocationManager.GPS_PROVIDER);
            }
        }
        return false;
    }

    public static boolean isLocationEnabled(Context context) {
        return (isGpsEnabled(context) || isNetworkEnabled(context));
    }

    public static boolean isNetworkEnabled(Context context) {
        return isEnabledWithType(context, LocationManager.NETWORK_PROVIDER);
    }

    public static boolean isGpsEnabled(Context context) {
        return isEnabledWithType(context, LocationManager.GPS_PROVIDER);
    }

    private static boolean isEnabledWithType(Context context, String type) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(type);
    }
}

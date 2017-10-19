package com.littlesparkle.growler.core.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.f2prateek.rx.preferences.RxSharedPreferences;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

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

public class PrefUtility {

    public static int getInteger(Context context, String key) {
        return getInteger(context, key, 0);
    }

    public static int getInteger(Context context, String key, int defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);
        return rxPreferences.getInteger(key, defaultValue).get();
    }

    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);
        return rxPreferences.getLong(key, defaultValue).get();
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);
        return rxPreferences.getFloat(key, defaultValue).get();
    }

    public static double getDouble(Context context, String key, double defaultValue) {
        return getFloat(context, key, (float) defaultValue);
    }

    public static String getString(Context context, String key) {
        return getString(context, key, "");
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);
        return rxPreferences.getString(key, defaultValue).get();
    }

    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);
        return rxPreferences.getBoolean(key, defaultValue).get();
    }

    public static Subscription observeBool(Context context, String key, Subscriber<Boolean> subscriber) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);
        return rxPreferences
                .getBoolean(key)
                .asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public static void delete(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);
        rxPreferences.getString(key).delete();
    }

    public static void setString(Context context, String key, String value) {
        if (!"".equals(key) && !"".equals(value)) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);
            rxPreferences.getString(key).set(value);
        }
    }

    public static void setInteger(Context context, String key, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);
        rxPreferences.getInteger(key).set(value);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);
        rxPreferences.getBoolean(key).set(value);
    }

    public static void setLong(Context context, String key, long value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);
        rxPreferences.getLong(key).set(value);
    }

    public static void setDouble(Context context, String key, double value) {
        setFloat(context, key, (float) value);
    }

    public static void setFloat(Context context, String key, float value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);
        rxPreferences.getFloat(key).set(value);
    }
}

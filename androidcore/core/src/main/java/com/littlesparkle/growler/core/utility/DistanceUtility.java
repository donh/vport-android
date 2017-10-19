package com.littlesparkle.growler.core.utility;

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

import android.content.Context;

import com.littlesparkle.growler.core.R;

public class DistanceUtility {
    public static String unitConversion(Context context, int distace) {
        String dis;
        if (distace / 1000 < 999) {
            //999000
            dis = String.format("%.1f", distace / 1000f) + context.getString(R.string.kilometre);
        } else if (distace / 1000000 < 999) {
            //999 000 000
            dis = String.format("%.1f", distace / 1000000f) + context.getString(R.string.megameter);
        } else if (distace / 1000000000 < 999) {
            //999 000 000 000
            dis = String.format("%.1f", distace / 1000000000f) + context.getString(R.string.miriadMilimetre);
        } else {
            //999 000 000 000
            dis = String.format("%.1f", distace / 1000000000000f) + context.getString(R.string.ThousandsKilometers);
        }
        return dis;
    }
}


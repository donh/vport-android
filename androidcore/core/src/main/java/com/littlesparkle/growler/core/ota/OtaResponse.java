package com.littlesparkle.growler.core.ota;

import com.littlesparkle.growler.core.http.Response;

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

public class OtaResponse extends Response<OtaResponse.Date> {
    public class Date {
        public OtaPackageInfo ota;

        @Override
        public String toString() {
            return "Date{" +
                    "ota=" + ota +
                    '}';
        }
    }
}

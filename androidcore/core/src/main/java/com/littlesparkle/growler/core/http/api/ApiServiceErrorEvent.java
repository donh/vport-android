package com.littlesparkle.growler.core.http.api;

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

public class ApiServiceErrorEvent {
    private String error_no;
    private String error_msg;

    public ApiServiceErrorEvent(String error_no) {
        this.error_no = error_no;
    }

    public ApiServiceErrorEvent(String error_no, String error_msg) {
        this.error_no = error_no;
        this.error_msg = error_msg;
    }

    public String getError_no() {
        return error_no;
    }

    public String getError_msg() {
        return error_msg;
    }
}

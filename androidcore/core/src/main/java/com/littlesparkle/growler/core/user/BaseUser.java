package com.littlesparkle.growler.core.user;

import android.os.Parcel;

import com.littlesparkle.growler.core.bean.Bean;

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

public class BaseUser extends Bean {
    public int user_id;
    public String name;
    public String mobile;
    public String thumb;
    public String token;

    public BaseUser() {
    }

    protected BaseUser(Parcel in) {
        user_id = in.readInt();
        name = in.readString();
        mobile = in.readString();
        thumb = in.readString();
        token = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(user_id);
        dest.writeString(name);
        dest.writeString(mobile);
        dest.writeString(thumb);
        dest.writeString(token);
    }

    @Override
    public String toString() {
        return "BaseUser{" +
                "user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", thumb='" + thumb + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}

package com.littlesparkle.growler.core.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class MiscUtility {
    //    CMCC:
    //        134 135 136 137 138 139 147 150 151 152 157 158 159 178 182 183 184 187 188
    //    CU:
    //        130 131 132 145 155 156 171 175 176 185 186
    //    CT:
    //        133 149 153 173 177 180 181 189
    //    V:
    //        170
    public static boolean checkPhoneNumber(String phoneNumber) {
        phoneNumber = phoneNumber.trim();
        Pattern p = Pattern
                .compile("^((13[0-9])|(14[5,7,9])|(15[^4,\\D])|(17[0,1,3,5-8])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    public static boolean checkPassword(String password) {
        password = password.trim();
        if (password.length() < 6 || password.length() > 16) {
            return false;
        }

        Pattern p = Pattern
                .compile("^[A-Za-z0-9]+$");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    public static File launchCamera(Activity activity, File file, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, requestCode);
        return file;
    }

    public static void launchGallery(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    public static List<String> fillArrayList(Context context, int[] resArray) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < resArray.length; i++) {
            list.add(context.getString(resArray[i]));
        }
        return list;
    }
}

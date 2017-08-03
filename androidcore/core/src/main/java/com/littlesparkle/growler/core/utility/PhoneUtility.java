package com.littlesparkle.growler.core.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.littlesparkle.growler.core.R;

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
public class PhoneUtility {
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 188;

    public static void CallWithPermission(Activity context, String phoneNumber) {
        if (!PermissionUtility.hasPermissions(context, Manifest.permission.CALL_PHONE)) {
            PermissionUtility.requestPermissionsNoNegativeButton(context, new PermissionUtility.PermissionListener() {
                @Override
                public void onPermissionsGranted(List<String> perms) {

                }

                @Override
                public void onPermissionsDenied(List<String> perms) {

                }

                @Override
                public void onPermissionRequestRejected() {

                }

                @Override
                public void onAllPermissionGranted() {

                }
            }, context.getString(R.string.permission_call_message), R.string.i_know, Manifest.permission.CALL_PHONE);
        } else {
            call(context, phoneNumber);
        }
    }

    private static void call(Context context, String phoneNumber) {
        Intent phoneIntent = new Intent("android.intent.action.CALL",
                Uri.parse("tel:" + phoneNumber));
        phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(phoneIntent);
    }
}

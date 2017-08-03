package com.littlesparkle.growler.core.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.a21vianet.quincysx.library.SweetAlertDialog;
import com.littlesparkle.growler.core.R;


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

public class DialogHelper {
    public static Dialog showDialogWithMessage(final Context context, final String message) {
        return showDialogWithMessage(context, null, message);
    }

    public static Dialog showDialogWithMessage(
            final Context context,
            final SweetAlertDialog.OnSweetClickListener listener,
            final String message) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(message)
                .setConfirmText(context.getString(R.string.ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        if (listener != null) {
                            listener.onClick(sweetAlertDialog);
                        }
                        if (sweetAlertDialog.isShowing()) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    }
                });
        dialog.show();
        return dialog;
    }

    public static Dialog showDialogWithCancelButton(
            final Context context,
            final SweetAlertDialog.OnSweetClickListener listener,
            final String title, final String message) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText(context.getString(R.string.ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        if (listener != null) {
                            listener.onClick(sweetAlertDialog);
                        }
                        if (sweetAlertDialog.isShowing()) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    }
                })
                .showCancelButton(true)
                .setCancelText(context.getString(R.string.cancel))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
        dialog.show();
        return dialog;
    }

    public static void showOptionsDialog(
            final Context context,
            final int[] options,
            final DialogInterface.OnClickListener clickListener,
            final DialogInterface.OnCancelListener cancelListener,
            final DialogInterface.OnDismissListener dismissListener) {
        new AlertDialog.Builder(context)
                .setAdapter(new DialogOptionsAdapter(context, R.layout.option_item, options), clickListener)
                .setOnCancelListener(cancelListener)
                .setOnDismissListener(dismissListener)
                .show();
    }
}

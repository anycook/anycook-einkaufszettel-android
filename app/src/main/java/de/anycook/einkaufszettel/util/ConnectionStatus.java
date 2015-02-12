/*
 * This file is part of anycook Einkaufszettel
 * Copyright (C) 2014 Jan Graßegger, Claudia Sichting
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see [http://www.gnu.org/licenses/].
 */

package de.anycook.einkaufszettel.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public final class ConnectionStatus {
    private ConnectionStatus() {

    }

    public static boolean isConnected(Context context) {
        Properties properties = new Properties(context);
        ConnectivityManager cm =
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        }
        if (properties.isMobileDataAllowed()) {
            networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        }
        return false;
    }

    public static void showNoConnectionDialog(Context context, DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Keine Verbindung zum Internet");
        builder.setNeutralButton("OK", clickListener);
        builder.create().show();
    }

    public static void showOfflineMessage(final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showNoConnectionDialog(activity, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                });
            }
        });
    }
}

/*
 * Copyright (C) 2016 CWDO Systems Ltd  All Rights Reserved.
 * Unauthorized copying in any way of this file via any medium
 * is strictly prohibited Proprietary and confidential.
 *  <portplayers@gmail.com>, October 2016
 *
 * FURTHERMORE CWDO CONFIDENTIAL
 * __________________
 *
 * [2015] - [2030] CWDO Systems Ltd
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of CWDO Systems Ltd and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to CWDO Systems Ltd
 * and its suppliers and may be covered by patents, patents in process,
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from CWDO Systems Ltd.
 */

package com.ble.multiple.data;

import java.io.File;
import java.util.UUID;

/**
 * Settings
 */
public class SettingsManager {

    private static final SettingsManager mInstance;

    public static final String APP_CHACHE = ("smartPower" + File.separator);
    public static final String APP_CHACHE_ERROR = (APP_CHACHE + "error" + File.separator);
    public static final UUID UUID_NOTIFY =  UUID.fromString("0000ffe4-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_RENAME =  UUID.fromString("0000ffe6-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_SERVICE = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");

    public static final String REMOTE_ON_OFF = "remote_on_off";

    /**
     * These are the Device types.
     */
    public enum DeviceType {

        REMOTE_DEVICE,
        LOCAL_DEVICE
    }

    static {
        mInstance = new SettingsManager();
    }

    public static SettingsManager getInstance() {
        return mInstance;
    }

    private SettingsManager() {
    }
 }
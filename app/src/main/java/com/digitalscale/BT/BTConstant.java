package com.digitalscale.BT;

import java.util.UUID;

/**
 * Created by Vishal Gadhiya on 5/27/2017.
 */

public class BTConstant {

    // Standard service port :
    public static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Message types sent from the threads Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_SERVER_CONNECTED = 7;
    public static final int MESSAGE_SOCKET_DISCONNECTED = 8;

    public static final int STATE_CONNECTED = 100;
    public static final int STATE_DISCONNECTED = 200;

}

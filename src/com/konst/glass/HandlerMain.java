package com.konst.glass;

import android.os.Handler;

/**
 * Created by Kostya on 14.05.2015.
 */
public abstract class HandlerMain extends Handler {

    public abstract int messageCash(int cash);
    public abstract int messageSippingGlass(float glass, int sum);
    public abstract int messageSippingGlass(Unit.Goods goods);
}

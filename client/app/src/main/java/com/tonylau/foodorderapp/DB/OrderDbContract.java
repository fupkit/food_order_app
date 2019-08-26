package com.tonylau.foodorderapp.DB;

import android.provider.BaseColumns;

public class OrderDbContract {
    // Inner class that defines the table contents
    public static abstract class OrderDbEntry implements BaseColumns {
        public static final String TABLE_NAME = "TBL_ORDER";
        public static final String COLUMN_NAME_ORDERID = "ORDERID";
        public static final String COLUMN_NAME_ITEMID = "ITEMID";
        public static final String COLUMN_NAME_QUANTITY ="QUANTITY";
        public static final String COLUMN_NAME_DONE ="DONE";
    }
}

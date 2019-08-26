package com.tonylau.foodorderapp.DB;

import android.provider.BaseColumns;

public class CartDbContract {
    // Inner class that defines the table contents
    public static abstract class CartDbEntry implements BaseColumns {
        public static final String TABLE_NAME = "TBL_CART";
        public static final String COLUMN_NAME_ITEMID = "ITEMID";
        public static final String COLUMN_NAME_QUANTITY ="QUANTITY";
    }
}

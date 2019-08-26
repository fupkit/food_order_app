package com.tonylau.foodorderapp.DB;

import android.provider.BaseColumns;

public class ItemDbContract {
    // Inner class that defines the table contents
    public static abstract class ItemDbEntry implements BaseColumns {
        public static final String TABLE_NAME = "TBL_ITEM";
        public static final String COLUMN_NAME_ITEMID = "ITEMID";
        public static final String COLUMN_NAME_NAME = "NAME";
        public static final String COLUMN_NAME_CATEGORY ="CATEGORY";
        public static final String COLUMN_NAME_PRICE = "PRICE";
        public static final String COLUMN_NAME_REMAIN = "REMAIN";
        public static final String COLUMN_NAME_IMGPATH = "IMGPATH";
    }

}

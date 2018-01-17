package com.cloudipsp.android;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

/**
 * Created by vberegovoy on 22.12.15.
 */
public final class CardNumberEdit extends CardInputBase {
    public CardNumberEdit(Context context) {
        super(context);
        init();
    }

    public CardNumberEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardNumberEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(19)});
        setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        setSingleLine();
    }
}

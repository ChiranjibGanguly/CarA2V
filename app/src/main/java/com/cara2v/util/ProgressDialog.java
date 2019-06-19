package com.cara2v.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.cara2v.R;


/**
 * Created by android-5 on 24/8/17.
 */

public class ProgressDialog extends Dialog {
    Context context;
    public ProgressDialog(Context context) {
        super(context);

        this.context = context;
        // This is the fragment_search_details XML file that describes your Dialog fragment_search_details
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.progress_dialog);
    }
}
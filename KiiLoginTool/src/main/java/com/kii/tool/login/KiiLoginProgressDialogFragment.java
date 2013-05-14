package com.kii.tool.login;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class KiiLoginProgressDialogFragment extends DialogFragment {
    public static KiiLoginProgressDialogFragment newInstance() {
        KiiLoginProgressDialogFragment fragment = new KiiLoginProgressDialogFragment();
        return fragment;
    }

    /*
     * (non-Javadoc)
     * @see android.app.DialogFragment#onCreateDialog(android.os.Bundle)
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.kii_tool_progress);
        return dialog;
    }
    
    
}

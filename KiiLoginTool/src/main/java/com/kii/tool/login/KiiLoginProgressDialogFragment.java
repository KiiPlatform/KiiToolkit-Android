package com.kii.tool.login;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

public class KiiLoginProgressDialogFragment extends DialogFragment {
    private static final String ARGS_TEXT = "text";

    public static KiiLoginProgressDialogFragment newInstance(String text) {
        KiiLoginProgressDialogFragment fragment = new KiiLoginProgressDialogFragment();
        
        Bundle args = new Bundle();
        args.putString(ARGS_TEXT, text);
        fragment.setArguments(args);
        
        return fragment;
    }

    /*
     * (non-Javadoc)
     * @see android.app.DialogFragment#onCreateDialog(android.os.Bundle)
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // get args
        Bundle args = getArguments();
        String message = args.getString(ARGS_TEXT);
        
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(message);
        return dialog;
    }
    
    
}

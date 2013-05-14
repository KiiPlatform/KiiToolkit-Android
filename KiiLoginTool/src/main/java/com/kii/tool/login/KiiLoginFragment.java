package com.kii.tool.login;

import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiUserCallBack;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * A fragment that displays a form for login KiiCloud
 */
public class KiiLoginFragment extends Fragment {
    
    private EditText identifierEdit;
    private EditText passwordEdit;
    private DialogFragment progressDialog;
    
    private static final String ARGS_TYPE = "type";
    
    public interface Type {
        public static final int USERNAME = 0;
        public static final int EMAIL = 1;
        public static final int PHONE = 2;
    }
    
    public interface ErrorCode {
        public static final int INVALID_USERNAME = 1;
        public static final int INVALID_EMAIL = 2;
        public static final int INVALID_PHONE = 3;
        public static final int INVALID_PASSWORD = 11;
        public static final int LOGIN_FAILED = 100;
    }
    
    /*
     * (non-Javadoc)
     * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.kii_fragment_login, container, false);
        return root;
    }
    
    /**
     * Set identification type.
     * @param type The identification type. See {@link Type}
     */
    public void setType(int type) {
        Bundle args = getArguments();
        if (args == null) {
            args = new Bundle();
            setArguments(args);
        }
        args.putInt(ARGS_TYPE, type);
    }

    /*
     * (non-Javadoc)
     * @see android.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set local variables
        identifierEdit = (EditText) view.findViewById(R.id.kii_tool_edit_identifier);
        passwordEdit = (EditText) view.findViewById(R.id.kii_tool_edit_password);
        if (identifierEdit == null) {
            throw new RuntimeException("EditText R.id.kii_tool_edit_identifier is not found in the layout.");
        }
        if (passwordEdit == null) {
            throw new RuntimeException("EditText R.id.kii_tool_edit_password is not found in the layout.");
        }        
        setListeners(view);
    }

    private void setListeners(View view) {
        View loginButton = view.findViewById(R.id.kii_tool_button_login);
        if (loginButton != null) { 
            loginButton.setOnClickListener(clickListener);
        }
    }
    
    private void performLogin() {
        Bundle args = getArguments();
        int type = args.getInt(ARGS_TYPE, Type.USERNAME);
        String identifier = identifierEdit.getText().toString();
        String password = passwordEdit.getText().toString();
                
        // input check
        if (!isValidIdentifier(identifier, type)) {
            return;
        }
        if (!KiiUser.isValidPassword(password)) {
            onError(ErrorCode.INVALID_PASSWORD);
            return;
        }
        
        // show progress
        onShowProgress();
        
        KiiUser.logIn(new KiiUserCallBack() {
            /*
             * (non-Javadoc)
             * @see com.kii.cloud.storage.callback.KiiUserCallBack#onLoginCompleted(int, com.kii.cloud.storage.KiiUser, java.lang.Exception)
             */
            @Override
            public void onLoginCompleted(int token, KiiUser user, Exception exception) {
                super.onLoginCompleted(token, user, exception);
                onDismissProgress();
                if (exception != null) {
                    onError(ErrorCode.LOGIN_FAILED);
                    return;
                }
                onPostLogin(user);
            }
        }, identifier, password);
    }

    private boolean isValidIdentifier(String identifier, int type) {
        switch (type) {
        case Type.USERNAME:
            if (KiiUser.isValidUserName(identifier)) {
                return true;
            }
            onError(ErrorCode.INVALID_USERNAME);
            return false;
        case Type.EMAIL:
            if (KiiUser.isValidEmail(identifier)) {
                return true;
            }
            onError(ErrorCode.INVALID_EMAIL);
            return false;
        case Type.PHONE:
            if (KiiUser.isValidPhone(identifier)) {
                return true;
            }
            onError(ErrorCode.INVALID_PHONE);
            return false;
        default:
            throw new RuntimeException("Unknown identification type " + type);
        }
    }

    /**
     * This method will be called before {@link KiiUser#logIn(String, String)}
     */
    protected void onShowProgress() {
        progressDialog = KiiLoginProgressDialogFragment.newInstance();
        progressDialog.show(getFragmentManager(), "");
    }

    /**
     * This method will be called after {@link KiiUser#logIn(String, String)}
     */
    protected void onDismissProgress() {
        if (progressDialog == null) { return; }
        progressDialog.dismiss();
        progressDialog = null;
    }
    
    /**
     * This method will be called when login is succeeded.
     * @param user The user who logins KiiCloud
     */
    protected void onPostLogin(KiiUser user) {
        
    }

    /**
     * This method will be called when error happens on login.
     * @param errorCode Error code. See {@link ErrorCode}
     */
    protected void onError(int errorCode) {
        
    }

    private final View.OnClickListener clickListener = new View.OnClickListener() {
        /*
         * (non-Javadoc)
         * @see android.view.View.OnClickListener#onClick(android.view.View)
         */
        @Override
        public void onClick(View v) {
            performLogin();
        }
    };
    
}

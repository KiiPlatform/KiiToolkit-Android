package com.kii.tool.login;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiUserCallBack;

/**
 * A fragment that displays a form for login KiiCloud
 */
public class KiiLoginFragment extends DialogFragment {
    
    private EditText identifierEdit;
    private EditText passwordEdit;
    private View loginButton;
    private View registerButton;
    
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
        public static final int REGISTER_FAILED = 200;
    }

    /*
     * (non-Javadoc)
     * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = ViewBuilder.createView(inflater.getContext());
        
        // get type for setting identifier label
        TextView identifierText = (TextView) root.findViewById(ViewBuilder.ViewId.TEXT_IDENTIFIER);
        int type = getType();
        switch (type) {
        case Type.USERNAME:
            identifierText.setText("Username");
            break;
        case Type.EMAIL:
            identifierText.setText("Email");
            break;
        case Type.PHONE:
            identifierText.setText("Phone number");
            break;
        default:
            throw new RuntimeException("Unknown identification type " + type);
        }
        
        identifierEdit = (EditText) root.findViewById(ViewBuilder.ViewId.EDIT_IDENTIFIER);
        passwordEdit = (EditText) root.findViewById(ViewBuilder.ViewId.EDIT_PASSWORD);
        loginButton = root.findViewById(ViewBuilder.ViewId.BUTTON_LOGIN);
        registerButton = root.findViewById(ViewBuilder.ViewId.BUTTON_REGISTER);
        
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
    
    private int getType() {
        Bundle args = getArguments();
        if (args == null) { return Type.USERNAME; }
        return args.getInt(ARGS_TYPE, Type.USERNAME);
    }

    /*
     * (non-Javadoc)
     * @see android.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners(view);
    }

    private void setListeners(View view) {
        View loginButton = getLoginButton();
        if (loginButton != null) { 
            loginButton.setOnClickListener(loginClickListener);
        }
        
        View registerButton = getRegisterButton();
        if (registerButton != null) {
            registerButton.setOnClickListener(registerClickListener);
        }
    }

    /**
     * Get Login button. If {@link KiiLoginFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)} is overridden, 
     * this method must be overridden, too.
     * @return button for login
     */
    protected View getLoginButton() {
        return loginButton;
    }

    /**
     * Get Register button. If {@link KiiLoginFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)} is overridden,
     * this method must be overridden, too.
     * @return button for register
     */
    protected View getRegisterButton() {
        return registerButton;
    }    
    
    /**
     * Get identifier that user inputs. If {@link KiiLoginFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)} is overridden, 
     * This method must be overridden, too.
     * @return identifier that user inputs
     */
    protected String getIdentifier() {
        return identifierEdit.getText().toString();
    }
    
    /**
     * Get password that user inputs. If {@link KiiLoginFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)} is overridden, 
     * This method must be overridden, too.
     * @return password that user inputs
     */
    protected String getPassword() {
        return passwordEdit.getText().toString();
    }    
    
    private void performLogin() {
        int type = Type.USERNAME;
        Bundle args = getArguments();
        if (args != null) {
            type = args.getInt(ARGS_TYPE, Type.USERNAME);
        }
        String identifier = getIdentifier();
        String password = getPassword();
                
        // input check
        if (!isValidIdentifier(identifier, type)) {
            return;
        }
        if (!KiiUser.isValidPassword(password)) {
            onLoginError(ErrorCode.INVALID_PASSWORD);
            return;
        }
        
        // show progress
        onShowLoginProgress();
        
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
                    onLoginError(ErrorCode.LOGIN_FAILED);
                    return;
                }
                onLoginSucceeded(user);
            }
        }, identifier, password);
    }

    private boolean isValidIdentifier(String identifier, int type) {
        switch (type) {
        case Type.USERNAME:
            if (KiiUser.isValidUserName(identifier)) {
                return true;
            }
            onLoginError(ErrorCode.INVALID_USERNAME);
            return false;
        case Type.EMAIL:
            if (KiiUser.isValidEmail(identifier)) {
                return true;
            }
            onLoginError(ErrorCode.INVALID_EMAIL);
            return false;
        case Type.PHONE:
            if (KiiUser.isValidPhone(identifier)) {
                return true;
            }
            onLoginError(ErrorCode.INVALID_PHONE);
            return false;
        default:
            throw new RuntimeException("Unknown identification type " + type);
        }
    }

    /**
     * This method will be called before {@link KiiUser#logIn(String, String)}
     */
    protected void onShowLoginProgress() {
        progressDialog = KiiLoginProgressDialogFragment.newInstance("Login");
        progressDialog.show(getFragmentManager(), "");
    }
    
    /**
     * This method will be called before {@link KiiUser}
     */
    protected void onShowRegisterProgress() {
        progressDialog = KiiLoginProgressDialogFragment.newInstance("Register");
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
    protected void onLoginSucceeded(KiiUser user) {
        
    }

    /**
     * This method will be called when error happens on login.
     * @param errorCode Error code. See {@link ErrorCode}
     */
    protected void onLoginError(int errorCode) {
        
    }
    
    private void performRegister() {
        int type = Type.USERNAME;
        Bundle args = getArguments();
        if (args != null) {
            type = args.getInt(ARGS_TYPE, Type.USERNAME);
        }
        String identifier = getIdentifier();
        String password = getPassword();
                
        // input check
        if (!isValidIdentifier(identifier, type)) {
            return;
        }
        if (!KiiUser.isValidPassword(password)) {
            onLoginError(ErrorCode.INVALID_PASSWORD);
            return;
        }
        
        KiiUser user = createKiiUser(identifier, type);
        
        // for setting ext fields to user
        onPrepareRegistration(user);
        
        // show progress
        onShowRegisterProgress();
        
        user.register(new KiiUserCallBack() {
            /*
             * (non-Javadoc)
             * @see com.kii.cloud.storage.callback.KiiUserCallBack#onRegisterCompleted(int, com.kii.cloud.storage.KiiUser, java.lang.Exception)
             */
            @Override
            public void onRegisterCompleted(int token, KiiUser user, Exception exception) {
                super.onRegisterCompleted(token, user, exception);
                onDismissProgress();
                if (exception != null) {
                	exception.printStackTrace();
                    onRegisterError(ErrorCode.REGISTER_FAILED);
                    return;
                }
                onRegisterSucceeded(user);
            }
        }, password);
    }

    private KiiUser createKiiUser(String identifier, int type) {
        switch (type) {
        case Type.USERNAME:
            return KiiUser.builderWithName(identifier).build();
        case Type.EMAIL:
            return KiiUser.builderWithEmail(identifier).build();
        case Type.PHONE:
            return KiiUser.builderWithPhone(identifier).build();
        default:
            throw new RuntimeException("Unknown identification type " + type);
        }
    }
    
    /**
     * This method will be called just before {@link KiiUser#register(KiiUserCallBack, String)}. 
     * Developer can set extra fields such as age, gender to KiiUser.
     * @param user 
     */
    protected void onPrepareRegistration(KiiUser user) {
        
    }
    
    /**
     * This method will be called when register is succeeded.
     * @param user The user who is registered on KiiCloud
     */
    protected void onRegisterSucceeded(KiiUser user) {
        
    }

    /**
     * This method will be called when error happens on register.
     * @param errorCode Error code. See {@link ErrorCode}
     */
    protected void onRegisterError(int errorCode) {
        
    }
    
    private final View.OnClickListener loginClickListener = new View.OnClickListener() {
        /*
         * (non-Javadoc)
         * @see android.view.View.OnClickListener#onClick(android.view.View)
         */
        @Override
        public void onClick(View v) {
            performLogin();
        }
    };
    
    private final View.OnClickListener registerClickListener = new View.OnClickListener() {
        /*
         * (non-Javadoc)
         * @see android.view.View.OnClickListener#onClick(android.view.View)
         */
        @Override
        public void onClick(View v) {
            performRegister();
        }
    };
    
}

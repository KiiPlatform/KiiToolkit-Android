package com.kii.tool.login;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

class ViewBuilder {
    interface ViewId {
        static final int TEXT_IDENTIFIER = 100;
        static final int EDIT_IDENTIFIER = 101;
        static final int TEXT_PASSWORD = 200;
        static final int EDIT_PASSWORD = 201;
        static final int BUTTON_LOGIN = 300;
        static final int BUTTON_REGISTER = 301;
        
        static final int TEXT_LOGIN = 400;
        
    }
    
    public static View createView(Context context) {
        RelativeLayout root = new RelativeLayout(context);
        RelativeLayout.LayoutParams params;
        
        View identifireText = createTextView(context, ViewId.TEXT_IDENTIFIER, "");
        params = createParams();
        root.addView(identifireText, params);
        
        View identifierEdit = createEditText(context, ViewId.EDIT_IDENTIFIER, InputType.TYPE_CLASS_TEXT);
        params = createParams();
        params.addRule(RelativeLayout.BELOW, ViewId.TEXT_IDENTIFIER);
        root.addView(identifierEdit, params);

        // password
        View passwordText = createTextView(context, ViewId.TEXT_PASSWORD, "Password");
        params = createParams();
        params.addRule(RelativeLayout.BELOW, ViewId.EDIT_IDENTIFIER);
        root.addView(passwordText, params);
        
        View passwordEdit = createEditText(context, ViewId.EDIT_PASSWORD, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        params = createParams();
        params.addRule(RelativeLayout.BELOW, ViewId.TEXT_PASSWORD);
        root.addView(passwordEdit, params);
        
        // button
        View loginButton = createButton(context, ViewId.BUTTON_LOGIN, "Login");
        params = createParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        root.addView(loginButton, params);
        
        View registerButton = createButton(context, ViewId.BUTTON_REGISTER, "Register");
        params = createParams();
        params.addRule(RelativeLayout.ABOVE, ViewId.BUTTON_LOGIN);
        root.addView(registerButton, params);
        
        return root;
    }
    
    private static View createTextView(Context context, int id, String text) {
        TextView view = new TextView(context);
        view.setId(id);
        view.setText(text);
        return view;
    }

    private static View createEditText(Context context, int id, int type) {
        EditText edit = new EditText(context);
        edit.setId(id);
        edit.setInputType(type);
        return edit;
    }

    private static View createButton(Context context, int id, String text) {
        Button button = new Button(context);
        button.setId(id);
        button.setText(text);
        return button;
    }
    
    private static RelativeLayout.LayoutParams createParams() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        return params;
    }
    
}

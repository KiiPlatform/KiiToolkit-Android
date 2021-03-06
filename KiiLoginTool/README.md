# KiiLoginTool
## What is KiiLoginTool?
KiiLoginTool provides easy-to-use Login/Registration Fragment. You can add Login/Registration Fragment with a little implementation.  

## Requirement
* Android 3.0 or more - KiiLoginTool uses android.app.Fragment.

## How to build jar file
### Windows
    git clone https://github.com/KiiPlatform/KiiToolkit-Android.git toolkit
    cd toolkit/KiiLoginTool
    gradlew.bat

### Mac / Linux
    git clone https://github.com/KiiPlatform/KiiToolkit-Android.git toolkit
    cd toolkit/KiiLoginTool
    ./gradlew 

On both platform, you can find KiiLoginTool-x.y.z.jar file on build/libs folder. Please, notice that ANDROID_HOME environment variable should be set to your Android SDK path before running this script.

## getting started
### Add new Fragment that extends KiiLoginFragment
    public class MyLoginFragment extends KiiLoginFragment {
        public static MyLoginFragment newInstance() {
            MyLoginFragment fragment = new MyLoginFragment();
            return fragment;
        }
        // This method will be called when login is succeeded.
        @Override
        protected void onLoginSucceeded(KiiUser user) {
            super.onLoginSucceeded(user);
            Toast.makeText(getActivity(), "Login succeeded", Toast.LENGTH_LONG).show();
        }
    
        // This method will be called when login is failed
        @Override
        protected void onLoginError(int errorCode) {
            super.onLoginError(errorCode);
            Toast.makeText(getActivity(), "Login failed code=" + errorCode, Toast.LENGTH_LONG).show();
        }
    }

### Replace Fragment on MainActivity
    public class MainActivity extends Activity {
        private static final String APP_ID = "<APP_ID>";
        private static final String APP_KEY = "<APP_KEY>";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Kii.initialize(APP_ID, APP_KEY, Kii.Site.JP);

            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            MyLoginFragment fragment = MyLoginFragment.newInstance();
            // Developer can choose login type by this method.
            // fragment.setType(KiiLoginFragment.Type.EMAIL);
            transaction.replace(R.id.main, fragment);
    
            transaction.commit();
        }
    }

## If you want to let user login by Email...
Please call `KiiLoginFragment#setType(int type)` before calling `FragmentTransaction#replace(id, fragment)`

## If you want to use custom layout
You can set your custom layout by overriding `Fragment#onCreateView()`. If you use custom layout, please override the following methods.

* getIdentifier
* getPassword
* getLoginButton

Here is the example of using custom layout.

    public class GoodLoginFragment extends KiiLoginFragment {
        public static GoodLoginFragment newInstance() {
            GoodLoginFragment fragment = new GoodLoginFragment();
            return fragment;
        }
 
        private EditText usernameEdit;
        private EditText passwordEdit;
        private Button loginButton;
     
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.fragment_login, container, false);
     
            usernameEdit = (EditText) root.findViewById(R.id.edit_username);
            passwordEdit = (EditText) root.findViewById(R.id.edit_password);
            loginButton = (Button) root.findViewById(R.id.button_login);
     
            return root;
        }
     
        @Override
        protected String getIdentifier() {
            return usernameEdit.getText().toString();
        }
     
        @Override
        protected String getPassword() {
            return passwordEdit.getText().toString();
        }
     
        @Override
        protected View getLoginButton() {
            return loginButton;
        }
     
        @Override
        protected void onLoginSucceeded(KiiUser user) {
            super.onLoginSucceeded(user);
            Toast.makeText(getActivity(), "Login succeeded", Toast.LENGTH_LONG).show();
        }
     
        @Override
        protected void onLoginError(int errorCode) {
            super.onLoginError(errorCode);
            Toast.makeText(getActivity(), "Login failed code=" + errorCode, Toast.LENGTH_LONG).show();
        }
    }

## Creating a Registration fragment

KiiLoginFragment can be used for both Login actions and/or Registration actions. If you want to create a Registration fragment just simply:


### Override registration callbacks

    public class MyLoginFragment extends KiiLoginFragment {
	...
	    /**
     * This method will be called just before registering the user in the Cloud
     * Developer can set extra fields such as age, gender to KiiUser.
     */
    @Override
    protected void onPrepareRegistration(KiiUser user) {
        
    }
    
    /**
     * This method will be called when register is succeeded.
     */
    @Override
    protected void onRegisterSucceeded(KiiUser user) {
        
    }

    /**
     * This method will be called when error happens on register.
     */
    @Override
    protected void onRegisterError(int errorCode) {
        
    }

## If you want to use custom layout
You can set your custom layout by overriding `Fragment#onCreateView()`. If you use custom layout, please override the following method.

getRegisterButton()


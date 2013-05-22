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

On both platform, you can find KiiLoginTool-x.y.z.jar file on build/libs folder.

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
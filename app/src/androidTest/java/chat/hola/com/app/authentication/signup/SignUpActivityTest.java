package chat.hola.com.app.authentication.signup;

import android.app.KeyguardManager;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * <h1>SignUpActivityTest</h1>
 *
 * @author Shaktisisnh Jadeja
 * @version 1.0
 * @since 01 August 2019
 */
public class SignUpActivityTest {

    @Rule
    public ActivityTestRule<SignUpActivity> activityTestRule = new ActivityTestRule<>(SignUpActivity.class);
    private SignUpActivity activity;
    private int max_digit = 10;

    private String COUNTRY_CODE = "+91";
    private String COUNTRY = "India";
    private String MOBILE_NUMBER = "9712671852";
    private String EMAIL = "rahul@mobifyi.com";
    private String USER_NAME = "appcrip";
    private String FULL_NAME = "Rahul Sharma";
    private String PASSWORD = "rahul@007";
    private String CONFIRM_PASSWORD = "rahul@007";
    private String REFERRAL_CODE = "REF1234567";

    @Before
    public void setUp() throws Exception {
        activity = activityTestRule.getActivity();
        wakeUpDevice(activity);
    }

    @Test
    public void test() {
//        Espresso.onView(withId(R.id.tvRegion)).perform(text(COUNTRY + "(" + COUNTRY_CODE + ")"), closeSoftKeyboard());
        Espresso.onView(withId(R.id.etPhone)).perform(typeText(MOBILE_NUMBER), closeSoftKeyboard());
        Espresso.onView(withId(R.id.etName)).perform(typeText(FULL_NAME), closeSoftKeyboard());
        Espresso.onView(withId(R.id.etUserName)).perform(typeText(USER_NAME), closeSoftKeyboard());
        Espresso.onView(withId(R.id.etEmail)).perform(typeText(EMAIL), closeSoftKeyboard());
        Espresso.onView(withId(R.id.etPassword)).perform(typeText(PASSWORD), closeSoftKeyboard());
        Espresso.onView(withId(R.id.etConfirmPassword)).perform(typeText(CONFIRM_PASSWORD), closeSoftKeyboard());
        Espresso.onView(withId(R.id.etReferralCode)).perform(typeText(REFERRAL_CODE), closeSoftKeyboard());
        Espresso.onView(withId(R.id.cbTerms)).perform(pressKey(KeyEvent.FLAG_EDITOR_ACTION));
        Espresso.onView(withId(R.id.done)).perform(click());
    }

    //    @Test
//    public void countryCode_validator() {
//        assertFalse(countryCode.isEmpty());
//        Espresso.onView(withId(R.id.tvRegion)).check(matches(withText(countryName + " (" + countryCode + ")")));
//    }
//
//    @Test
//    public void mobileNumber_validator() {
//        Espresso.onView(withId(R.id.etPhone)).perform(typeText(mobileNumber));
//        sizeValidator(max_digit, mobileNumber);
//    }
//
//    private void sizeValidator(int max_digit, String text) {
//        assertFalse(text.length() > max_digit);
//    }
    private static void wakeUpDevice(SignUpActivity activity) {
        if (BuildConfig.DEBUG) {

            KeyguardManager myKM = (KeyguardManager) activity.getSystemService(activity.KEYGUARD_SERVICE);
            boolean isPhoneLocked = myKM.inKeyguardRestrictedInputMode();

            if (isPhoneLocked) {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            }
        }
    }

    @After
    public void tearDown() throws Exception {
    }
}
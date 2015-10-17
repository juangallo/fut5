/**
 * File generated by Magnet rest2mobile 1.1 - Oct 17, 2015 9:18:08 PM
 * @see {@link http://developer.magnet.com}
 */

package com.ort.num172159_180968.fut5.controller.api.test;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.test.suitebuilder.annotation.Suppress;

import com.magnet.android.mms.MagnetMobileClient;
import com.magnet.android.mms.async.Call;
import com.magnet.android.mms.exception.SchemaException;
import java.util.concurrent.ExecutionException;

import com.ort.num172159_180968.fut5.controller.api.UserExists;
import com.ort.num172159_180968.fut5.controller.api.UserExistsFactory;

/**
* This is generated stub to test {@link UserExists}
* <p>
* All test cases are suppressed by defaullt. To run the test, you need to fix all the FIXMEs first :
* <ul>
* <li>Set proper value for the parameters
* <li>Remove out the @Suppress annotation
* <li>(optional)Add more asserts for the result
* <ul><p>
*/

public class UserExistsTest extends InstrumentationTestCase {
  private UserExists userExists;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    // Instantiate a controller
    MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(this.getInstrumentation().getTargetContext());
    UserExistsFactory controllerFactory = new UserExistsFactory(magnetClient);
    userExists = controllerFactory.obtainInstance();

    assertNotNull(userExists);
  }

  /**
    * Generated unit test for {@link UserExists#userExists}
    */
  @Suppress //FIXME : set proper parameter value and un-suppress this test case
  @SmallTest
  public void testUserExists() throws SchemaException, ExecutionException, InterruptedException {
    // FIXME : set proper value for the parameters
    String username = null;

    Call<String> callObject = userExists.userExists(
      username, null);
    assertNotNull(callObject);

    String result = callObject.get();
    assertNotNull(result);
    //TODO : add more asserts
  }

}
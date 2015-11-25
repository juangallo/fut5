/**
 * File generated by Magnet rest2mobile 1.1 - Nov 25, 2015 12:58:57 AM
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

import com.ort.num172159_180968.fut5.controller.api.MatchTest;
import com.ort.num172159_180968.fut5.model.beans.*;

/**
* This is generated stub to test {@link MatchTest}
* <p>
* All test cases are suppressed by defaullt. To run the test, you need to fix all the FIXMEs first :
* <ul>
* <li>Set proper value for the parameters
* <li>Remove out the @Suppress annotation
* <li>(optional)Add more asserts for the result
* <ul><p>
*/

public class MatchTestTest extends InstrumentationTestCase {
  private MatchTest matchTest;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    // Instantiate a controller
    MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(this.getInstrumentation().getTargetContext());
    MatchTestFactory controllerFactory = new MatchTestFactory(magnetClient);
    matchTest = controllerFactory.obtainInstance();

    assertNotNull(matchTest);
  }

  /**
    * Generated unit test for {@link MatchTest#addMatch}
    */
  @Suppress //FIXME : set proper parameter value and un-suppress this test case
  @SmallTest
  public void testAddMatch() throws SchemaException, ExecutionException, InterruptedException {
    // FIXME : set proper value for the parameters
    String username = null;
    String idField = null;
    String date = null;
    AddMatchRequest body = null;

    Call<AddMatchResult> callObject = matchTest.addMatch(
      username, 
      idField, 
      date, 
      body, null);
    assertNotNull(callObject);

    AddMatchResult result = callObject.get();
    assertNotNull(result);
    //TODO : add more asserts
  }

}

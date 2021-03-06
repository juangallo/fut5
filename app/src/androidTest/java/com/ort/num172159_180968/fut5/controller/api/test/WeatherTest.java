/**
 * File generated by Magnet rest2mobile 1.1 - Nov 27, 2015 6:34:55 PM
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

import com.ort.num172159_180968.fut5.controller.api.Weather;
import com.ort.num172159_180968.fut5.controller.api.WeatherFactory;
import com.ort.num172159_180968.fut5.model.beans.*;

/**
* This is generated stub to test {@link Weather}
* <p>
* All test cases are suppressed by defaullt. To run the test, you need to fix all the FIXMEs first :
* <ul>
* <li>Set proper value for the parameters
* <li>Remove out the @Suppress annotation
* <li>(optional)Add more asserts for the result
* <ul><p>
*/

public class WeatherTest extends InstrumentationTestCase {
  private Weather weather;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    // Instantiate a controller
    MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(this.getInstrumentation().getTargetContext());
    WeatherFactory controllerFactory = new WeatherFactory(magnetClient);
    weather = controllerFactory.obtainInstance();

    assertNotNull(weather);
  }

  /**
    * Generated unit test for {@link Weather#getWeather}
    */
  @Suppress //FIXME : set proper parameter value and un-suppress this test case
  @SmallTest
  public void testGetWeather() throws SchemaException, ExecutionException, InterruptedException {
    // FIXME : set proper value for the parameters
    String id = null;
    String aPPID = null;
    String units = null;

    Call<WeatherResult> callObject = weather.getWeather(
      id, 
      aPPID, 
      units, null);
    assertNotNull(callObject);

    WeatherResult result = callObject.get();
    assertNotNull(result);
    //TODO : add more asserts
  }

}

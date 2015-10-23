/**
 * File generated by Magnet rest2mobile 1.1 - Oct 14, 2015 9:00:34 PM
 * @see {@link http://developer.magnet.com}
 */

package com.ort.num172159_180968.fut5.controller.api;

import com.magnet.android.mms.MagnetMobileClient;
import com.magnet.android.mms.controller.ControllerFactory;
import com.magnet.android.mms.controller.AbstractControllerSchemaFactory;
import com.magnet.android.mms.controller.RequestSchema;
import com.magnet.android.mms.controller.RequestSchema.JMethod;

import java.util.Arrays;

import com.ort.num172159_180968.fut5.model.beans.*;
import static com.ort.num172159_180968.fut5.R.string.backend_ip;

public class UserFactory extends ControllerFactory<User> {
  public UserFactory(MagnetMobileClient magnetClient) {
    super(User.class, UserSchemaFactory.getInstance().getSchema(), magnetClient);
  }

  // Schema factory for controller User
  public static class UserSchemaFactory extends AbstractControllerSchemaFactory {
    private static UserSchemaFactory __instance;

    public static synchronized UserSchemaFactory getInstance() {
      if(null == __instance) {
        __instance = new UserSchemaFactory();
      }

      return __instance;
    }

    private UserSchemaFactory() {}

    protected synchronized void initSchemaMaps() {
      if(null != schema) {
        return;
      }

      schema = new RequestSchema();
      schema.setRootPath("");

      //controller schema for controller method addUser
      String ip;
      //ip = Resources.getSystem().getString(R.string.backend_ip);
      JMethod method1 = addMethod("addUser",
        "Fut5-war/webservice/addUser",
        "POST",
        AddUserResult.class,
        null,
        null,
        Arrays.asList("application/json"));
      method1.setBaseUrl("http://" + "192.168.1.106" + ":8080");
      method1.addParam("username",
        "QUERY",
        String.class,
        null,
        "",
        true);
      method1.addParam("password",
        "QUERY",
        String.class,
        null,
        "",
        true);
      method1.addParam("firstName",
        "QUERY",
        String.class,
        null,
        "",
        true);
      method1.addParam("lastName",
        "QUERY",
        String.class,
        null,
        "",
        true);
      method1.addParam("email",
        "QUERY",
        String.class,
        null,
        "",
        true);
      method1.addParam("url",
        "QUERY",
        String.class,
        null,
        "",
        true);
    }

  }

}

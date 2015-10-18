/**
 * File generated by Magnet rest2mobile 1.1 - Oct 17, 2015 9:18:08 PM
 * @see {@link http://developer.magnet.com}
 */

package com.ort.num172159_180968.fut5.controller.api;

import com.magnet.android.mms.MagnetMobileClient;
import com.magnet.android.mms.controller.ControllerFactory;
import com.magnet.android.mms.controller.AbstractControllerSchemaFactory;
import com.magnet.android.mms.controller.RequestSchema;
import com.magnet.android.mms.controller.RequestSchema.JMethod;
import com.magnet.android.mms.controller.RequestSchema.JMeta;
import com.magnet.android.mms.controller.RequestSchema.JParam;

import java.util.Arrays;
import java.util.Collection;


public class UserExistsFactory extends ControllerFactory<UserExists> {
  public UserExistsFactory(MagnetMobileClient magnetClient) {
    super(UserExists.class, UserExistsSchemaFactory.getInstance().getSchema(), magnetClient);
  }

  // Schema factory for controller UserExists
  public static class UserExistsSchemaFactory extends AbstractControllerSchemaFactory {
    private static UserExistsSchemaFactory __instance;

    public static synchronized UserExistsSchemaFactory getInstance() {
      if(null == __instance) {
        __instance = new UserExistsSchemaFactory();
      }

      return __instance;
    }

    private UserExistsSchemaFactory() {}

    protected synchronized void initSchemaMaps() {
      if(null != schema) {
        return;
      }

      schema = new RequestSchema();
      schema.setRootPath("");

      //controller schema for controller method userExists
      JMethod method1 = addMethod("userExists",
        "Fut5-war/webservice/existsUser",
        "GET",
        String.class,
        null,
        null,
        Arrays.asList("text/plain"));
      method1.setBaseUrl("http://192.168.1.113:8080");
      method1.addParam("username",
        "QUERY",
        String.class,
        null,
        "",
        true);
    }

  }

}

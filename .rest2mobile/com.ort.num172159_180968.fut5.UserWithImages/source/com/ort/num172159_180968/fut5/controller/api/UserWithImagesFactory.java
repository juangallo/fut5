/**
 * File generated by Magnet rest2mobile 1.1 - Nov 20, 2015 7:41:04 PM
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

import com.ort.num172159_180968.fut5.model.beans.*;
import java.util.List;

public class UserWithImagesFactory extends ControllerFactory<UserWithImages> {
  public UserWithImagesFactory(MagnetMobileClient magnetClient) {
    super(UserWithImages.class, UserWithImagesSchemaFactory.getInstance().getSchema(), magnetClient);
  }

  // Schema factory for controller UserWithImages
  public static class UserWithImagesSchemaFactory extends AbstractControllerSchemaFactory {
    private static UserWithImagesSchemaFactory __instance;

    public static synchronized UserWithImagesSchemaFactory getInstance() {
      if(null == __instance) {
        __instance = new UserWithImagesSchemaFactory();
      }

      return __instance;
    }

    private UserWithImagesSchemaFactory() {}

    protected synchronized void initSchemaMaps() {
      if(null != schema) {
        return;
      }

      schema = new RequestSchema();
      schema.setRootPath("");

      //controller schema for controller method getusersWithImages
      JMethod method1 = addMethod("getusersWithImages",
        "Fut5-war/webservice/getUserWithImage",
        "GET",
        List.class,
        UsersWithImagesResult.class,
        null,
        Arrays.asList("application/json"));
      method1.setBaseUrl("http://localhost:8080");
    }

  }

}

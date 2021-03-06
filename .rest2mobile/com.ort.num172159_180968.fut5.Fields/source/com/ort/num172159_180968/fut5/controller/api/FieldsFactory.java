/**
 * File generated by Magnet rest2mobile 1.1 - Oct 12, 2015 11:32:22 PM
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

public class FieldsFactory extends ControllerFactory<Fields> {
  public FieldsFactory(MagnetMobileClient magnetClient) {
    super(Fields.class, FieldsSchemaFactory.getInstance().getSchema(), magnetClient);
  }

  // Schema factory for controller Fields
  public static class FieldsSchemaFactory extends AbstractControllerSchemaFactory {
    private static FieldsSchemaFactory __instance;

    public static synchronized FieldsSchemaFactory getInstance() {
      if(null == __instance) {
        __instance = new FieldsSchemaFactory();
      }

      return __instance;
    }

    private FieldsSchemaFactory() {}

    protected synchronized void initSchemaMaps() {
      if(null != schema) {
        return;
      }

      schema = new RequestSchema();
      schema.setRootPath("");

      //controller schema for controller method getFields
      JMethod method1 = addMethod("getFields",
        "Fut5-war/webservice/getFields",
        "GET",
        List.class,
        FieldsResult.class,
        null,
        Arrays.asList("application/json"));
      method1.setBaseUrl("http://192.168.1.106:8080");
    }

  }

}

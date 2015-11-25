/**
 * File generated by Magnet rest2mobile 1.1 - Nov 25, 2015 12:31:56 AM
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

public class MatchFactory extends ControllerFactory<Match> {
  public MatchFactory(MagnetMobileClient magnetClient) {
    super(Match.class, MatchSchemaFactory.getInstance().getSchema(), magnetClient);
  }

  // Schema factory for controller Match
  public static class MatchSchemaFactory extends AbstractControllerSchemaFactory {
    private static MatchSchemaFactory __instance;

    public static synchronized MatchSchemaFactory getInstance() {
      if(null == __instance) {
        __instance = new MatchSchemaFactory();
      }

      return __instance;
    }

    private MatchSchemaFactory() {}

    protected synchronized void initSchemaMaps() {
      if(null != schema) {
        return;
      }

      schema = new RequestSchema();
      schema.setRootPath("");

      //controller schema for controller method addMatch
      JMethod method1 = addMethod("addMatch",
        "Fut5-war/webservice/addMatch",
        "POST",
        AddMatchResult.class,
        null,
        Arrays.asList("application/json"),
        null);
      method1.setBaseUrl("http://localhost:8080");
      method1.addParam("username",
        "QUERY",
        String.class,
        null,
        "",
        true);
      method1.addParam("idField",
        "QUERY",
        String.class,
        null,
        "",
        true);
      method1.addParam("date",
        "QUERY",
        String.class,
        null,
        "",
        true);
      method1.addParam("body",
        "PLAIN",
        AddMatchRequest.class,
        null,
        "",
        true);
    }

  }

}

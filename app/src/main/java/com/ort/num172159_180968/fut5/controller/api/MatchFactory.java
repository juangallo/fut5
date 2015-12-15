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
import java.util.List;

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
              Arrays.asList("application/json"));
      method1.setBaseUrl("http://" + Value.ip + ":8080");
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

        JMethod method2 = addMethod("getMatches",
                "Fut5-war/webservice/getMatches",
                "GET",
                List.class,
                MatchesResult.class,
                null,
                Arrays.asList("application/json"));
        method2.setBaseUrl("http://" + Value.ip + ":8080");
        method2.addParam("username",
                "QUERY",
                String.class,
                null,
                "",
                true);
        method2.addParam("mine",
                "QUERY",
                String.class,
                null,
                "",
                true);
        method2.addParam("next",
                "QUERY",
                String.class,
                null,
                "",
                true);

        JMethod method3 = addMethod("updateMatchDate",
                "Fut5-war/webservice/updateMatchDate",
                "POST",
                void.class,
                null,
                null,
                null);
        method3.setBaseUrl("http://" + Value.ip + ":8080");
        method3.addParam("idMatch",
                "QUERY",
                String.class,
                null,
                "",
                true);
        method3.addParam("date",
                "QUERY",
                String.class,
                null,
                "",
                true);

    }

  }

}

/**
 * File generated by Magnet rest2mobile 1.1 - Dec 13, 2015 1:36:57 PM
 * @see {@link http://developer.magnet.com}
 */

package com.ort.num172159_180968.fut5.controller.api;

import com.magnet.android.mms.async.Call;
import com.magnet.android.mms.async.StateChangedListener;

import com.ort.num172159_180968.fut5.model.beans.*;

public interface Statistic {

  /**
   * Generated from URL GET http://192.168.1.6:8080/Fut5-war/webservice/getMatchStadistics?idMatch=62
   * GET Fut5-war/webservice/getMatchStadistics
   * @param idMatch  style:QUERY
   * @param listener
   * @return MatchGoalsResult
   */
  Call<MatchGoalsResult> getMatchGoals(
     String idMatch,
     StateChangedListener listener
  );



  /**
   * Generated from URL GET http://192.168.1.6:8080/Fut5-war/webservice/addMatchStadistics?idMatch=51&localGoals=1&visitorGoals=2
   * GET Fut5-war/webservice/addMatchStadistics
   * @param idMatch  style:QUERY
   * @param localGoals  style:QUERY
   * @param visitorGoals  style:QUERY
   * @param listener
   * @return SaveMatchGoalsResult
   */
  Call<SaveMatchGoalsResult> saveMatchGoals(
     String idMatch,
     String localGoals,
     String visitorGoals,
     StateChangedListener listener
  );

  Call<PlayerStatisticsResult> getPlayerStatistics(
     String username,
     StateChangedListener listener
  );

}

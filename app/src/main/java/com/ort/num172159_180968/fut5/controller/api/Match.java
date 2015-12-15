/**
 * File generated by Magnet rest2mobile 1.1 - Nov 25, 2015 12:31:56 AM
 * @see {@link http://developer.magnet.com}
 */

package com.ort.num172159_180968.fut5.controller.api;

import com.magnet.android.mms.async.Call;
import com.magnet.android.mms.async.StateChangedListener;

import com.ort.num172159_180968.fut5.model.beans.*;

import java.util.List;

public interface Match {

  /**
   * Generated from URL POST http://localhost:8080/Fut5-war/webservice/addMatch?username=a&idField=40&date=29%2F09%2F2015+18%3A00%3A00
   * POST Fut5-war/webservice/addMatch
   * @param username  style:QUERY
   * @param idField  style:QUERY
   * @param date  style:QUERY
   * @param body  style:PLAIN
   * @param listener
   * @return void
   */
  Call<AddMatchResult> addMatch(
     String username,
     String idField,
     String date,
     AddMatchRequest body,
     StateChangedListener listener
  );

    Call<List<MatchesResult>> getMatches(
            String username,
            String mine,
            String next,
            StateChangedListener listener
    );


    /**
     * Generated from URL POST http://192.168.1.7:8080/Fut5-war/webservice/updateMatchDate?idMatch=701&date=1450141200000
     * POST Fut5-war/webservice/updateMatchDate
     * @param idMatch  style:QUERY
     * @param date  style:QUERY
     * @param listener
     * @return void
     */
    Call<Void> updateMatchDate(
            String idMatch,
            String date,
            StateChangedListener listener
    );

}

/**
 * File generated by Magnet rest2mobile 1.1 - Nov 27, 2015 6:34:55 PM
 * @see {@link http://developer.magnet.com}
 */

package com.ort.num172159_180968.fut5.model.beans;

import java.util.List;

/**
 * Generated from json example
{
  "dt" : 1448658000,
  "main" : {
    "temp" : 22.38,
    "temp_min" : 21.96,
    "temp_max" : 22.38,
    "pressure" : 1010.42,
    "sea_level" : 1012.63,
    "grnd_level" : 1010.42,
    "humidity" : 95,
    "temp_kf" : 0.42
  },
  "weather" : [ {
    "id" : 500,
    "main" : "Rain",
    "description" : "lightrain",
    "icon" : "ozd"
  } ],
  "clouds" : {
    "all" : 80
  ...

 */

public class ListWeather {

  
  private Clouds clouds;

  
  private Integer dt;

  
  private String dt_txt;

  
  private Main main;

  
  private Rain rain;

  
  private ListSys sys;

  
  private java.util.List<Weather> weather;

  
  private Wind wind;

  public Clouds getClouds() {
    return clouds;
  }
  public Integer getDt() {
    return dt;
  }
  public String getDt_txt() {
    return dt_txt;
  }
  public Main getMain() {
    return main;
  }
  public Rain getRain() {
    return rain;
  }
  public ListSys getSys() {
    return sys;
  }
  public List<Weather> getWeather() {
    return weather;
  }
  public Wind getWind() {
    return wind;
  }

  /**
  * Builder for ListWeather
  **/
  public static class ListBuilder {
    private ListWeather toBuild = new ListWeather();

    public ListBuilder() {
    }

    public ListWeather build() {
      return toBuild;
    }

    public ListBuilder clouds(Clouds value) {
      toBuild.clouds = value;
      return this;
    }
    public ListBuilder dt(Integer value) {
      toBuild.dt = value;
      return this;
    }
    public ListBuilder dt_txt(String value) {
      toBuild.dt_txt = value;
      return this;
    }
    public ListBuilder main(Main value) {
      toBuild.main = value;
      return this;
    }
    public ListBuilder rain(Rain value) {
      toBuild.rain = value;
      return this;
    }
    public ListBuilder sys(ListSys value) {
      toBuild.sys = value;
      return this;
    }
    public ListBuilder weather(List<Weather> value) {
      toBuild.weather = value;
      return this;
    }
    public ListBuilder wind(Wind value) {
      toBuild.wind = value;
      return this;
    }
  }
}

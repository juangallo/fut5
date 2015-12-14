/**
 * File generated by Magnet rest2mobile 1.1 - Dec 14, 2015 12:31:04 AM
 * @see {@link http://developer.magnet.com}
 */

package com.ort.num172159_180968.fut5.model.beans;


/**
 * Generated from json example
{
  "notificationId" : 52,
  "notificationType" : "NEWMATCH",
  "notificationText" : "Youhavebeeninvitedbynull,toplayagamein:CampusPrado",
  "notificationDate" : 1448319600000
}

 */

public class UserNotification {

  
  private Long notificationDate;

  
  private Integer notificationId;

  
  private String notificationText;

  
  private String notificationType;

  public Long getNotificationDate() {
    return notificationDate;
  }
  public Integer getNotificationId() {
    return notificationId;
  }
  public String getNotificationText() {
    return notificationText;
  }
  public String getNotificationType() {
    return notificationType;
  }

  /**
  * Builder for UserNotification
  **/
  public static class UserNotificationBuilder {
    private UserNotification toBuild = new UserNotification();

    public UserNotificationBuilder() {
    }

    public UserNotification build() {
      return toBuild;
    }

    public UserNotificationBuilder notificationDate(Long value) {
      toBuild.notificationDate = value;
      return this;
    }
    public UserNotificationBuilder notificationId(Integer value) {
      toBuild.notificationId = value;
      return this;
    }
    public UserNotificationBuilder notificationText(String value) {
      toBuild.notificationText = value;
      return this;
    }
    public UserNotificationBuilder notificationType(String value) {
      toBuild.notificationType = value;
      return this;
    }
  }
}
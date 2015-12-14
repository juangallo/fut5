/**
 * File generated by Magnet rest2mobile 1.1 - Dec 14, 2015 12:31:05 AM
 * @see {@link http://developer.magnet.com}
 */

package com.ort.num172159_180968.fut5.model.beans;

import java.util.List;

/**
 * Generated from json example
{
  "userId" : 1,
  "username" : "juanma",
  "password" : "juanma",
  "firstName" : "JuanMartin",
  "lastName" : "Gallo",
  "email" : "juan_gallo1@hotmail.com",
  "userNotifications" : [ {
    "notificationId" : 52,
    "notificationType" : "NEWMATCH",
    "notificationText" : "Youhavebeeninvitedbynull,toplayagamein:CampusPrado",
    "notificationDate" : 1448319600000
  }, {
    "notificationId" : 63,
    "notificationType" : "NEWMATCH",
    "notificationText" : "Youhavebeeninvitedbymika,toplayagamein:CampusPrado",
    "notificationDate" : 1448319600000
  }, {
    "notificationId" : 102,
    "notificationType" : "NEWMATCH",
      ...

 */

public class User {

  
  private String email;

  
  private String firstName;

  
  private String lastName;

  
  private Boolean needsFieldReload;

  
  private Boolean needsUserReload;

  
  private String password;

  
  private Integer userId;

  
  private List<UserNotification> userNotifications;

  
  private String username;

  public String getEmail() {
    return email;
  }
  public String getFirstName() {
    return firstName;
  }
  public String getLastName() {
    return lastName;
  }
  public Boolean getNeedsFieldReload() {
    return needsFieldReload;
  }
  public Boolean getNeedsUserReload() {
    return needsUserReload;
  }
  public String getPassword() {
    return password;
  }
  public Integer getUserId() {
    return userId;
  }
  public List<UserNotification> getUserNotifications() {
    return userNotifications;
  }
  public String getUsername() {
    return username;
  }

  /**
  * Builder for User
  **/
  public static class UserBuilder {
    private User toBuild = new User();

    public UserBuilder() {
    }

    public User build() {
      return toBuild;
    }

    public UserBuilder email(String value) {
      toBuild.email = value;
      return this;
    }
    public UserBuilder firstName(String value) {
      toBuild.firstName = value;
      return this;
    }
    public UserBuilder lastName(String value) {
      toBuild.lastName = value;
      return this;
    }
    public UserBuilder needsFieldReload(Boolean value) {
      toBuild.needsFieldReload = value;
      return this;
    }
    public UserBuilder needsUserReload(Boolean value) {
      toBuild.needsUserReload = value;
      return this;
    }
    public UserBuilder password(String value) {
      toBuild.password = value;
      return this;
    }
    public UserBuilder userId(Integer value) {
      toBuild.userId = value;
      return this;
    }
    public UserBuilder userNotifications(List<UserNotification> value) {
      toBuild.userNotifications = value;
      return this;
    }
    public UserBuilder username(String value) {
      toBuild.username = value;
      return this;
    }
  }
}
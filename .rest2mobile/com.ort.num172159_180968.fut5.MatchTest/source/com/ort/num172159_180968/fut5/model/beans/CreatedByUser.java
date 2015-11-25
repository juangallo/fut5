/**
 * File generated by Magnet rest2mobile 1.1 - Nov 25, 2015 12:58:57 AM
 * @see {@link http://developer.magnet.com}
 */

package com.ort.num172159_180968.fut5.model.beans;


/**
 * Generated from json example
{
  "userId" : 10000,
  "username" : "a",
  "password" : "a",
  "firstName" : "a",
  "lastName" : "a",
  "email" : "a",
  "userNotifications" : [ ],
  "needsFieldReload" : false,
  "needsUserReload" : false
}

TODO (generated by Magnet r2m) : Property userNotifications is ignored because the value is empty array([]) in the json example.

 */

public class CreatedByUser {

  
  private String email;

  
  private String firstName;

  
  private String lastName;

  
  private Boolean needsFieldReload;

  
  private Boolean needsUserReload;

  
  private String password;

  
  private Integer userId;

  
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
  public String getUsername() {
    return username;
  }

  /**
  * Builder for CreatedByUser
  **/
  public static class CreatedByUserBuilder {
    private CreatedByUser toBuild = new CreatedByUser();

    public CreatedByUserBuilder() {
    }

    public CreatedByUser build() {
      return toBuild;
    }

    public CreatedByUserBuilder email(String value) {
      toBuild.email = value;
      return this;
    }
    public CreatedByUserBuilder firstName(String value) {
      toBuild.firstName = value;
      return this;
    }
    public CreatedByUserBuilder lastName(String value) {
      toBuild.lastName = value;
      return this;
    }
    public CreatedByUserBuilder needsFieldReload(Boolean value) {
      toBuild.needsFieldReload = value;
      return this;
    }
    public CreatedByUserBuilder needsUserReload(Boolean value) {
      toBuild.needsUserReload = value;
      return this;
    }
    public CreatedByUserBuilder password(String value) {
      toBuild.password = value;
      return this;
    }
    public CreatedByUserBuilder userId(Integer value) {
      toBuild.userId = value;
      return this;
    }
    public CreatedByUserBuilder username(String value) {
      toBuild.username = value;
      return this;
    }
  }
}

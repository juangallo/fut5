/**
 * File generated by Magnet rest2mobile 1.1 - Oct 14, 2015 9:00:34 PM
 * @see {@link http://developer.magnet.com}
 */

package com.ort.num172159_180968.fut5.model.beans;


/**
 * Generated from json example
{
  "userId" : 1,
  "username" : "juan",
  "password" : "juan",
  "firstName" : "Juan",
  "lastName" : "Gallo",
  "email" : "juan_gallo1@hotmail.com",
  "userNotifications" : [ ]
}

TODO (generated by Magnet r2m) : Property userNotifications is ignored because the value is empty array([]) in the json example.

 */

public class UserResult {

  
  private String email;

  
  private String firstName;

  
  private String lastName;

  
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
  * Builder for UserResult
  **/
  public static class AddUserResultBuilder {
    private UserResult toBuild = new UserResult();

    public AddUserResultBuilder() {
    }

    public UserResult build() {
      return toBuild;
    }

    public AddUserResultBuilder email(String value) {
      toBuild.email = value;
      return this;
    }
    public AddUserResultBuilder firstName(String value) {
      toBuild.firstName = value;
      return this;
    }
    public AddUserResultBuilder lastName(String value) {
      toBuild.lastName = value;
      return this;
    }
    public AddUserResultBuilder password(String value) {
      toBuild.password = value;
      return this;
    }
    public AddUserResultBuilder userId(Integer value) {
      toBuild.userId = value;
      return this;
    }
    public AddUserResultBuilder username(String value) {
      toBuild.username = value;
      return this;
    }
  }
}

/**
 * File generated by Magnet rest2mobile 1.1 - Nov 22, 2015 7:40:30 PM
 * @see {@link http://developer.magnet.com}
 */

package com.ort.num172159_180968.fut5.model.beans;


/**
 * Generated from json example*/

public class UpdateUserRequest {

  
  private String image;

  public String getImage() {
    return image;
  }

  /**
  * Builder for UpdateUserRequest
  **/
  public static class UpdateUserRequestBuilder {
    private UpdateUserRequest toBuild = new UpdateUserRequest();

    public UpdateUserRequestBuilder() {
    }

    public UpdateUserRequest build() {
      return toBuild;
    }

    public UpdateUserRequestBuilder image(String value) {
      toBuild.image = value;
      return this;
    }
  }
}

/**
 * File generated by Magnet rest2mobile 1.1 - Nov 18, 2015 9:22:20 PM
 * @see {@link http://developer.magnet.com}
 */

package com.ort.num172159_180968.fut5.model.beans;


/**
 * Generated from json example
{
  "image" : "asdasdAsdfsdfgi345mfshfgvsdf"
}

 */

public class AddUserImageRequest {

  
  private String image;

  public String getImage() {
    return image;
  }

  /**
  * Builder for AddUserImageRequest
  **/
  public static class AddUserImageRequestBuilder {
    private AddUserImageRequest toBuild = new AddUserImageRequest();

    public AddUserImageRequestBuilder() {
    }

    public AddUserImageRequest build() {
      return toBuild;
    }

    public AddUserImageRequestBuilder image(String value) {
      toBuild.image = value;
      return this;
    }
  }
}

package com.rtiming.shared.dataexchange.cache;

/**
 * 
 */
public class CityCacheKey {

  private final String cityName;
  private final String zipCode;
  private final String countryCode;

  public CityCacheKey(String cityName, String zipCode, String countryCode) {
    this.cityName = cityName;
    this.zipCode = zipCode;
    this.countryCode = countryCode;
  }

  public String getCityName() {
    return cityName;
  }

  public String getZipCode() {
    return zipCode;
  }

  public String getCountryCode() {
    return countryCode;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((cityName == null) ? 0 : cityName.hashCode());
    result = prime * result + ((countryCode == null) ? 0 : countryCode.hashCode());
    result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof CityCacheKey)) {
      return false;
    }
    CityCacheKey other = (CityCacheKey) obj;
    if (cityName == null) {
      if (other.cityName != null) {
        return false;
      }
    }
    else if (!cityName.equals(other.cityName)) {
      return false;
    }
    if (countryCode == null) {
      if (other.countryCode != null) {
        return false;
      }
    }
    else if (!countryCode.equals(other.countryCode)) {
      return false;
    }
    if (zipCode == null) {
      if (other.zipCode != null) {
        return false;
      }
    }
    else if (!zipCode.equals(other.zipCode)) {
      return false;
    }
    return true;
  }

}

package com.rtiming.shared.dataexchange.cache;

/**
 * 
 */
public class CityRegionCacheKey {

  private final String cityName;
  private final String zipCode;
  private final String region;
  private final String countryCode;
  private final Long languageUid;
  private final String countryName;

  public CityRegionCacheKey(String cityName, String zipCode, String region, String countryCode, Long languageUid, String countryName) {
    this.cityName = cityName;
    this.zipCode = zipCode;
    this.region = region;
    this.countryCode = countryCode;
    this.languageUid = languageUid;
    this.countryName = countryName;
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

  public String getRegion() {
    return region;
  }

  public Long getLanguageUid() {
    return languageUid;
  }

  public String getCountryName() {
    return countryName;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((cityName == null) ? 0 : cityName.hashCode());
    result = prime * result + ((countryCode == null) ? 0 : countryCode.hashCode());
    result = prime * result + ((countryName == null) ? 0 : countryName.hashCode());
    result = prime * result + ((languageUid == null) ? 0 : languageUid.hashCode());
    result = prime * result + ((region == null) ? 0 : region.hashCode());
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
    if (!(obj instanceof CityRegionCacheKey)) {
      return false;
    }
    CityRegionCacheKey other = (CityRegionCacheKey) obj;
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
    if (countryName == null) {
      if (other.countryName != null) {
        return false;
      }
    }
    else if (!countryName.equals(other.countryName)) {
      return false;
    }
    if (languageUid == null) {
      if (other.languageUid != null) {
        return false;
      }
    }
    else if (!languageUid.equals(other.languageUid)) {
      return false;
    }
    if (region == null) {
      if (other.region != null) {
        return false;
      }
    }
    else if (!region.equals(other.region)) {
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

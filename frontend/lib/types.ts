export interface ZoneLookupResponse {
    success: boolean;
    message?: string;
    data?: ZoneResult;
    timestamp: number;
  }
  
  export interface ZoneResult {
    postcode: string;
    zone: string;
    zoneNumber: number;
    area?: string;
    latitude: number;
    longitude: number;
    distanceFromCentreKm: number;
    fareInfo?: FareInfo;
  }
  
  export interface FareInfo {
    peakFare?: number;
    offPeakFare?: number;
    currency?: string;
  }
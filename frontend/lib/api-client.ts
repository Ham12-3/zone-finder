import type { ZoneLookupResponse } from './types';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL ?? 'http://localhost:8080';

export async function lookupZone(postcode: string): Promise<ZoneLookupResponse> {
  const response = await fetch(`${API_BASE_URL}/api/zones/lookup`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ postcode }),
  });

  const data: ZoneLookupResponse = await response.json();
  if (!response.ok) {
    throw new Error(data.message ?? 'Unable to fetch zone information');
  }
  return data;    
}
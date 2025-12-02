'use client';

import { useState } from 'react';
import SearchForm from '@/components/SearchForm';
import ResultsCard from '@/components/ResultsCard';
import InfoSection from '@/components/InfoSection';
import type { ZoneLookupResponse, ZoneResult } from '@/lib/types';
import { lookupZone } from '@/lib/api-client';

export default function HomePage() {
  const [result, setResult] = useState<ZoneResult | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const handleSearch = async (postcode: string) => {
    setLoading(true);
    setError(null);
    setResult(null);

    try {
      const response = await lookupZone(postcode);
      if (!response.success || !response.data) {
        throw new Error(response.message ?? 'Lookup failed');
      }
      setResult(response.data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Unexpected error');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-600 via-indigo-600 to-purple-800 py-12 px-4 app-gradient">
      <div className="max-w-4xl mx-auto bg-white rounded-2xl shadow-2xl p-8">
        <header className="text-center mb-10">
          <h1 className="text-4xl font-bold text-gray-900 mb-2">🚇 London Zone Finder</h1>
          <p className="text-gray-600">Enter a London postcode to discover its TfL zone.</p>
        </header>

        <SearchForm onSearch={handleSearch} isLoading={loading} />

        {error && (
          <div className="mt-6 p-4 bg-red-50 border-l-4 border-red-500 rounded-lg text-red-700 fade-in-up">
            {error}
          </div>
        )}

        {result && <ResultsCard result={result} className="fade-in-up" />}

        <InfoSection className="fade-in-up" />
      </div>
    </div>
  );
}
'use client';

import { FormEvent, useState } from 'react';

interface Props {
  onSearch: (postcode: string) => void;
  isLoading: boolean;
}

export default function SearchForm({ onSearch, isLoading }: Props) {
  const [postcode, setPostcode] = useState('');

  const handleSubmit = (event: FormEvent) => {
    event.preventDefault();
    if (!postcode.trim()) return;
    onSearch(postcode.trim());
  };

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-3 sm:flex-row">
      <input
        type="text"
        value={postcode}
        onChange={(e) => setPostcode(e.target.value)}
        placeholder="e.g. SW1A 1AA"
        className="flex-1 rounded-lg border-2 border-gray-300 px-4 py-3 text-lg focus:border-purple-500 focus:outline-none"
        disabled={isLoading}
      />
      <button
        type="submit"
        disabled={isLoading}
        className="rounded-lg bg-gradient-to-r from-purple-600 to-indigo-600 px-6 py-3 text-white font-semibold transition hover:shadow-lg disabled:opacity-60"
      >
        {isLoading ? 'Searching…' : 'Find Zone'}
      </button>
    </form>
  );
}
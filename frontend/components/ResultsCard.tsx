import type { ZoneResult } from '@/lib/types';
interface ResultsCardProps {
    result: ZoneResult;
    className?: string;
  }

export default function ResultsCard({ result, className }: ResultsCardProps) {
  return (
    <section  className={`mt-8 rounded-xl border border-gray-200 bg-gray-50 p-6 ${className ?? ''}`}>
      <h2 className="mb-4 text-2xl font-semibold text-gray-800">Results</h2>
      <dl className="space-y-3">
        <InfoRow label="Postcode" value={result.postcode} />
        <InfoRow
          label="Zone"
          value={result.zone}
          highlight
        />
        <InfoRow label="Area" value={result.area ?? 'Unknown'} />
        <InfoRow label="Distance from centre" value={`${result.distanceFromCentreKm} km`} />
        <InfoRow label="Latitude" value={result.latitude.toFixed(6)} />
        <InfoRow label="Longitude" value={result.longitude.toFixed(6)} />
        {result.fareInfo && (
          <>
            <InfoRow label="Peak fare" value={`£${result.fareInfo.peakFare?.toFixed(2) ?? '--'}`} />
            <InfoRow label="Off-peak fare" value={`£${result.fareInfo.offPeakFare?.toFixed(2) ?? '--'}`} />
          </>
        )}
      </dl>
    </section>
  );
}

function InfoRow({ label, value, highlight = false }: { label: string; value: string | number; highlight?: boolean }) {
  return (
    <div className="flex items-center justify-between border-b border-gray-200 pb-2 last:border-none">
      <dt className="font-semibold text-gray-600">{label}</dt>
      <dd className={`text-gray-900 ${highlight ? 'rounded-full bg-purple-600 px-4 py-1 text-white font-bold' : ''}`}>
        {value}
      </dd>
    </div>
  );
}
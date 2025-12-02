const items = [
    { zone: 'Zone 1', text: 'Central London (Westminster, City of London)' },
    { zone: 'Zones 2-3', text: 'Inner London suburbs' },
    { zone: 'Zones 4-6', text: 'Outer London areas' },
    { zone: 'Zones 7-9', text: 'Greater London outskirts' },
  ];
  
  interface InfoSectionProps {
    className?: string;
  }

  export default function InfoSection({ className }: InfoSectionProps) {
    return (
      <section className={`mt-10 rounded-xl bg-purple-50 p-6 ${className ?? ''}`}>
        <h3 className="text-xl font-semibold text-gray-800 mb-3">About London Zones</h3>
        <p className="text-gray-700 mb-4">
          TfL divides London into nine fare zones. Zone 1 covers central London, with higher zones extending outward.
        </p>
        <ul className="space-y-2">
          {items.map((item) => (
            <li key={item.zone} className="flex gap-3">
              <span className="font-semibold text-purple-700 w-24">{item.zone}:</span>
              <span className="text-gray-700">{item.text}</span>
            </li>
          ))}
        </ul>
      </section>
    );
  }
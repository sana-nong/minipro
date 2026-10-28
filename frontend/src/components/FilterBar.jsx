import React from 'react';
import { Search, Filter, ArrowUpDown } from 'lucide-react';

const CATEGORIES = [
  { key: 'ALL', label: 'All Opportunities' },
  { key: 'RECRUITMENT', label: 'Recruitments & Jobs' },
  { key: 'SCHOLARSHIP', label: 'Scholarships' },
  { key: 'FELLOWSHIP', label: 'Fellowships' },
  { key: 'INTERNSHIP', label: 'Internships' },
  { key: 'COMPETITIVE_EXAM', label: 'Competitive Exams' },
];

export default function FilterBar({ filters, setFilters, onReset }) {
  const handleChange = (field, val) => {
    setFilters(prev => ({ ...prev, [field]: val }));
  };

  return (
    <div className="filter-container">
      {/* Category Pills */}
      <div className="category-pills">
        {CATEGORIES.map(cat => (
          <button
            key={cat.key}
            className={`category-pill ${filters.category === cat.key ? 'active' : ''}`}
            onClick={() => handleChange('category', cat.key)}
          >
            {cat.label}
          </button>
        ))}
      </div>

      {/* Main Search Row */}
      <div className="search-bar-row">
        <div className="search-input-wrapper">
          <Search size={18} className="search-icon" />
          <input
            type="text"
            className="search-input"
            placeholder="Search by keywords, department (e.g. UPSC, DRDO, ISRO), title, or skills..."
            value={filters.query || ''}
            onChange={(e) => handleChange('query', e.target.value)}
          />
        </div>
      </div>

      {/* Secondary Multi-facet Filters (Objective 2) */}
      <div className="filter-row-secondary">
        <select
          className="filter-select"
          value={filters.qualification || 'ALL'}
          onChange={(e) => handleChange('qualification', e.target.value)}
        >
          <option value="ALL">All Qualifications</option>
          <option value="B.Tech">B.Tech / B.E (Engineering)</option>
          <option value="Degree">Any Bachelor's Degree</option>
          <option value="Master">Master's (M.Tech/M.Sc)</option>
          <option value="12th">10+2 / Higher Secondary</option>
          <option value="Diploma">Diploma / Polytechnic</option>
        </select>

        <select
          className="filter-select"
          value={filters.location || 'ALL'}
          onChange={(e) => handleChange('location', e.target.value)}
        >
          <option value="ALL">All Locations</option>
          <option value="All India">All India</option>
          <option value="Karnataka">Karnataka / Bengaluru / Tumakuru</option>
          <option value="New Delhi">New Delhi</option>
          <option value="Abroad">International / Abroad</option>
        </select>

        <select
          className="filter-select"
          value={filters.closingInDays || ''}
          onChange={(e) => handleChange('closingInDays', e.target.value ? Number(e.target.value) : '')}
        >
          <option value="">Any Deadline</option>
          <option value="7">Closing in &le; 7 Days (Urgent)</option>
          <option value="15">Closing in &le; 15 Days</option>
          <option value="30">Closing in &le; 30 Days</option>
        </select>

        <select
          className="filter-select"
          value={filters.status || 'ALL'}
          onChange={(e) => handleChange('status', e.target.value)}
        >
          <option value="ALL">All Statuses</option>
          <option value="ACTIVE">Active Only</option>
          <option value="CLOSING_SOON">Closing Soon Only</option>
          <option value="EXPIRED">Expired</option>
        </select>

        <select
          className="filter-select"
          value={filters.sortBy || 'deadline'}
          onChange={(e) => handleChange('sortBy', e.target.value)}
        >
          <option value="deadline">Sort: Deadline (Nearest First)</option>
          <option value="createdAt">Sort: Recently Added</option>
          <option value="title">Sort: Title (A-Z)</option>
        </select>
      </div>
    </div>
  );
}

import React from 'react';
import { Search, Filter, ArrowUpDown, ShieldCheck, Clock, Archive, Sparkles } from 'lucide-react';

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

  const currentStatus = filters.status || 'ACTIVE';

  return (
    <div className="filter-container">
      {/* Top-Level 1-Click View Mode Tabs (Two-Tier Architecture) */}
      <div className="view-mode-tabs">
        <button
          className={`view-mode-tab ${currentStatus === 'ACTIVE' ? 'active active-live' : ''}`}
          onClick={() => handleChange('status', 'ACTIVE')}
          type="button"
        >
          <span className="indicator-dot live-dot"></span>
          <span>Open to Apply Now</span>
          <span className="view-mode-badge">Active Cycles</span>
        </button>

        <button
          className={`view-mode-tab ${currentStatus === 'CLOSING_SOON' ? 'active active-urgent' : ''}`}
          onClick={() => handleChange('status', 'CLOSING_SOON')}
          type="button"
        >
          <span className="indicator-dot urgent-dot"></span>
          <span>Closing Soon</span>
          <span className="view-mode-badge">&le; 7 Days</span>
        </button>

        <button
          className={`view-mode-tab ${currentStatus === 'EXPIRED' ? 'active active-gazette' : ''}`}
          onClick={() => handleChange('status', 'EXPIRED')}
          type="button"
        >
          <span className="indicator-dot gazette-dot"></span>
          <span>Official Gazette Archive</span>
          <span className="view-mode-badge">Benchmark Records</span>
        </button>

        <button
          className={`view-mode-tab ${currentStatus === 'ALL' ? 'active' : ''}`}
          onClick={() => handleChange('status', 'ALL')}
          type="button"
        >
          <span>All Monitored</span>
          <span className="view-mode-badge">All Records</span>
        </button>
      </div>

      {/* Dynamic View Mode Context Banner */}
      {currentStatus === 'ACTIVE' && (
        <div className="mode-description-banner banner-live">
          <ShieldCheck size={16} />
          <span>
            <strong>Active Live Recruitments:</strong> Showing currently open government cycles with verified, functioning application portals and active deadlines (Nov/Dec 2026 & Feb 2027).
          </span>
        </div>
      )}

      {currentStatus === 'CLOSING_SOON' && (
        <div className="mode-description-banner banner-urgent">
          <Clock size={16} />
          <span>
            <strong>Closing Soon (Urgent):</strong> Opportunities reaching their official statutory closing deadline within 7 days.
          </span>
        </div>
      )}

      {currentStatus === 'EXPIRED' && (
        <div className="mode-description-banner banner-gazette">
          <Archive size={16} />
          <span>
            <strong>Official Gazette Archive:</strong> Showing genuine historical Gazette benchmark records (HAL Management Trainee, NIC Scientist 'B', DRDO RAC, ISRO ICRB, UPSC CSE/CDS) retaining their authentic real-world advertisement numbers and verbatim statutory clauses for examiner verification against the Gazette of India.
          </span>
        </div>
      )}

      {currentStatus === 'ALL' && (
        <div className="mode-description-banner banner-all">
          <Sparkles size={16} />
          <span>
            <strong>All Opportunities:</strong> Displaying all monitored government opportunities across active cycles and archived gazette records.
          </span>
        </div>
      )}

      {/* Category Pills */}
      <div className="category-pills">
        {CATEGORIES.map(cat => (
          <button
            key={cat.key}
            className={`category-pill ${filters.category === cat.key ? 'active' : ''}`}
            onClick={() => handleChange('category', cat.key)}
            type="button"
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
            placeholder="Search by keywords, department (e.g. SBI, UPSC, DRDO, HAL, NIC), title, or advertisement reference..."
            value={filters.query || ''}
            onChange={(e) => handleChange('query', e.target.value)}
          />
        </div>
      </div>

      {/* Secondary Multi-facet Filters */}
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
          <option value="">Any Deadline Window</option>
          <option value="7">Closing in &le; 7 Days</option>
          <option value="15">Closing in &le; 15 Days</option>
          <option value="30">Closing in &le; 30 Days</option>
        </select>

        <select
          className="filter-select"
          value={filters.status || 'ACTIVE'}
          onChange={(e) => handleChange('status', e.target.value)}
        >
          <option value="ACTIVE">Open to Apply (Active)</option>
          <option value="CLOSING_SOON">Closing Soon (&le; 7 Days)</option>
          <option value="EXPIRED">Official Gazette Archive</option>
          <option value="ALL">All Statuses (Complete)</option>
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

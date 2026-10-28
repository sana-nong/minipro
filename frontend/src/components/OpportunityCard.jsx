import React from 'react';
import { Building2, Calendar, MapPin, GraduationCap, ExternalLink, Bookmark, CheckCircle2, AlertCircle } from 'lucide-react';

export default function OpportunityCard({ opportunity, onSelect, isBookmarked, onToggleBookmark }) {
  const getCategoryClass = (cat) => {
    switch (cat) {
      case 'RECRUITMENT': return 'tag-recruitment';
      case 'SCHOLARSHIP': return 'tag-scholarship';
      case 'FELLOWSHIP': return 'tag-fellowship';
      case 'INTERNSHIP': return 'tag-internship';
      case 'COMPETITIVE_EXAM': return 'tag-exam';
      default: return 'tag-recruitment';
    }
  };

  const getCategoryLabel = (cat) => {
    switch (cat) {
      case 'RECRUITMENT': return 'Job / Recruitment';
      case 'SCHOLARSHIP': return 'Scholarship';
      case 'FELLOWSHIP': return 'Fellowship';
      case 'INTERNSHIP': return 'Internship';
      case 'COMPETITIVE_EXAM': return 'Competitive Exam';
      default: return cat;
    }
  };

  const daysLeft = opportunity.daysRemaining ?? 0;
  const isClosingSoon = opportunity.status === 'CLOSING_SOON' || (daysLeft <= 7 && daysLeft >= 0);

  return (
    <div className="opp-card">
      <div>
        <div className="opp-card-header">
          <span className={`category-tag ${getCategoryClass(opportunity.category)}`}>
            {getCategoryLabel(opportunity.category)}
          </span>

          <span className={`deadline-indicator ${isClosingSoon ? 'deadline-closing' : 'deadline-active'}`}>
            <Calendar size={13} />
            {daysLeft > 0 ? `${daysLeft} days left` : daysLeft === 0 ? 'Last day today' : 'Expired'}
          </span>
        </div>

        <div className="opp-org">
          <Building2 size={14} />
          <span>{opportunity.organization}</span>
        </div>

        <h2 className="opp-title">{opportunity.title}</h2>

        <p className="opp-desc">{opportunity.description}</p>

        <div className="opp-metadata-chips">
          <span className="meta-chip">
            <GraduationCap size={13} />
            {opportunity.qualification}
          </span>
          <span className="meta-chip">
            <MapPin size={13} />
            {opportunity.location}
          </span>
          {opportunity.maxAge && (
            <span className="meta-chip">
              Age: &le; {opportunity.maxAge} yrs
            </span>
          )}
          {opportunity.applicationFee && (
            <span className="meta-chip">
              Fee: {opportunity.applicationFee.split('|')[0]}
            </span>
          )}
        </div>
      </div>

      <div className="opp-footer">
        <button
          className="opp-primary-btn"
          onClick={() => onSelect(opportunity)}
        >
          Check Eligibility & Details
        </button>

        <div style={{ display: 'flex', gap: '0.4rem' }}>
          <button
            className={`opp-icon-btn ${isBookmarked ? 'active' : ''}`}
            onClick={() => onToggleBookmark(opportunity)}
            title={isBookmarked ? 'Bookmarked in Tracker' : 'Bookmark for Tracking'}
          >
            <Bookmark size={16} fill={isBookmarked ? 'currentColor' : 'none'} />
          </button>

          <a
            href={opportunity.officialUrl}
            target="_blank"
            rel="noopener noreferrer"
            className="opp-icon-btn"
            title="Open official portal notification"
          >
            <ExternalLink size={16} />
          </a>
        </div>
      </div>
    </div>
  );
}

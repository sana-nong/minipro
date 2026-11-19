import React from 'react';
import { Building2, Calendar, MapPin, GraduationCap, ExternalLink, Bookmark, FileText, CheckCircle2, Sparkles, Send } from 'lucide-react';

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
    <div className={`opp-card ${opportunity.newlySynced ? 'newly-synced-card' : ''}`}>
      <div>
        <div className="opp-card-header">
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.4rem', flexWrap: 'wrap' }}>
            <span className={`category-tag ${getCategoryClass(opportunity.category)}`}>
              {getCategoryLabel(opportunity.category)}
            </span>
            {opportunity.newlySynced && (
              <span className="newly-synced-pill" title="Freshly extracted in latest aggregation sync">
                <Sparkles size={11} />
                <span>Newly Synced</span>
              </span>
            )}
          </div>

          <span className={`deadline-indicator ${daysLeft < 0 ? 'deadline-archived' : isClosingSoon ? 'deadline-closing' : 'deadline-active'}`}>
            <Calendar size={13} />
            <strong>{opportunity.deadline}</strong>
            <span style={{ opacity: 0.85 }}>
              {daysLeft > 0 ? `(${daysLeft}d left)` : daysLeft === 0 ? '(Last day today)' : '(Gazette Extracted)'}
            </span>
          </span>
        </div>

        <div className="opp-org">
          <Building2 size={14} />
          <span>{opportunity.organization}</span>
        </div>

        <h2 className="opp-title" onClick={() => onSelect(opportunity)} style={{ cursor: 'pointer' }}>
          {opportunity.title}
        </h2>

        {/* Gazette / Advertisement Reference Badge */}
        {opportunity.notificationReference && (
          <div className="notice-ref-badge">
            <FileText size={13} color="#2563eb" />
            <span>Official Gazette Ref: <strong>{opportunity.notificationReference}</strong></span>
          </div>
        )}

        {/* Extracted Deadline Clause Quote Box */}
        {opportunity.extractedDateSnippet && (
          <div className="extracted-clause-box">
            <span className="extracted-clause-label">Extracted Deadline Clause:</span>
            <p className="extracted-clause-text">"{opportunity.extractedDateSnippet}"</p>
          </div>
        )}

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

        {/* Direct Portal Indicator */}
        <div className="portal-indicator-strip">
          <span style={{ fontSize: '0.75rem', color: '#64748b' }}>Direct Apply Portal:</span>
          <strong style={{ fontSize: '0.78rem', color: '#1e3a8a' }}>
            {opportunity.portalName || (opportunity.sourceAgency + ' Portal')}
          </strong>
        </div>
      </div>

      <div className="opp-footer">
        <button
          className="opp-primary-btn"
          onClick={() => onSelect(opportunity)}
        >
          Check Eligibility & Details
        </button>

        <div style={{ display: 'flex', gap: '0.4rem', alignItems: 'center' }}>
          {/* Direct Apply Button */}
          <a
            href={opportunity.directApplyUrl || opportunity.officialUrl}
            target="_blank"
            rel="noopener noreferrer"
            className="opp-apply-btn"
            title={`Apply directly on official portal: ${opportunity.portalName || opportunity.directApplyUrl}`}
          >
            <Send size={13} />
            <span>Direct Apply</span>
          </a>

          {/* Official PDF / Circular Link */}
          {opportunity.notificationPdfUrl && (
            <a
              href={opportunity.notificationPdfUrl}
              target="_blank"
              rel="noopener noreferrer"
              className="opp-icon-btn pdf-btn"
              title="Open Official Gazette Notification (PDF)"
            >
              <FileText size={16} />
            </a>
          )}

          <button
            className={`opp-icon-btn ${isBookmarked ? 'active' : ''}`}
            onClick={() => onToggleBookmark(opportunity)}
            title={isBookmarked ? 'Bookmarked in Tracker' : 'Bookmark for Tracking'}
          >
            <Bookmark size={16} fill={isBookmarked ? 'currentColor' : 'none'} />
          </button>
        </div>
      </div>
    </div>
  );
}

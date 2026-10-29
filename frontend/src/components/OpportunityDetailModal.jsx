import React, { useState } from 'react';
import { X, ExternalLink, Calendar, MapPin, GraduationCap, Building2, CheckCircle2, XCircle, HelpCircle, Bookmark } from 'lucide-react';
import { checkEligibility } from '../services/api';

export default function OpportunityDetailModal({ opportunity, onClose, isBookmarked, onToggleBookmark }) {
  const [candidateProfile, setCandidateProfile] = useState({
    qualification: 'B.Tech / B.E in Computer Science',
    age: 22,
    percentage: 75
  });

  const [checkResult, setCheckResult] = useState(null);
  const [checking, setChecking] = useState(false);

  if (!opportunity) return null;

  const handleEligibilityCheck = async (e) => {
    e.preventDefault();
    setChecking(true);
    try {
      const res = await checkEligibility(opportunity.id, candidateProfile);
      setCheckResult(res);
    } catch (err) {
      console.error(err);
    } finally {
      setChecking(false);
    }
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.4rem' }}>
              <span className="category-tag tag-recruitment">{opportunity.category}</span>
              <span style={{ fontSize: '0.8rem', color: '#64748b', fontWeight: 600 }}>Source Agency: {opportunity.sourceAgency}</span>
            </div>
            <h2 style={{ fontSize: '1.25rem', fontWeight: 800, color: '#0f172a' }}>{opportunity.title}</h2>
          </div>
          <button className="opp-icon-btn" onClick={onClose}>
            <X size={20} />
          </button>
        </div>

        <div className="modal-body">
          {/* Key Meta Grid */}
          <div style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
            gap: '1rem',
            background: '#f8fafc',
            padding: '1.25rem',
            borderRadius: '10px',
            marginBottom: '1.5rem',
            border: '1px solid #e2e8f0'
          }}>
            <div>
              <p style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: 600 }}>ISSUING DEPARTMENT / PSU</p>
              <p style={{ fontSize: '0.9rem', fontWeight: 700, color: '#0f172a' }}>{opportunity.organization}</p>
            </div>
            <div>
              <p style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: 600 }}>APPLICATION DEADLINE</p>
              <p style={{ fontSize: '0.9rem', fontWeight: 700, color: '#b91c1c' }}>
                {opportunity.deadline} ({opportunity.daysRemaining} days left)
              </p>
            </div>
            <div>
              <p style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: 600 }}>LOCATION</p>
              <p style={{ fontSize: '0.9rem', fontWeight: 700, color: '#0f172a' }}>{opportunity.location}</p>
            </div>
            <div>
              <p style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: 600 }}>APPLICATION FEE</p>
              <p style={{ fontSize: '0.9rem', fontWeight: 700, color: '#0f172a' }}>{opportunity.applicationFee || 'Free'}</p>
            </div>
          </div>

          {/* Description */}
          <div style={{ marginBottom: '1.5rem' }}>
            <h3 style={{ fontSize: '1rem', fontWeight: 700, marginBottom: '0.5rem', color: '#0f172a' }}>Notification Summary & Scope</h3>
            <p style={{ fontSize: '0.9rem', color: '#334155', lineHeight: '1.6' }}>{opportunity.description}</p>
          </div>

          {/* Official Eligibility Requirements */}
          <div style={{ marginBottom: '1.5rem' }}>
            <h3 style={{ fontSize: '1rem', fontWeight: 700, marginBottom: '0.5rem', color: '#0f172a' }}>Official Eligibility Criteria</h3>
            <div style={{ background: '#eff6ff', borderLeft: '4px solid #2563eb', padding: '1rem', borderRadius: '4px' }}>
              <p style={{ fontSize: '0.9rem', color: '#1e3a8a', fontWeight: 600, marginBottom: '0.4rem' }}>
                Required Qualification: {opportunity.qualification}
              </p>
              <p style={{ fontSize: '0.85rem', color: '#1e40af' }}>{opportunity.eligibilityCriteria}</p>
              <div style={{ display: 'flex', gap: '1.5rem', marginTop: '0.6rem', fontSize: '0.8rem', color: '#1e40af' }}>
                <span><strong>Min Age:</strong> {opportunity.minAge || 18} yrs</span>
                <span><strong>Max Age:</strong> {opportunity.maxAge || 'No limit'} yrs</span>
                <span><strong>Min Marks:</strong> {opportunity.minPercentage ? `${opportunity.minPercentage}%` : 'Passing score'}</span>
              </div>
            </div>
          </div>

          {/* Interactive Candidate Eligibility Matcher */}
          <div style={{
            background: '#ffffff',
            border: '1px solid #cbd5e1',
            borderRadius: '12px',
            padding: '1.25rem',
            boxShadow: '0 2px 4px rgba(0,0,0,0.04)'
          }}>
            <h4 style={{ fontSize: '0.95rem', fontWeight: 700, marginBottom: '0.75rem', color: '#0f172a', display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
              <HelpCircle size={16} color="#2563eb" />
              Instant Eligibility & Compatibility Checker
            </h4>

            <form onSubmit={handleEligibilityCheck} style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(160px, 1fr))', gap: '0.75rem', marginBottom: '1rem' }}>
              <div>
                <label style={{ fontSize: '0.75rem', fontWeight: 600, color: '#64748b' }}>Your Qualification</label>
                <input
                  type="text"
                  className="filter-select"
                  style={{ width: '100%', marginTop: '0.2rem' }}
                  value={candidateProfile.qualification}
                  onChange={(e) => setCandidateProfile(p => ({ ...p, qualification: e.target.value }))}
                />
              </div>

              <div>
                <label style={{ fontSize: '0.75rem', fontWeight: 600, color: '#64748b' }}>Your Age (Years)</label>
                <input
                  type="number"
                  className="filter-select"
                  style={{ width: '100%', marginTop: '0.2rem' }}
                  value={candidateProfile.age}
                  onChange={(e) => setCandidateProfile(p => ({ ...p, age: Number(e.target.value) }))}
                />
              </div>

              <div>
                <label style={{ fontSize: '0.75rem', fontWeight: 600, color: '#64748b' }}>Aggregate Percentage (%)</label>
                <input
                  type="number"
                  className="filter-select"
                  style={{ width: '100%', marginTop: '0.2rem' }}
                  value={candidateProfile.percentage}
                  onChange={(e) => setCandidateProfile(p => ({ ...p, percentage: Number(e.target.value) }))}
                />
              </div>

              <div style={{ display: 'flex', alignItems: 'flex-end' }}>
                <button
                  type="submit"
                  className="opp-primary-btn"
                  style={{ width: '100%', justifyContent: 'center' }}
                  disabled={checking}
                >
                  {checking ? 'Checking...' : 'Check My Eligibility'}
                </button>
              </div>
            </form>

            {/* Check Result Display */}
            {checkResult && (
              <div style={{
                padding: '0.85rem 1rem',
                borderRadius: '8px',
                background: checkResult.eligible ? '#f0fdf4' : '#fef2f2',
                border: `1px solid ${checkResult.eligible ? '#bbf7d0' : '#fecaca'}`
              }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.4rem' }}>
                  {checkResult.eligible ? (
                    <CheckCircle2 size={18} color="#15803d" />
                  ) : (
                    <XCircle size={18} color="#b91c1c" />
                  )}
                  <strong style={{ fontSize: '0.9rem', color: checkResult.eligible ? '#15803d' : '#b91c1c' }}>
                    {checkResult.eligible ? 'Candidate is Eligible for this Opportunity' : 'Candidate Criteria Mismatch'}
                  </strong>
                </div>

                {checkResult.matchReasons && checkResult.matchReasons.length > 0 && (
                  <ul style={{ fontSize: '0.8rem', color: '#166534', paddingLeft: '1.25rem', marginBottom: '0.25rem' }}>
                    {checkResult.matchReasons.map((m, i) => <li key={i}>{m}</li>)}
                  </ul>
                )}

                {checkResult.mismatchReasons && checkResult.mismatchReasons.length > 0 && (
                  <ul style={{ fontSize: '0.8rem', color: '#991b1b', paddingLeft: '1.25rem' }}>
                    {checkResult.mismatchReasons.map((m, i) => <li key={i}>{m}</li>)}
                  </ul>
                )}
              </div>
            )}
          </div>
        </div>

        <div className="modal-footer">
          <button
            className={`tab-btn ${isBookmarked ? 'active' : ''}`}
            onClick={() => onToggleBookmark(opportunity)}
          >
            <Bookmark size={16} fill={isBookmarked ? 'currentColor' : 'none'} />
            <span>{isBookmarked ? 'Bookmarked' : 'Add to Application Tracker'}</span>
          </button>

          <a
            href={opportunity.officialUrl}
            target="_blank"
            rel="noopener noreferrer"
            className="sync-quick-btn"
          >
            <ExternalLink size={16} />
            <span>Apply on Official Portal</span>
          </a>
        </div>
      </div>
    </div>
  );
}

import React, { useState } from 'react';
import { UserCheck, CheckCircle2, AlertTriangle, ArrowRight, ExternalLink } from 'lucide-react';
import { checkEligibility } from '../services/api';

export default function ProfileMatcherModal({ opportunities, onSelectOpportunity }) {
  const [profile, setProfile] = useState({
    qualification: 'B.Tech',
    age: 22,
    percentage: 70,
    category: 'General'
  });

  const [matches, setMatches] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleMatchAll = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const results = await Promise.all(
        opportunities.map(async (opp) => {
          const res = await checkEligibility(opp.id, profile);
          return {
            opportunity: opp,
            evaluation: res
          };
        })
      );

      setMatches(results);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const eligibleCount = matches ? matches.filter(m => m.evaluation.eligible).length : 0;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
      <div style={{
        background: 'white',
        border: '1px solid #cbd5e1',
        borderRadius: '12px',
        padding: '1.5rem',
        boxShadow: '0 1px 3px rgba(0,0,0,0.05)'
      }}>
        <h2 style={{ fontSize: '1.25rem', fontWeight: 800, color: '#0f172a', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <UserCheck size={22} color="#1e3a8a" />
          Automated Profile-to-Opportunity Eligibility Engine
        </h2>
        <p style={{ fontSize: '0.85rem', color: '#64748b', marginTop: '0.2rem', marginBottom: '1.25rem' }}>
          Fulfills <strong>Objective 2 & 3</strong>: Evaluates candidate credentials against requirements across central and state government notifications.
        </p>

        <form onSubmit={handleMatchAll} style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
          gap: '1rem',
          background: '#f8fafc',
          padding: '1.25rem',
          borderRadius: '10px',
          border: '1px solid #e2e8f0'
        }}>
          <div>
            <label style={{ fontSize: '0.75rem', fontWeight: 700, color: '#475569' }}>Target Qualification / Degree</label>
            <input
              type="text"
              className="filter-select"
              style={{ width: '100%', marginTop: '0.3rem' }}
              value={profile.qualification}
              onChange={(e) => setProfile(p => ({ ...p, qualification: e.target.value }))}
            />
          </div>

          <div>
            <label style={{ fontSize: '0.75rem', fontWeight: 700, color: '#475569' }}>Candidate Age (Years)</label>
            <input
              type="number"
              className="filter-select"
              style={{ width: '100%', marginTop: '0.3rem' }}
              value={profile.age}
              onChange={(e) => setProfile(p => ({ ...p, age: Number(e.target.value) }))}
            />
          </div>

          <div>
            <label style={{ fontSize: '0.75rem', fontWeight: 700, color: '#475569' }}>Academic Score (%)</label>
            <input
              type="number"
              className="filter-select"
              style={{ width: '100%', marginTop: '0.3rem' }}
              value={profile.percentage}
              onChange={(e) => setProfile(p => ({ ...p, percentage: Number(e.target.value) }))}
            />
          </div>

          <div style={{ display: 'flex', alignItems: 'flex-end' }}>
            <button
              type="submit"
              className="sync-quick-btn"
              style={{ width: '100%', justifyContent: 'center', height: '42px' }}
              disabled={loading}
            >
              {loading ? 'Evaluating All Records...' : 'Run Eligibility Matching Engine'}
            </button>
          </div>
        </form>
      </div>

      {matches && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <h3 style={{ fontSize: '1.1rem', fontWeight: 700, color: '#0f172a' }}>
              Matching Analysis Results ({eligibleCount} of {matches.length} eligible)
            </h3>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(360px, 1fr))', gap: '1rem' }}>
            {matches.map(({ opportunity: opp, evaluation: ev }) => (
              <div
                key={opp.id}
                style={{
                  background: 'white',
                  borderRadius: '12px',
                  border: `1px solid ${ev.eligible ? '#bbf7d0' : '#fecaca'}`,
                  padding: '1.25rem',
                  display: 'flex',
                  flexDirection: 'column',
                  justifyContent: 'space-between',
                  boxShadow: '0 1px 3px rgba(0,0,0,0.04)'
                }}
              >
                <div>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.4rem' }}>
                    <span className="category-tag tag-recruitment" style={{ fontSize: '0.7rem' }}>
                      {opp.category}
                    </span>
                    <span style={{
                      display: 'inline-flex',
                      alignItems: 'center',
                      gap: '0.3rem',
                      fontSize: '0.75rem',
                      fontWeight: 700,
                      color: ev.eligible ? '#15803d' : '#b91c1c'
                    }}>
                      {ev.eligible ? <CheckCircle2 size={14} /> : <AlertTriangle size={14} />}
                      {ev.eligible ? 'Eligible' : 'Not Eligible'}
                    </span>
                  </div>

                  <h4 style={{ fontSize: '0.95rem', fontWeight: 700, color: '#0f172a', marginBottom: '0.3rem' }}>
                    {opp.title}
                  </h4>
                  <p style={{ fontSize: '0.8rem', color: '#64748b', marginBottom: '0.5rem' }}>
                    {opp.organization}
                  </p>

                  <div style={{ background: ev.eligible ? '#f0fdf4' : '#fef2f2', padding: '0.6rem 0.8rem', borderRadius: '6px', fontSize: '0.75rem' }}>
                    {ev.eligible ? (
                      <p style={{ color: '#166534', fontWeight: 600 }}>All age, score, and degree prerequisites verified.</p>
                    ) : (
                      <ul style={{ color: '#991b1b', paddingLeft: '1rem' }}>
                        {ev.mismatchReasons.map((r, idx) => <li key={idx}>{r}</li>)}
                      </ul>
                    )}
                  </div>
                </div>

                <div style={{ marginTop: '1rem', display: 'flex', justifyContent: 'flex-end', gap: '0.5rem' }}>
                  <button
                    className="opp-primary-btn"
                    style={{ fontSize: '0.75rem', padding: '0.4rem 0.8rem' }}
                    onClick={() => onSelectOpportunity(opp)}
                  >
                    View Details
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

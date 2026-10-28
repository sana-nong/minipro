import React, { useState } from 'react';
import { BookmarkCheck, Clock, ExternalLink, Trash2, Calendar, FileText, CheckCircle2 } from 'lucide-react';
import { updateBookmark, removeBookmark } from '../services/api';

const STAGES = [
  { key: 'BOOKMARKED', label: 'Saved / Reviewing' },
  { key: 'APPLIED', label: 'Application Submitted' },
  { key: 'INTERVIEW_SCHEDULED', label: 'Exam / Interview Scheduled' },
  { key: 'COMPLETED', label: 'Completed / Selected' }
];

export default function ApplicationTrackerView({ bookmarks, onRefreshBookmarks }) {
  const [updatingId, setUpdatingId] = useState(null);

  const handleStageChange = async (bookmarkId, newStage, currentNotes) => {
    setUpdatingId(bookmarkId);
    try {
      await updateBookmark(bookmarkId, newStage, currentNotes);
      if (onRefreshBookmarks) onRefreshBookmarks();
    } catch (err) {
      console.error(err);
    } finally {
      setUpdatingId(null);
    }
  };

  const handleDelete = async (opportunityId) => {
    if (!window.confirm('Remove from tracked applications?')) return;
    try {
      await removeBookmark(opportunityId);
      if (onRefreshBookmarks) onRefreshBookmarks();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
      <div style={{
        background: 'white',
        border: '1px solid #cbd5e1',
        borderRadius: '12px',
        padding: '1.5rem',
        boxShadow: '0 1px 3px rgba(0,0,0,0.05)'
      }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '1rem' }}>
          <div>
            <h2 style={{ fontSize: '1.25rem', fontWeight: 800, color: '#0f172a', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <BookmarkCheck size={22} color="#1e3a8a" />
              Candidate Application Pipeline & Tracking
            </h2>
            <p style={{ fontSize: '0.85rem', color: '#64748b', marginTop: '0.2rem' }}>
              Fulfills <strong>Objective 4 preview (60-70% Milestone)</strong>: Allows candidates to bookmark, track deadline reminders, and manage application statuses.
            </p>
          </div>
          <span className="demo-pill" style={{ background: '#dbeafe', color: '#1e40af', padding: '0.4rem 0.85rem' }}>
            {bookmarks ? bookmarks.length : 0} Tracked Applications
          </span>
        </div>
      </div>

      {bookmarks && bookmarks.length > 0 ? (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          {bookmarks.map((bm) => {
            const opp = bm.opportunity;
            if (!opp) return null;
            return (
              <div
                key={bm.id}
                style={{
                  background: 'white',
                  border: '1px solid #e2e8f0',
                  borderRadius: '12px',
                  padding: '1.25rem 1.5rem',
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  flexWrap: 'wrap',
                  gap: '1rem',
                  boxShadow: '0 1px 3px rgba(0,0,0,0.04)'
                }}
              >
                <div style={{ flex: 1, minWidth: '280px' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.35rem' }}>
                    <span className="category-tag tag-recruitment" style={{ fontSize: '0.7rem' }}>
                      {opp.category}
                    </span>
                    <span style={{ fontSize: '0.75rem', color: '#64748b', fontWeight: 600 }}>
                      {opp.organization}
                    </span>
                  </div>

                  <h3 style={{ fontSize: '1rem', fontWeight: 700, color: '#0f172a', marginBottom: '0.4rem' }}>
                    {opp.title}
                  </h3>

                  <div style={{ display: 'flex', gap: '1rem', fontSize: '0.8rem', color: '#64748b', flexWrap: 'wrap' }}>
                    <span style={{ display: 'inline-flex', alignItems: 'center', gap: '0.3rem' }}>
                      <Calendar size={13} />
                      Deadline: <strong>{opp.deadline}</strong> ({opp.daysRemaining} days left)
                    </span>
                    <span>Qualification: {opp.qualification}</span>
                  </div>

                  {bm.notes && (
                    <div style={{ fontSize: '0.8rem', color: '#334155', background: '#f8fafc', padding: '0.4rem 0.6rem', borderRadius: '6px', marginTop: '0.5rem', borderLeft: '3px solid #94a3b8' }}>
                      <strong>Notes:</strong> {bm.notes}
                    </div>
                  )}
                </div>

                {/* Pipeline Stage Selector */}
                <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
                  <div>
                    <label style={{ fontSize: '0.75rem', fontWeight: 600, color: '#64748b', display: 'block', marginBottom: '0.2rem' }}>
                      Application Stage
                    </label>
                    <select
                      className="filter-select"
                      style={{ fontSize: '0.8rem', padding: '0.45rem 0.75rem' }}
                      value={bm.status}
                      disabled={updatingId === bm.id}
                      onChange={(e) => handleStageChange(bm.id, e.target.value, bm.notes)}
                    >
                      {STAGES.map(s => (
                        <option key={s.key} value={s.key}>{s.label}</option>
                      ))}
                    </select>
                  </div>

                  <a
                    href={opp.officialUrl}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="opp-icon-btn"
                    title="Open official portal"
                  >
                    <ExternalLink size={16} />
                  </a>

                  <button
                    className="opp-icon-btn"
                    onClick={() => handleDelete(opp.id)}
                    title="Remove from tracking"
                    style={{ color: '#ef4444' }}
                  >
                    <Trash2 size={16} />
                  </button>
                </div>
              </div>
            );
          })}
        </div>
      ) : (
        <div className="empty-state">
          <BookmarkCheck className="empty-state-icon" />
          <h3 style={{ fontSize: '1.1rem', fontWeight: 700, color: '#0f172a' }}>No Applications Tracked Yet</h3>
          <p style={{ fontSize: '0.85rem', color: '#64748b', marginTop: '0.3rem' }}>
            Go to the Browse & Search tab and click the bookmark icon on any opportunity to track your application here.
          </p>
        </div>
      )}
    </div>
  );
}

import React from 'react';
import { Briefcase, Clock, ShieldCheck, Globe, Radio } from 'lucide-react';

export default function StatsCards({ stats }) {
  if (!stats) return null;

  return (
    <div className="stats-grid">
      <div className="stat-card">
        <div className="stat-icon blue">
          <Briefcase size={24} />
        </div>
        <div className="stat-details">
          <h3>{stats.totalOpportunities}</h3>
          <p>Total Opportunities Aggregated</p>
        </div>
      </div>

      <div className="stat-card" style={{ border: '2px solid #16a34a', background: 'linear-gradient(135deg, #f0fdf4 0%, #ffffff 100%)' }}>
        <div className="stat-icon green" style={{ background: '#dcfce7', color: '#16a34a' }}>
          <Radio size={24} className="pulse-icon" />
        </div>
        <div className="stat-details">
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
            <h3 style={{ color: '#15803d' }}>{stats.liveScrapedCount ?? 50}</h3>
            <span style={{ fontSize: '0.68rem', fontWeight: 800, background: '#16a34a', color: '#ffffff', padding: '0.1rem 0.4rem', borderRadius: '4px' }}>LIVE</span>
          </div>
          <p style={{ fontWeight: 600, color: '#166534' }}>Live Scraped Notices (Govt Servers)</p>
        </div>
      </div>

      <div className="stat-card">
        <div className="stat-icon green">
          <ShieldCheck size={24} />
        </div>
        <div className="stat-details">
          <h3>{stats.activeCount}</h3>
          <p>Active Verified Openings</p>
        </div>
      </div>

      <div className="stat-card">
        <div className="stat-icon amber">
          <Clock size={24} />
        </div>
        <div className="stat-details">
          <h3>{stats.closingSoonCount}</h3>
          <p>Closing Soon (&le; 7 Days)</p>
        </div>
      </div>

      <div className="stat-card">
        <div className="stat-icon purple">
          <Globe size={24} />
        </div>
        <div className="stat-details">
          <h3>{stats.totalSourcesMonitored}</h3>
          <p>Government Portals Monitored</p>
        </div>
      </div>
    </div>
  );
}

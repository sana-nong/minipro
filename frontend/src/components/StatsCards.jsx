import React from 'react';
import { Briefcase, Clock, ShieldCheck, Globe } from 'lucide-react';

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

import React, { useState } from 'react';
import { Database, RefreshCw, CheckCircle2, Clock, Globe, ArrowRight, Terminal } from 'lucide-react';
import { triggerSyncAll, triggerSyncSource } from '../services/api';

export default function AggregationEngineView({ sources, onRefreshSources, onRefreshOpportunities }) {
  const [logs, setLogs] = useState([
    'System initialized.',
    'Objective 1 Data Aggregation Engine: Online.',
    'Crawler listening to 7 official government feeds and portals.',
    'Deduplication & Normalization Service active.'
  ]);
  const [runningSync, setRunningSync] = useState(false);
  const [syncingId, setSyncingId] = useState(null);

  const addLog = (msg) => {
    const time = new Date().toLocaleTimeString();
    setLogs(prev => [`[${time}] ${msg}`, ...prev.slice(0, 40)]);
  };

  const handleSyncAll = async () => {
    setRunningSync(true);
    addLog('Initiating batch aggregation across all official government sources...');
    try {
      const results = await triggerSyncAll();
      results.forEach(r => {
        addLog(`Source: ${r.agencyCode} (${r.sourceName}) -> Status: ${r.status} | Found: ${r.itemsFound} | New: ${r.newAdded} | Updated: ${r.updated} (${r.executionTimeMs}ms)`);
      });
      addLog('Batch aggregation finished successfully. Central database updated.');
      if (onRefreshSources) onRefreshSources();
      if (onRefreshOpportunities) onRefreshOpportunities();
    } catch (err) {
      addLog(`Aggregation error: ${err.message}`);
    } finally {
      setRunningSync(false);
    }
  };

  const handleSyncSingle = async (source) => {
    setSyncingId(source.id);
    addLog(`Crawling ${source.name} [${source.agencyCode}] using ${source.aggregationType}...`);
    try {
      const res = await triggerSyncSource(source.id);
      addLog(`Result for ${source.agencyCode}: Status: ${res.status} | Found: ${res.itemsFound} | Centralized: ${res.newAdded} new records in ${res.executionTimeMs}ms.`);
      if (onRefreshSources) onRefreshSources();
      if (onRefreshOpportunities) onRefreshOpportunities();
    } catch (err) {
      addLog(`Error syncing ${source.agencyCode}: ${err.message}`);
    } finally {
      setSyncingId(null);
    }
  };

  return (
    <div className="aggregation-view">
      {/* Overview Card */}
      <div style={{
        background: 'white',
        border: '1px solid #cbd5e1',
        borderRadius: '12px',
        padding: '1.5rem',
        boxShadow: '0 1px 3px rgba(0,0,0,0.05)'
      }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '1rem', marginBottom: '1rem' }}>
          <div>
            <h2 style={{ fontSize: '1.25rem', fontWeight: 800, color: '#0f172a', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <Database size={22} color="#1e3a8a" />
              Automated Government Data Aggregation Engine
            </h2>
            <p style={{ fontSize: '0.85rem', color: '#64748b', marginTop: '0.2rem' }}>
              Fulfills <strong>Objective 1</strong>: Automatically collects, parses RSS feeds/web scrapers, extracts normalized schemas, and deduplicates government opportunities.
            </p>
          </div>

          <button
            className="sync-quick-btn"
            onClick={handleSyncAll}
            disabled={runningSync}
          >
            <RefreshCw size={16} className={runningSync ? 'spin' : ''} />
            <span>{runningSync ? 'Crawling All Sources...' : 'Run Full Multi-Source Aggregation'}</span>
          </button>
        </div>

        {/* Monitored Portals Table */}
        <div style={{ overflowX: 'auto', marginTop: '1.25rem' }}>
          <table className="sources-table">
            <thead>
              <tr>
                <th>Agency</th>
                <th>Official Portal</th>
                <th>Protocol</th>
                <th>Status</th>
                <th>Last Synchronized</th>
                <th>Total Centralized</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {sources && sources.length > 0 ? (
                sources.map((s) => (
                  <tr key={s.id}>
                    <td>
                      <strong>{s.agencyCode}</strong>
                      <div style={{ fontSize: '0.75rem', color: '#64748b' }}>{s.name}</div>
                    </td>
                    <td>
                      <a href={s.portalUrl} target="_blank" rel="noopener noreferrer" style={{ color: '#2563eb', textDecoration: 'none', display: 'inline-flex', alignItems: 'center', gap: '0.3rem' }}>
                        <Globe size={13} />
                        {s.portalUrl.replace('https://', '')}
                      </a>
                    </td>
                    <td>
                      <span className="demo-pill" style={{ color: '#0f172a', background: '#e2e8f0' }}>
                        {s.aggregationType}
                      </span>
                    </td>
                    <td>
                      <span style={{
                        display: 'inline-flex',
                        alignItems: 'center',
                        gap: '0.3rem',
                        fontSize: '0.75rem',
                        fontWeight: 700,
                        color: s.syncStatus === 'SUCCESS' ? '#15803d' : '#b45309'
                      }}>
                        <CheckCircle2 size={13} />
                        {s.syncStatus}
                      </span>
                    </td>
                    <td style={{ fontSize: '0.8rem', color: '#64748b' }}>
                      {s.lastSyncTime ? new Date(s.lastSyncTime).toLocaleString() : 'Just now'}
                    </td>
                    <td>
                      <strong style={{ color: '#0f172a' }}>{s.totalOpportunitiesExtracted} items</strong>
                    </td>
                    <td>
                      <button
                        className="opp-icon-btn"
                        onClick={() => handleSyncSingle(s)}
                        disabled={syncingId === s.id}
                        title={`Run crawler for ${s.agencyCode}`}
                      >
                        <RefreshCw size={14} className={syncingId === s.id ? 'spin' : ''} />
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="7" style={{ textAlign: 'center', padding: '2rem' }}>
                    Loading monitored government sources...
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Live Aggregation Console Log */}
      <div className="terminal-console">
        <div className="terminal-header">
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
            <Terminal size={14} />
            <span>Aggregation Engine Live Logs (Scheduled Crawler & Normalizer)</span>
          </div>
          <span>Status: ACTIVE</span>
        </div>

        <div>
          {logs.map((log, index) => (
            <div key={index} style={{ padding: '0.15rem 0' }}>
              {log}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

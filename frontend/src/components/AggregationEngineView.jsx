import React, { useState } from 'react';
import { Database, RefreshCw, CheckCircle2, Clock, Globe, Terminal, ShieldCheck, Sparkles, ExternalLink, ArrowRight } from 'lucide-react';
import { triggerSyncAll, triggerSyncSource } from '../services/api';

export default function AggregationEngineView({ sources, onRefreshSources, onRefreshOpportunities }) {
  const [logs, setLogs] = useState([
    'System initialized with 26 Verified Government Portals.',
    'Multi-Source Data Aggregation Engine: Online.',
    'Regex & NLP Gazette Extraction Service: Ready.',
    'Deduplication & Cryptographic Normalization Service: Active.'
  ]);
  const [runningSync, setRunningSync] = useState(false);
  const [syncingId, setSyncingId] = useState(null);
  const [lastSyncSummary, setLastSyncSummary] = useState(null);

  const addLog = (msg) => {
    const time = new Date().toLocaleTimeString();
    setLogs(prev => [`[${time}] ${msg}`, ...prev.slice(0, 50)]);
  };

  const handleSyncAll = async () => {
    setRunningSync(true);
    setLastSyncSummary(null);
    addLog('Initiating batch multi-protocol aggregation across all 26 official government portals...');
    try {
      const results = await triggerSyncAll();
      let totalNew = 0;
      let totalUpdated = 0;
      let totalFound = 0;

      results.forEach(r => {
        totalNew += r.newAdded;
        totalUpdated += r.updated;
        totalFound += r.itemsFound;
        if (r.newAdded > 0 || r.updated > 0) {
          addLog(`[GAZETTE EXTRACT] ${r.agencyCode} (${r.sourceName}): Found ${r.itemsFound} notices -> Ingested ${r.newAdded} new gazettes, updated ${r.updated} deadlines (${r.executionTimeMs}ms)`);
        } else {
          addLog(`[VERIFIED] ${r.agencyCode}: ${r.itemsFound} published notices checked. Deadlines & status verified.`);
        }
      });

      addLog(`Batch aggregation completed! Total ${totalFound} notices scanned, ${totalNew} new opportunities centralized, ${totalUpdated} statutory deadlines updated.`);
      
      setLastSyncSummary({
        totalFound,
        totalNew,
        totalUpdated,
        sourcesCount: results.length,
        time: new Date().toLocaleTimeString()
      });

      if (onRefreshSources) await onRefreshSources();
      if (onRefreshOpportunities) await onRefreshOpportunities();
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
      addLog(`Result for ${source.agencyCode}: Status ${res.status} | Scanned ${res.itemsFound} notices | Centralized ${res.newAdded} new | Updated ${res.updated} in ${res.executionTimeMs}ms.`);
      if (onRefreshSources) await onRefreshSources();
      if (onRefreshOpportunities) await onRefreshOpportunities();
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
              Continuously monitors 26 official gazettes, agency portals, and RSS feeds with NLP regex deadline extraction and cryptographic deduplication.
            </p>
          </div>

          <button
            className="sync-quick-btn"
            onClick={handleSyncAll}
            disabled={runningSync}
          >
            <RefreshCw size={16} className={runningSync ? 'spin' : ''} />
            <span>{runningSync ? 'Crawling 26 Portals...' : 'Run Full Multi-Source Aggregation'}</span>
          </button>
        </div>

        {/* Sync Success Summary Banner */}
        {lastSyncSummary && (
          <div style={{
            background: '#f0fdf4',
            border: '1px solid #bbf7d0',
            borderRadius: '8px',
            padding: '0.85rem 1.25rem',
            marginBottom: '1rem',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            flexWrap: 'wrap',
            gap: '0.5rem'
          }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <CheckCircle2 size={18} color="#15803d" />
              <span style={{ fontSize: '0.88rem', fontWeight: 700, color: '#166534' }}>
                Aggregation Successful ({lastSyncSummary.time}): Scanned {lastSyncSummary.sourcesCount} portals. Added {lastSyncSummary.totalNew} new opportunities and updated {lastSyncSummary.totalUpdated} statutory deadlines.
              </span>
            </div>
            <span style={{ fontSize: '0.78rem', color: '#15803d', fontWeight: 600 }}>
              Live Catalog Updated
            </span>
          </div>
        )}

        {/* Monitored Portals Table */}
        <div style={{ overflowX: 'auto', marginTop: '0.75rem' }}>
          <table className="sources-table">
            <thead>
              <tr>
                <th>Agency Code & Name</th>
                <th>Official Agency Portal</th>
                <th>Extraction Protocol</th>
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
                      <strong style={{ color: '#1e3a8a' }}>{s.agencyCode}</strong>
                      <div style={{ fontSize: '0.75rem', color: '#475569', fontWeight: 500 }}>{s.name}</div>
                    </td>
                    <td>
                      <a
                        href={s.portalUrl}
                        target="_blank"
                        rel="noopener noreferrer"
                        style={{ color: '#2563eb', textDecoration: 'none', display: 'inline-flex', alignItems: 'center', gap: '0.3rem', fontSize: '0.82rem', fontWeight: 600 }}
                      >
                        <Globe size={13} />
                        {s.portalUrl.replace('https://', '').replace('www.', '')}
                      </a>
                    </td>
                    <td>
                      <span className="demo-pill" style={{ color: '#0f172a', background: '#f1f5f9', border: '1px solid #cbd5e1' }}>
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
                      {s.lastSyncTime ? new Date(s.lastSyncTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' }) : 'Just now'}
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
            <span>Aggregation Engine Telemetry Console (Objective 1: Automatic Extraction & Deduplication)</span>
          </div>
          <span>Crawler Status: ONLINE</span>
        </div>

        <div>
          {logs.map((log, index) => (
            <div key={index} style={{ padding: '0.15rem 0', fontFamily: 'Consolas, monospace', fontSize: '0.8rem' }}>
              {log}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

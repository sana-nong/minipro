import React from 'react';
import { Layers, RefreshCw, BookmarkCheck, Search, Database, UserCheck, ShieldCheck } from 'lucide-react';

export default function Navbar({ activeTab, setActiveTab, onTriggerSync, isSyncing }) {
  return (
    <header className="navbar">
      <div className="nav-inner">
        <div className="brand-section">
          <div className="brand-badge">
            <Layers size={24} />
          </div>
          <div className="brand-info">
            <h1>Centralized Government Opportunity Platform</h1>
            <p>Siddaganga Institute of Technology, Tumkur • Dept. of CSE</p>
          </div>
        </div>

        <nav className="nav-actions">
          <button
            className={`tab-btn ${activeTab === 'browse' ? 'active' : ''}`}
            onClick={() => setActiveTab('browse')}
          >
            <Search size={16} />
            <span>Browse & Search</span>
          </button>

          <button
            className={`tab-btn ${activeTab === 'aggregation' ? 'active' : ''}`}
            onClick={() => setActiveTab('aggregation')}
          >
            <Database size={16} />
            <span>Aggregation Engine</span>
          </button>

          <button
            className={`tab-btn ${activeTab === 'tracker' ? 'active' : ''}`}
            onClick={() => setActiveTab('tracker')}
          >
            <BookmarkCheck size={16} />
            <span>Application Tracker</span>
          </button>

          <button
            className={`tab-btn ${activeTab === 'matcher' ? 'active' : ''}`}
            onClick={() => setActiveTab('matcher')}
          >
            <UserCheck size={16} />
            <span>Eligibility Matcher</span>
          </button>

          <button
            className="sync-quick-btn"
            onClick={onTriggerSync}
            disabled={isSyncing}
            title="Trigger automated crawler & aggregation across all government sources"
          >
            <RefreshCw size={15} className={isSyncing ? 'spin' : ''} />
            <span>{isSyncing ? 'Crawling...' : 'Sync Sources'}</span>
          </button>
        </nav>
      </div>
    </header>
  );
}

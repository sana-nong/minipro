import React, { useState, useEffect } from 'react';
import Navbar from './components/Navbar';
import StatsCards from './components/StatsCards';
import FilterBar from './components/FilterBar';
import OpportunityCard from './components/OpportunityCard';
import OpportunityDetailModal from './components/OpportunityDetailModal';
import AggregationEngineView from './components/AggregationEngineView';
import ApplicationTrackerView from './components/ApplicationTrackerView';
import ProfileMatcherModal from './components/ProfileMatcherModal';
import { fetchOpportunities, fetchStats, fetchSources, fetchBookmarks, addBookmark, removeBookmark, triggerSyncAll } from './services/api';
import { ShieldCheck, AlertCircle, Sparkles } from 'lucide-react';

export default function App() {
  const [activeTab, setActiveTab] = useState('browse');
  const [opportunities, setOpportunities] = useState([]);
  const [stats, setStats] = useState(null);
  const [sources, setSources] = useState([]);
  const [bookmarks, setBookmarks] = useState([]);
  const [selectedOpportunity, setSelectedOpportunity] = useState(null);
  const [loading, setLoading] = useState(true);
  const [isSyncing, setIsSyncing] = useState(false);

  const [filters, setFilters] = useState({
    query: '',
    category: 'ALL',
    qualification: 'ALL',
    location: 'ALL',
    status: 'ALL',
    closingInDays: '',
    sortBy: 'deadline'
  });

  // Initial load
  useEffect(() => {
    loadAllData();
  }, []);

  // Filter load
  useEffect(() => {
    loadOpportunities();
  }, [filters]);

  const loadAllData = async () => {
    setLoading(true);
    try {
      await Promise.all([
        loadOpportunities(),
        loadStats(),
        loadSources(),
        loadBookmarks()
      ]);
    } catch (err) {
      console.error('Error loading initial data:', err);
    } finally {
      setLoading(false);
    }
  };

  const loadOpportunities = async () => {
    try {
      const data = await fetchOpportunities(filters);
      setOpportunities(data);
    } catch (err) {
      console.error('Error loading opportunities:', err);
    }
  };

  const loadStats = async () => {
    try {
      const data = await fetchStats();
      setStats(data);
    } catch (err) {
      console.error('Error loading stats:', err);
    }
  };

  const loadSources = async () => {
    try {
      const data = await fetchSources();
      setSources(data);
    } catch (err) {
      console.error('Error loading sources:', err);
    }
  };

  const loadBookmarks = async () => {
    try {
      const data = await fetchBookmarks();
      setBookmarks(data);
    } catch (err) {
      console.error('Error loading bookmarks:', err);
    }
  };

  const handleGlobalSync = async () => {
    setIsSyncing(true);
    try {
      await triggerSyncAll();
      await Promise.all([
        loadOpportunities(),
        loadStats(),
        loadSources()
      ]);
    } catch (err) {
      console.error('Sync failed:', err);
    } finally {
      setIsSyncing(false);
    }
  };

  const isOpportunityBookmarked = (id) => {
    return bookmarks.some(b => b.opportunity && b.opportunity.id === id);
  };

  const handleToggleBookmark = async (opp) => {
    try {
      if (isOpportunityBookmarked(opp.id)) {
        await removeBookmark(opp.id);
      } else {
        await addBookmark(opp.id, 'Bookmarked for tracking');
      }
      await loadBookmarks();
    } catch (err) {
      console.error('Bookmark toggle failed:', err);
    }
  };

  return (
    <div className="app-container">
      <div className="top-gov-accent-bar" />
      <Navbar
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        onTriggerSync={handleGlobalSync}
        isSyncing={isSyncing}
      />

      <main className="main-content">
        {/* Official Portal Notice & Verification Strip */}
        <div className="gov-hero-banner">
          <div className="gov-hero-content">
            <div className="gov-hero-badge">
              <ShieldCheck size={16} />
              <span>Verified National Government Aggregator</span>
            </div>
            <h2 className="gov-hero-title">
              Centralized Government Opportunity Portal
            </h2>
            <p className="gov-hero-subtitle">
              Automated aggregation, gazette monitoring, and eligibility matching across UPSC, SSC, AICTE, NSP, DRDO, ISRO, CSIR, DST, BARC & State Public Service Commissions.
            </p>
          </div>

          <div className="gov-hero-stats">
            <div className="hero-stat-pill">
              <span className="dot active"></span>
              <strong>{stats ? stats.totalOpportunities : '50+'} Verified Openings</strong>
            </div>
            <div className="hero-stat-pill">
              <span className="dot pulse"></span>
              <strong>{sources ? sources.length : 12} Portals Monitored</strong>
            </div>
            <div className="hero-stat-pill">
              <Sparkles size={14} color="#f59e0b" />
              <span>Real-Time Gazette Sync</span>
            </div>
          </div>
        </div>

        {/* Tab 1: Browse & Search Opportunities */}
        {activeTab === 'browse' && (
          <>
            <StatsCards stats={stats} />

            <FilterBar
              filters={filters}
              setFilters={setFilters}
              onReset={() => setFilters({
                query: '',
                category: 'ALL',
                qualification: 'ALL',
                location: 'ALL',
                status: 'ALL',
                closingInDays: '',
                sortBy: 'deadline'
              })}
            />

            <div className="results-bar">
              <span>
                Showing <strong>{opportunities.length}</strong> available opportunities matching criteria
              </span>
            </div>

            {loading ? (
              <div className="empty-state">
                <p>Loading verified government opportunities...</p>
              </div>
            ) : opportunities.length > 0 ? (
              <div className="opportunities-grid">
                {opportunities.map((opp) => (
                  <OpportunityCard
                    key={opp.id}
                    opportunity={opp}
                    onSelect={setSelectedOpportunity}
                    isBookmarked={isOpportunityBookmarked(opp.id)}
                    onToggleBookmark={handleToggleBookmark}
                  />
                ))}
              </div>
            ) : (
              <div className="empty-state">
                <AlertCircle className="empty-state-icon" />
                <h3>No Opportunities Match Your Filter Criteria</h3>
                <p style={{ fontSize: '0.85rem', color: '#64748b', marginTop: '0.3rem' }}>
                  Try resetting filters or searching with broader keywords.
                </p>
              </div>
            )}
          </>
        )}

        {/* Tab 2: Data Aggregation Engine */}
        {activeTab === 'aggregation' && (
          <AggregationEngineView
            sources={sources}
            onRefreshSources={loadSources}
            onRefreshOpportunities={loadOpportunities}
          />
        )}

        {/* Tab 3: Application Tracker */}
        {activeTab === 'tracker' && (
          <ApplicationTrackerView
            bookmarks={bookmarks}
            onRefreshBookmarks={loadBookmarks}
          />
        )}

        {/* Tab 4: Eligibility Matcher */}
        {activeTab === 'matcher' && (
          <ProfileMatcherModal
            opportunities={opportunities}
            onSelectOpportunity={setSelectedOpportunity}
          />
        )}
      </main>

      {/* Opportunity Detail Modal */}
      {selectedOpportunity && (
        <OpportunityDetailModal
          opportunity={selectedOpportunity}
          onClose={() => setSelectedOpportunity(null)}
          isBookmarked={isOpportunityBookmarked(selectedOpportunity.id)}
          onToggleBookmark={handleToggleBookmark}
        />
      )}
    </div>
  );
}

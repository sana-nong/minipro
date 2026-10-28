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
import { Award, CheckCircle2, AlertCircle } from 'lucide-react';

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
      <Navbar
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        onTriggerSync={handleGlobalSync}
        isSyncing={isSyncing}
      />

      <main className="main-content">
        {/* Project Review Banner */}
        <div className="demo-banner">
          <div>
            <div className="demo-banner-title">
              <Award size={18} />
              <span>Mini Project First Review Presentation • SIT Tumkur (Dept. of CSE)</span>
            </div>
            <p style={{ fontSize: '0.8rem', opacity: 0.9, marginTop: '0.2rem' }}>
              Guide: <strong>Dr. Anupama T A</strong> | Team: Narendra Kumar Yadav, Nongthombam Sanathoi Singh, Rahul Dadimana Kshetri, Tomthilnganba Soraisham
            </p>
          </div>

          <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
            <span className="demo-pill" style={{ background: '#22c55e', color: '#ffffff' }}>
              ✓ Objective 1: Aggregation Engine [100%]
            </span>
            <span className="demo-pill" style={{ background: '#22c55e', color: '#ffffff' }}>
              ✓ Objective 2: Search & Filter [100%]
            </span>
            <span className="demo-pill" style={{ background: '#3b82f6', color: '#ffffff' }}>
              ⚡ Objective 3 & 4: Preview [65-70%]
            </span>
          </div>
        </div>

        {/* Tab 1: Browse & Search Opportunities (Objective 2) */}
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

            {loading ? (
              <div className="empty-state">
                <p>Loading centralized government opportunities...</p>
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
                  Try relaxing your search keywords, qualification, or deadline filter.
                </p>
              </div>
            )}
          </>
        )}

        {/* Tab 2: Data Aggregation Engine (Objective 1) */}
        {activeTab === 'aggregation' && (
          <AggregationEngineView
            sources={sources}
            onRefreshSources={loadSources}
            onRefreshOpportunities={loadOpportunities}
          />
        )}

        {/* Tab 3: Application Tracker (Objective 4 Preview) */}
        {activeTab === 'tracker' && (
          <ApplicationTrackerView
            bookmarks={bookmarks}
            onRefreshBookmarks={loadBookmarks}
          />
        )}

        {/* Tab 4: Eligibility Matcher (Objective 2 & 3 Preview) */}
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

const API_BASE = 'http://localhost:8081/api';

export async function fetchOpportunities(filters = {}) {
  const params = new URLSearchParams();
  if (filters.query) params.append('query', filters.query);
  if (filters.category && filters.category !== 'ALL') params.append('category', filters.category);
  if (filters.qualification && filters.qualification !== 'ALL') params.append('qualification', filters.qualification);
  if (filters.location && filters.location !== 'ALL') params.append('location', filters.location);
  if (filters.status && filters.status !== 'ALL') params.append('status', filters.status);
  if (filters.closingInDays) params.append('closingInDays', filters.closingInDays);
  if (filters.maxAge) params.append('maxAge', filters.maxAge);
  if (filters.userPercentage) params.append('userPercentage', filters.userPercentage);
  if (filters.sortBy) params.append('sortBy', filters.sortBy);
  if (filters.sortDirection) params.append('sortDirection', filters.sortDirection);

  const res = await fetch(`${API_BASE}/opportunities?${params.toString()}`);
  if (!res.ok) throw new Error('Failed to fetch opportunities');
  return res.json();
}

export async function fetchOpportunityById(id) {
  const res = await fetch(`${API_BASE}/opportunities/${id}`);
  if (!res.ok) throw new Error('Failed to fetch opportunity');
  return res.json();
}

export async function fetchStats() {
  const res = await fetch(`${API_BASE}/opportunities/stats`);
  if (!res.ok) throw new Error('Failed to fetch stats');
  return res.json();
}

export async function fetchSources() {
  const res = await fetch(`${API_BASE}/aggregation/sources`);
  if (!res.ok) throw new Error('Failed to fetch sources');
  return res.json();
}

export async function triggerSyncAll() {
  const res = await fetch(`${API_BASE}/aggregation/sync-all`, {
    method: 'POST'
  });
  if (!res.ok) throw new Error('Aggregation failed');
  return res.json();
}

export async function triggerSyncSource(sourceId) {
  const res = await fetch(`${API_BASE}/aggregation/sync/${sourceId}`, {
    method: 'POST'
  });
  if (!res.ok) throw new Error('Source sync failed');
  return res.json();
}

export async function checkEligibility(opportunityId, candidateProfile) {
  const res = await fetch(`${API_BASE}/opportunities/${opportunityId}/check-eligibility`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(candidateProfile)
  });
  if (!res.ok) throw new Error('Failed to evaluate eligibility');
  return res.json();
}

export async function fetchBookmarks(userIdentifier = 'student@sit.ac.in') {
  const res = await fetch(`${API_BASE}/tracker/bookmarks?userIdentifier=${encodeURIComponent(userIdentifier)}`);
  if (!res.ok) throw new Error('Failed to fetch bookmarks');
  return res.json();
}

export async function addBookmark(opportunityId, notes = '', userIdentifier = 'student@sit.ac.in') {
  const res = await fetch(`${API_BASE}/tracker/bookmark`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ opportunityId, userIdentifier, notes })
  });
  if (!res.ok) throw new Error('Failed to bookmark opportunity');
  return res.json();
}

export async function updateBookmark(bookmarkId, status, notes) {
  const res = await fetch(`${API_BASE}/tracker/bookmark/${bookmarkId}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ status, notes })
  });
  if (!res.ok) throw new Error('Failed to update bookmark');
  return res.json();
}

export async function removeBookmark(opportunityId, userIdentifier = 'student@sit.ac.in') {
  const res = await fetch(`${API_BASE}/tracker/bookmark/${opportunityId}?userIdentifier=${encodeURIComponent(userIdentifier)}`, {
    method: 'DELETE'
  });
  if (!res.ok) throw new Error('Failed to remove bookmark');
}

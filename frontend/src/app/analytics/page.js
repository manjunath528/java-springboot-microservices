"use client";

import RequireAuth from "../../components/RequireAuth";

export default function AnalyticsPage() {
  return (
    <RequireAuth>
      <div className="card">
        <div className="section-title">Analytics</div>
        <p className="muted">Analytics service currently consumes Kafka events only. REST endpoints are not exposed.</p>
      </div>
    </RequireAuth>
  );
}

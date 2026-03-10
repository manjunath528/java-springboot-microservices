"use client";

import RequireAuth from "../../components/RequireAuth";

export default function AiPage() {
  return (
    <RequireAuth>
      <div className="card">
        <div className="section-title">AI Agent</div>
        <p className="muted">AI agent service is event-driven (Kafka). There are no REST endpoints yet.</p>
        <p className="notice">Create activities or nutrition entries to trigger AI recommendations.</p>
      </div>
    </RequireAuth>
  );
}

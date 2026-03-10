"use client";

import { useState } from "react";
import RequireAuth from "../../components/RequireAuth";
import { apiFetch, authHeader } from "../../lib/api";
import { useAuth } from "../../lib/auth";

export default function BillingPage() {
  const { token } = useAuth();
  const [health, setHealth] = useState(null);
  const [status, setStatus] = useState("");

  const checkHealth = async () => {
    setStatus("Checking billing service health...");
    try {
      const data = await apiFetch("/api/billing/actuator/health", {
        headers: authHeader(token),
      });
      setHealth(data);
      setStatus("");
    } catch (err) {
      setStatus(`Health check failed: ${err.message}`);
    }
  };

  return (
    <RequireAuth>
      <div className="card">
        <div className="section-title">Billing Service</div>
        <p className="muted">Billing currently exposes gRPC. Use this page to check health.</p>
        <button className="btn" onClick={checkHealth}>Check Health</button>
        {status && <p className="muted">{status}</p>}
        {health && <pre className="muted">{JSON.stringify(health, null, 2)}</pre>}
      </div>
    </RequireAuth>
  );
}

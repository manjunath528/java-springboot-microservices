"use client";

import { useState } from "react";
import RequireAuth from "../../components/RequireAuth";
import { apiFetch, authHeader } from "../../lib/api";
import { useAuth } from "../../lib/auth";

export default function NotificationsPage() {
  const { token } = useAuth();
  const [userId, setUserId] = useState("");
  const [items, setItems] = useState([]);
  const [status, setStatus] = useState("");

  const handleFetch = async () => {
    if (!userId) return;
    setStatus("Loading notifications...");
    try {
      const data = await apiFetch(`/api/notifications/user/${userId}`, {
        headers: authHeader(token),
      });
      setItems(data || []);
      setStatus("");
    } catch (err) {
      setStatus(`Load failed: ${err.message}`);
    }
  };

  return (
    <RequireAuth>
      <div className="card">
        <div className="section-title">Notifications</div>
        <div className="form-grid" style={{ maxWidth: 360 }}>
          <input className="input" placeholder="User ID" value={userId} onChange={(e) => setUserId(e.target.value)} />
          <button className="btn secondary" onClick={handleFetch}>Load</button>
        </div>
        {status && <p className="muted">{status}</p>}
        <table className="table" style={{ marginTop: 16 }}>
          <thead>
            <tr>
              <th>Title</th>
              <th>Message</th>
              <th>Type</th>
            </tr>
          </thead>
          <tbody>
            {items.map((n) => (
              <tr key={n.id}>
                <td>{n.title}</td>
                <td>{n.message}</td>
                <td>{n.type}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </RequireAuth>
  );
}

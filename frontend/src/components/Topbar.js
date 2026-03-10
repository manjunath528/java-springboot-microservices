"use client";

import { useAuth } from "../lib/auth";

export default function Topbar() {
  const { token, logout, ready } = useAuth();

  return (
    <div className="topbar">
      <div className="topbar-title">Microservices Console</div>
      <div className="topbar-actions">
        {!ready ? (
          <span className="chip">Loading...</span>
        ) : token ? (
          <>
            <span className="chip">Signed in</span>
            <button className="btn" onClick={logout}>Log out</button>
          </>
        ) : (
          <span className="chip warn">Not authenticated</span>
        )}
      </div>
    </div>
  );
}

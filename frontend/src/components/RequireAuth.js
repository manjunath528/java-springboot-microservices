"use client";

import Link from "next/link";
import { useAuth } from "../lib/auth";

export default function RequireAuth({ children }) {
  const { token, ready } = useAuth();

  if (!ready) {
    return <div className="card">Checking authentication...</div>;
  }

  if (!token) {
    return (
      <div className="card">
        <div className="section-title">Authentication required</div>
        <p className="muted">Please sign in to access this page.</p>
        <Link className="btn" href="/login">Go to Login</Link>
      </div>
    );
  }

  return children;
}

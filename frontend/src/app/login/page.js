"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { apiFetch } from "../../lib/api";
import { useAuth } from "../../lib/auth";

export default function LoginPage() {
  const { login } = useAuth();
  const router = useRouter();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [status, setStatus] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setStatus("Signing in...");
    try {
      const data = await apiFetch("/auth/login", {
        method: "POST",
        body: JSON.stringify({ email, password }),
      });
      login(data.token);
      setStatus("Signed in successfully.");
      router.push("/");
    } catch (err) {
      setStatus(`Login failed: ${err.message}`);
    }
  };

  return (
    <div className="card" style={{ maxWidth: 420 }}>
      <div className="section-title">Login</div>
      <form className="form-grid" onSubmit={handleSubmit}>
        <label>
          Email
          <input
            className="input"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </label>
        <label>
          Password
          <input
            className="input"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </label>
        <button className="btn" type="submit">Sign In</button>
      </form>
      {status && <p className="muted">{status}</p>}
    </div>
  );
}

"use client";

import { useState } from "react";
import RequireAuth from "../../components/RequireAuth";
import { apiFetch, authHeader } from "../../lib/api";
import { useAuth } from "../../lib/auth";

const emptyGoal = {
  userId: "",
  goalType: "ENDURANCE",
  targetValue: 10,
  targetDate: "",
};

export default function GoalsPage() {
  const { token } = useAuth();
  const [form, setForm] = useState(emptyGoal);
  const [userId, setUserId] = useState("");
  const [activeGoal, setActiveGoal] = useState(null);
  const [status, setStatus] = useState("");

  const handleChange = (field, value) => {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    setStatus("Creating goal...");
    try {
      const payload = {
        ...form,
        targetValue: Number(form.targetValue),
      };
      const data = await apiFetch("/api/goals", {
        method: "POST",
        headers: authHeader(token),
        body: JSON.stringify(payload),
      });
      setStatus("Goal created.");
      setActiveGoal(data);
    } catch (err) {
      setStatus(`Create failed: ${err.message}`);
    }
  };

  const handleFetch = async () => {
    if (!userId) return;
    setStatus("Loading active goal...");
    try {
      const data = await apiFetch(`/api/goals/active/${userId}`, {
        headers: authHeader(token),
      });
      setActiveGoal(data);
      setStatus("");
    } catch (err) {
      setStatus(`Load failed: ${err.message}`);
    }
  };

  return (
    <RequireAuth>
      <div className="grid two">
        <div className="card">
          <div className="section-title">Create Goal</div>
          <form className="form-grid" onSubmit={handleCreate}>
            <input className="input" placeholder="User ID" value={form.userId} onChange={(e) => handleChange("userId", e.target.value)} required />
            <select className="input" value={form.goalType} onChange={(e) => handleChange("goalType", e.target.value)}>
              <option value="WEIGHT_LOSS">WEIGHT_LOSS</option>
              <option value="MUSCLE_GAIN">MUSCLE_GAIN</option>
              <option value="ENDURANCE">ENDURANCE</option>
              <option value="MAINTENANCE">MAINTENANCE</option>
            </select>
            <input className="input" placeholder="Target Value" type="number" value={form.targetValue} onChange={(e) => handleChange("targetValue", e.target.value)} />
            <input className="input" placeholder="Target Date (YYYY-MM-DD)" value={form.targetDate} onChange={(e) => handleChange("targetDate", e.target.value)} required />
            <button className="btn" type="submit">Create</button>
          </form>
          {status && <p className="muted">{status}</p>}
        </div>
        <div className="card">
          <div className="section-title">Active Goal</div>
          <div className="form-grid">
            <input className="input" placeholder="User ID" value={userId} onChange={(e) => setUserId(e.target.value)} />
            <button className="btn secondary" onClick={handleFetch}>Load</button>
          </div>
          {activeGoal && (
            <div style={{ marginTop: 16 }}>
              <div><strong>Type:</strong> {activeGoal.goalType}</div>
              <div><strong>Target:</strong> {activeGoal.targetValue}</div>
              <div><strong>Status:</strong> {activeGoal.status}</div>
            </div>
          )}
        </div>
      </div>
    </RequireAuth>
  );
}

"use client";

import { useState } from "react";
import RequireAuth from "../../components/RequireAuth";
import { apiFetch, authHeader } from "../../lib/api";
import { useAuth } from "../../lib/auth";

const emptyActivity = {
  userId: "",
  workoutType: "RUNNING",
  durationMinutes: 30,
  caloriesBurned: 200,
  activityDate: "",
  status: "COMPLETED",
};

export default function ActivitiesPage() {
  const { token } = useAuth();
  const [form, setForm] = useState(emptyActivity);
  const [queryUserId, setQueryUserId] = useState("");
  const [activities, setActivities] = useState([]);
  const [status, setStatus] = useState("");

  const handleChange = (field, value) => {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    setStatus("Creating activity...");
    try {
      const payload = {
        ...form,
        durationMinutes: Number(form.durationMinutes),
        caloriesBurned: Number(form.caloriesBurned),
      };
      await apiFetch("/api/activities", {
        method: "POST",
        headers: authHeader(token),
        body: JSON.stringify(payload),
      });
      setStatus("Activity created.");
      setForm(emptyActivity);
    } catch (err) {
      setStatus(`Create failed: ${err.message}`);
    }
  };

  const handleFetch = async () => {
    if (!queryUserId) return;
    setStatus("Loading activities...");
    try {
      const data = await apiFetch(`/api/activities/user/${queryUserId}`, {
        headers: authHeader(token),
      });
      setActivities(data || []);
      setStatus("");
    } catch (err) {
      setStatus(`Load failed: ${err.message}`);
    }
  };

  return (
    <RequireAuth>
      <div className="grid two">
        <div className="card">
          <div className="section-title">Create Activity</div>
          <form className="form-grid" onSubmit={handleCreate}>
            <input className="input" placeholder="User ID" value={form.userId} onChange={(e) => handleChange("userId", e.target.value)} required />
            <select className="input" value={form.workoutType} onChange={(e) => handleChange("workoutType", e.target.value)}>
              <option value="RUNNING">RUNNING</option>
              <option value="WALKING">WALKING</option>
              <option value="CYCLING">CYCLING</option>
              <option value="HIIT">HIIT</option>
              <option value="YOGA">YOGA</option>
              <option value="CARDIO">CARDIO</option>
              <option value="STRENGTH">STRENGTH</option>
              <option value="FLEXIBILITY">FLEXIBILITY</option>
            </select>
            <input className="input" placeholder="Duration Minutes" type="number" value={form.durationMinutes} onChange={(e) => handleChange("durationMinutes", e.target.value)} />
            <input className="input" placeholder="Calories Burned" type="number" value={form.caloriesBurned} onChange={(e) => handleChange("caloriesBurned", e.target.value)} />
            <input className="input" placeholder="Activity Date (YYYY-MM-DD)" value={form.activityDate} onChange={(e) => handleChange("activityDate", e.target.value)} required />
            <select className="input" value={form.status} onChange={(e) => handleChange("status", e.target.value)}>
              <option value="PLANNED">PLANNED</option>
              <option value="COMPLETED">COMPLETED</option>
              <option value="SKIPPED">SKIPPED</option>
            </select>
            <button className="btn" type="submit">Create</button>
          </form>
          {status && <p className="muted">{status}</p>}
        </div>
        <div className="card">
          <div className="section-title">Fetch Activities</div>
          <div className="form-grid">
            <input className="input" placeholder="User ID" value={queryUserId} onChange={(e) => setQueryUserId(e.target.value)} />
            <button className="btn secondary" onClick={handleFetch}>Load</button>
          </div>
          <table className="table" style={{ marginTop: 16 }}>
            <thead>
              <tr>
                <th>Type</th>
                <th>Duration</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {activities.map((a) => (
                <tr key={a.id}>
                  <td>{a.workoutType}</td>
                  <td>{a.durationMinutes} min</td>
                  <td>{a.status}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </RequireAuth>
  );
}

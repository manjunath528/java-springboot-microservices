"use client";

import { useEffect, useState } from "react";
import RequireAuth from "../../components/RequireAuth";
import { apiFetch, authHeader } from "../../lib/api";
import { useAuth } from "../../lib/auth";

const emptyUser = {
  name: "",
  email: "",
  address: "",
  dateOfBirth: "",
  weight: 0,
  height: 0,
  gender: "FEMALE",
  fitnessGoal: "GENERAL_FITNESS",
  dailyStepGoal: 8000,
  sleepGoalHours: 8,
  notificationsEnabled: true,
};

export default function UsersPage() {
  const { token } = useAuth();
  const [users, setUsers] = useState([]);
  const [form, setForm] = useState(emptyUser);
  const [status, setStatus] = useState("");

  const loadUsers = async () => {
    if (!token) return;
    try {
      const data = await apiFetch("/api/users", {
        headers: authHeader(token),
      });
      setUsers(data || []);
    } catch (err) {
      setStatus(`Failed to load users: ${err.message}`);
    }
  };

  useEffect(() => {
    loadUsers();
  }, [token]);

  const handleChange = (field, value) => {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setStatus("Creating user...");
    try {
      const payload = {
        ...form,
        weight: Number(form.weight),
        height: Number(form.height),
        dailyStepGoal: Number(form.dailyStepGoal),
        sleepGoalHours: Number(form.sleepGoalHours),
      };
      await apiFetch("/api/users", {
        method: "POST",
        headers: authHeader(token),
        body: JSON.stringify(payload),
      });
      setStatus("User created.");
      setForm(emptyUser);
      loadUsers();
    } catch (err) {
      setStatus(`Create failed: ${err.message}`);
    }
  };

  return (
    <RequireAuth>
      <div className="grid two">
        <div className="card">
          <div className="section-title">Create User</div>
          <form className="form-grid" onSubmit={handleSubmit}>
            <input className="input" placeholder="Name" value={form.name} onChange={(e) => handleChange("name", e.target.value)} required />
            <input className="input" placeholder="Email" type="email" value={form.email} onChange={(e) => handleChange("email", e.target.value)} required />
            <input className="input" placeholder="Address" value={form.address} onChange={(e) => handleChange("address", e.target.value)} required />
            <input className="input" placeholder="Date of Birth (YYYY-MM-DD)" value={form.dateOfBirth} onChange={(e) => handleChange("dateOfBirth", e.target.value)} required />
            <input className="input" placeholder="Weight" type="number" value={form.weight} onChange={(e) => handleChange("weight", e.target.value)} required />
            <input className="input" placeholder="Height" type="number" value={form.height} onChange={(e) => handleChange("height", e.target.value)} required />
            <select className="input" value={form.gender} onChange={(e) => handleChange("gender", e.target.value)}>
              <option value="FEMALE">FEMALE</option>
              <option value="MALE">MALE</option>
              <option value="OTHER">OTHER</option>
            </select>
            <select className="input" value={form.fitnessGoal} onChange={(e) => handleChange("fitnessGoal", e.target.value)}>
              <option value="GENERAL_FITNESS">GENERAL_FITNESS</option>
              <option value="LOSE_WEIGHT">LOSE_WEIGHT</option>
              <option value="GAIN_WEIGHT">GAIN_WEIGHT</option>
              <option value="MAINTAIN_WEIGHT">MAINTAIN_WEIGHT</option>
              <option value="IMPROVE_SLEEP">IMPROVE_SLEEP</option>
            </select>
            <input className="input" placeholder="Daily Step Goal" type="number" value={form.dailyStepGoal} onChange={(e) => handleChange("dailyStepGoal", e.target.value)} />
            <input className="input" placeholder="Sleep Goal Hours" type="number" value={form.sleepGoalHours} onChange={(e) => handleChange("sleepGoalHours", e.target.value)} />
            <label className="muted">
              <input
                type="checkbox"
                checked={form.notificationsEnabled}
                onChange={(e) => handleChange("notificationsEnabled", e.target.checked)}
              />
              Enable Notifications
            </label>
            <button className="btn" type="submit">Create</button>
          </form>
          {status && <p className="muted">{status}</p>}
        </div>
        <div className="card">
          <div className="section-title">Users</div>
          <table className="table">
            <thead>
              <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Goal</th>
              </tr>
            </thead>
            <tbody>
              {users.map((u) => (
                <tr key={u.id}>
                  <td>{u.name}</td>
                  <td>{u.email}</td>
                  <td>{u.fitnessGoal}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </RequireAuth>
  );
}

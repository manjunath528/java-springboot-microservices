"use client";

import { useEffect, useMemo, useState } from "react";
import { apiFetch, authHeader } from "../lib/api";
import { useAuth } from "../lib/auth";
import RequireAuth from "../components/RequireAuth";

const USER_ID_KEY = "selected_user_id";

export default function DashboardPage() {
  const { token } = useAuth();
  const [userId, setUserId] = useState("");
  const [activities, setActivities] = useState([]);
  const [goal, setGoal] = useState(null);
  const [notifications, setNotifications] = useState([]);
  const [nutrition, setNutrition] = useState([]);
  const [aiQuery, setAiQuery] = useState("");
  const [status, setStatus] = useState("");

  useEffect(() => {
    const stored = window.localStorage.getItem(USER_ID_KEY);
    if (stored) setUserId(stored);
  }, []);

  useEffect(() => {
    if (!userId || !token) return;
    window.localStorage.setItem(USER_ID_KEY, userId);

    const headers = authHeader(token);
    setStatus("Loading dashboard data...");

    Promise.allSettled([
      apiFetch(`/api/activities/user/${userId}`, { headers }),
      apiFetch(`/api/goals/active/${userId}`, { headers }),
      apiFetch(`/api/notifications/user/${userId}`, { headers }),
      apiFetch(`/api/nutrition/user/${userId}`, { headers }),
    ])
      .then((results) => {
        const [acts, goalRes, notes, nutri] = results;
        if (acts.status === "fulfilled") setActivities(acts.value || []);
        if (goalRes.status === "fulfilled") setGoal(goalRes.value || null);
        if (notes.status === "fulfilled") setNotifications(notes.value || []);
        if (nutri.status === "fulfilled") setNutrition(nutri.value || []);
      })
      .finally(() => setStatus(""));
  }, [userId, token]);

  const recentActivities = useMemo(() => activities.slice(0, 5), [activities]);
  const latestNotifications = useMemo(() => notifications.slice(0, 5), [notifications]);

  const nutritionSummary = useMemo(() => {
    if (!nutrition.length) return null;
    return nutrition.reduce(
      (acc, item) => ({
        calories: acc.calories + (item.calories || 0),
        protein: acc.protein + (item.protein || 0),
        carbs: acc.carbs + (item.carbs || 0),
        fat: acc.fat + (item.fat || 0),
      }),
      { calories: 0, protein: 0, carbs: 0, fat: 0 }
    );
  }, [nutrition]);

  return (
    <RequireAuth>
      <div className="grid two">
        <div className="card">
          <div className="section-title">Dashboard</div>
          <p className="muted">Pick a user to load activity, goals, and nutrition data.</p>
          <div className="form-grid" style={{ maxWidth: 420 }}>
            <input
              className="input"
              placeholder="User ID"
              value={userId}
              onChange={(e) => setUserId(e.target.value)}
            />
            <div className="muted">Stored locally for quick access.</div>
          </div>
          {status && <p className="muted">{status}</p>}
        </div>

        <div className="card">
          <div className="section-title">Notifications</div>
          {latestNotifications.length === 0 ? (
            <p className="muted">No notifications yet.</p>
          ) : (
            <ul className="muted">
              {latestNotifications.map((n) => (
                <li key={n.id}>{n.title} — {n.message}</li>
              ))}
            </ul>
          )}
        </div>

        <div className="card">
          <div className="section-title">Recent Activities</div>
          {recentActivities.length === 0 ? (
            <p className="muted">No activities found.</p>
          ) : (
            <table className="table">
              <thead>
                <tr>
                  <th>Type</th>
                  <th>Duration</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {recentActivities.map((a) => (
                  <tr key={a.id}>
                    <td>{a.workoutType}</td>
                    <td>{a.durationMinutes} min</td>
                    <td>{a.status}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>

        <div className="card">
          <div className="section-title">Active Goal</div>
          {goal ? (
            <div className="muted">
              <div><strong>Type:</strong> {goal.goalType}</div>
              <div><strong>Target:</strong> {goal.targetValue}</div>
              <div><strong>Status:</strong> {goal.status}</div>
            </div>
          ) : (
            <p className="muted">No active goal found.</p>
          )}
        </div>

        <div className="card">
          <div className="section-title">Nutrition Summary</div>
          {nutritionSummary ? (
            <div className="muted">
              <div><strong>Calories:</strong> {nutritionSummary.calories}</div>
              <div><strong>Protein:</strong> {nutritionSummary.protein} g</div>
              <div><strong>Carbs:</strong> {nutritionSummary.carbs} g</div>
              <div><strong>Fat:</strong> {nutritionSummary.fat} g</div>
            </div>
          ) : (
            <p className="muted">No nutrition entries found.</p>
          )}
        </div>

        <div className="card">
          <div className="section-title">AI Search</div>
          <p className="muted">Describe what you want help with.</p>
          <div className="form-grid">
            <input
              className="input"
              placeholder="e.g. Create a 4-week endurance plan"
              value={aiQuery}
              onChange={(e) => setAiQuery(e.target.value)}
            />
            <button className="btn ghost" onClick={() => setStatus("AI search not wired yet.")}>Send</button>
          </div>
        </div>

        <div className="card">
          <div className="section-title">Quick Actions</div>
          <div className="form-grid">
            <a className="btn" href="/users">Create User</a>
            <a className="btn secondary" href="/activities">Log Activity</a>
            <a className="btn secondary" href="/nutrition">Log Nutrition</a>
            <a className="btn secondary" href="/notifications">View Notifications</a>
          </div>
        </div>
      </div>
    </RequireAuth>
  );
}

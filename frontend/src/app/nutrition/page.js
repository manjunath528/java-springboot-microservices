"use client";

import { useState } from "react";
import RequireAuth from "../../components/RequireAuth";
import { apiFetch, authHeader } from "../../lib/api";
import { useAuth } from "../../lib/auth";

const emptyEntry = {
  userId: "",
  mealType: "Breakfast",
  calories: 500,
  protein: 25,
  carbs: 60,
  fat: 20,
  date: "",
};

export default function NutritionPage() {
  const { token } = useAuth();
  const [form, setForm] = useState(emptyEntry);
  const [queryUserId, setQueryUserId] = useState("");
  const [items, setItems] = useState([]);
  const [status, setStatus] = useState("");

  const handleChange = (field, value) => {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    setStatus("Creating nutrition entry...");
    try {
      const payload = {
        ...form,
        calories: Number(form.calories),
        protein: Number(form.protein),
        carbs: Number(form.carbs),
        fat: Number(form.fat),
      };
      await apiFetch("/api/nutrition", {
        method: "POST",
        headers: authHeader(token),
        body: JSON.stringify(payload),
      });
      setStatus("Nutrition entry created.");
      setForm(emptyEntry);
    } catch (err) {
      setStatus(`Create failed: ${err.message}`);
    }
  };

  const handleFetch = async () => {
    if (!queryUserId) return;
    setStatus("Loading nutrition entries...");
    try {
      const data = await apiFetch(`/api/nutrition/user/${queryUserId}`, {
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
      <div className="grid two">
        <div className="card">
          <div className="section-title">Create Nutrition Entry</div>
          <form className="form-grid" onSubmit={handleCreate}>
            <input className="input" placeholder="User ID" value={form.userId} onChange={(e) => handleChange("userId", e.target.value)} required />
            <input className="input" placeholder="Meal Type" value={form.mealType} onChange={(e) => handleChange("mealType", e.target.value)} required />
            <input className="input" placeholder="Calories" type="number" value={form.calories} onChange={(e) => handleChange("calories", e.target.value)} />
            <input className="input" placeholder="Protein" type="number" value={form.protein} onChange={(e) => handleChange("protein", e.target.value)} />
            <input className="input" placeholder="Carbs" type="number" value={form.carbs} onChange={(e) => handleChange("carbs", e.target.value)} />
            <input className="input" placeholder="Fat" type="number" value={form.fat} onChange={(e) => handleChange("fat", e.target.value)} />
            <input className="input" placeholder="Date (YYYY-MM-DD)" value={form.date} onChange={(e) => handleChange("date", e.target.value)} required />
            <button className="btn" type="submit">Create</button>
          </form>
          {status && <p className="muted">{status}</p>}
        </div>
        <div className="card">
          <div className="section-title">Fetch Nutrition</div>
          <div className="form-grid">
            <input className="input" placeholder="User ID" value={queryUserId} onChange={(e) => setQueryUserId(e.target.value)} />
            <button className="btn secondary" onClick={handleFetch}>Load</button>
          </div>
          <table className="table" style={{ marginTop: 16 }}>
            <thead>
              <tr>
                <th>Meal</th>
                <th>Calories</th>
                <th>Protein</th>
              </tr>
            </thead>
            <tbody>
              {items.map((n) => (
                <tr key={n.id}>
                  <td>{n.mealType}</td>
                  <td>{n.calories}</td>
                  <td>{n.protein}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </RequireAuth>
  );
}

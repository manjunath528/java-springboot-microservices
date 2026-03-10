const DEFAULT_BASE = "http://localhost:4004";

export function getApiBase() {
  return process.env.NEXT_PUBLIC_API_BASE || DEFAULT_BASE;
}

export async function apiFetch(path, options = {}) {
  const base = getApiBase();
  const url = `${base}${path}`;
  const headers = {
    "Content-Type": "application/json",
    ...(options.headers || {}),
  };

  const response = await fetch(url, {
    ...options,
    headers,
  });

  const text = await response.text();
  const data = text ? JSON.parse(text) : null;

  if (!response.ok) {
    const message = data?.message || data?.error || response.statusText;
    const error = new Error(message);
    error.status = response.status;
    throw error;
  }

  return data;
}

export function authHeader(token) {
  if (!token) return {};
  return { Authorization: `Bearer ${token}` };
}

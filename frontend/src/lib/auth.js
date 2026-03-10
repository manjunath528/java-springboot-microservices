"use client";

import { createContext, useContext, useEffect, useMemo, useState } from "react";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState("");
  const [ready, setReady] = useState(false);

  useEffect(() => {
    const stored = window.localStorage.getItem("auth_token");
    if (stored) setToken(stored);
    setReady(true);
  }, []);

  const value = useMemo(
    () => ({
      token,
      ready,
      login: (newToken) => {
        setToken(newToken);
        window.localStorage.setItem("auth_token", newToken);
      },
      logout: () => {
        setToken("");
        window.localStorage.removeItem("auth_token");
      },
    }),
    [token, ready]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error("useAuth must be used within AuthProvider");
  }
  return ctx;
}

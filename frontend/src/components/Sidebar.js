"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";

const links = [
  { href: "/", label: "Dashboard" },
  { href: "/login", label: "Login" },
  { href: "/users", label: "Users" },
  { href: "/activities", label: "Activities" },
  { href: "/nutrition", label: "Nutrition" },
  { href: "/notifications", label: "Notifications" },
  { href: "/goals", label: "Goals" },
  { href: "/billing", label: "Billing" },
  { href: "/ai", label: "AI" },
  { href: "/analytics", label: "Analytics" },
];

export default function Sidebar() {
  const pathname = usePathname();

  return (
    <aside className="sidebar">
      <div className="sidebar-brand">FitOps</div>
      <nav className="sidebar-nav">
        {links.map((link) => (
          <Link
            key={link.href}
            href={link.href}
            className={`nav-link ${pathname === link.href ? "active" : ""}`}
          >
            {link.label}
          </Link>
        ))}
      </nav>
      <div className="sidebar-footer">API via Gateway</div>
    </aside>
  );
}

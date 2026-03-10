import "./globals.css";
import Providers from "./providers";
import Sidebar from "../components/Sidebar";
import Topbar from "../components/Topbar";

export const metadata = {
  title: "Fitness Platform",
  description: "Frontend for the microservices platform",
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body>
        <Providers>
          <div className="app-shell">
            <Sidebar />
            <div className="app-main">
              <Topbar />
              <main className="app-content">{children}</main>
            </div>
          </div>
        </Providers>
      </body>
    </html>
  );
}

import { createContext, useContext, useState } from "react";
import { decodeToken } from "../utils/jwt";
import { isTokenExpired } from "../utils/jwt";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(() => {
    const storedToken = localStorage.getItem("token");
    if (!storedToken) return null;
    if (isTokenExpired(storedToken)) {
      localStorage.removeItem("token");
      return null;
    }
    return storedToken;
  });

  const decoded = token ? decodeToken(token) : null;

  const user = decoded
    ? {
        role: decoded.role,
        id: decoded.id,
        username: decoded.sub,
      }
    : null;

  const login = (jwt) => {
    localStorage.setItem("token", jwt);
    setToken(jwt);
  };

  const logout = () => {
    localStorage.removeItem("token");
    setToken(null);
  };

  const isAuthenticated = !!token;

  return (
    <AuthContext.Provider
      value={{
        token,
        user,
        login,
        logout,
        isAuthenticated,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
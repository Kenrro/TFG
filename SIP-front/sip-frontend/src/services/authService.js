const API = import.meta.env.VITE_API_URL;

export async function loginCustomer(data) {
  return request("/login-customer", data);
}

export async function loginEmployee(data) {
  return request("/login-employee", data);
}

export async function registerCustomer(data) {
  return request("/register-customer", data);
}

async function request(path, body) {
  const res = await fetch(`${API}${path}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });

  if (!res.ok) throw new Error("Auth error");

  return res.json();
}
const BASE = import.meta.env.VITE_API_STABLISHMENT;

export async function createEstablishment(form) {
  const payload = {
    stablishment: {
      name: form.name,
      address: form.address,
      phone: form.phone,
      email: form.email,
      description: form.description
    },
    adminUser: {
      username: form.adminUsername,
      password: form.adminPassword,
      name: form.adminName,
      lastname: form.adminLastname,
      role: "ADMIN"
    }
  };

  const res = await fetch(`${BASE}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });

  if (!res.ok) throw new Error("Failed to create establishment");

  return res.json();
}
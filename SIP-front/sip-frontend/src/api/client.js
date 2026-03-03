const BASE_URL = import.meta.env.VITE_API_URL

async function request(endpoint, options = {}) {
  const token = localStorage.getItem("token")

  const res = await fetch(`${BASE_URL}${endpoint}`, {
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...options.headers
    },
    ...options
  })

  if (!res.ok) {
    let message = "Request error"

    try {
      const data = await res.json()
      message = data.message || message
    } catch {}

    throw new Error(message)
  }

  if (res.status === 204) return null

  return res.json()
}

export const api = {
  get: (url) => request(url),
  post: (url, body) =>
    request(url, {
      method: "POST",
      body: JSON.stringify(body)
    }),
  put: (url, body) =>
    request(url, {
      method: "PUT",
      body: JSON.stringify(body)
    }),
  del: (url) =>
    request(url, {
      method: "DELETE"
    })
}
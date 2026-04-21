
export default async function request(path, options = {}) {

  const token = localStorage.getItem("token");

  try {

    const res = await fetch(path, {
      ...options,
      headers: {
        "Content-Type": "application/json",
        ...(token && { Authorization: `Bearer ${token}` }),
        ...(options.headers || {})
      }
    });

    if (res.status === 401 && token) {
      localStorage.removeItem("token");
      window.location.href = "/login";
    }

    // controlar 204 antes de leer body
    if (res.status === 204) {
      return null;
    }

    let data = null;

    const text = await res.text();

    if (text) {
      try {
        data = JSON.parse(text);
      } catch {
        data = text;
      }
    }
    // if (!res.status === 400) {
    //   const error = new Error(
    //     data?.message ||
    //     data?.error ||
    //     "API_ERROR"
    //   );
    // }

    if (!res.ok) {
      console.log(res)
      console.log(data)
      const error = new Error(
        data?.message ||
        data?.error ||
        (data?.reasons && typeof data.reasons === "object"
          ? Object.values(data.reasons).join(", ")
          : data?.reasons) ||
        "API_ERROR"
      );

      error.status = res.status;
      error.data = data;

      throw error;
    }

    return data;

  } catch (err) {
    console.error(err)
    if (err.status) throw err;

    const error = new Error("NETWORK_ERROR");
    error.status = 0;

    throw error;
  }
}
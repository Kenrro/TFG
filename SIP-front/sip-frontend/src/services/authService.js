import request from "../api/Request";
import { API } from "../api/config";

// const API = import.meta.env.VITE_API_URL;

export async function loginCustomer(data) {
  return request(`${API.AUTH}/login-customer`, {"method": "POST", "body": JSON.stringify(data)});
}

export async function loginEmployee(data) {
  return request(`${API.AUTH}/login-employee`, {"method": "POST", "body": JSON.stringify(data)});
}

export async function registerCustomer(data) {
  return request(`${API.AUTH}/register-customer`, {"method": "POST", "body": JSON.stringify(data)});
}
export async function updateCustomer(data) {
  return request(`${API.AUTH}/update-customer`, {"method": "PUT", "body": JSON.stringify(data)});
}
export async function deleteCustomer() {
  return request(`${API.AUTH}/delete-customer`, {"method": "DELETE"});
}


import request from "../api/Request";
import { API } from "../api/config";

// const API = import.meta.env.VITE_API_URL;

export async function loginCustomer(data) {
  return request(`${API.AUTH}/login/customers`, {"method": "POST", "body": JSON.stringify(data)});
}

export async function loginEmployee(data) {
  return request(`${API.AUTH}/login/employees`, {"method": "POST", "body": JSON.stringify(data)});
}

export async function registerCustomer(data) {
  return request(`${API.AUTH}/customers`, {"method": "POST", "body": JSON.stringify(data)});
}
export async function updateCustomer(data) {
  return request(`${API.AUTH}/customers`, {"method": "PUT", "body": JSON.stringify(data)});
}
export async function deleteCustomer() {
  return request(`${API.AUTH}/customers`, {"method": "DELETE"});
}
export async function updateEmployee(id, data) {
  return request(`${API.AUTH}/employees/${id}`, {
    "method": "PUT", "body": JSON.stringify(data)});
}
export async function createEmployee(data) {
  return request(`${API.AUTH}/employees`, {
    "method": "POST", "body": JSON.stringify({
      ...data,
      role: "SELLER"
    })});
}
export async function deleteEmployee(id) {
  return request(`${API.AUTH}/employees/${id}`, {"method": "DELETE"});
}
export async function changePassword(id, data) {
  return request(`${API.AUTH}/employees/${id}/password`, {"method": "PUT", "body": JSON.stringify(data)});
}

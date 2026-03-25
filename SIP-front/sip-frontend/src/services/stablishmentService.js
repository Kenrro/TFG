import request from "../api/Request";
import { API } from "../api/config";

const BASE = import.meta.env.VITE_API_STABLISHMENT;
const USER_URL = import.meta.env.VITE_API_USER_STABLISHMENT;


export async function createEstablishment(form) {
  const payload = {
    stablishment: {
      name: form.name,
      address: form.address,
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
  console.log(payload)
  return request(`${API.STABLISHMENT}`, 
    {"method": "POST", "body": JSON.stringify(payload)});
}
export async function getStablishments() {
  return request(`${API.STABLISHMENT}/stablishments/me`, {"method": "GET"});
}
export async function updateStablishment(data) {
  return request(`${API.STABLISHMENT}`, {"method": "PUT", "body": JSON.stringify(data)});
}
export async function deleteStablishment() {
  return request(`${API.STABLISHMENT}`, {"method": "DELETE"});
}

export async function leaveStablishment(stablishmentCode) {
  return request(`${API.USER_STABLISHMENT}/customers/me/stablishment/${stablishmentCode}`, {"method": "DELETE"});
}
export async function joinStablishment(stablishmentCode) {
  return request(`${API.USER_STABLISHMENT}/stablishments/customers`, {
    "method": "POST",
    "body": JSON.stringify({ stablishmentCode })
  });
}

export async function getDashboard() {
  return request(`${API.STABLISHMENT}/dashboard`, {"method": "GET"});
}
export async function getStaff() {
  return request(`${API.USER_STABLISHMENT}/stablishments/employees`, {"method": "GET"});
}
export async function getCustomers() {
  return request(`${API.USER_STABLISHMENT}/stablishments/customers`, {"method": "GET"});
}
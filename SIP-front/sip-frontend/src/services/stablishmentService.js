import request from "../api/Request";
import { API } from "../api/config";

const BASE = import.meta.env.VITE_API_STABLISHMENT;
const USER_URL = import.meta.env.VITE_API_USER_STABLISHMENT;


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
  return request(`${API.STABLISHMENT}`, 
    {"method": "POST", "body": JSON.stringify(payload)});
}
export async function getStablishments() {
  return request(`${API.STABLISHMENT}/get-stablihsments-by-token`, {"method": "GET"});
}

export async function leaveStablishment(stablishmentCode) {
  return request(`${API.USER_STABLISHMENT}/delete-customer-relation-by-code/${stablishmentCode}`, {"method": "DELETE"});
}
export async function joinStablishment(stablishmentCode) {
  return request(`${API.USER_STABLISHMENT}/add-relation-customer-stablishment`, {
    "method": "POST",
    "body": JSON.stringify({ stablishmentCode })
  });
}
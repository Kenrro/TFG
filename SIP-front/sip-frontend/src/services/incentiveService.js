import request from "../api/Request";
import { API } from "../api/config";
const BASE = import.meta.env.VITE_API_INCENTIVES;

export async function getIncentives(stablishmentCode) {
  return request(`${API.INCENTIVES}/stablishment/${stablishmentCode}/incentives`, {"method": "GET"});
}
export async function deleteIncentive(id) {
  return request(`${API.INCENTIVES}/${id}`,
    {
      "method": "DELETE"
    }
  )
}
export async function updateIncentive(id, data) {
  console.log(data)
  return request(`${API.INCENTIVES}/${id}`,{
    "method": "PUT",
    "body": JSON.stringify(data)
  })
}
export async function createIncentive(data) {
  return request(`${API.INCENTIVES}`, 
    {"method": "POST",
      "body": JSON.stringify(data)
    }
  )
}
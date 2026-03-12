import request from "../api/Request";
import { API } from "../api/config";
const BASE = import.meta.env.VITE_API_INCENTIVES;

export async function getIncentives(stablishmentCode) {
  return request(`${API.INCENTIVES}/get-by-stablishment-code/${stablishmentCode}`, {"method": "GET"});
}
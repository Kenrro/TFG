import { API } from "../api/config";
import request from "../api/Request";


export async function getConfiguration(data) {
  return request(`${API.CONFIGURATION}`, {"method": "GET"});
}
export async function updateConfiguration(data) {
  return request(`${API.CONFIGURATION}`, {"method": "PUT", "body": JSON.stringify(data)});
}
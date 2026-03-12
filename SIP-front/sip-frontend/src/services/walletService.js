import request from "../api/Request";
import { API } from "../api/config";
const url = import.meta.env.VITE_API_WALLET;

export async function getWalletPoints( establishmentCode  ) {
    return request(
        `${API.WALLET}/${establishmentCode}`, 
        {
            "method": "GET"
        });   
}
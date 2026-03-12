import request from '../api/Request';
import { API } from '../api/config';

const API_URL = import.meta.env.VITE_API_TRANSACTIONS;

// CUSTOMER
export async function addPoints(code) {
    return request(`${API.TRANSACTION}/give-points`, {
        method: 'PUT',
        body: JSON.stringify({ id: code })
    })
}
export async function redeemProduct(incentiveId, productId, stablishmentCode) {
    return request(`${API.TRANSACTION}/redeems`, {
        method: 'POST',
        body: JSON.stringify({ incentiveId, productId, stablishmentCode })
    })
}
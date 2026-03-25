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
// STAFF
export async function createGivePointsTransaction(data) {
    return request(`${API.TRANSACTION}/give-points`, {
        method: 'POST',
        body: JSON.stringify(data)
    })
}
export async function updateRedeemTransaction(id) {
    return request(`${API.TRANSACTION}/redeem`, {
        method: 'PUT',
        body: JSON.stringify({ id })
    })
}
export async function getGivePointsTransactions() {
    return request(`${API.TRANSACTION}/points`, {
        method: 'GET'
    })
}
export async function getRedeemTransactions() {
    return request(`${API.TRANSACTION}/redeem`, {
        method: 'GET'
    })
}

export async function getIncentiveQuantity() {
    return request(`${API.TRANSACTION}/incentives-quantity`, {
        method: 'GET'
    })
}
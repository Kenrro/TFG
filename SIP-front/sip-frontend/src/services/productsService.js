import request from "../api/Request";
import { API } from "../api/config";

export async function getProducts(code) {
    return request(
        `${API.PRODUCTS}/stablishments/${code}/products`, {method: "GET"}
    )
}
export async function createProduct(data) {
    return request(
        `${API.PRODUCTS}`, {method: "POST", "body": JSON.stringify(data)}
    )
}
export async function updateProduct(id ,data) {
    return request(
        `${API.PRODUCTS}/${id}`, {
            "method": "PUT",
            "body": JSON.stringify(data)
        }
    )
}
export async function deleteProduct(id) {
    return request(
        `${API.PRODUCTS}/${id}`, {
            "method": "DELETE"
        }
    )
}

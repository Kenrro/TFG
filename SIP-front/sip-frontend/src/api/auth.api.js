import { api } from "./client"

// CUSTOMER
export const loginCustomer = (data) =>
  api.post("/api/auth/login-customer", data)

export const registerCustomer = (data) =>
  api.post("/api/auth/register-customer", data)

// EMPLOYEE
export const loginEmployee = (data) =>
  api.post("/api/auth/login-employee", data)
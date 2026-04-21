const HOST = window.location.hostname;

export const API = {
  AUTH: `http://${HOST}:${import.meta.env.VITE_API_URL}`,
  STABLISHMENT: `http://${HOST}:${import.meta.env.VITE_API_STABLISHMENT}`,
  USER_STABLISHMENT: `http://${HOST}:${import.meta.env.VITE_API_USER_STABLISHMENT}`,
  WALLET: `http://${HOST}:${import.meta.env.VITE_API_WALLET}`,
  TRANSACTION: `http://${HOST}:${import.meta.env.VITE_API_TRANSACTIONS}`,
  INCENTIVES: `http://${HOST}:${import.meta.env.VITE_API_INCENTIVES}`,
  PRODUCTS: `http://${HOST}:${import.meta.env.VITE_API_PRODUCT}`,
  CONFIGURATION: `http://${HOST}:${import.meta.env.VITE_API_INCENTIVES}`,
};


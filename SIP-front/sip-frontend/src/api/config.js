const HOST = window.location.hostname;

export const API = {
  AUTH: `http://${HOST}:${import.meta.env.VITE_API_URL}/api/auth`,
  STABLISHMENT: `http://${HOST}:${import.meta.env.VITE_API_STABLISHMENT}/stablishments`,
  USER_STABLISHMENT: `http://${HOST}:${import.meta.env.VITE_API_USER_STABLISHMENT}/user-stablishments`,
  WALLET: `http://${HOST}:${import.meta.env.VITE_API_WALLET}/v1/user-points`,
  TRANSACTION: `http://${HOST}:${import.meta.env.VITE_API_TRANSACTIONS}/v1/transactions`,
  INCENTIVES: `http://${HOST}:${import.meta.env.VITE_API_INCENTIVES}/v1/incentives`
};


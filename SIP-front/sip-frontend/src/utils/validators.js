export const required = (value) => {
  if (!value || value.trim() === "") {
    return "Este campo es obligatorio";
  }
  return null;
};

export const minLength = (min) => (value) => {
  if (!value || value.length < min) {
    return `Debe tener al menos ${min} caracteres`;
  }
  return null;
};

export const pattern = (regex, message = "Formato inválido") => (value) => {
  if (!value) return null;

  if (!regex.test(value)) {
    return message;
  }

  return null;
};

export const validateField = (value, validators = []) => {
  for (const validator of validators) {
    const error = validator(value);
    if (error) return error;
  }
  return null;
};

export const REGEX = {
  EMAIL: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
  PHONE: /^[0-9]{9}$/,
  USERNAME: /^[0-9]{9}$/, 
  PASSWORD: /^(?=.*[A-Za-z])(?=.*\\d).{8,}$/
};


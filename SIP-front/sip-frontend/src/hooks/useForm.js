import { useState } from "react";
import { validateField } from "../utils/validators";

export default function useForm(initialValues, validationSchema = {}) {
  const [values, setValues] = useState(initialValues);
  const [errors, setErrors] = useState({});

  const handleChange = (field) => (value) => {
    setValues(prev => ({
      ...prev,
      [field]: value
    }));

    if (validationSchema[field]) {
      const error = validateField(value, validationSchema[field]);

      setErrors(prev => ({
        ...prev,
        [field]: error
      }));
    }
  };

  const validateForm = () => {
    const newErrors = {};

    for (const field in validationSchema) {
      const error = validateField(values[field], validationSchema[field]);

      if (error) newErrors[field] = error;
    }

    setErrors(newErrors);

    return Object.keys(newErrors).length === 0;
  };

  return {
    values,
    errors,
    handleChange,
    validateForm
  };
}
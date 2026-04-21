import { Children, createContext, useContext } from "react";
import useStablishment from "../hooks/useStablishment";
import useEmployees from "../hooks/useEmployees";

const StablishmentContext = createContext();

export const StablishmentProvider = ({ children }) => {

    const { stablishment, configuration } = useStablishment()
    const employees = useEmployees()


    return (
        <StablishmentContext.Provider
            value={{
                stablishment,
                configuration,
                employees
            }}
        >
            {children}
        </StablishmentContext.Provider>

    )
}
export const useStablishmentContext = () => useContext(StablishmentContext)
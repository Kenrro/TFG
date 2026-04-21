import { useEffect, useState } from "react"
import { getStablishments, getStablishmentsByAdminRole } from "../services/stablishmentService"

export default function useStablishment() {

    // Timeout
    const sleep = (ms) => new Promise(resolve => setTimeout(resolve, ms));

    const [stablishment, setStablishment] = useState(null)
    const [stablishmentLoading, setStablishmentLoading] = useState(false)
    const [stablishmentError, setStablishmentError] = useState(null)
    const [stablishmentIsChanged, setStablishmentIsChanged] = useState(false)
    // Configuration
    const [configuration, setConfiguration] = useState(null)
    const [configurationLoading, setConfigurationLoading] = useState(false)
    const [configurationError, setConfigurationError] = useState(null)


    useEffect(()=> 
        {
            setStablishmentLoading(true)
            fetchStablishment()

        }, [stablishmentIsChanged])
    
    async function fetchStablishment(tried = 0) {
        try {
            const data = await getStablishmentsByAdminRole();
            setStablishmentLoading(false)
            setStablishmentError(null)
            setStablishment(data.stablishment)
            setConfiguration(data.configuration)
        } catch(error) {
            tried++
            if(tried => 3) {
                setStablishmentError(error)
                setStablishmentLoading(false)
                return
            }
            await sleep(3000)
            fetchStablishment(tried)
        }
    }

    return {
        stablishment: {
            data: stablishment,
            stablishmentLoading,
            stablishmentError,
            refetch: () => setStablishmentIsChanged(prev => !prev)
        },
        configuration: {
            data: configuration,
            configurationLoading,
            configurationError,
            refetch: () => setStablishmentIsChanged(prev => !prev)
        }
    }
}
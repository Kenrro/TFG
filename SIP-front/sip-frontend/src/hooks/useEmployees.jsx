import { useEffect, useState } from "react";
import { decodeToken } from "../utils/jwt";
import { useAuth } from "../contex/AuthContext";
import { getStaff } from "../services/stablishmentService";

export default function useEmployees() {
    
    // User
    const { token } = useAuth()
    // Timeout
    const sleep = (ms) => new Promise(resolve => setTimeout(resolve, ms));


    const [employees, setEmployees] = useState([]);
    const [employeesError, setEmployeesError] = useState(null);
    const [employeesIsChanged, setEmployeesIsChanged] = useState(false);
    const [employeesLoading, setEmployeesLoading] = useState(false)
    useEffect(()=>{
        fetchStaff()
    }, [employeesIsChanged])
    async function fetchStaff(tried = 0 ) {
        setEmployeesLoading(true)

        try {
            const res = await getStaff();
            console.log(res)
            // const currentUserId = decodeToken(token)?.id;

            // const filteredEmployees = res.relations.filter(
            // (rel) => rel.userId.id !== currentUserId
            // );
            setEmployees(res.relations)
            setEmployeesLoading(false)
        } catch (error) {
            tried++
            if(tried >= 3) {
                setEmployeesLoading(false)
                setEmployeesError(error)
                return
            }
            await sleep(3000)
            fetchStaff(tried)
        }
    }
    return {
        data: employees,
        error: employeesError,
        loading: employeesLoading,
        refetch: () => setEmployeesIsChanged(prev => !prev)
    }

}
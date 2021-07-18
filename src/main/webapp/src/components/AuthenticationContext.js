import { createContext } from "react";

const AuthenticationContext = createContext({
    username: "",
    password: "",
    role: ""
})

export default AuthenticationContext;
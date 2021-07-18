import './App.css';
import React from "react";
import AuthenticationForm from './components/AuthenticationForm';
import AuthenticationContext from './components/AuthenticationContext';
import StudentPanel from './components/StudnetPanel';
import AdminPanel from './components/AdminPanel';
import axios from "./http-common";

function App() {
  const [status, setStatus] = React.useState("");
  const [authInfo, setAuthInfo] = React.useState({});

  return (
    <main className="App">
      <div className="contents">
        <AuthenticationForm onSubmit={
          async (values) => {
            const { username, password } = values;
            try {
              let response = await axios.get(`/auth?user_name=${username}&password=${password}`);
              setAuthInfo({
                username,
                password,
                role: response.data.role
              });
              setStatus(`Auth Success: Role ${response.data.role}. Hi ${username}!`)
            }
            catch (err) {
              console.log(err);
              setAuthInfo({});
              setStatus("Auth Failed");
            }
          }} />
        <h3 className="status">Status: {status}</h3>
      </div>
      <AuthenticationContext.Provider value={{...authInfo}}>
          {authInfo.role === "admin" ? <AdminPanel/>: []}
          {authInfo.role === "student" ? <StudentPanel/>: []}
      </AuthenticationContext.Provider>
    </main>
  );
}

export default App;

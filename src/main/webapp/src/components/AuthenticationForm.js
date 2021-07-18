import React from "react";
import { Formik } from "formik";

export default function AuthenticionForm({onSubmit}) {
    return <div className="column">
        <h1>Authentication</h1>
        <Formik
            initialValues={{ username: '', password: '' }}
            onSubmit={onSubmit}
        >
            {({
                values,
                handleChange,
                handleBlur,
                handleSubmit,
                isSubmitting,
            }) => (
                <form onSubmit={handleSubmit} className="contents">
                    <label>User Name</label>
                    <input
                        name="username"
                        onChange={handleChange}
                        onBlur={handleBlur}
                        value={values.username}
                    />
                    <label>Password</label>
                    <input
                        type="password"
                        name="password"
                        onChange={handleChange}
                        onBlur={handleBlur}
                        value={values.password}
                    />
                    <button type="submit" disabled={isSubmitting}>
                        Submit
                    </button>
                </form>
            )}
        </Formik>
    </div>
}
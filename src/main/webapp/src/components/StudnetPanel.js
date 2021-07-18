import { useContext, useEffect, useState } from "react"
import AuthenticationContext from "./AuthenticationContext"
import Entity from "./Entity"
import { Formik, Field } from "formik";
import axios from "../http-common";

export default function StudentPanel() {
    const { username, password } = useContext(AuthenticationContext);
    const [status, setStaus] = useState("");
    const [updateCount, setUpdateCount] = useState(0);
    const [enrolledCourses, setEnrolledCourses] = useState([0])

    useEffect(() => {
        let fetchData = async () => {
            let response = await axios.get(`/student/${username}/courses`, { auth: { username, password } });
            setEnrolledCourses(response.data);
        }
        fetchData();
    }, [updateCount, username, password]);

    return <div className="contents">
        <Entity path="/course" columns={["id", "name"]} />
        <div className="column">
            <h1>Courses Taken</h1>
            <ol>
                {enrolledCourses.map(course => <li key={course}>{course}</li>)}
            </ol>
            <h1>Take or Reject</h1>
            <Formik
                initialValues={{ id: '', action: 'take' }}
                onSubmit={async (values) => {
                    const { id, action } = values;
                    const url = `/student/${username}/courses/${id}`
                    try {
                        if (action === "take")
                            await axios.put(url, {}, { auth: { username, password } });
                        else
                            await axios.delete(url, { auth: { username, password } });
                        setStaus(`${action} on ${id} successful`);
                        setUpdateCount(updateCount + 1);
                    }
                    catch (error) {
                        setStaus(`${action} on ${id} failed: ${error.response.status}`)
                    }
                }}
            >
                {({
                    values,
                    handleChange,
                    handleBlur,
                    handleSubmit,
                    isSubmitting,
                }) => (
                    <form onSubmit={handleSubmit}>
                        <div role="group">
                            <label>Course ID</label>
                            <input
                                name="id"
                                onChange={handleChange}
                                onBlur={handleBlur}
                                value={values.id}
                            />
                        </div>
                        <div role="group" aria-labelledby="my-radio-group">
                            <div role="group">
                                <label>Take</label>
                                <Field type="radio" name="action" value="take" />
                            </div>
                            <div role="group">
                                <label>Rejct</label>
                                <Field type="radio" name="action" value="reject" />
                            </div>
                        </div>
                        <button type="submit" disabled={isSubmitting}>
                            Submit
                        </button>
                    </form>
                )}
            </Formik>
        </div>
        <h3 className="status">Status: {status}</h3>
    </div>
}
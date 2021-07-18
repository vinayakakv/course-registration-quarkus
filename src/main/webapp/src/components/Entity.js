import { useState, useContext, useEffect } from "react";
import AuthenticationContext from "./AuthenticationContext";
import axios from "../http-common";
import Table from "./Table";
import { Formik, Field } from "formik";

export default function Entity({ path, columns }) {
    let [data, setData] = useState([]);
    let [updateCount, setUpdateCount] = useState(0);
    let [status, setStaus] = useState("");

    let [page, setPage] = useState(0);
    let [size, setSize] = useState(10);

    const dataColumns = columns.map((x) => ({ Header: x, accessor: x }));
    let { username, password, role } = useContext(AuthenticationContext);

    useEffect(() => {
        let fetchData = async () => {
            let entityData = await axios.get(`${path}?page=${page}&size=${size}`, {
                auth: {
                    username,
                    password
                }
            });
            setData(entityData.data);
        }
        fetchData();
    }, [path, username, password, updateCount, page, size]);

    const createUpdateForm = (
        <>
            <h3>Create/Update</h3>
            <Formik
                initialValues={
                    columns.reduce((o, key) => ({ ...o, [key]: '' }), { operation: 'create' })
                }
                onSubmit={async (values) => {
                    let { operation, ...entity } = values;
                    let method = operation === 'create' ? axios.post : axios.put;
                    let request_path = operation === 'update' ? `${path}/${entity.id}` : path;
                    entity = operation === 'update' ?
                        Object.keys(entity)
                            .filter(key => key !== 'id' && entity[key])
                            .reduce((obj, key) => {
                                obj[key] = entity[key];
                                return obj;
                            }, {})
                        : entity;
                    try {
                        await method(request_path, entity, { auth: { username, password } });
                        setStaus(`${operation} success`); // Patient died?! 🤪
                        setUpdateCount(updateCount + 1);
                    }
                    catch (error) {
                        setStaus(`${operation} failure: ${error.response.status}`);
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
                        {columns.map(column => {
                            return <div role="group" key={column}>
                                <label>{column}</label>
                                <input
                                    name={column}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    value={values[column]}
                                />
                            </div>
                        })}
                        <div role="group">
                            <label>
                                <Field type="radio" name="operation" value="create" />
                                Create
                            </label>
                            <label>
                                <Field type="radio" name="operation" value="update" />
                                Update
                            </label>
                        </div>
                        <button type="submit" disabled={isSubmitting}>
                            Submit
                        </button>
                    </form>
                )}
            </Formik>
        </>
    );

    const deleteForm = (
        <>
            <h3>Delete</h3>
            <Formik
                initialValues={{ id: '' }}
                onSubmit={async (values) => {
                    const { id } = values;
                    try {
                        await axios.delete(`${path}/${id}`, { auth: { username, password } });
                        setStaus(`DELETE ${id} successful`);
                        setUpdateCount(updateCount + 1);
                    }
                    catch (error) {
                        setStaus(`DELETE ${id} failed: ${error.response.status}`)
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
                            <label>ID</label>
                            <input
                                name="id"
                                onChange={handleChange}
                                onBlur={handleBlur}
                                value={values.id}
                            />
                        </div>
                        <button type="submit" disabled={isSubmitting}>
                            Submit
                        </button>
                    </form>
                )}

            </Formik>
        </>
    )

    return (
        <>
            <div className="column">
                <h1>{path.slice(1)} Data </h1>
                <div role="form">
                    <label>Page</label>
                    <input type="number" value={page} onChange={(e) => setPage(e.target.value)} />
                    <label>Size </label>
                    <input type="number" value={size} onChange={(e) => setSize(e.target.value)} />
                </div>
                <Table data={data} columns={dataColumns} />
                {role === "admin" ? createUpdateForm : []}
                {role === "admin" ? deleteForm : []}

            </div>
            {role === "admin" ? <h3 className="status">Status: {status}</h3> : []}
        </>
    )
}
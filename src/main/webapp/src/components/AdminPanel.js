import Entity from "./Entity";

export default function AdminPanel() {
    return <div className="contents">
        <Entity path="/student" columns={["id", "name", "email"]} />
        <Entity path="/course" columns={["id", "name"]} />
    </div>
}
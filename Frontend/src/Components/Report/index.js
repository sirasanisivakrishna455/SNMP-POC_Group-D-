import { useEffect, useState } from "react";
import { Styles } from "../../CommonComponents/Commonstyles"
import DataGridDemo from "../../CommonComponents/CommonTable"
import Searchinput from "../../CommonComponents/Searchinput"
import { customerPlansUrl } from "../../Endpoint/endpoint";

export default function Planlist() {
    const [data, setData] = useState();
    const response = async () => {
        try {
            const getData = await fetch(`${customerPlansUrl}/api/plans/fetechPlans`);
            const APIData = await getData.json();
            if (APIData) {
                setData(APIData)
            }
        } catch (error) {
            console.log("log the error response here", error);
        }
    }
    useEffect(() => {
        response();
    }, []);
    const columns = [
        { field: "id", headerName: "ID", width: 90 },
        {
            field: "planName",
            headerName: "Plan Name",
            width: 150,
        },
        {
            field: "dataLimit",
            headerName: "Data Limit",
            width: 150,
        },
        {
            field: "benefits",
            headerName: "Benefits",
            width: 150,
        },
        {
            field: "nextPlan",
            headerName: "Next_Plan",
            width: 150,
        },
    ];
    return (
        <>
            <div style={Styles.container}>
                <div>
                    <Searchinput PlaceHolder={"Search by content"} />
                </div>
                <div style={Styles.tableContainer}>
                    <DataGridDemo columns={columns} rows={data?.map((data, index) => ({
                        ...data,
                        id: index + 1,
                    }))} />

                </div>

            </div>
        </>
    )
}
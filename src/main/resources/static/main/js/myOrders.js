window.onload=function(){
	displayOrders();
}

function displayOrders() {
    fetch(`/home/secure/getOrdersList`, {
        method: 'GET'
    })
    .then(res => res.json().then(data => ({ status: res.status, body: data })))
    .then(({ status, body }) => {
        const tableBody = document.querySelector(".table-container tbody");

        if (status !== 200 || body.status !== "success") {
            const errorMessage = body.error || "Unable to fetch orders.";
            tableBody.innerHTML = `<tr><td colspan="5" class="text-danger text-center">${errorMessage}</td></tr>`;
            return;
        }

        renderOrdersTable(body.ordersList);
    })
    .catch(error => {
        console.error("Fetch error:", error.message);
        const tableBody = document.querySelector(".table-container tbody");
        tableBody.innerHTML = `<tr><td colspan="5" class="text-danger text-center">Network error: ${error.message}</td></tr>`;
    });
}

function renderOrdersTable(orders) {
    const tableBody = document.querySelector(".table-container tbody");
    tableBody.innerHTML = ""; // clear previous rows

    if (!orders || orders.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="5" class="text-center">No orders found.</td></tr>`;
        return;
    }

    orders.forEach(order => {
        const createdDate = new Date(order.date);
        const formattedDate = createdDate.toLocaleDateString("en-GB", {
            year: "numeric",
            month: "short",
            day: "2-digit"
        });

        const row = document.createElement("tr");
        row.innerHTML = `
            <td>#${order.orderID}</td>
            <td>${formattedDate}</td>
            <td>${order.amount.toFixed(2)}</td>
            <td><span class="status-badge status-placed">Placed</span></td>
            <td>
				<a href="/home/secure/orderSucess/${order.id}" class="text-primary" title="View Order">
				  <i class="bi bi-eye-fill"></i>
				</a>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

window.onload=function(){
	const orderIdElement = document.getElementById("orderId");
	const orderId = orderIdElement.value
	
	showOrder(orderId);
}

function showOrder(orderId) {
    fetch(`/home/secure/getOrderDetails/${orderId}`, {
        method: 'GET'
    })
    .then(res => {
        if (!res.ok) {
            return res.json().then(err => {
                throw new Error(err.error || 'Something went wrong');
            });
        }
        return res.json();
    })
    .then(data => {
        console.log(data);
        orderInformation(data.buyerData);
		orderItems(data.orderItems, data.finalAmount);
    })
    .catch(error => {
        console.error('Fetch error:', error.message);

        // Hide main content if needed
        document.querySelector(".container-lg").innerHTML = `
            <div class="alert alert-danger text-center" role="alert">
                🚫 ${error.message}
            </div>
        `;
    });
}

function orderInformation(info){
	const orderNumber = document.getElementById('orderNumber');
	const customerName = document.getElementById('customerName');
    const customerEmail = document.getElementById('customerEmail');
    const customerPhone = document.getElementById('customerPhone');
	const deliveryAddress = document.getElementById('deliveryAddress');
	
	orderNumber.textContent = `Order Id : ${info.orderId}`;
	customerName.innerHTML = `<strong>Full Name:</strong><br>${info.name}`;
	customerEmail.innerHTML = `<strong>E-mail:</strong><br><a href="mailto:${info.email}" class="link-primary">${info.email}</a>`;
	customerPhone.innerHTML= `<strong>Phone Number:</strong><br> ${info.phone}`;
	deliveryAddress.innerHTML=`${info.name}, <br/>
							   ${info.address}, <br/>
							   ${info.city}, ${info.state},<br/>
							   ${info.country} - ${info.pincode}`;
							   
	
}

function orderItems(items,totalAmount){
	const itemListBody = document.getElementById('itemListBody');
	
	items.forEach((item, index) => {
	        const row = document.createElement('tr');
	        row.innerHTML = `
	            <td class="text-center">${index + 1}</td>
	            <td class="d-flex align-items-center gap-2">
	                <img src="${item.productImage}" alt="${item.name}" class="item-img" />
	                ${item.name}
	            </td>
	            <td>${item.amount.toFixed(2)}</td>
	            <td>${item.quantity}</td>
	            <td class="text-end fw-semibold">${item.total.toFixed(2)}</td>
	        `;
	        itemListBody.appendChild(row);
	    });
		
		const totalRow = document.createElement('tr');
	    totalRow.classList.add('table-light');
	    totalRow.innerHTML = `
	        <td colspan="4" class="text-end fw-bold">All Total</td>
	        <td id="allTotal" class="text-end fw-bold">${totalAmount.toFixed(2)}</td>
	    `;
	    itemListBody.appendChild(totalRow);
}
/*
document.addEventListener('DOMContentLoaded', function() {
    // --- General Order Information ---
    const orderIdInput = document.getElementById('orderId');
    const orderNumberDisplay = document.getElementById('orderNumber');

    // --- Stepper Elements ---
    const step1 = document.getElementById('step1');
    const step1Date = document.getElementById('step1Date');
    const step2 = document.getElementById('step2');
    const step2Date = document.getElementById('step2Date');
    const step3 = document.getElementById('step3');
    const step3Date = document.getElementById('step3Date');
    const step4 = document.getElementById('step4');
    const step4Date = document.getElementById('step4Date');

    // --- Order Details Section ---
    const pickupDate = document.getElementById('pickupDate');
    const estimateDrop = document.getElementById('estimateDrop');
    const returnTime = document.getElementById('returnTime');

    // --- Location Information ---
    const pickupLocationAddress = document.getElementById('pickupLocationAddress');
    const dropoffLocationAddress = document.getElementById('dropoffLocationAddress');

    // --- Customer Details ---
    const customerName = document.getElementById('customerName');
    const customerEmail = document.getElementById('customerEmail');
    const customerPhone = document.getElementById('customerPhone');

    // --- Item List ---
    const itemListBody = document.getElementById('itemListBody');
    const allTotal = document.getElementById('allTotal');

    // --- Map ---
    const routeMap = document.getElementById('routeMap');

    // --- Example of how to use these variables ---
    // You would fetch your order data from your backend/API here.
    // For demonstration, we'll use some sample data.

    const orderData = {
        orderId: '67890',
        pickupDate: 'June 1, 2024 10:00:00 am',
        estimateDrop: '5 Days',
        returnTime: 'In 7 Days',
        pickupAddress: 'Ritesh Thete<br/>123 Main St, Pune, MH 411041<br/>India',
        dropoffAddress: 'John Doe<br/>987 Market St, Mumbai, MH 400001<br/>India',
        customer: {
            name: 'Ritesh Thete',
            email: 'theteritesh2@gmail.com',
            phone: '+91 9067467472'
        },
        items: [
            { name: 'Keyboard', price: 25.00, quantity: 1 },
            { name: 'Mouse', price: 15.00, quantity: 1 },
        ],
        status: 'Shipped', // Could be 'Order Received', 'Shipped', 'On the Way', 'Delivered'
        statusDates: {
            received: 'June 1, 2024',
            shipped: 'June 2, 2024',
            onTheWay: null,
            delivered: null
        },
        mapUrl: 'https://www.openstreetmap.org/export/embed.html?bbox=73.7,18.4,74.0,18.6&layer=mapnik'
    };

    // Now, you can populate the elements using the fetched data
    orderNumberDisplay.textContent = `Order #${orderData.orderId}`;
    
    // Example for pickupDate
    pickupDate.innerHTML = `<strong>Pickup Date:</strong><br>${orderData.pickupDate}`;

    // Example for customer name
    customerName.innerHTML = `<strong>Full Name:</strong><br>${orderData.customer.name}`;
    
    // Example for customer email
    customerEmail.innerHTML = `<strong>E-mail:</strong><br><a href="mailto:${orderData.customer.email}" class="link-primary">${orderData.customer.email}</a>`;

    // Example for item list (clear existing rows first)
    itemListBody.innerHTML = ''; // Clear the static example rows
    let grandTotal = 0;
    orderData.items.forEach((item, index) => {
        const row = document.createElement('tr');
        const itemTotal = item.price * item.quantity;
        grandTotal += itemTotal;
        row.innerHTML = `
            <td class="text-center">${index + 1}</td>
            <td class="d-flex align-items-center gap-2">
                <img src="https://placehold.co/40x40/png?text=${item.name.substring(0,1)}" alt="${item.name}" class="item-img" />
                ${item.name}
            </td>
            <td>$${item.price.toFixed(2)}</td>
            <td>${item.quantity}</td>
            <td class="text-end fw-semibold">$${itemTotal.toFixed(2)}</td>
        `;
        itemListBody.appendChild(row);
    });

    // Add the total row
    const totalRow = document.createElement('tr');
    totalRow.classList.add('table-light');
    totalRow.innerHTML = `
        <td colspan="4" class="text-end fw-bold">All Total</td>
        <td id="allTotal" class="text-end fw-bold">$${grandTotal.toFixed(2)}</td>
    `;
    itemListBody.appendChild(totalRow);
    
    // Example for map
    routeMap.src = orderData.mapUrl;

    console.log("All elements selected and ready for data population.");
});

*/
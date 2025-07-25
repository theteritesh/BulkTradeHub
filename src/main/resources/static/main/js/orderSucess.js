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
	const step1Date = document.getElementById('step1Date');
	const orderReciveDate = new Date(info.orderReciveDate.split(".")[0]);
	const formattedDate = orderReciveDate.toLocaleDateString("en-US", {
	  year: "numeric",
	  month: "long",
	  day: "numeric"
	});
	
	const estimateDrop = document.getElementById('estimateDrop'); 
	
	orderNumber.textContent = `Order Id : ${info.orderId}`;
	step1Date.innerText=`${formattedDate}`;
	customerName.innerHTML = `<strong>Full Name:</strong><br>${info.name}`;
	customerEmail.innerHTML = `<strong>E-mail:</strong><br><a href="mailto:${info.email}" class="link-primary">${info.email}</a>`;
	customerPhone.innerHTML= `<strong>Phone Number:</strong><br> ${info.phone}`;
	deliveryAddress.innerHTML=`${info.name}, <br/>
							   ${info.address}, <br/>
							   ${info.city}, ${info.state},<br/>
							   ${info.country} - ${info.pincode}`;
	   if (info.minDeliveryDays === info.maxDeliveryDays) {
	       estimateDrop.innerHTML = `<strong>Estimate Drop:</strong><br> ${info.minDeliveryDays} Days`;
	   } else {
	       estimateDrop.innerHTML = `<strong>Estimate Drop:</strong><br> ${info.minDeliveryDays} Days to ${info.maxDeliveryDays} Days`;
	   }
	   
	   const pickupStartDate = new Date(orderReciveDate);
	   pickupStartDate.setDate(pickupStartDate.getDate() + info.minDeliveryDays);

	   const pickupEndDate = new Date(orderReciveDate);
	   pickupEndDate.setDate(pickupEndDate.getDate() + info.maxDeliveryDays);

	   // Format both dates
	   const options = { year: "numeric", month: "long", day: "numeric" };
	   const formattedStart = pickupStartDate.toLocaleDateString("en-US", options);
	   const formattedEnd = pickupEndDate.toLocaleDateString("en-US", options);

	   // Display the pickup date
	   const pickupDate = document.getElementById("pickupDate");
	   if (info.minDeliveryDays === info.maxDeliveryDays) {
	     pickupDate.innerHTML = `<strong>Pickup Date:</strong><br> ${formattedStart}`;
	   } else {
	     pickupDate.innerHTML = `<strong>Pickup Date:</strong><br> ${formattedStart} to ${formattedEnd}`;
	   }
							   
							   
	
}

function orderItems(items,totalAmount){
	const itemListBody = document.getElementById('itemListBody');
	
	items.forEach((item, index) => {
	        const row = document.createElement('tr');
	        row.innerHTML = `
	            <td class="text-center">${index + 1}</td>
	            <td class="d-flex align-items-center gap-2">
	                <img src="${item.productImage}" alt="${item.name}" class="item-img" />
	                <a href="/home/productDisplay/${item.productPostId}">${item.name}</a>
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

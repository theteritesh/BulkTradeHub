const csrfToken  = document.querySelector('meta[name="_csrf"]').content;
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;


window.onload = function() {
	fetchDeliveryAddresses();
}

function fetchDeliveryAddresses() {
    fetch('/retailShop/getDeliveryAddresses')
        .then(response => {
            if (!response.ok) throw new Error("Failed to fetch addresses.");
            return response.json();
        })
        .then(data => {
            const addressListDiv = document.getElementById("addressList");
            addressListDiv.innerHTML = "";

            if (data.length === 0) {
                addressListDiv.innerHTML = '<p class="text-muted">No addresses saved yet.</p>';
                return;
            }

            data.forEach(addr => {
                const card = renderAddressCard(addr);
                addressListDiv.appendChild(card);
            });
        })
        .catch(error => {
            console.error("Error fetching addresses:", error);
            document.getElementById("addressList").innerHTML = '<p class="text-danger">Failed to load addresses.</p>';
        });
}

function renderAddressCard(addr) {
    const card = document.createElement("div");
    card.className = "card mb-2 p-3 position-relative shadow-sm";
    card.id = `address-card-${addr.id}`;

    card.innerHTML = `
        <div class="d-flex justify-content-between align-items-start">
            <div>
                <h6 class="fw-bold mb-1">
                    ${addr.name} - ${addr.phone}
                    ${addr.primary ? '<span class="badge bg-primary ms-2">Primary</span>' : ''}
                </h6>
                <p class="mb-1">${addr.address}, ${addr.locality}</p>
                <p class="mb-1">${addr.city}, ${addr.state} - ${addr.pincode}</p>
                <p class="mb-0">${addr.country}</p>
            </div>
            <div class="dropdown">
                <i class="bi bi-three-dots-vertical fs-5" id="dropdownMenu${addr.id}" data-bs-toggle="dropdown" style="cursor:pointer;"></i>
                <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="dropdownMenu${addr.id}">
                    <li><a class="dropdown-item" href="#" onclick="editAddress(${addr.id})">Edit</a></li>
                    <li><a class="dropdown-item text-danger" href="#" onclick="deleteAddress(${addr.id})">Delete</a></li>
                    ${!addr.primary ? `<li><a class="dropdown-item" href="#" onclick="makePrimary(${addr.id})">Make Primary</a></li>` : ''}
                </ul>
            </div>
        </div>
    `;

    return card;
}


function editAddress(id) {
	const card = document.getElementById(`address-card-${id}`);
	const lines = card.querySelectorAll("p");

	const namePhone = card.querySelector("h6").textContent.split(" - ");
	const name = namePhone[0].trim();
	const phone = namePhone[1].trim();

	const addressLine = lines[0].textContent.split(", ");
	const cityStatePin = lines[1].textContent.split(", ");
	const [city, statePin] = cityStatePin;
	const [state, pincode] = statePin.split(" - ");
	const country = lines[2].textContent.trim();

	// Fill modal inputs
	document.getElementById("addressId").value = id;
	document.getElementById("name").value = name;
	document.getElementById("mobile").value = phone;
	document.getElementById("address").value = addressLine[0];
	document.getElementById("locality").value = addressLine[1];
	document.getElementById("city").value = city.trim();
	document.getElementById("state").value = state.trim();
	document.getElementById("pincode").value = pincode.trim();
	document.getElementById("country").value = country;

	// Change modal title and button text
	document.getElementById("addressModalLabel").textContent = "Edit Address";
	document.querySelector("#addressForm button[type='submit']").textContent = "Update Address";

	// Show modal
	const modal = new bootstrap.Modal(document.getElementById('addressModal'));
	modal.show();
}

function deleteAddress(id) {
    fetch(`/retailShop/deleteDeliveryAddress/${id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        }
    })
    .then(res => {
        if (!res.ok) throw new Error("Delete failed");
        // Remove the address card from DOM
        const card = document.getElementById(`address-card-${id}`);
        if (card) card.remove();
    })
    .catch(err => {
        console.error("Delete error:", err);
        alert("Failed to delete address.");
    });
}

function saveAddress(event) {
	event.preventDefault();

	const id = document.getElementById('addressId').value.trim();
	const name = document.getElementById('name').value.trim();
	const mobile = document.getElementById('mobile').value.trim();
	const pincode = document.getElementById('pincode').value.trim();
	const locality = document.getElementById('locality').value.trim();
	const address = document.getElementById('address').value.trim();
	const city = document.getElementById('city').value.trim();
	const state = document.getElementById('state').value.trim();
	const country = document.getElementById('country').value.trim();

	const payload = {
		id: id || null,
		name,
		mobile,
		pincode,
		locality,
		address,
		city,
		state,
		country
	};

	fetch('/retailShop/saveOrUpdateDeliveryAddress', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
			[csrfHeader]: csrfToken
		},
		body: JSON.stringify(payload)
	})
	.then(res => res.json())
	.then(data => {
		const addressListDiv = document.getElementById("addressList");

		if (id) {
			// Edit: Replace existing card
			const oldCard = document.getElementById(`address-card-${id}`);
			const newCard = renderAddressCard(data);
			addressListDiv.replaceChild(newCard, oldCard);
		} else {
			// Add: Append new card
			const card = renderAddressCard(data);
			addressListDiv.appendChild(card);
		}

		// Reset form & modal
		document.getElementById("addressForm").reset();
		document.getElementById("addressId").value = "";
		document.getElementById("addressModalLabel").textContent = "Add Address";
		document.querySelector("#addressForm button[type='submit']").textContent = "Add Address";

		const modal = bootstrap.Modal.getInstance(document.getElementById('addressModal'));
		modal.hide();
	})
	.catch(error => {
		console.error("Save failed:", error);
		alert("Failed to save address.");
	});
}

function makePrimary(id) {
    fetch(`/retailShop/makePrimaryAddress/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        }
    })
    .then(res => {
        if (!res.ok) throw new Error("Failed to update primary address");
        return res.text();
    })
    .then(() => {
        fetchDeliveryAddresses(); // Reload UI
    })
    .catch(err => {
        console.error("Primary set error:", err);
        alert("Failed to make this address primary.");
    });
}

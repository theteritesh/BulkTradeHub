 function payNow() {
      const paybleElement=document.getElementById("payable");
	  let amountText = paybleElement.innerText.trim().replace(/[^\d]/g, '');
	  const amount = parseInt(amountText);
	  console.log(amount)
	  fetch('/allPermit/create-order?amount=' + amount, {
	              method: 'POST',
				  headers: {
				  		  [header]: token 
				           }
	          })
	          .then(res => res.json())
	          .then(order => {
	              var options = {
	                  "key": "rzp_test_uMMypIJ2X2bn1N", 
	                  "amount": order.amount,
	                  "currency": "INR",
	                  "name": "BulkTradeHub",
	                  "description": "Order Payment",
	                  "order_id": order.id,
	                  "handler": function (response){
							console.log("response",response);
							clearCart();
	                  },
	                  "prefill": {
	                      "name": order.userName,
	                      "email": order.userEmail,
	                      "contact": order.userPhone
	                  },
	                  "theme": {
	                      "color": "#3399cc"
	                  }
	              };
	              var rzp1 = new Razorpay(options);
	              rzp1.open();
	          })
	          .catch(err => {
	              console.error(err);
	              alert("Failed to create order. Check console for error.");
	          });
  }
 
  
  window.onload=function(){
	loadCart();
  };
  
  function loadCart(){
	const cartContainer = document.getElementById('guest-cart-items');
  	const cart = JSON.parse(localStorage.getItem('guestCart')) || [];

  	if (cart.length === 0) {
  		cartContainer.innerHTML = '<p class="text-muted">Your cart is empty.</p>';
		const clearCartBtn=document.getElementById("clearCartBtn");
		clearCartBtn.className="d-none";
  		return;
  	}

	cart.forEach(item => {
	  const sellingPrice = item.wholesalePrice * item.minOrderQuantity;
	  const mrpPrice = item.retailPrice * item.minOrderQuantity;

	  const cartItem = document.createElement('div');
	  cartItem.className = 'd-flex flex-column flex-sm-row gap-3 border-bottom py-3';
	  cartItem.id = `cart-item-${item.productPostId}`; // ✅ Unique ID

	  cartItem.innerHTML = `
	    <div class="flex-shrink-0 text-center">
	      <img src="${item.image}" alt="Product Image" class="img-fluid" style="max-width: 100px;">
	    </div>
	    <div class="flex-grow-1">
	      <p class="mb-1 fw-bold">${item.productName}</p>
	      <p class="text-success" style="font-size: 0.8rem;">Available Lots (<span class="availableLots">${item.availableLots}</span>)</p>
	      <p class="small mb-2">Category: <strong>${item.category}</strong></p>
	      <p class="small mb-2">Brand: <strong>${item.brand}</strong></p>

	      <div class="d-flex flex-wrap align-items-center gap-2 mb-2">
	        <div class="border rounded-pill px-2 py-1 d-flex align-items-center gap-2">
			<div class="quantity-control">
			  <button class="btn btn-sm btn-custom-qty p-1 decreaseBtn" onclick="decreaseBtnQty(${item.productPostId})">-</button>
			  <span class="numberInput">${item.quantity}</span>
			  <button class="btn btn-sm btn-custom-qty p-1 increaseBtn" onclick="increaseBtnQty(${item.productPostId})">+</button>
			</div>
	        </div>
	        <a class="text-primary text-decoration-underline small" onclick="deleteProduct(${item.productPostId})">Delete</a>
	        <a href="#" class="text-primary text-decoration-underline small">Save for later</a>
	        <a href="#" class="text-primary text-decoration-underline small">See more like this</a>
	      </div>
	    </div>
	    <div class="text-end text-sm-start text-nowrap">
	      <div class="fw-bold">₹${sellingPrice.toFixed(2)}</div>
	      <div class="text-muted small">
	        M.R.P.: ₹<span class="line-through">${mrpPrice.toFixed(2)}</span>
	      </div>
	    </div>
	  `;

	  cartContainer.appendChild(cartItem);
	});
	updateCartSummary();
  }
  
  function clearCart(){
	if (confirm('Are you sure you want to clear the cart?')) {
      localStorage.removeItem('guestCart');
	  loadCart();
	  updateCartCount();
    }
  }
  
  function deleteProduct(idToDelete) {
    let cart = JSON.parse(localStorage.getItem('guestCart')) || [];

    // Remove from localStorage
    cart = cart.filter(item => item.productPostId != idToDelete);
    localStorage.setItem('guestCart', JSON.stringify(cart));

    // Remove from DOM
    const elementToRemove = document.getElementById(`cart-item-${idToDelete}`);
    if (elementToRemove) {
      elementToRemove.remove();
    }

    // Optional: check if cart is empty now
    if (cart.length === 0) {
      document.getElementById('guest-cart-items').innerHTML = '<p class="text-muted">Your cart is empty.</p>';
      const clearCartBtn = document.getElementById("clearCartBtn");
      if (clearCartBtn) clearCartBtn.className = "d-none";
    }
	updateCartSummary();
	updateCartCount();
  }
  
  function updateCartSummary() {
    const cart = JSON.parse(localStorage.getItem('guestCart')) || [];

    let totalAmount = 0;
    let totalWholesale = 0;

    cart.forEach(item => {
      const quantity = item.quantity || 1;
      const minQty = item.minOrderQuantity || 1;

      const wholesale = parseFloat(item.wholesalePrice) * quantity * minQty;
      const retail = parseFloat(item.retailPrice || 0) * quantity * minQty;

      totalAmount += retail;
      totalWholesale += wholesale;
    });

    const discount = totalAmount - totalWholesale;
    const payable = totalWholesale;

    // Update DOM
    document.getElementById('totalAmount').textContent = `₹${totalAmount.toFixed(2)}`;
    document.getElementById('discountAmount').textContent = `₹${discount.toFixed(2)}`;
    document.getElementById('payable').textContent = `₹${payable.toFixed(2)}`;
  }

	
  function increaseBtnQty(productPostId) {
  	const cart = JSON.parse(localStorage.getItem('guestCart')) || [];

  	const product = cart.find(item => item.productPostId == productPostId);
  	if (!product) return;

  	const maxQty = parseInt(product.availableLots) || 1;
  	if (product.quantity < maxQty) {
  		product.quantity += 1;
  		localStorage.setItem('guestCart', JSON.stringify(cart));
  		updateQuantityDisplay(productPostId, product.quantity);
  		updateCartSummary();
  	}
  }

  function decreaseBtnQty(productPostId) {
  	const cart = JSON.parse(localStorage.getItem('guestCart')) || [];

  	const product = cart.find(item => item.productPostId == productPostId);
  	if (!product) return;

  	if (product.quantity > 1) {
  		product.quantity -= 1;
  		localStorage.setItem('guestCart', JSON.stringify(cart));
  		updateQuantityDisplay(productPostId, product.quantity);
  		updateCartSummary();
  	}
  }
  
  function updateQuantityDisplay(productPostId, newQty) {
  	const cartItem = document.getElementById(`cart-item-${productPostId}`);
  	if (!cartItem) return;

  	const qtySpan = cartItem.querySelector('.numberInput');
  	if (qtySpan) {
  		qtySpan.textContent = newQty;
  	}
  }


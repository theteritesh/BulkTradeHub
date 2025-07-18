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
  
  /* ------------  CART LOADER  ------------ */
  async function loadCart() {
    const cartContainer = document.getElementById("guest-cart-items");
    const clearCartBtn  = document.getElementById("clearCartBtn");
    const isUserLoggedIn = document.getElementById("user-logged-in") !== null;

    cartContainer.innerHTML = "";          // reset
    clearCartBtn.classList.remove("d-none");

    /* ---------- 1️⃣  GUEST CART ---------- */
    if (!isUserLoggedIn) {
      const cart = JSON.parse(localStorage.getItem("guestCart")) || [];

      if (cart.length === 0) {
        cartContainer.innerHTML =
          '<p class="text-muted">Your cart is empty.</p>';
        clearCartBtn.classList.add("d-none");
        updateCartSummary();
        return;
      }

      cart.forEach(renderGuestItem);
      return;
    }

    /* ---------- 2️⃣  LOGGED‑IN CART ---------- */
    try {
      const res = await fetch("/home/getLoggedInUserCart");
      if (!res.ok) throw new Error("Failed to fetch cart");

      const userCart = await res.json();

      if (userCart.length === 0) {
        cartContainer.innerHTML =
          '<p class="text-muted">Your cart is empty.</p>';
        clearCartBtn.classList.add("d-none");
        return;
      }

      userCart.forEach(renderUserItem);
    } catch (err) {
      console.error(err);
      cartContainer.innerHTML =
        "<p class='text-danger'>Unable to load cart.</p>";
      clearCartBtn.classList.add("d-none");
    }
  }

  /* ------------  RENDER HELPERS  ------------ */
  function renderGuestItem(item) {
    const cartContainer = document.getElementById("guest-cart-items");

    const sellingPrice = item.wholesalePrice * item.minOrderQuantity;
    const mrpPrice     = item.retailPrice    * item.minOrderQuantity;

    const div = document.createElement("div");
	div.className = "d-flex flex-column flex-sm-row gap-3 border-bottom py-3 cart-item";
	div.id = `cart-item-${item.productPostId || item.cartId}`;
	div.setAttribute("data-product-id", item.productPostId || item.cartId);
	div.setAttribute("data-quantity", item.quantity);
	div.setAttribute("data-retail-price", item.retailPrice);
	div.setAttribute("data-wholesale-price", item.wholesalePrice);
	div.setAttribute("data-min-qty", item.minOrderQuantity);

    div.innerHTML = `
      <div class="flex-shrink-0 text-center">
        <img src="${item.image}" class="img-fluid" style="max-width:100px;">
      </div>
      <div class="flex-grow-1">
        <p class="mb-1 fw-bold">${item.productName}</p>
        <p class="text-success" style="font-size:0.8rem;">
          Available Lots (<span class="availableLots">${item.availableLots}</span>)
        </p>
        <p class="small mb-2">Category: <strong>${item.category}</strong></p>
        <p class="small mb-2">Brand: <strong>${item.brand}</strong></p>
        <div class="d-flex flex-wrap align-items-center gap-2 mb-2">
          <div class="border rounded-pill px-2 py-1 d-flex align-items-center gap-2">
            <div class="quantity-control">
              <button class="btn btn-sm btn-custom-qty p-1"
                      onclick="decreaseBtnQty(${item.productPostId}, false)">-</button>
              <span class="numberInput">${item.quantity}</span>
              <button class="btn btn-sm btn-custom-qty p-1"
                      onclick="increaseBtnQty(${item.productPostId}, false)">+</button>
            </div>
          </div>
          <a class="text-primary text-decoration-underline small"
             onclick="deleteProduct(${item.productPostId})">Delete</a>
        </div>
      </div>
      <div class="text-end text-sm-start text-nowrap">
        <div class="fw-bold">₹${sellingPrice.toFixed(2)}</div>
        <div class="text-muted small">
          M.R.P.: ₹<span class="line-through">${mrpPrice.toFixed(2)}</span>
        </div>
      </div>`;
    cartContainer.appendChild(div);
	updateCartSummaryFromDOM();
  }

  function renderUserItem(item) {
    const cartContainer = document.getElementById("guest-cart-items");

    const sellingPrice = item.wholesalePrice * item.minOrderQuantity ;
    const mrpPrice     = item.retailPrice    * item.minOrderQuantity ;

    const div = document.createElement("div");
	div.className = "d-flex flex-column flex-sm-row gap-3 border-bottom py-3 cart-item";
	div.id = `cart-item-${item.productPostId || item.cartId}`;
	div.setAttribute("data-product-id", item.productPostId || item.cartId);
	div.setAttribute("data-quantity", item.quantity);
	div.setAttribute("data-retail-price", item.retailPrice);
	div.setAttribute("data-wholesale-price", item.wholesalePrice);
	div.setAttribute("data-min-qty", item.minOrderQuantity);

    div.innerHTML = `
      <div class="flex-shrink-0 text-center">
        <img src="${item.mainImageBase64}" class="img-fluid" style="max-width:100px;">
      </div>
      <div class="flex-grow-1">
        <p class="mb-1 fw-bold">${item.productName}</p>
        <p class="text-success" style="font-size:0.8rem;">
          Available Lots (<span class="availableLots">${item.availableLots}</span>)
        </p>
        <p class="small mb-2">Category: <strong>${item.category}</strong></p>
        <p class="small mb-2">Brand: <strong>${item.brand}</strong></p>
        <div class="d-flex flex-wrap align-items-center gap-2 mb-2">
          <div class="border rounded-pill px-2 py-1 d-flex align-items-center gap-2">
            <div class="quantity-control">
              <button class="btn btn-sm btn-custom-qty p-1"
                      onclick="decreaseBtnQty(${item.cartId}, true)">-</button>
              <span class="numberInput">${item.quantity}</span>
              <button class="btn btn-sm btn-custom-qty p-1"
                      onclick="increaseBtnQty(${item.cartId}, true)">+</button>
            </div>
          </div>
          <a class="text-primary text-decoration-underline small"
             onclick="deleteProduct(${item.cartId})">Delete</a>
        </div>
      </div>
      <div class="text-end text-sm-start text-nowrap">
        <div class="fw-bold">₹${sellingPrice.toFixed(2)}</div>
        <div class="text-muted small">
          M.R.P.: ₹<span class="line-through">${mrpPrice.toFixed(2)}</span>
        </div>
      </div>`;
    cartContainer.appendChild(div);
	updateCartSummaryFromDOM();
  }

  
  async function clearCart() {
    if (!confirm('Are you sure you want to clear the cart?')) return;

    const isUserLoggedIn = document.getElementById("user-logged-in") !== null;

    if (isUserLoggedIn) {
      try {
        const res = await fetch(`/home/cart/clear`, {
          method: "POST",
          headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
          }
        });

        if (!res.ok) {
          const errorText = await res.text();
          console.error("Error clearing cart:", errorText);
          return;
        }

        console.log("Cart cleared successfully for user.");
        loadCart();
		updateCartSummaryFromDOM();
        updateCartCount();
      } catch (err) {
        console.error("Unexpected error clearing cart:", err);
      }
    } else {
      localStorage.removeItem('guestCart');
      loadCart();    
      updateCartCount();
	  updateCartSummaryFromDOM();
    }
  }

  
  async function deleteProduct(idToDelete) {
    const isUserLoggedIn = document.getElementById("user-logged-in") !== null;

    if (isUserLoggedIn) {
      try {
        const res = await fetch(`/home/cart/delete/${idToDelete}`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
          }
        });

        if (!res.ok) throw new Error("Failed to delete product");

        const deletedCart = await res.json();
		
		const elementToRemove = document.getElementById(`cart-item-${deletedCart.productPostId}`);
		if (elementToRemove) elementToRemove.remove();
		

      } catch (err) {
        console.error("Error deleting product:", err);
      }
    } else {
      let cart = JSON.parse(localStorage.getItem('guestCart')) || [];
      cart = cart.filter(item => item.productPostId != idToDelete);
      localStorage.setItem('guestCart', JSON.stringify(cart));
    }

    // Remove from DOM
    const elementToRemove = document.getElementById(`cart-item-${idToDelete}`);
    if (elementToRemove) elementToRemove.remove();

    const remainingItems = document.querySelectorAll('[id^="cart-item-"]');
    if (remainingItems.length === 0) {
      document.getElementById('guest-cart-items').innerHTML = '<p class="text-muted">Your cart is empty.</p>';
      document.getElementById("clearCartBtn").className = "d-none";
    }

    updateCartSummaryFromDOM();
    updateCartCount();
  }


  
  function updateCartSummaryFromDOM() {
    const cartItems = document.querySelectorAll('.cart-item');

    let totalAmount = 0;
    let totalWholesale = 0;

    cartItems.forEach(item => {
      const quantity = parseInt(item.dataset.quantity || "1");
      const retailPrice = parseFloat(item.dataset.retailPrice || "0");
      const wholesalePrice = parseFloat(item.dataset.wholesalePrice || "0");
      const minQty = parseInt(item.dataset.minQty || "1");

      const retailTotal = quantity * retailPrice * minQty;
      const wholesaleTotal = quantity * wholesalePrice * minQty;

      totalAmount += retailTotal;
      totalWholesale += wholesaleTotal;
    });

    const discount = totalAmount - totalWholesale;
    const payable = totalWholesale;

    document.getElementById("totalAmount").textContent = `₹${totalAmount.toFixed(2)}`;
    document.getElementById("discountAmount").textContent = `₹${discount.toFixed(2)}`;
    document.getElementById("payable").textContent = `₹${payable.toFixed(2)}`;
  }



	
  async function increaseBtnQty(id, isUser = false) {
    if (isUser) {
      try {
        const res = await fetch(`/home/cart/increase/${id}`, {
          method: "POST",
          headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
          }
        });

        if (res.status === 400) {
          const errorMsg = await res.text();
          console.log("Error: " + errorMsg);
          return;
        }

        if (!res.ok) throw new Error("Failed to increase quantity");

        const updatedCart = await res.json();
        updateQuantityDisplay(updatedCart.productPostId, updatedCart.quantity);
        updateCartSummaryFromDOM();
      } catch (err) {
        console.error("Error increasing quantity:", err);
        alert("An unexpected error occurred. Please try again.");
      }
    } else {
      const cart = JSON.parse(localStorage.getItem('guestCart')) || [];
      const product = cart.find(item => item.productPostId == id);
      if (!product) return;

      const maxQty = parseInt(product.availableLots) || 1;
      if (product.quantity < maxQty) {
        product.quantity += 1;
        localStorage.setItem('guestCart', JSON.stringify(cart));
        updateQuantityDisplay(id, product.quantity);
        updateCartSummaryFromDOM();
      }
    }
  }



  async function decreaseBtnQty(id, isUser = false) {
    if (isUser) {
      try {
        const res = await fetch(`/home/cart/decrease/${id}`, {
          method: "POST",
          headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
          }
        });

        if (res.status === 400) {
          const errorMsg = await res.text();
          console.log("Error: " + errorMsg);
          return;
        }

        if (!res.ok) throw new Error("Failed to decrease quantity");

        const updatedCart = await res.json();
        updateQuantityDisplay(updatedCart.productPostId, updatedCart.quantity);
        updateCartSummaryFromDOM();
      } catch (err) {
        console.error("Error decreasing quantity:", err);
        alert("An unexpected error occurred. Please try again.");
      }
    } else {
      const cart = JSON.parse(localStorage.getItem('guestCart')) || [];
      const product = cart.find(item => item.productPostId == id);
      if (!product) return;

      if (product.quantity > 1) {
        product.quantity -= 1;
        localStorage.setItem('guestCart', JSON.stringify(cart));
        updateQuantityDisplay(id, product.quantity);
        updateCartSummaryFromDOM();
      }
    }
  }

  
  function updateQuantityDisplay(productPostId, newQty) {
  	const cartItem = document.getElementById(`cart-item-${productPostId}`);
  	if (!cartItem) return;

  	const qtySpan = cartItem.querySelector('.numberInput');
  	if (qtySpan) {
  		qtySpan.textContent = newQty;
  	}
	cartItem.dataset.quantity = newQty;
  }


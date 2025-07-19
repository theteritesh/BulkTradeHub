const csrfToken  = document.querySelector('meta[name="_csrf"]').content;
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    updateCartCount();
	displayGuestModal()

	function updateCartCount() {
	  const countSpan = document.getElementById('cart-count');
	  const isUserLoggedIn = document.getElementById("user-logged-in") !== null;

	  if (!isUserLoggedIn) {
	    const cart = JSON.parse(localStorage.getItem('guestCart')) || [];
	    const totalCount = cart.length;

	    if (totalCount === 0) {
	      countSpan.style.display = 'none';
	    } else {
	      countSpan.style.display = 'inline-block';
	      animateCartCount(countSpan, totalCount);
	    }
	  } else {
	    // For logged-in users, fetch count from backend
	    fetch('/home/cart/count')
	      .then(response => response.json())
	      .then(data => {
	        const totalCount = data.count || 0;

	        if (totalCount === 0) {
	          countSpan.style.display = 'none';
	        } else {
	          countSpan.style.display = 'inline-block';
	          animateCartCount(countSpan, totalCount);
	        }
	      })
	      .catch(error => {
	        console.error("Failed to fetch cart count", error);
	      });
	  }
	}

	function animateCartCount(span, count) {
	  if (span.textContent !== count.toString()) {
	    span.textContent = count;
	    span.classList.remove('cart-pop');
	    void span.offsetWidth; // Force reflow to restart animation
	    span.classList.add('cart-pop');
	  }
	}
	
	
	
	function displayGuestModal(){
		
		const isUserLoggedIn = document.getElementById("user-logged-in") !== null;
	    const guestCart = JSON.parse(localStorage.getItem('guestCart') || '[]');
	
	    if (isUserLoggedIn && guestCart.length > 0) {
	      populateGuestCartModal(guestCart);
	      const modal = new bootstrap.Modal(document.getElementById('guestCartMergeModal'));
	      modal.show();
	    }
	 }
	 
	 function populateGuestCartModal(cart) {
	   const container = document.getElementById('guest-cart-items-modal');
	   container.innerHTML = ''; // Clear before append
	
	   cart.forEach((item, index) => {
	     const div = document.createElement('div');
	     div.className = "d-flex justify-content-between align-items-center border p-2";
	     div.innerHTML = `
	       <div class="d-flex align-items-center">
	         <img src="${item.image}" style="height: 50px; width: 50px; object-fit: cover;" class="me-2">
	         <span>${item.productName} (Qty: ${item.quantity})</span>
	       </div>
	       <button class="btn btn-sm btn-outline-danger" onclick="removeItemFromGuestCart(${item.productPostId})">Delete</button>
	     `;
	     container.appendChild(div);
	   });
	 }
	
	 
	 function clearGuestCart() {
	     localStorage.removeItem('guestCart');
	     $('#guestCartMergeModal').modal('hide');
	   
	 }
	
	 function removeItemFromGuestCart(id) {
	   let cart = JSON.parse(localStorage.getItem('guestCart')) || [];
	   cart = cart.filter(item => item.productPostId !== id);
	   localStorage.setItem('guestCart', JSON.stringify(cart));
	   populateGuestCartModal(cart);
	 }
	 
	 function mergeGuestCartToUser() {
	   const cart = JSON.parse(localStorage.getItem('guestCart')) || [];

	   // Map to required fields only
	   const minimalCart = cart.map(item => ({
	     productPostId: item.productPostId,
	     quantity: item.quantity,
	     addedAt: item.addedAt
	   }));

	   console.log("Sending to backend:", minimalCart);
	   
	   fetch('/home/mergeGuestCart', {
	       method: 'POST',
	       headers: {
	         'Content-Type': 'application/json',
	         [csrfHeader]: csrfToken
	       },
	       credentials: 'same-origin',
	       body: JSON.stringify(minimalCart)
	     })
	   .then(response => {
	     if (response.ok) {
			clearGuestCart();
			updateCartCount();
	     }
	   })
	   .catch(error => {
	     console.error('Error merging cart:', error);
	   });
	 }

	 
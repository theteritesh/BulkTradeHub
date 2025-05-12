const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

function increaseQuantity(cartId,availableLots,currentValue){
	if (currentValue < availableLots) {
		updateCartQuantity(cartId,"increase");
	}
}

function decreaseQuantity(cartId,availableLots,currentValue){
	updateCartQuantity(cartId, "decrease");
}

  function updateCartQuantity(cartId, action, currentValue) {
	console.log("cart id : " + cartId , "action :"+action);
      fetch("/allPermit/updateCartQuantity", {
          method: "POST",
          headers: {
              "Content-Type": "application/json",
			  [header]: token 
          },
          body: JSON.stringify({
              cartId: cartId,
              action: action
          })
      })
      .then(response => {
          if (!response.ok) {
              throw new Error("Failed to update quantity.");
          }
          return response.json();
      })
      .then(data => {
          console.log("Cart updated successfully", data);
		  location.reload();
      })
      .catch(error => {
          console.error("Error:", error);
          alert("Could not update cart. Please try again.");
      });
  }
  function deleteCartItem(cartId) {
      if (!confirm("Are you sure you want to delete this item from the cart?")) return;

      fetch(`/allPermit/deleteCartItem/${cartId}`, {
          method: 'DELETE',
          headers: {
              'Content-Type': 'application/json',
			  [header]: token 
          }
      })
      .then(response => {
          if (response.ok) {
              location.reload(); 
          } else {
              return response.text().then(text => { throw new Error(text) });
          }
      })
      .catch(error => {
          console.error("Error deleting cart item:", error);
          alert("Failed to delete cart item: " + error.message);
      });
  }
  
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
  
  function clearCart() {
      fetch('/allPermit/clearCart', {
          method: 'GET',
          headers: {
              'Content-Type': 'application/json',
              [header]: token  // optional if you are using CSRF or token-based auth
          }
      })
      .then(response => {
          if (!response.ok) {
              throw new Error("Failed to clear cart");
          }
          return response.text();
      })
      .then(message => {
		location.reload(); 
      })
      .catch(error => {
          console.error("Error:", error);
          alert("Error clearing cart");
      });
  }

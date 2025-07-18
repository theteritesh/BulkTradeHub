function changeMainImage(element) {
	var mainImage = document.getElementById('mainProductImage');
	mainImage.src = element.src;
}


const input = document.getElementById('numberInput');
const increaseBtn = document.getElementById('increaseBtn');
const decreaseBtn = document.getElementById('decreaseBtn');
const availableLots = document.getElementById('availableLots');
const maxLots = parseInt(availableLots.innerText);

increaseBtn.addEventListener('click', () => {
  let currentValue = parseInt(input.value);
  if (currentValue < maxLots) {
    input.value = currentValue + 1;
  }
});

decreaseBtn.addEventListener('click', () => {
  let currentValue = parseInt(input.value);
  if (currentValue > 1) {
    input.value = currentValue - 1;
  }
});

function showTab(tabName) {
    const descriptionContent = document.getElementById('description-content');
    const additionalContent = document.getElementById('additional-content');
    const descriptionTab = document.getElementById('description-tab');
    const additionalTab = document.getElementById('additional-tab');

    if (tabName === 'description') {
        descriptionContent.classList.remove('hidden');
        additionalContent.classList.add('hidden');
        descriptionTab.classList.add('text-blue-600', 'border-b-2', 'border-blue-600');
        descriptionTab.classList.remove('text-gray-600');
        additionalTab.classList.remove('text-blue-600', 'border-b-2', 'border-blue-600');
        additionalTab.classList.add('text-gray-600');
    } else {
        descriptionContent.classList.add('hidden');
        additionalContent.classList.remove('hidden');
        additionalTab.classList.add('text-blue-600', 'border-b-2', 'border-blue-600');
        additionalTab.classList.remove('text-gray-600');
        descriptionTab.classList.remove('text-blue-600', 'border-b-2', 'border-blue-600');
        descriptionTab.classList.add('text-gray-600');
    }
}



const mainImage = document.getElementById("mainProductImage");
  const zoomBox = document.getElementById("zoom-box");

  mainImage.addEventListener("mouseenter", () => {
    zoomBox.style.display = "block";
    zoomBox.style.backgroundImage = `url('${mainImage.src}')`;
  });

  mainImage.addEventListener("mousemove", (e) => {
    const rect = mainImage.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;

    const zoomLevel = 2.5;
    const bgX = -x * zoomLevel + zoomBox.offsetWidth / 2;
    const bgY = -y * zoomLevel + zoomBox.offsetHeight / 2;

    zoomBox.style.left = `${x - zoomBox.offsetWidth / 2}px`;
    zoomBox.style.top = `${y - zoomBox.offsetHeight / 2}px`;
    zoomBox.style.backgroundSize = `${mainImage.offsetWidth * zoomLevel}px ${mainImage.offsetHeight * zoomLevel}px`;
    zoomBox.style.backgroundPosition = `${bgX}px ${bgY}px`;
  });

  mainImage.addEventListener("mouseleave", () => {
    zoomBox.style.display = "none";
  });
 
 
  document.addEventListener('DOMContentLoaded', function () {
    const category = document.getElementById("productCategoryName")?.innerText || '';
    if (category) loadRelatedAndNew(category);
  });

  function loadRelatedAndNew(category) {
    fetch(`/home/productPost/related-and-new?category=${encodeURIComponent(category)}`)
      .then(res => res.json())
      .then(data => {
        renderProductList(data.related, document.getElementById("related-products"));
        renderProductList(data.new, document.getElementById("new-products"));
      })
      .catch(err => console.error("Error loading product posts: ", err));
  }

  function renderProductList(products, container) {
    if (!container) return;
    container.innerHTML = "";
    products.forEach(product => {
      const card = document.createElement('div');
      card.className = "w-full md:w-1/4 p-4";
      card.innerHTML = `
        <a href="/home/productDisplay/${product.id}" class="block">
          <div class="bg-gray-100 p-4 rounded-lg shadow-lg text-center">
            <img src="${product.base64Image}" class="rounded-lg card-img" alt="Product Image" />
            <h3 class="text-gray-800 text-lg font-bold mt-2">${product.productName}</h3>
            <p class="text-gray-600 text-sm mt-2">₹${product.wholesalePrice * product.minOrderQuantity}</p>
          </div>
        </a>
      `;
      container.appendChild(card);
    });
  }
  
  
  async function addToCart() {
  	const quantityInput = document.getElementById('numberInput');
  	const isUserLoggedIn = document.getElementById('user-logged-in') !== null;

  	const productPostId = parseInt(document.getElementById("productPostId").value);
  	const productName = document.getElementById("productName").innerText;
  	const quantity = parseInt(quantityInput.value);
  	const wholesalePrice = parseInt(document.getElementById("wholesalePrice").innerText);
  	const retailPrice = parseInt(document.getElementById("retailPrice").innerText);
  	const minOrderQuantity = parseInt(document.getElementById("minOrderQuantity").innerText);
  	const category = document.getElementById("productCategoryName").innerText;
  	const brand = document.getElementById("brandName").innerText;
  	const availableLots = parseInt(document.getElementById("availableLots").innerText);
  	const base64Image = document.getElementById('mainProductImage').src;

  	const product = {
  		productPostId,
  		productName,
  		quantity,
  		wholesalePrice,
  		retailPrice,
  		minOrderQuantity,
  		category,
  		brand,
  		availableLots,
  		image: base64Image,
  		addedAt: new Date().toISOString()
  	};

  	if (!isUserLoggedIn) {
  		// Guest user: Save to localStorage
  		let cart = JSON.parse(localStorage.getItem('guestCart')) || [];
  		const existing = cart.find(item => item.productPostId === productPostId);
  		if (existing) {
  			existing.quantity += quantity;
  		} else {
  			cart.push(product);
  		}
  		localStorage.setItem('guestCart', JSON.stringify(cart));
  		updateCartCount();
  	} else {
  		// Logged-in user: Save to DB via backend
  		try {
  			const response = await fetch("/home/cart/add", {
  				method: "POST",
  				headers: {
  					'Content-Type': 'application/json',
  					[csrfHeader]: csrfToken
  				},
  				body: JSON.stringify({
  					productPostId: product.productPostId,
  					quantity: product.quantity,
  					addedAt: product.addedAt
  				})
  			});

  			if (!response.ok) {
  				const errorMsg = await response.text();
  				console.error("Server error:", errorMsg);
  				alert("Failed to add product to cart.");
  				return;
  			}

  			updateCartCount();
  		} catch (error) {
  			console.error("Error adding to cart:", error);
  			alert("An error occurred. Please try again.");
  		}
  	}
  }


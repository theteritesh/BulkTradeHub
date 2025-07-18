let currentPage = 0;
const container = document.getElementById('product-container');
const prevBtn = document.getElementById('prev-btn');
const nextBtn = document.getElementById('next-btn');

window.onload=function(){
	loadProducts(currentPage);
};

async function loadProducts(page) {
    const res = await fetch(`/home/productPost?page=${page}`);
    const data = await res.json();

    container.innerHTML = '';
	
    data.products.forEach(product => {
        const card = document.createElement('div');
        card.className = "w-full md:w-1/4 p-4";

		card.innerHTML = `
		    <a href="/home/productDisplay/${product.id}" class="block">
		        <div class="bg-gray-100 p-4 rounded-lg shadow-lg text-center">
		            <img src="${product.imageData || ''}" alt="Product Image" class="card-img rounded-lg" />
		            <h3 class="text-gray-800 text-lg font-bold mt-2">${product.productName}</h3>
		            <p class="text-gray-600 text-sm mt-2">₹${product.wholesalePrice * product.minOrderQuantity}</p>
		        </div>
		    </a>
		`;
        container.appendChild(card);
    });

    currentPage = data.currentPage;
	prevBtn.style.visibility = currentPage === 0 ? 'hidden' : 'visible';
	nextBtn.style.visibility = currentPage >= data.totalPages - 1 ? 'hidden' : 'visible';
	
}


prevBtn.addEventListener('click', () => {
    if (currentPage > 0) loadProducts(currentPage - 1);
});

nextBtn.addEventListener('click', () => {
    loadProducts(currentPage + 1);
});
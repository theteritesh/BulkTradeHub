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
 
 
 function addToCart(productPostId) {
	const quntity=document.getElementById('numberInput').value;
	 fetch(`/allPermit/addProductToCart?productPostId=${productPostId}&quntity=${quntity}`, {
	      method: 'GET',
	      headers: {
	          'Accept': 'application/json'
	      }
	  })
	  .then(async response => {
	      if (!response.ok) {
	          const text = await response.text();
              throw new Error(text);
	      }
	      return response.json();
	  })
	  .then(data => {
	      console.log("Product added to cart successfully.",data);
	  })
	  .catch(error => {
	      console.error("Error: " + error.message);
	  });
  }
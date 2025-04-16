let data=null;

window.onload = async function () {
	await loadRetailDashboardOverview();
	await loadMonthlyProductTrendChart();
	const totalProduct = document.getElementById("totalProduct");
	const postedProduct = document.getElementById("postedProduct");
	const lowStock = document.getElementById("lowStock");
	const outOfStock = document.getElementById("outOfStock");
	
	totalProduct.innerText = data.inStock || 0;
	postedProduct.innerText = data.totalPosted || 0;
	lowStock.innerText = data.lowStock || 0;
	outOfStock.innerText = data.outOfStock || 0; 
};

let productStatusChartInstance = null;

async function loadRetailDashboardOverview() {
	try {
		const response = await fetch('/retailShop/retailDashboardOverview');

		if (!response.ok) {
			throw new Error(`HTTP error! Status: ${response.status}`);
		}

		const text = await response.text();
		console.log(text);
		if (!text) {
			throw new Error('Empty response from server');
		}

		data = JSON.parse(text);
		const chartData = {
			labels: ['In Stock', 'Posted', 'Low Stock', 'Out of Stock'],
			datasets: [{
				data: [
					data.inStock || 0,
					data.totalPosted || 0,
					data.lowStock || 0,
					data.outOfStock || 0
				],
				backgroundColor: [
					'#4e73df',
					'#1cc88a',
					'#f6c23e',
					'#e74a3b'
				]
			}]
		};

		const statusCtx = document.getElementById('productStatusChart').getContext('2d');
		if (productStatusChartInstance) {
			productStatusChartInstance.destroy();
		}
		
		productStatusChartInstance = new Chart(statusCtx, {
			type: 'pie',
			data: chartData,
			options: {
				responsive: true,
				maintainAspectRatio: false,
				plugins: {
					legend: {
						position: 'bottom'
					}
				}
			}
		});
	} catch (error) {
		console.error('Error fetching retail dashboard overview:', error);
	}
}


let productTrendChartInstance = null;

async function loadMonthlyProductTrendChart() {
	try {
		const response = await fetch('/retailShop/monthlyProductCount');
		if (!response.ok) {
			throw new Error(`HTTP error! Status: ${response.status}`);
		}

		const monthlyData = await response.json();
		console.log('Monthly Product Data:', monthlyData);
		
		const allMonths = ['January', 'February', 'March', 'April', 'May', 'June', 
		                   'July', 'August', 'September', 'October', 'November', 'December'];
		const monthlyCounts = Array(12).fill(0);

		
		monthlyData.forEach(item => {
			const index = allMonths.indexOf(item.month);
			if (index !== -1) {
				monthlyCounts[index] = item.count;
			}
		});

		
		const trendCtx = document.getElementById('productTrendChart').getContext('2d');
		if (productTrendChartInstance) {
			productTrendChartInstance.destroy();
		}

		productTrendChartInstance = new Chart(trendCtx, {
			type: 'line',
			data: {
				labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 
				         'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
				datasets: [{
					label: 'Products Added',
					data: monthlyCounts,
					borderColor: '#4e73df',
					tension: 0.3,
					fill: false
				}]
			},
			options: {
				responsive: true,
				maintainAspectRatio: false,
				scales: {
					y: {
						beginAtZero: true
					}
				},
				plugins: {
					legend: {
						position: 'bottom'
					}
				}
			}
		});
	} catch (error) {
		console.error('Error fetching monthly product trend:', error);
	}
}


// Function to refresh data
function refreshData() {

	// Call this when DOM is ready
	loadRetailDashboardOverview();
	// Add animation to refresh button
	const refreshBtn = document.getElementById("refreshBtn");
	refreshBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Refreshing...';
	refreshBtn.disabled = true;

	// Simulate data refresh (would be an API call in real application)
	setTimeout(() => {
		refreshBtn.innerHTML = '<i class="fas fa-sync-alt me-2"></i>Refresh Data';
		refreshBtn.disabled = false;

		// Update random numbers for demonstration
		document.querySelectorAll('.stat-card .h3').forEach(card => {
			const currentValue = parseInt(card.textContent);
			const newValue = currentValue + Math.floor(Math.random() * 10) - 5;
			card.textContent = Math.max(0, newValue);
		});
	}, 1500);
}

// Product Status Distribution Chart
const statusCtx = document.getElementById('productStatusChart').getContext('2d');
new Chart(statusCtx, {
	type: 'pie',
	data: {
		labels: ['In Stock', 'Posted', 'Low Stock', 'Out of Stock'],
		datasets: [{
			data: [65, 150, 25, 10],
			backgroundColor: [
				'#4e73df',
				'#1cc88a',
				'#f6c23e',
				'#e74a3b'
			]
		}]
	},
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

// Monthly Product Addition Trend Chart
const trendCtx = document.getElementById('productTrendChart').getContext('2d');
new Chart(trendCtx, {
	type: 'line',
	data: {
		labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
		datasets: [{
			label: 'Products Added',
			data: [65, 78, 90, 115, 140, 150],
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

// Declare chart instance outside the function to maintain reference
/*let productStatusChartInstance = null;

async function loadRetailDashboardOverview() {
	try {
		const response = await fetch('/retailShop/retailDashboardOverview');

		if (!response.ok) {
			throw new Error(`HTTP error! Status: ${response.status}`);
		}

		// Ensure there's content before parsing JSON
		const text = await response.text();
		if (!text) {
			throw new Error('Empty response from server');
		}

		const data = JSON.parse(text);

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

		// ✅ Destroy existing chart instance if it exists
		if (productStatusChartInstance) {
			productStatusChartInstance.destroy();
		}

		// ✅ Create new chart and store instance
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
}*/



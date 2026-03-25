document.addEventListener("DOMContentLoaded", function () {
  const API_URL = contextPath + "api/reports/dashboard/departments";

  let countChartInstance = null;
  let valueChartInstance = null;

  // DOM Elements
  const totalAssetCountEl = document.getElementById("totalAssetCount");
  const totalAssetValueEl = document.getElementById("totalAssetValue");
  const tableBody = document.getElementById("tableBody");
  const loadingSpinner = document.getElementById("loadingSpinner");
  const dataTable = document.getElementById("dataTable");

  function loadDashboardData() {
    // Show loading
    loadingSpinner.style.display = "block";
    dataTable.style.display = "none";

    fetch(API_URL)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Lỗi khi tải dữ liệu từ server.");
        }
        return response.json();
      })
      .then((data) => {
        console.log("Dashboard data:", data);
        renderDashboard(data);
      })
      .catch((error) => {
        console.error("Error fetching dashboard data:", error);
        alert("Không thể tải dữ liệu báo cáo: " + error.message);
      })
      .finally(() => {
        // Hide loading
        loadingSpinner.style.display = "none";
        dataTable.style.display = "table";
      });
  }

  function renderDashboard(data) {
    if (!data || data.length === 0) {
      tableBody.innerHTML = `<tr><td colspan="4" class="text-center text-muted">Không có dữ liệu</td></tr>`;
      return;
    }

    // Sort data by totalValue descending
    data.sort((a, b) => b.totalValue - a.totalValue);

    let sumCount = 0;
    let sumValue = 0;

    data.forEach((item) => {
      sumCount += item.assetCount;
      sumValue += item.totalValue;
    });

    // Update Summary Header
    totalAssetCountEl.textContent = sumCount.toLocaleString("vi-VN");
    totalAssetValueEl.textContent = formatCurrency(sumValue);

    // Prepare chart data
    const labels = [];
    const countData = [];
    const valueData = [];

    // Populate Table
    tableBody.innerHTML = "";
    data.forEach((item) => {
      const { departmentName, assetCount, totalValue } = item;
      const deptLabel = departmentName || "Chưa phân bổ";

      labels.push(deptLabel);
      countData.push(assetCount);
      valueData.push(totalValue);

      const percent =
        sumValue > 0 ? ((totalValue / sumValue) * 100).toFixed(2) : 0;

      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td class="fw-semibold">${deptLabel}</td>
        <td class="text-center">${assetCount}</td>
        <td class="text-end text-primary fw-bold">${formatCurrency(totalValue)}</td>
        <td class="text-center">
            <div class="progress" style="height: 20px;">
              <div class="progress-bar bg-success" role="progressbar" style="width: ${percent}%;" aria-valuenow="${percent}" aria-valuemin="0" aria-valuemax="100">${percent}%</div>
            </div>
        </td>
      `;
      tableBody.appendChild(tr);
    });

    // Render Charts
    renderCharts(labels, countData, valueData);
  }

  function renderCharts(labels, countData, valueData) {
    // Colors
    const bgColors = [
      "rgba(54, 162, 235, 0.7)",
      "rgba(255, 99, 132, 0.7)",
      "rgba(255, 206, 86, 0.7)",
      "rgba(75, 192, 192, 0.7)",
      "rgba(153, 102, 255, 0.7)",
      "rgba(255, 159, 64, 0.7)",
      "rgba(199, 199, 199, 0.7)",
      "rgba(83, 102, 255, 0.7)",
      "rgba(40, 159, 64, 0.7)",
      "rgba(210, 199, 199, 0.7)",
    ];

    const borderColors = bgColors.map((color) => color.replace("0.7", "1"));

    // 1. Chart: Count (Pie Chart)
    const ctxCount = document.getElementById("countChart").getContext("2d");
    if (countChartInstance) countChartInstance.destroy();

    countChartInstance = new Chart(ctxCount, {
      type: "pie",
      data: {
        labels: labels,
        datasets: [
          {
            label: "Số lượng tài sản",
            data: countData,
            backgroundColor: bgColors,
            borderColor: borderColors,
            borderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { position: "right" },
        },
      },
    });

    // 2. Chart: Value (Bar Chart)
    const ctxValue = document.getElementById("valueChart").getContext("2d");
    if (valueChartInstance) valueChartInstance.destroy();

    valueChartInstance = new Chart(ctxValue, {
      type: "bar",
      data: {
        labels: labels,
        datasets: [
          {
            label: "Giá trị (VNĐ)",
            data: valueData,
            backgroundColor: bgColors.slice(0, 1), // Using primary color
            borderColor: borderColors.slice(0, 1),
            borderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false },
          tooltip: {
            callbacks: {
              label: function (context) {
                return formatCurrency(context.raw);
              },
            },
          },
        },
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              callback: function (value) {
                if (value >= 1e9) return (value / 1e9).toFixed(1) + " Tỷ";
                if (value >= 1e6) return (value / 1e6).toFixed(1) + " Tr";
                return value;
              },
            },
          },
        },
      },
    });
  }

  function formatCurrency(value) {
    if (!value && value !== 0) return "0 đ";
    return (
      new Intl.NumberFormat("vi-VN", {
        minimumFractionDigits: 0,
        maximumFractionDigits: 0,
      }).format(value) + " đ"
    );
  }

  // Khởi chạy khi load xong DOM
  loadDashboardData();
});

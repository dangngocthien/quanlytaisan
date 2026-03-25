/**
 * Depreciation Report Module
 * Professional Grade JavaScript
 *
 * Architecture: Class-based with Fetch API, Chart.js visualization
 * Pattern: Following asset.js structure (constructor, event delegation)
 * Author: Senior Spring Boot Developer
 */

class DepreciationReportManager {
  static CONFIG = {
    API_BASE: contextPath + "api/reports",
    MESSAGES: {
      FILTER_ERROR: "❌ Lỗi lọc dữ liệu.",
      NETWORK_ERROR: "❌ Lỗi kết nối mạng.",
      NO_DATA: "❌ Không tìm thấy dữ liệu.",
    },
  };

  constructor() {
    this.reportData = [];
    this.filteredData = [];
    this.allAssets = [];
    this.chart = null;
    this.init();
  }

  init() {
    this.attachEventListeners();
    this.loadAssets();
    this.setDefaultYear();
  }

  attachEventListeners() {
    document
      .getElementById("btnFilter")
      .addEventListener("click", () => this.handleFilter());
    document
      .getElementById("btnReset")
      .addEventListener("click", () => this.handleReset());
    document
      .getElementById("filterYear")
      .addEventListener("change", () => this.handleFilter());
  }

  setDefaultYear() {
    const year = new Date().getFullYear();
    document.getElementById("filterYear").value = year;
    this.loadReportData(year);
  }

  /**
   * Load all assets for dropdown
   */
  async loadAssets() {
    try {
      const response = await fetch(
        `${DepreciationReportManager.CONFIG.API_BASE}/all-assets`,
      );
      if (!response.ok) throw new Error("Không thể tải danh sách tài sản");

      const data = await response.json();
      this.allAssets = data.data || [];
      this.populateAssetFilter();
    } catch (error) {
      console.error("Load assets error:", error);
    }
  }

  /**
   * Populate asset dropdown
   */
  populateAssetFilter() {
    const filterSelect = document.getElementById("filterAsset");
    this.allAssets.forEach((asset) => {
      const option = document.createElement("option");
      option.value = asset.id;
      option.textContent = `${asset.assetCode} - ${asset.name}`;
      filterSelect.appendChild(option);
    });
  }

  /**
   * Load report data by year
   */
  async loadReportData(year) {
    try {
      this.showLoading(true);
      const response = await fetch(
        `${DepreciationReportManager.CONFIG.API_BASE}/depreciation-summary/${year}`,
      );

      if (!response.ok) throw new Error("Không thể tải báo cáo");

      const result = await response.json();
      this.reportData = this.processReportData(result.data || {});
      this.filteredData = [...this.reportData];

      this.renderStats();
      this.renderChart();
      this.renderTable();
    } catch (error) {
      alert(DepreciationReportManager.CONFIG.MESSAGES.FILTER_ERROR);
      console.error("Error:", error);
    } finally {
      this.showLoading(false);
    }
  }

  /**
   * Process report data from API
   */
  processReportData(data) {
    const processed = [];
    for (const [month, details] of Object.entries(data)) {
      if (details && typeof details === "object") {
        const items = details.records || [];
        items.forEach((item) => {
          processed.push({
            month: parseInt(month),
            assetId: item.assetId,
            assetCode: item.assetCode || "N/A",
            assetName: item.assetName || "N/A",
            depreciationAmount: parseFloat(item.depreciationAmount) || 0,
            remainingValue: parseFloat(item.remainingValue) || 0,
            notes: item.notes || "",
          });
        });
      }
    }
    return processed;
  }

  /**
   * Handle Filter Action
   */
  handleFilter() {
    const month = document.getElementById("filterMonth").value;
    const assetId = document.getElementById("filterAsset").value;
    const year = document.getElementById("filterYear").value;

    if (!year) {
      alert("⚠️ Vui lòng chọn năm");
      return;
    }

    this.filteredData = this.reportData.filter((item) => {
      const monthMatch = !month || item.month === parseInt(month);
      const assetMatch = !assetId || item.assetId === parseInt(assetId);
      return monthMatch && assetMatch;
    });

    this.renderStats();
    this.renderChart();
    this.renderTable();
  }

  /**
   * Handle Reset Action
   */
  handleReset() {
    document.getElementById("filterMonth").value = "";
    document.getElementById("filterAsset").value = "";
    this.filteredData = [...this.reportData];
    this.renderStats();
    this.renderChart();
    this.renderTable();
  }

  /**
   * Render Statistics Cards
   */
  renderStats() {
    const dataToUse = this.filteredData;

    const totalRecords = dataToUse.length;
    const totalDepreciation = dataToUse.reduce(
      (sum, item) => sum + item.depreciationAmount,
      0,
    );
    const totalRemaining = dataToUse.reduce(
      (sum, item) => sum + item.remainingValue,
      0,
    );
    const uniqueAssets = new Set(dataToUse.map((item) => item.assetId)).size;

    const statsHTML = `
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="stat-icon"><i class="fas fa-list"></i></div>
                    <div class="stat-content">
                        <h6>Tổng bản ghi</h6>
                        <h3>${totalRecords}</h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="stat-icon"><i class="fas fa-chart-line"></i></div>
                    <div class="stat-content">
                        <h6>Tổng khấu hao</h6>
                        <h3>${this.formatCurrency(totalDepreciation)}</h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="stat-icon"><i class="fas fa-wallet"></i></div>
                    <div class="stat-content">
                        <h6>Giá trị còn lại</h6>
                        <h3>${this.formatCurrency(totalRemaining)}</h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="stat-icon"><i class="fas fa-cube"></i></div>
                    <div class="stat-content">
                        <h6>Tài sản khác nhau</h6>
                        <h3>${uniqueAssets}</h3>
                    </div>
                </div>
            </div>
        `;

    document.getElementById("statsContainer").innerHTML = statsHTML;
  }

  /**
   * Render Chart
   */
  renderChart() {
    const dataToUse = this.filteredData;

    const monthlyData = {};
    for (let i = 1; i <= 12; i++) {
      monthlyData[i] = 0;
    }

    dataToUse.forEach((item) => {
      monthlyData[item.month] =
        (monthlyData[item.month] || 0) + item.depreciationAmount;
    });

    const labels = Array.from({ length: 12 }, (_, i) => `Tháng ${i + 1}`);
    const values = Array.from(
      { length: 12 },
      (_, i) => monthlyData[i + 1] || 0,
    );

    const canvas = document.getElementById("depreciationChart");
    if (this.chart) this.chart.destroy();

    this.chart = new Chart(canvas, {
      type: "bar",
      data: {
        labels: labels,
        datasets: [
          {
            label: "Khấu hao (VNĐ)",
            data: values,
            backgroundColor: "#667eea",
            borderColor: "#764ba2",
            borderWidth: 2,
            borderRadius: 4,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { display: true, position: "top" } },
        scales: {
          y: {
            beginAtZero: true,
            ticks: { callback: (value) => this.formatCurrency(value) },
          },
        },
      },
    });
  }

  /**
   * Render Data Table
   */
  renderTable() {
    const dataToUse = this.filteredData;

    if (dataToUse.length === 0) {
      document.getElementById("dataTable").style.display = "none";
      document.getElementById("emptyState").style.display = "block";
    } else {
      document.getElementById("emptyState").style.display = "none";
      document.getElementById("dataTable").style.display = "table";

      const tbody = document.getElementById("tableBody");
      tbody.innerHTML = dataToUse
        .map(
          (item, index) => `
                <tr>
                    <td class="text-center fw-semibold text-muted">${index + 1}</td>
                    <td><span class="badge bg-info">${item.assetCode}</span></td>
                    <td>${item.assetName}</td>
                    <td>Tháng ${item.month}</td>
                    <td class="text-end">${this.formatCurrency(item.depreciationAmount)}</td>
                    <td class="text-end">${this.formatCurrency(item.remainingValue)}</td>
                    <td>${item.notes}</td>
                </tr>
            `,
        )
        .join("");
    }

    document.getElementById("totalRecords").textContent = dataToUse.length;
  }

  /**
   * Format currency VND
   */
  formatCurrency(value) {
    if (typeof value !== "number") value = parseFloat(value) || 0;
    return new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
      minimumFractionDigits: 0,
      maximumFractionDigits: 0,
    }).format(value);
  }

  /**
   * Show Loading Spinner
   */
  showLoading(show) {
    const spinner = document.getElementById("loadingSpinner");
    if (show) {
      spinner.style.display = "block";
      document.getElementById("dataTable").style.display = "none";
      document.getElementById("emptyState").style.display = "none";
    } else {
      spinner.style.display = "none";
    }
  }
}

/**
 * Initialize on DOM ready
 */
document.addEventListener("DOMContentLoaded", () => {
  new DepreciationReportManager();
});

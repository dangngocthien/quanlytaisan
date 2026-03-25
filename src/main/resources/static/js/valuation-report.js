/**
 * Valuation Report Module
 * Professional Grade JavaScript
 *
 * Architecture: Class-based with Fetch API, Chart.js visualization
 * Pattern: Following asset.js structure (constructor, event delegation)
 * Author: Senior Spring Boot Developer
 */

class ValuationReportManager {
  static CONFIG = {
    API_BASE: contextPath + "api/reports",
    DEPRECIATION_THRESHOLDS: {
      high: 60,
      medium: 30,
    },
    MESSAGES: {
      FILTER_ERROR: "❌ Lỗi lọc dữ liệu.",
      NETWORK_ERROR: "❌ Lỗi kết nối mạng.",
      NO_DATA: "❌ Không tìm thấy dữ liệu.",
    },
  };

  constructor() {
    this.allAssets = [];
    this.filteredAssets = [];
    this.departments = [];
    this.chart = null;
    this.totalStats = {};
    this.init();
  }

  init() {
    this.attachEventListeners();
    this.loadInitialData();
  }

  attachEventListeners() {
    document
      .getElementById("btnFilter")
      .addEventListener("click", () => this.handleFilter());
    document
      .getElementById("btnReset")
      .addEventListener("click", () => this.handleReset());
  }

  /**
   * Load Initial Data
   */
  async loadInitialData() {
    try {
      this.showLoading(true);
      const response = await fetch(
        `${ValuationReportManager.CONFIG.API_BASE}/all-assets`,
      );
      if (!response.ok) throw new Error("Không thể tải danh sách tài sản");

      const data = await response.json();
      this.allAssets = data.data || [];

      this.extractDepartments();
      this.populateDepartmentFilter();

      this.filteredAssets = [...this.allAssets];
      await this.loadValuations();
    } catch (error) {
      alert(ValuationReportManager.CONFIG.MESSAGES.FILTER_ERROR);
      console.error("Error:", error);
    } finally {
      this.showLoading(false);
    }
  }

  /**
   * Extract unique departments
   */
  extractDepartments() {
    const deptMap = new Map();
    this.allAssets.forEach((asset) => {
      if (
        asset.currentDepartmentId &&
        !deptMap.has(asset.currentDepartmentId)
      ) {
        deptMap.set(asset.currentDepartmentId, {
          id: asset.currentDepartmentId,
          name: asset.currentDepartmentName,
        });
      }
    });
    this.departments = Array.from(deptMap.values());
  }

  /**
   * Populate department dropdown
   */
  populateDepartmentFilter() {
    const filterSelect = document.getElementById("filterDepartment");
    this.departments.forEach((dept) => {
      const option = document.createElement("option");
      option.value = dept.id;
      option.textContent = dept.name;
      filterSelect.appendChild(option);
    });
  }

  /**
   * Load valuations for filtered assets
   */
  async loadValuations() {
    try {
      const valuationPromises = this.filteredAssets.map((asset) =>
        this.loadAssetValuation(asset),
      );
      await Promise.all(valuationPromises);

      this.calculateTotalStats();
      this.renderSummaryStats();
      this.renderChart();
      this.renderValuationCards();
    } catch (error) {
      alert(ValuationReportManager.CONFIG.MESSAGES.FILTER_ERROR);
      console.error("Error:", error);
    }
  }

  /**
   * Load valuation for single asset
   */
  async loadAssetValuation(asset) {
    try {
      const response = await fetch(
        `${ValuationReportManager.CONFIG.API_BASE}/valuation/${asset.id}`,
      );
      if (!response.ok) return null;

      const result = await response.json();
      // Bỏ .data vì response là Map<String, Object> trả về trực tiếp các field
      const valuation = result || {};

      Object.assign(asset, {
        purchasePrice: parseFloat(valuation.purchasePrice) || 0,
        accumulatedDepreciation:
          parseFloat(valuation.accumulatedDepreciation) || 0,
        bookValue: parseFloat(valuation.bookValue) || 0,
        depreciationRate: parseFloat(valuation.depreciationRate) || 0,
      });
    } catch (error) {
      console.warn("Could not load valuation for asset", asset.id);
    }
  }

  /**
   * Handle Filter Action
   */
  async handleFilter() {
    const departmentId = document.getElementById("filterDepartment").value;

    this.filteredAssets = this.allAssets.filter((asset) => {
      if (!departmentId) return true;
      return asset.currentDepartmentId === parseInt(departmentId);
    });

    await this.loadValuations();
  }

  /**
   * Handle Reset Action
   */
  async handleReset() {
    document.getElementById("filterDepartment").value = "";
    this.filteredAssets = [...this.allAssets];
    await this.loadValuations();
  }

  /**
   * Calculate total statistics
   */
  calculateTotalStats() {
    this.totalStats = {
      purchasePrice: this.filteredAssets.reduce(
        (sum, a) => sum + (a.purchasePrice || 0),
        0,
      ),
      accumulatedDepreciation: this.filteredAssets.reduce(
        (sum, a) => sum + (a.accumulatedDepreciation || 0),
        0,
      ),
      bookValue: this.filteredAssets.reduce(
        (sum, a) => sum + (a.bookValue || 0),
        0,
      ),
      count: this.filteredAssets.length,
    };
  }

  /**
   * Render Summary Statistics Cards
   */
  renderSummaryStats() {
    const stats = this.totalStats;

    const statsHTML = `
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="stat-icon"><i class="fas fa-shopping-cart"></i></div>
                    <div class="stat-content">
                        <h6>Tổng giá mua</h6>
                        <h3>${this.formatCurrency(stats.purchasePrice)}</h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="stat-icon"><i class="fas fa-chart-line"></i></div>
                    <div class="stat-content">
                        <h6>Khấu hao lũy tích</h6>
                        <h3>${this.formatCurrency(stats.accumulatedDepreciation)}</h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="stat-icon"><i class="fas fa-wallet"></i></div>
                    <div class="stat-content">
                        <h6>Giá trị sổ sách</h6>
                        <h3>${this.formatCurrency(stats.bookValue)}</h3>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="stat-icon"><i class="fas fa-cube"></i></div>
                    <div class="stat-content">
                        <h6>Tổng số tài sản</h6>
                        <h3>${stats.count}</h3>
                    </div>
                </div>
            </div>
        `;

    document.getElementById("summaryStatsContainer").innerHTML = statsHTML;
  }

  /**
   * Render Chart
   */
  renderChart() {
    const stats = this.totalStats;
    const canvas = document.getElementById("valuationChart");

    if (this.chart) this.chart.destroy();

    this.chart = new Chart(canvas, {
      type: "doughnut",
      data: {
        labels: ["Giá trị sổ sách", "Khấu hao lũy tích"],
        datasets: [
          {
            data: [stats.bookValue, stats.accumulatedDepreciation],
            backgroundColor: ["#10b981", "#ef4444"],
            borderColor: ["#ffffff", "#ffffff"],
            borderWidth: 2,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { position: "bottom" },
          tooltip: {
            callbacks: {
              label: (context) => {
                const value = context.parsed;
                const total = stats.bookValue + stats.accumulatedDepreciation;
                const percentage =
                  total > 0 ? ((value / total) * 100).toFixed(2) : 0;
                return `${this.formatCurrency(value)} (${percentage}%)`;
              },
            },
          },
        },
      },
    });
  }

  /**
   * Render Valuation Cards
   */
  renderValuationCards() {
    if (this.filteredAssets.length === 0) {
      document.getElementById("emptyState").style.display = "block";
      document.getElementById("valuationCardsContainer").innerHTML = "";
    } else {
      document.getElementById("emptyState").style.display = "none";
      const cardsHTML = this.filteredAssets
        .map((asset) => this.createValuationCard(asset))
        .join("");
      document.getElementById("valuationCardsContainer").innerHTML = cardsHTML;
    }

    document.getElementById("totalAssets").textContent =
      this.filteredAssets.length;
  }

  /**
   * Create single valuation card
   */
  createValuationCard(asset) {
    const depRate = asset.depreciationRate || 0;
    const rateClass = this.getDepreciationRateClass(depRate);
    const statusBadgeClass = this.getStatusBadgeClass(asset.status);

    return `
            <div class="col-lg-4 col-md-6">
                <div class="valuation-card">
                    <div class="valuation-card-header">
                        <span class="badge bg-info">${asset.assetCode}</span>
                        <span class="badge ${statusBadgeClass}">${this.getStatusLabel(asset.status)}</span>
                    </div>
                    <div class="valuation-card-body">
                        <h6 class="fw-bold mb-3">${asset.name}</h6>
                        
                        <div class="row mb-2">
                            <div class="col-5 text-muted"><small>Danh mục:</small></div>
                            <div class="col-7"><small class="fw-semibold">${asset.categoryName || "N/A"}</small></div>
                        </div>
                        
                        <div class="row mb-2">
                            <div class="col-5 text-muted"><small>Ngày mua:</small></div>
                            <div class="col-7"><small>${this.formatDate(asset.purchaseDate)}</small></div>
                        </div>

                        <hr class="my-2" />
                        
                        <div class="row mb-2">
                            <div class="col-8 text-muted"><small>Giá mua:</small></div>
                            <div class="col-4 text-end"><small class="fw-semibold">${this.formatCurrency(asset.purchasePrice)}</small></div>
                        </div>
                        
                        <div class="row mb-2">
                            <div class="col-8 text-muted"><small>Khấu hao lũy tích:</small></div>
                            <div class="col-4 text-end"><small class="fw-semibold text-danger">${this.formatCurrency(asset.accumulatedDepreciation)}</small></div>
                        </div>
                        
                        <div class="row mb-3 p-2 valuation-highlight">
                            <div class="col-8"><strong>Giá trị sổ sách:</strong></div>
                            <div class="col-4 text-end"><strong class="text-success">${this.formatCurrency(asset.bookValue)}</strong></div>
                        </div>
                        
                        <div class="row">
                            <div class="col-8 text-muted"><small>Tỷ lệ khấu hao:</small></div>
                            <div class="col-4 text-end">
                                <span class="badge ${rateClass}">${depRate.toFixed(2)}%</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;
  }

  /**
   * Get depreciation rate badge class
   */
  getDepreciationRateClass(rate) {
    if (rate >= ValuationReportManager.CONFIG.DEPRECIATION_THRESHOLDS.high) {
      return "bg-danger";
    } else if (
      rate >= ValuationReportManager.CONFIG.DEPRECIATION_THRESHOLDS.medium
    ) {
      return "bg-warning";
    } else {
      return "bg-success";
    }
  }

  /**
   * Get status badge class
   */
  getStatusBadgeClass(status) {
    const statusMap = {
      DANG_SUDUNG: "bg-success",
      TRONG_KHO: "bg-secondary",
      BAO_TRI: "bg-warning",
      THANH_LY: "bg-danger",
    };
    return statusMap[status] || "bg-info";
  }

  /**
   * Get status label
   */
  getStatusLabel(status) {
    const statusMap = {
      DANG_SUDUNG: "Đang sử dụng",
      TRONG_KHO: "Trong kho",
      BAO_TRI: "Bảo trì",
      THANH_LY: "Thanh lý",
    };
    return statusMap[status] || status;
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
   * Format date
   */
  formatDate(dateString) {
    if (!dateString) return "N/A";
    try {
      const date = new Date(dateString);
      return date.toLocaleDateString("vi-VN");
    } catch (e) {
      return dateString;
    }
  }

  /**
   * Show Loading Spinner
   */
  showLoading(show) {
    const spinner = document.getElementById("loadingSpinner");
    if (show) {
      spinner.style.display = "block";
      document.getElementById("valuationCardsContainer").innerHTML = "";
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
  new ValuationReportManager();
});

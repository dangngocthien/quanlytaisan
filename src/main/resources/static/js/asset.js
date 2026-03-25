/**
 * Asset Management Module - Professional Grade JavaScript
 * ========================================================
 * Manages CRUD operations for Asset management with proper
 * error handling, validation, and event delegation.
 *
 * Architecture: Single Page Application Pattern with Event Delegation
 * Author: Senior Spring Boot Developer
 * Last Updated: 2026-03-25
 */

class AssetManager {
  /**
   * Configuration & Constants
   */
  static CONFIG = {
    API_BASE: contextPath + "api/assets",
    MODAL_ID: "modalAsset",
    FORM_ID: "formAsset",
    VALIDATION: {
      minPriceValue: 0,
      maxStringLength: 500,
    },
    MESSAGES: {
      ADD_SUCCESS: "✅ Thêm tài sản thành công!",
      UPDATE_SUCCESS: "✅ Cập nhật tài sản thành công!",
      DELETE_SUCCESS: "✅ Xóa tài sản thành công!",
      VALIDATE_ERROR: "⚠️ Vui lòng nhập đầy đủ các trường bắt buộc (*)",
      NETWORK_ERROR: "❌ Lỗi kết nối mạng. Vui lòng kiểm tra lại.",
      SERVER_ERROR: "❌ Lỗi server. Vui lòng thử lại sau.",
      DELETE_CONFIRM: "❓ Xóa tài sản này?\nThao tác này không thể hoàn tác!",
    },
  };

  /**
   * Instance Variables
   */
  constructor() {
    this.modal = null;
    this.formData = null;
    this.isEditMode = false;

    this.init();
  }

  /**
   * Initialize the module
   */
  init() {
    this.setupModal();
    this.attachEventListeners();
    this.logInitialization();
  }

  /**
   * Setup Bootstrap Modal instance
   */
  setupModal() {
    const modalElement = document.getElementById(AssetManager.CONFIG.MODAL_ID);
    if (modalElement) {
      this.modal = new bootstrap.Modal(modalElement);
    }
  }

  /**
   * Attach all event listeners
   * Using event delegation for better performance
   */
  attachEventListeners() {
    // Button: Add Asset
    document.addEventListener("click", (e) => {
      if (e.target.closest("#btnAddAsset")) {
        this.handleAddAsset();
      }
    });

    // Button: Save Asset (Both Add & Update)
    document.addEventListener("click", (e) => {
      if (e.target.closest("#btnSaveAsset")) {
        this.handleSaveAsset();
      }
    });

    // Button: Edit Asset - Event delegation on parent
    document.addEventListener("click", (e) => {
      const editBtn = e.target.closest(".btn-edit-asset");
      if (editBtn) {
        const assetId = editBtn.getAttribute("data-asset-id");
        this.handleEditAsset(assetId);
      }
    });

    // Button: Delete Asset
    document.addEventListener("click", (e) => {
      const deleteBtn = e.target.closest(".btn-delete-asset");
      if (deleteBtn) {
        const assetId = deleteBtn.getAttribute("data-asset-id");
        this.handleDeleteAsset(assetId);
      }
    });

    // Row selection (highlight)
    document.addEventListener("click", (e) => {
      const row = e.target.closest(".asset-row");
      if (row) {
        this.selectAssetRow(row);
      }
    });
  }

  /**
   * HANDLER: Add new asset
   */
  handleAddAsset() {
    console.log("[ADD] Opening modal for new asset");

    this.isEditMode = false;
    this.resetForm();

    document.getElementById("modalAssetTitle").innerHTML =
      '<i class="fas fa-plus-circle"></i> Thêm Tài Sản Mới';

    if (this.modal) {
      this.modal.show();
    }
  }

  /**
   * HANDLER: Edit asset
   */
  handleEditAsset(assetId) {
    console.log(`[EDIT] Loading asset ID: ${assetId}`);

    if (!assetId) {
      this.showError("Không tìm thấy ID tài sản");
      return;
    }

    this.isEditMode = true;
    this.loadAssetData(assetId);
  }

  /**
   * HANDLER: Delete asset
   */
  handleDeleteAsset(assetId) {
    if (!assetId) {
      this.showError("Không tìm thấy ID tài sản");
      return;
    }

    if (!confirm(AssetManager.CONFIG.MESSAGES.DELETE_CONFIRM)) {
      console.log("[DELETE] Cancelled by user");
      return;
    }

    console.log(`[DELETE] Deleting asset ID: ${assetId}`);
    this.performDelete(assetId);
  }

  /**
   * HANDLER: Save asset (both add & update)
   */
  handleSaveAsset() {
    console.log("[SAVE] Processing form submission");

    // Validate form
    if (!this.validateForm()) {
      this.showError(AssetManager.CONFIG.MESSAGES.VALIDATE_ERROR);
      return;
    }

    // Collect form data
    this.formData = this.collectFormData();

    if (this.isEditMode) {
      this.performUpdate();
    } else {
      this.performCreate();
    }
  }

  /**
   * Validate form fields using HTML5 validation
   * @returns {boolean} True if valid
   */
  validateForm() {
    const form = document.getElementById(AssetManager.CONFIG.FORM_ID);

    // HTML5 Validation API - returns false if any required field is empty or invalid
    if (!form.checkValidity()) {
      // Show native browser validation errors
      form.classList.add("was-validated");
      form.reportValidity();
      return false;
    }

    // Form is valid - all required fields are filled and correct format
    return true;
  }

  /**
   * Collect form data into object
   * @returns {Object} Form data
   */
  collectFormData() {
    return {
      assetCode: document.getElementById("assetCode").value?.trim(),
      name: document.getElementById("assetName").value?.trim(),
      categoryId: document.getElementById("categoryId").value,
      purchasePrice: document.getElementById("purchasePrice").value,
      currentValue: document.getElementById("currentValue").value || null,
      purchaseDate: document.getElementById("purchaseDate").value,
      usageStartDate: document.getElementById("usageStartDate").value || null,
      status: document.getElementById("status").value,
      warrantyProvider:
        document.getElementById("warrantyProvider").value?.trim() || null,
      warrantyExpiryDate:
        document.getElementById("warrantyExpiryDate").value || null,
      currentDepartmentId: document.getElementById("currentDepartmentId").value,
    };
  }

  /**
   * Reset form to default state
   */
  resetForm() {
    const form = document.getElementById(AssetManager.CONFIG.FORM_ID);
    form.reset();
    document.getElementById("assetId").value = "";
  }

  /**
   * Load asset data from API
   * @param {number} assetId - Asset ID
   */
  loadAssetData(assetId) {
    this.showLoading(true);

    fetch(`${AssetManager.CONFIG.API_BASE}/${assetId}`)
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
        return response.json();
      })
      .then((data) => {
        this.populateForm(data);
        document.getElementById("modalAssetTitle").innerHTML =
          '<i class="fas fa-edit"></i> Cập Nhật Tài Sản';

        if (this.modal) {
          this.modal.show();
        }
      })
      .catch((error) => {
        console.error("[LOAD] Error:", error);
        this.showError(`Không tải được dữ liệu: ${error.message}`);
      })
      .finally(() => {
        this.showLoading(false);
      });
  }

  /**
   * Populate form with asset data
   * @param {Object} data - Asset data
   */
  populateForm(data) {
    document.getElementById("assetId").value = data.id || "";
    document.getElementById("assetCode").value = data.assetCode || "";
    document.getElementById("assetName").value = data.name || "";
    document.getElementById("categoryId").value = data.categoryId || "";
    document.getElementById("status").value = data.status || "ACTIVE";
    document.getElementById("purchasePrice").value = data.purchasePrice || "";
    document.getElementById("currentValue").value = data.currentValue || "";
    document.getElementById("purchaseDate").value = data.purchaseDate || "";
    document.getElementById("usageStartDate").value = data.usageStartDate || "";
    document.getElementById("currentDepartmentId").value =
      data.currentDepartmentId || "";
    document.getElementById("warrantyProvider").value =
      data.warrantyProvider || "";
    document.getElementById("warrantyExpiryDate").value =
      data.warrantyExpiryDate || "";
  }

  /**
   * Perform CREATE operation
   */
  performCreate() {
    this.showLoading(true);

    fetch(AssetManager.CONFIG.API_BASE, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(this.formData),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
        return response.json();
      })
      .then((data) => {
        console.log("[CREATE] Success:", data);
        this.showSuccess(AssetManager.CONFIG.MESSAGES.ADD_SUCCESS);

        // Close modal & reload
        if (this.modal) {
          this.modal.hide();
        }
        setTimeout(() => window.location.reload(), 1000);
      })
      .catch((error) => {
        console.error("[CREATE] Error:", error);
        this.showError(`Thêm tài sản thất bại: ${error.message}`);
      })
      .finally(() => {
        this.showLoading(false);
      });
  }

  /**
   * Perform UPDATE operation
   */
  performUpdate() {
    const assetId = document.getElementById("assetId").value;

    this.showLoading(true);

    fetch(`${AssetManager.CONFIG.API_BASE}/${assetId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(this.formData),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
        return response.json();
      })
      .then((data) => {
        console.log("[UPDATE] Success:", data);
        this.showSuccess(AssetManager.CONFIG.MESSAGES.UPDATE_SUCCESS);

        // Close modal & reload
        if (this.modal) {
          this.modal.hide();
        }
        setTimeout(() => window.location.reload(), 1000);
      })
      .catch((error) => {
        console.error("[UPDATE] Error:", error);
        this.showError(`Cập nhật tài sản thất bại: ${error.message}`);
      })
      .finally(() => {
        this.showLoading(false);
      });
  }

  /**
   * Perform DELETE operation
   */
  performDelete(assetId) {
    this.showLoading(true);

    fetch(`${AssetManager.CONFIG.API_BASE}/${assetId}`, {
      method: "DELETE",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
        return response.ok ? null : response.json();
      })
      .then(() => {
        console.log("[DELETE] Success");
        this.showSuccess(AssetManager.CONFIG.MESSAGES.DELETE_SUCCESS);
        // Reload page
        setTimeout(() => window.location.reload(), 1000);
      })
      .catch((error) => {
        console.error("[DELETE] Error:", error);
        this.showError(`Xóa tài sản thất bại: ${error.message}`);
      })
      .finally(() => {
        this.showLoading(false);
      });
  }

  /**
   * Select asset row (highlight)
   */
  selectAssetRow(row) {
    // Remove active class from all rows
    document.querySelectorAll(".asset-row").forEach((r) => {
      r.classList.remove("table-active");
    });

    // Add active class to clicked row
    row.classList.add("table-active");
  }

  /**
   * Show success notification
   */
  showSuccess(message) {
    console.log(message);
    alert(message);
  }

  /**
   * Show error notification
   */
  showError(message) {
    console.error(message);
    alert(message);
  }

  /**
   * Show/hide loading state
   */
  showLoading(isLoading) {
    const button = document.getElementById("btnSaveAsset");
    if (button) {
      button.disabled = isLoading;
      button.innerHTML = isLoading
        ? '<i class="fas fa-spinner fa-spin"></i> Đang xử lý...'
        : '<i class="fas fa-save"></i> Lưu thông tin';
    }
  }

  /**
   * Log module initialization
   */
  logInitialization() {
    console.group("🚀 Asset Management Module");
    console.log("✅ Module initialized successfully");
    console.log("📍 API Base:", AssetManager.CONFIG.API_BASE);
    console.log("🎯 Ready for user interactions");
    console.groupEnd();
  }
}

/**
 * Initialize module when DOM is ready
 */
document.addEventListener("DOMContentLoaded", () => {
  window.assetManager = new AssetManager();
});

/**
 * =====================================
 * UTILITY FUNCTIONS
 * =====================================
 */

/**
 * Format currency to VND format
 * @param {number} amount - Amount to format
 * @returns {string} Formatted currency string
 */
function formatCurrency(amount) {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(amount);
}

/**
 * Format date to DD/MM/YYYY format
 * @param {string} dateString - ISO date string
 * @returns {string} Formatted date
 */
function formatDate(dateString) {
  if (!dateString) return "";
  const options = { year: "numeric", month: "2-digit", day: "2-digit" };
  return new Date(dateString).toLocaleDateString("vi-VN", options);
}




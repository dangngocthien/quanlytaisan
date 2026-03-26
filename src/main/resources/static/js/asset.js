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
    this.html5QrcodeScanner = null;

    this.init();
  }

  /**
   * Initialize the module
   */
  init() {
    this.setupModal();
    this.attachEventListeners();
    this.setupQRScanner();
    this.logInitialization();
  }

  /**
   * Setup QR Scanner
   */
  setupQRScanner() {
    const qrModalEl = document.getElementById("qrScannerModal");
    if (!qrModalEl) return;

    qrModalEl.addEventListener("shown.bs.modal", () => {
      this.startQRScanner();
    });

    qrModalEl.addEventListener("hidden.bs.modal", () => {
      this.stopQRScanner();
    });
  }

  startQRScanner() {
    if (this.html5QrcodeScanner) return; // Already running

    // Ẩn text placeholder khi bắt đầu quét
    const resultsDiv = document.getElementById("qr-reader-results");
    if (resultsDiv) {
      resultsDiv.style.display = "none";
    }

    // Sử dụng html5-qrcode thuần thay vì Hml5QrcodeScanner UI tích hợp
    // để tránh các nút xin quyền rườm rà, ép nó chạy thẳng camera
    this.html5QrcodeScanner = new Html5Qrcode("qr-reader");

    // Tăng fps và cải thiện format cho dễ nhận diện trên mobile/webcam
    const config = {
      fps: 15,
      qrbox: { width: 250, height: 250 },
      aspectRatio: 1.0,
      disableFlip: false, // Cho phép lật camera nếu cần
    };

    // Quét với bất kỳ camera nào ban đầu mà nó tìm dc (hỗ trợ tốt hơn cho laptop k có camera sau)
    this.html5QrcodeScanner
      .start(
        { facingMode: "environment" },
        config,
        (decodedText, decodedResult) =>
          this.onScanSuccess(decodedText, decodedResult),
        (errorMessage) => this.onScanFailure(errorMessage),
      )
      .catch((err) => {
        console.warn(`Environment camera fail fallback to videoId...`, err);
        // Fallback: nếu k tìm thấy camera sau (thường là máy tính bàn/laptop), xin bật cam mặc định
        Html5Qrcode.getCameras()
          .then((devices) => {
            if (devices && devices.length) {
              var cameraId = devices[0].id;
              this.html5QrcodeScanner.start(
                cameraId,
                config,
                (decodedText, decodedResult) =>
                  this.onScanSuccess(decodedText, decodedResult),
                (errorMessage) => this.onScanFailure(errorMessage),
              );
            }
          })
          .catch((err2) => {
            console.error(`Error starting QR Scanner: ${err2}`);
            if (resultsDiv) {
              resultsDiv.style.display = "block";
              resultsDiv.innerHTML = `<span class="text-danger"><i class="fas fa-exclamation-triangle"></i> Lỗi Camera: ${err2.message || "Không truy cập được camera. Vui lòng cấp quyền."}</span>`;
            }
          });
      });
  }

  stopQRScanner() {
    if (this.html5QrcodeScanner && this.html5QrcodeScanner.isScanning) {
      this.html5QrcodeScanner
        .stop()
        .then((ignore) => {
          // QR Code scanning is stopped.
          this.html5QrcodeScanner.clear();
          this.html5QrcodeScanner = null;
        })
        .catch((err) => {
          console.error("Stop failed: ", err);
        });
    } else {
      this.html5QrcodeScanner = null;
    }
  }

  onScanSuccess(decodedText, decodedResult) {
    console.log(`[QR] Scan success: ${decodedText}`);

    // Play a beep sound if possible (optional)
    const qrModalEl = document.getElementById("qrScannerModal");
    const qrModalInstance = bootstrap.Modal.getInstance(qrModalEl);
    if (qrModalInstance) {
      qrModalInstance.hide();
    }

    // Auto fill the search field and submit the form
    const searchInput = document.getElementById("searchKeyword");
    if (searchInput) {
      searchInput.value = decodedText;
      searchInput.closest("form").submit();
    }
  }

  onScanFailure(error) {
    // handle scan failure, usually better to ignore and keep scanning.
    // console.warn(`Code scan error = ${error}`);
  }

  /**
   * Setup Bootstrap Modal instance
   */
  setupModal() {
    const modalElement = document.getElementById(AssetManager.CONFIG.MODAL_ID);
    if (modalElement) {
      this.modal = new bootstrap.Modal(modalElement);
    }

    const transferModalEl = document.getElementById("transferModal");
    if (transferModalEl) {
      this.transferModal = new bootstrap.Modal(transferModalEl);
    }

    const historyModalEl = document.getElementById("historyModal");
    if (historyModalEl) {
      this.historyModal = new bootstrap.Modal(historyModalEl);
    }
  }

  /**
   * Attach global DOM event listeners
   */
  attachEventListeners() {
    document.addEventListener("click", (e) => {
      // Nút Hiện QR
      const showQrBtn = e.target.closest(".btn-show-qr");
      if (showQrBtn) {
        const assetCode = showQrBtn.getAttribute("data-asset-code");
        const assetName = showQrBtn.getAttribute("data-asset-name");
        this.generateQRCode(assetCode, assetName);
        return;
      }

      if (e.target.closest("#btnAddAsset")) {
        this.handleAddAsset();
      }
    });

    // Download and Print QR Code Event
    document.addEventListener("click", (e) => {
      if (e.target.closest("#btnDownloadQR")) {
        this.downloadQRCode();
      }
      if (e.target.closest("#btnPrintQR")) {
        this.printQRCode();
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

    // Button: Transfer Asset
    document.addEventListener("click", (e) => {
      const btn = e.target.closest(".btn-transfer-asset");
      if (btn) {
        const assetId = btn.getAttribute("data-asset-id");
        const assetName = btn.getAttribute("data-asset-name");
        const currentDeptId = btn.getAttribute("data-current-dept-id");
        this.openTransferModal(assetId, assetName, currentDeptId);
      }
    });

    // Button: History Asset
    document.addEventListener("click", (e) => {
      const btn = e.target.closest(".btn-history-asset");
      if (btn) {
        const assetId = btn.getAttribute("data-asset-id");
        this.openHistoryModal(assetId);
      }
    });

    // Button: Save Transfer
    document.addEventListener("click", (e) => {
      if (e.target.closest("#btnSaveTransfer")) {
        this.handleSaveTransfer();
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

    // Hide QR section when adding new
    const qrContainer = document.getElementById("qrContainer");
    if (qrContainer) {
      qrContainer.style.display = "none";
    }

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

    // Hiển thị và tạo mã QR
    if (data.assetCode) {
      this.generateQRCodeInForm(data.assetCode);
    }
  }

  generateQRCodeInForm(assetCode) {
    const qrContainer = document.getElementById("qrContainer");
    const qrcodeDisplay = document.getElementById("qrcode-display");

    if (qrContainer && qrcodeDisplay) {
      qrContainer.style.display = "block";
      qrcodeDisplay.innerHTML = ""; // Xóa QR cũ

      // Tạo một div bọc ngoài để làm viền trắng (padding)
      const qrWrapper = document.createElement("div");
      qrWrapper.style.padding = "20px";
      qrWrapper.style.backgroundColor = "#ffffff";
      qrWrapper.style.display = "inline-block";
      qrcodeDisplay.appendChild(qrWrapper);

      // Chỉnh thông số lưới và cấp độ sửa lỗi
      new QRCode(qrWrapper, {
        text: assetCode,
        width: 200,
        height: 200,
        colorDark: "#000000",
        colorLight: "#ffffff",
        correctLevel: QRCode.CorrectLevel.M,
      });
    }
  }

  /**
   * Bấm nút hiển thị mã QR trên bảng
   */
  generateQRCode(assetCode, assetName) {
    let assetId = null;
    document.querySelectorAll(".asset-row").forEach((tr) => {
      const codeSpan = tr.querySelector("td:nth-child(2) span");
      if (codeSpan && codeSpan.textContent.trim() === assetCode) {
        assetId = tr.getAttribute("data-asset-id");
      }
    });

    if (assetId) {
      this.handleEditAsset(assetId);
    }
  }

  /**
   * Tính năng tải ảnh QR về máy
   */
  downloadQRCode() {
    // Tìm thẻ canvas sinh ra bởi qrcode.js
    const qrCanvas = document.querySelector("#qrcode-display canvas");
    if (!qrCanvas) return;

    // Tạo Canvas phụ để chèn viền (padding) cùng nền trắng giúp ảnh không bị trong suốt, bị đen
    const padding = 20;
    const downloadCanvas = document.createElement("canvas");
    downloadCanvas.width = qrCanvas.width + padding * 2;
    downloadCanvas.height = qrCanvas.height + padding * 2;
    const ctx = downloadCanvas.getContext("2d");

    // Tô nền trắng chuẩn
    ctx.fillStyle = "#ffffff";
    ctx.fillRect(0, 0, downloadCanvas.width, downloadCanvas.height);

    // Chèn mã QR gốc lên trên (giữa padding)
    ctx.drawImage(qrCanvas, padding, padding);

    // Đặt tên tệp theo mã
    const assetCode = document.getElementById("assetCode").value || "QR";
    const link = document.createElement("a");

    // Xuất ra hình định dạng png
    link.href = downloadCanvas.toDataURL("image/png");
    link.download = `QR_${assetCode}.png`;

    // Trigger download
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

  /**
   * Tính năng in trực tiếp tem mã QR độc lập
   */
  printQRCode() {
    const qrCanvas = document.querySelector("#qrcode-display canvas");
    if (!qrCanvas) {
      this.showError("Không tìm thấy mã QR để in!");
      return;
    }

    const assetCode = document.getElementById("assetCode").value || "N/A";
    const assetName =
      document.getElementById("assetName").value || "Tài sản chưa đặt tên";

    // Tạo data URI của ảnh để nhúng vào trang in
    const dataUrl = qrCanvas.toDataURL("image/png");

    // Mở popup in
    const printWindow = window.open("", "_blank", "width=400,height=600");
    if (!printWindow) {
      this.showError("Vui lòng cho phép popup để in tem.");
      return;
    }

    // Nội dung trang in
    printWindow.document.write(`
      <!DOCTYPE html>
      <html>
        <head>
          <title>In Tem Tài Sản</title>
          <style>
            @page {
              size: 50mm 50mm; /* Kích thước tối ưu cho máy in tem nhỏ */
              margin: 0;
            }
            body { 
              font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
              display: flex; 
              justify-content: center; 
              align-items: center; 
              height: 100vh; 
              margin: 0; 
              background: #fff; 
            }
            .label-wrap { 
              border: 1px solid #000; 
              padding: 5px; 
              text-align: center; 
              width: 45mm; 
              height: 45mm;
              box-sizing: border-box; 
              border-radius: 4px; 
              display: flex;
              flex-direction: column;
              align-items: center;
              justify-content: space-between;
            }
            .title { font-weight: bold; font-size: 10px; text-transform: uppercase; border-bottom: 1px solid #ddd; width: 100%; padding-bottom: 2px;}
            .name { font-size: 11px; font-weight: bold; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; max-width: 100%; margin-top: 3px; }
            img { width: 70px; height: 70px; margin: 2px 0; }
            .code { font-size: 10px; }
            
            /* Dành cho test máy in A4 bình thường (Hiển thị căn giữa) */
            @media print {
              body { background-color: white; padding: 0; }
              .label-wrap { border: 1px solid #000; page-break-inside: avoid; }
            }
          </style>
        </head>
        <body>
          <div class="label-wrap">
            <div class="title">TÀI SẢN CÔNG TY</div>
            <div class="name" title="${assetName}">${assetName}</div>
            <img src="${dataUrl}" />
            <div class="code">Mã: ${assetCode}</div>
          </div>
          <script>
            // Tự động gọi lệnh in sau khi load ảnh xong
            window.onload = function() {
              setTimeout(function() {
                window.print();
                window.close();
              }, 300);
            }
          </script>
        </body>
      </html>
    `);
    printWindow.document.close();
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

  // ==========================================
  // TRANSFER & HISTORY (PHASE 3)
  // ==========================================

  openTransferModal(assetId, assetName, currentDeptId) {
    document.getElementById("transferForm").reset();
    document.getElementById("transferAssetId").value = assetId;
    document.getElementById("transferAssetName").value = assetName || "";
    document.getElementById("transferFromDeptId").value = currentDeptId || "";

    // Set default date to today
    document.getElementById("transferDate").value = new Date()
      .toISOString()
      .split("T")[0];

    this.transferModal.show();
  }

  handleSaveTransfer() {
    const assetId = document.getElementById("transferAssetId").value;
    const fromDeptId = document.getElementById("transferFromDeptId").value;
    const toDeptId = document.getElementById("transferToDeptId").value;
    const transferDate = document.getElementById("transferDate").value;
    const transferBy = document.getElementById("transferBy").value;
    const reason = document.getElementById("transferReason").value;

    if (!toDeptId || !transferDate || !transferBy) {
      this.showError(
        "Vui lòng nhập đầy đủ phòng ban nhận, ngày và người thực hiện",
      );
      return;
    }

    const payload = {
      assetId: parseInt(assetId),
      fromDepartmentId: fromDeptId ? parseInt(fromDeptId) : null,
      toDepartmentId: parseInt(toDeptId),
      transferDate: transferDate,
      transferBy: transferBy,
      reason: reason,
    };

    this.showLoading(true);
    fetch(contextPath + "api/transfers", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Chuyển tài sản thất bại");
        return res.json();
      })
      .then((data) => {
        this.showSuccess("Điều chuyển tài sản thành công!");
        this.transferModal.hide();
        setTimeout(() => window.location.reload(), 1000);
      })
      .catch((err) => {
        this.showError(err.message);
      })
      .finally(() => this.showLoading(false));
  }

  openHistoryModal(assetId) {
    this.historyModal.show();
    const tbody = document.querySelector("#historyTable tbody");
    tbody.innerHTML =
      "<tr><td colspan='5' class='text-center'>Đang tải dữ liệu...</td></tr>";

    fetch(contextPath + "api/transfers/asset/" + assetId)
      .then((res) => res.json())
      .then((data) => {
        if (data.length === 0) {
          tbody.innerHTML =
            "<tr><td colspan='5' class='text-center'>Chưa có lịch sử điều chuyển nào</td></tr>";
          return;
        }

        tbody.innerHTML = "";
        data.forEach((t) => {
          const fromDept = t.fromDepartmentName || "Khởi tạo/Mua mới";
          const toDept = t.toDepartmentName || "-";
          const date = formatDate(t.transferDate);

          tbody.innerHTML += `
          <tr>
            <td>${date}</td>
            <td>${t.reason || "-"}</td>
            <td><span class='badge bg-secondary'>${fromDept}</span></td>
            <td><span class='badge bg-success'>${toDept}</span></td>
            <td>${t.transferBy || "-"}</td>
          </tr>
        `;
        });
      })
      .catch((err) => {
        tbody.innerHTML =
          "<tr><td colspan='5' class='text-center text-danger'>Lỗi tải dữ liệu</td></tr>";
      });
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

// =====================================
// WARRANTY MANAGER
// =====================================
class WarrantyManager {
  constructor() {
    this.modal = null;
    this.currentAssetId = null;
    this.init();
  }

  init() {
    document.addEventListener("DOMContentLoaded", () => {
      const modalEl = document.getElementById("warrantyModal");
      if (modalEl) {
        this.modal = new bootstrap.Modal(modalEl);
      }
      this.attachEventListeners();
    });
  }

  attachEventListeners() {
    document.body.addEventListener("click", (e) => {
      const btn = e.target.closest(".btn-warranty-asset");
      if (btn) {
        this.currentAssetId = btn.getAttribute("data-asset-id");
        this.openWarrantyModal();
      }
    });

    const form = document.getElementById("warrantyForm");
    if (form) {
      form.addEventListener("submit", (e) => {
        e.preventDefault();
        this.handleSaveWarranty();
      });
    }
  }

  openWarrantyModal() {
    if (!this.modal) return;

    const listTab = document.getElementById("warranty-list-tab");
    if (listTab) {
      const tab = new bootstrap.Tab(listTab);
      tab.show();
    }

    const form = document.getElementById("warrantyForm");
    if (form) form.reset();
    document.getElementById("warrantyAssetId").value = this.currentAssetId;

    this.loadWarrantyRecords(this.currentAssetId);
    this.modal.show();
  }

  loadWarrantyRecords(assetId) {
    const tbody = document.getElementById("warrantyTableBody");
    tbody.innerHTML =
      "<tr><td colspan='5' class='text-center'>Đang tải...</td></tr>";

    fetch(contextPath + "api/assets/" + assetId + "/warranties")
      .then((res) => res.json())
      .then((data) => {
        if (!data || data.length === 0) {
          tbody.innerHTML =
            "<tr><td colspan='5' class='text-center'>Chưa có dữ liệu bảo hành</td></tr>";
          return;
        }

        tbody.innerHTML = "";
        data.forEach((item) => {
          const fileLink = item.fileDownloadUrl
            ? `<a href="${item.fileDownloadUrl}" target="_blank"><i class="fas fa-paperclip"></i> ${item.attachmentFileName}</a>`
            : "<i>Không có file</i>";

          const tr = document.createElement("tr");
          tr.innerHTML = `
            <td>${formatDate(item.createdAt)}</td>
            <td>${item.providerCompany}<br><small>${item.contactPhone || ""}</small></td>
            <td>${formatDate(item.startDate)} - ${formatDate(item.endDate)}</td>
            <td>${fileLink}</td>
            <td>
              <button class="btn btn-sm btn-danger btn-delete-warranty" data-id="${item.id}">
                <i class="fas fa-trash"></i>
              </button>
            </td>
          `;
          tbody.appendChild(tr);
        });

        document.querySelectorAll(".btn-delete-warranty").forEach((btn) => {
          btn.addEventListener("click", (e) => {
            const id = e.currentTarget.getAttribute("data-id");
            this.deleteWarranty(id);
          });
        });
      })
      .catch((err) => {
        console.error(err);
        tbody.innerHTML =
          "<tr><td colspan='5' class='text-center text-danger'>Lỗi tải dữ liệu</td></tr>";
      });
  }

  handleSaveWarranty() {
    const assetId = document.getElementById("warrantyAssetId").value;
    const formData = new FormData();
    formData.append(
      "providerCompany",
      document.getElementById("warrantyProvider").value,
    );
    formData.append(
      "contactPhone",
      document.getElementById("warrantyPhone").value || "",
    );
    formData.append(
      "startDate",
      document.getElementById("warrantyStartDate").value || "",
    );
    formData.append(
      "endDate",
      document.getElementById("warrantyEndDate").value || "",
    );
    formData.append(
      "notes",
      document.getElementById("warrantyNotes").value || "",
    );

    const fileInput = document.getElementById("warrantyFile");
    if (fileInput.files.length > 0) {
      formData.append("file", fileInput.files[0]);
    }

    fetch(contextPath + "api/assets/" + assetId + "/warranties", {
      method: "POST",
      body: formData,
    })
      .then((res) => {
        if (res.ok) {
          alert("Thêm hồ sơ bảo hành thành công!");
          this.loadWarrantyRecords(assetId);
          document.getElementById("warrantyForm").reset();
          document.getElementById("warranty-list-tab").click();
        } else {
          res.text().then((text) => alert("Lỗi: " + text));
        }
      })
      .catch((err) => {
        console.error(err);
        alert("Lỗi kết nối khi lưu");
      });
  }

  deleteWarranty(id) {
    if (!confirm("Bạn có chắc muốn xóa hồ sơ bảo hành này?")) return;

    fetch(contextPath + "api/warranties/" + id, {
      method: "DELETE",
    })
      .then((res) => {
        if (res.ok) {
          alert("Xóa thành công!");
          this.loadWarrantyRecords(this.currentAssetId);
        } else {
          alert("Lỗi khi xóa");
        }
      })
      .catch((err) => {
        console.error(err);
        alert("Lỗi mạng khi xóa");
      });
  }
}

new WarrantyManager();

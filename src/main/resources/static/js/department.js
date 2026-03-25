/**
 * Department Management System - Clean & Simple
 */

// Configuration
const contextPath = window.contextPath || "";
const API_URL = contextPath + "/api/departments";

// Global Modal variable
let departmentModal = null;

// Initialize on page load
document.addEventListener("DOMContentLoaded", function () {
  initializeModal();
  attachEventListeners();
});

// ===== Initialize Modal =====
function initializeModal() {
  const modalElement = document.getElementById("departmentModal");
  if (modalElement) {
    departmentModal = new bootstrap.Modal(modalElement);
  }
}

// ===== Attach Event Listeners =====
function attachEventListeners() {
  // Button: Thêm Phòng ban mới
  const addBtn = document.getElementById("addNewDepartmentBtn");
  if (addBtn) {
    addBtn.addEventListener("click", openAddModal);
  }

  // Buttons: Sửa (Edit)
  document.querySelectorAll(".edit-btn").forEach((btn) => {
    btn.addEventListener("click", function () {
      const id = this.getAttribute("data-id");
      openEditModal(id);
    });
  });

  // Buttons: Xóa (Delete)
  document.querySelectorAll(".delete-btn").forEach((btn) => {
    btn.addEventListener("click", function () {
      const id = this.getAttribute("data-id");
      deleteDepartment(id);
    });
  });

  // Button: Lưu (Save)
  const saveBtn = document.getElementById("saveDepartmentBtn");
  if (saveBtn) {
    saveBtn.addEventListener("click", saveDepartment);
  }
}

// ===== Open Modal - Thêm Mới =====
function openAddModal() {
  // Reset form
  document.getElementById("departmentForm").reset();
  document.getElementById("departmentId").value = "";

  // Update title
  document.getElementById("modalTitle").textContent = "Thêm Phòng ban mới";

  // Show modal
  if (departmentModal) {
    departmentModal.show();
  }
}

// ===== Open Modal - Sửa =====
function openEditModal(id) {
  fetch(`${API_URL}/${id}`)
    .then((response) => {
      if (!response.ok) throw new Error("Không thể lấy dữ liệu");
      return response.json();
    })
    .then((data) => {
      // Fill form
      document.getElementById("departmentId").value = data.id;
      document.getElementById("code").value = data.code;
      document.getElementById("name").value = data.name;
      document.getElementById("description").value = data.description || "";

      // Update title
      document.getElementById("modalTitle").textContent = "Sửa Phòng ban";

      // Show modal
      if (departmentModal) {
        departmentModal.show();
      }
    })
    .catch((error) => {
      alert("Lỗi: " + error.message);
    });
}

// ===== Save Department =====
function saveDepartment(e) {
  e.preventDefault();

  // Get form values
  const id = document.getElementById("departmentId").value;
  const code = document.getElementById("code").value.trim();
  const name = document.getElementById("name").value.trim();
  const description = document.getElementById("description").value.trim();

  // Validate
  if (!code || !name) {
    alert("Vui lòng nhập Mã phòng và Tên phòng");
    return;
  }

  // Prepare data
  const data = { code, name, description };

  // Determine method and URL
  const method = id ? "PUT" : "POST";
  const url = id ? `${API_URL}/${id}` : API_URL;

  // Send request
  fetch(url, {
    method: method,
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error(`HTTP ${response.status}`);
      }
      return response.json();
    })
    .then(() => {
      const msg = id ? "Cập nhật thành công" : "Thêm mới thành công";
      alert(msg);

      // Close modal and reload
      if (departmentModal) departmentModal.hide();
      window.location.reload();
    })
    .catch((error) => {
      alert("Lỗi: " + error.message);
    });
}

// ===== Delete Department =====
function deleteDepartment(id) {
  if (!confirm("Bạn có chắc muốn xóa?")) return;

  fetch(`${API_URL}/${id}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((response) => {
      if (!response.ok) throw new Error(`HTTP ${response.status}`);
      return response.ok;
    })
    .then(() => {
      alert("Xóa thành công");
      window.location.reload();
    })
    .catch((error) => {
      alert("Lỗi: " + error.message);
    });
}

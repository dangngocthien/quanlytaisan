document.addEventListener("DOMContentLoaded", function () {
  // Elements
  const badge = document.getElementById("notificationBadge");
  const list = document.getElementById("notificationList");
  const markAllBtn = document.getElementById("markAllReadBtn");

  if (!badge || !list) return;

  // Load notifications
  function loadNotifications() {
    fetch("/quanlytaisan/api/notifications/recent")
      .then((response) => response.json())
      .then((data) => {
        renderNotifications(data);
      })
      .catch((error) => console.error("Error fetching notifications:", error));
  }

  function renderNotifications(notifications) {
    list.innerHTML = ""; // Clear current

    let unreadCount = 0;

    if (notifications.length === 0) {
      list.innerHTML =
        '<div class="text-center text-muted p-4">Không có thông báo nào</div>';
      badge.classList.add("d-none");
      return;
    }

    notifications.forEach((notif) => {
      if (!notif.read) unreadCount++;

      const item = document.createElement("div");
      item.className = `p-2 mb-2 rounded border-bottom notification-item ${!notif.read ? "bg-light border-start border-primary border-4" : ""}`;
      item.style.cursor = "pointer";

      // Format date basic
      const d = new Date(notif.createdAt);
      const dateStr = `${d.getDate()}/${d.getMonth() + 1}/${d.getFullYear()} ${d.getHours()}:${d.getMinutes()}`;

      item.innerHTML = `
                <div class="d-flex w-100 justify-content-between">
                  <h6 class="mb-1" style="font-size: 0.9rem; font-weight: ${!notif.read ? "bold" : "normal"}">${notif.title}</h6>
                  <small class="text-muted" style="font-size: 0.75rem;">${dateStr}</small>
                </div>
                <p class="mb-1 text-muted" style="font-size: 0.85rem;">${notif.message}</p>
            `;

      // Mark as read when clicked
      item.addEventListener("click", () => {
        if (!notif.read) {
          fetch(`/quanlytaisan/api/notifications/${notif.id}/read`, {
            method: "POST",
          }).then(() => {
            // Redirect to path
            if (notif.relatedPath)
              window.location.href = "/quanlytaisan" + notif.relatedPath;
            else loadNotifications();
          });
        } else if (notif.relatedPath) {
          window.location.href = "/quanlytaisan" + notif.relatedPath;
        }
      });

      list.appendChild(item);
    });

    // Update badge
    if (unreadCount > 0) {
      badge.textContent = unreadCount;
      badge.classList.remove("d-none");
    } else {
      badge.classList.add("d-none");
    }
  }

  // Mark all as read
  if (markAllBtn) {
    markAllBtn.addEventListener("click", (e) => {
      e.stopPropagation();
      fetch("/quanlytaisan/api/notifications/read-all", {
        method: "POST",
      }).then(() => loadNotifications());
    });
  }

  // Initial load
  loadNotifications();

  // Auto refresh every 60 seconds
  setInterval(loadNotifications, 60000);
});

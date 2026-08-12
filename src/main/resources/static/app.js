const API = "/api";

// ---------- tab switching ----------
document.querySelectorAll(".tab").forEach(function (tab) {
  tab.addEventListener("click", function () {
    document.querySelectorAll(".tab").forEach(function (t) { t.classList.remove("active"); });
    document.querySelectorAll(".panel").forEach(function (p) { p.classList.remove("active"); });
    tab.classList.add("active");
    document.getElementById("panel-" + tab.dataset.tab).classList.add("active");
    if (tab.dataset.tab === "summary") { loadSummary(); }
  });
});

// ---------- helpers ----------
function showFeedback(elId, message, isError) {
  const el = document.getElementById(elId);
  el.textContent = message;
  el.className = "feedback " + (isError ? "error" : "success");
}

async function apiCall(url, options) {
  const res = await fetch(url, options);
  let body = null;
  try { body = await res.json(); } catch (e) { /* no body */ }
  if (!res.ok) {
    const message = body && body.error ? body.error : "Something went wrong.";
    throw new Error(message);
  }
  return body;
}

function money(n) {
  return "RM " + Number(n).toFixed(2);
}

// ---------- ADD EXPENSE ----------
const typeSelect = document.getElementById("add-type");
const extraLabel = document.getElementById("add-extra-label");
const extraInput = document.getElementById("add-extra");

typeSelect.addEventListener("change", function () {
  if (typeSelect.value === "FIXED") {
    extraLabel.firstChild.textContent = "Due day (1-31)";
    extraInput.placeholder = "e.g. 15";
  } else {
    extraLabel.firstChild.textContent = "Category";
    extraInput.placeholder = "e.g. Groceries";
  }
});

document.getElementById("add-form").addEventListener("submit", async function (e) {
  e.preventDefault();
  const payload = {
    description: document.getElementById("add-description").value,
    amount: parseFloat(document.getElementById("add-amount").value),
    date: document.getElementById("add-date").value,
    type: typeSelect.value,
  };
  if (typeSelect.value === "FIXED") {
    payload.dueDay = parseInt(extraInput.value, 10);
  } else {
    payload.category = extraInput.value;
  }

  try {
    await apiCall(API + "/expenses", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });
    showFeedback("add-feedback", "Expense added successfully.", false);
    e.target.reset();
    typeSelect.dispatchEvent(new Event("change"));
  } catch (err) {
    showFeedback("add-feedback", err.message, true);
  }
});

// ---------- VIEW / MANAGE ----------
function renderExpenseLedger(expenses) {
  const container = document.getElementById("expense-ledger");
  if (expenses.length === 0) {
    container.innerHTML = '<p class="empty-state">No expenses found.</p>';
    return;
  }
  container.innerHTML = expenses.map(function (exp) {
    return (
      '<div class="ledger-row">' +
        '<span class="id-badge">#' + exp.id + '</span>' +
        '<span class="desc">' + escapeHtml(exp.description) + '</span>' +
        '<span class="meta">' + exp.date + ' · ' + escapeHtml(exp.categoryLabel) + '</span>' +
        '<span class="amount">' + money(exp.amount) + '</span>' +
      '</div>'
    );
  }).join("");
}

async function loadExpenses(keyword) {
  const url = keyword ? API + "/expenses?keyword=" + encodeURIComponent(keyword) : API + "/expenses";
  const data = await apiCall(url);
  renderExpenseLedger(data);
}

document.getElementById("view-search-btn").addEventListener("click", function () {
  loadExpenses(document.getElementById("view-search").value);
});
document.getElementById("view-showall-btn").addEventListener("click", function () {
  document.getElementById("view-search").value = "";
  loadExpenses();
});

document.getElementById("view-update-btn").addEventListener("click", async function () {
  const id = document.getElementById("view-id").value;
  const amount = document.getElementById("view-amount").value;
  const description = document.getElementById("view-description").value;
  try {
    await apiCall(API + "/expenses/" + id, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ amount: parseFloat(amount), description: description }),
    });
    showFeedback("view-feedback", "Expense updated successfully.", false);
    loadExpenses();
  } catch (err) {
    showFeedback("view-feedback", err.message, true);
  }
});

document.getElementById("view-delete-btn").addEventListener("click", async function () {
  const id = document.getElementById("view-id").value;
  try {
    await apiCall(API + "/expenses/" + id, { method: "DELETE" });
    showFeedback("view-feedback", "Expense deleted successfully.", false);
    loadExpenses();
  } catch (err) {
    showFeedback("view-feedback", err.message, true);
  }
});

// ---------- INVENTORY ----------
function renderInventoryLedger(items) {
  const container = document.getElementById("inventory-ledger");
  if (items.length === 0) {
    container.innerHTML = '<p class="empty-state">No items found.</p>';
    return;
  }
  container.innerHTML = items.map(function (item) {
    return (
      '<div class="ledger-row">' +
        '<span class="id-badge">#' + item.id + '</span>' +
        '<span class="desc">' + escapeHtml(item.itemName) + '</span>' +
        '<span class="meta">qty ' + item.quantity + ' × ' + money(item.unitPrice) + '</span>' +
        '<span class="amount">' + money(item.totalValue) + '</span>' +
      '</div>'
    );
  }).join("");
}

async function loadInventory(keyword) {
  const url = keyword ? API + "/inventory?keyword=" + encodeURIComponent(keyword) : API + "/inventory";
  const data = await apiCall(url);
  renderInventoryLedger(data);
}

document.getElementById("inv-add-form").addEventListener("submit", async function (e) {
  e.preventDefault();
  const payload = {
    itemName: document.getElementById("inv-name").value,
    quantity: parseInt(document.getElementById("inv-quantity").value, 10),
    unitPrice: parseFloat(document.getElementById("inv-price").value),
  };
  try {
    await apiCall(API + "/inventory", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });
    showFeedback("inv-feedback", "Item added successfully.", false);
    e.target.reset();
    loadInventory();
  } catch (err) {
    showFeedback("inv-feedback", err.message, true);
  }
});

document.getElementById("inv-search-btn").addEventListener("click", function () {
  loadInventory(document.getElementById("inv-search").value);
});
document.getElementById("inv-showall-btn").addEventListener("click", function () {
  document.getElementById("inv-search").value = "";
  loadInventory();
});

document.getElementById("inv-update-btn").addEventListener("click", async function () {
  const id = document.getElementById("inv-id").value;
  const quantity = document.getElementById("inv-new-quantity").value;
  const unitPrice = document.getElementById("inv-new-price").value;
  try {
    await apiCall(API + "/inventory/" + id, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ quantity: parseInt(quantity, 10), unitPrice: parseFloat(unitPrice) }),
    });
    showFeedback("inv-feedback", "Item updated successfully.", false);
    loadInventory();
  } catch (err) {
    showFeedback("inv-feedback", err.message, true);
  }
});

document.getElementById("inv-delete-btn").addEventListener("click", async function () {
  const id = document.getElementById("inv-id").value;
  try {
    await apiCall(API + "/inventory/" + id, { method: "DELETE" });
    showFeedback("inv-feedback", "Item deleted successfully.", false);
    loadInventory();
  } catch (err) {
    showFeedback("inv-feedback", err.message, true);
  }
});

// ---------- SUMMARY ----------
async function loadSummary() {
  try {
    const data = await apiCall(API + "/summary");
    document.getElementById("stat-total").textContent = money(data.totalAmount);
    document.getElementById("stat-fixed").textContent = money(data.totalFixed);
    document.getElementById("stat-variable").textContent = money(data.totalVariable);
    document.getElementById("stat-impact").textContent = money(data.totalMonthlyImpact);
    document.getElementById("stat-inventory").textContent = money(data.totalInventoryValue);
  } catch (err) {
    console.error(err);
  }
}

document.getElementById("summary-refresh-btn").addEventListener("click", loadSummary);

// ---------- utility ----------
function escapeHtml(str) {
  const div = document.createElement("div");
  div.textContent = str;
  return div.innerHTML;
}

// ---------- initial load ----------
loadExpenses();
loadInventory();
loadSummary();

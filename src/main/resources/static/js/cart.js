const cartToggle = document.getElementById("cart-toggle");
const cartPanel = document.getElementById("cart-panel");
const cartClose = document.getElementById("cart-close");
const cartOverlay = document.getElementById("cart-overlay");
const cartBrowse = document.querySelector(".cart-browse-link");
const orderForm = document.getElementById("order-form");
const orderStatus = document.getElementById("order-status");
const customerName = document.getElementById("customer-name");
const customerEmail = document.getElementById("customer-email");
const orderDescription = document.getElementById("description");
const cartCheckout = document.getElementById("cart-checkout");
const addToCartButtons = document.querySelectorAll(".add-to-cart");
const cartItems = document.getElementById("cart-items");
const cartTotal = document.getElementById("cart-total");
const cartCount = document.getElementById("cart-count");

let selectedPackage = null;

const openCart = () => {
  cartPanel.classList.add("active");
  cartOverlay.classList.add("active");
};

const closeCart = () => {
  cartPanel.classList.remove("active");
  cartOverlay.classList.remove("active");
};

cartToggle.addEventListener("click", openCart);
cartClose.addEventListener("click", closeCart);
cartOverlay.addEventListener("click", closeCart);
cartBrowse.addEventListener("click", closeCart);

addToCartButtons.forEach(button => {
  button.addEventListener("click", () => {
    selectedPackage = {
      name: button.dataset.package,
      price: Number(button.dataset.price)
    };

    orderStatus.hidden = true;
    orderStatus.textContent = "";
    orderStatus.classList.remove("is-error", "is-success");
    updateCart();
    openCart();
  });
});

function updateCart() {
  if (selectedPackage === null) {
    cartCount.hidden = true;
    cartCheckout.disabled = true;
    cartItems.innerHTML = `
      <div class="cart-empty-icon" aria-hidden="true">
        <img src="/images/cart-icon.svg" alt="" width="34" height="34">
      </div>
      <h3>Your cart is empty</h3>
      <p>Ready to get started? Choose a plan and it’ll show up here.</p>
      <a class="cart-browse-link" href="#pricing-heading">Explore the plans <span aria-hidden="true">→</span></a>
    `;
    cartItems.querySelector(".cart-browse-link").addEventListener("click", closeCart);
    cartTotal.textContent = "$0.00";
    return;
  }

  cartCount.hidden = false;
  cartCount.textContent = "1";
  cartCheckout.disabled = false;
  const displayName = selectedPackage.name.charAt(0).toUpperCase() + selectedPackage.name.slice(1);
  cartItems.innerHTML = `
    <div class="cart-item">
      <h3>${displayName} Package</h3>
      <p>Starting at $${selectedPackage.price.toLocaleString()}</p>
      <button type="button" id="remove-package">Remove</button>
    </div>
  `;
  cartTotal.textContent = "$" + selectedPackage.price.toLocaleString("en-US", {
    minimumFractionDigits: 2
  });
  cartItems.querySelector("#remove-package").addEventListener("click", removePackage);
}

function removePackage() {
  selectedPackage = null;
  orderStatus.hidden = true;
  orderStatus.textContent = "";
  orderStatus.classList.remove("is-error", "is-success");
  updateCart();
}

orderForm.addEventListener("submit", async event => {
  event.preventDefault();
  if (!orderForm.reportValidity()) {
    return;
  }

  if (selectedPackage === null) {
    showOrderStatus("Choose a package before sending your order request.", "is-error");
    return;
  }

  cartCheckout.disabled = true;
  cartCheckout.setAttribute("aria-busy", "true");
  cartCheckout.querySelector("span").textContent = "Sending request...";
  orderStatus.hidden = true;

  try {
    const response = await fetch("/checkout/order", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        name: customerName.value.trim(),
        email: customerEmail.value.trim(),
        packageName: selectedPackage.name.toLowerCase(),
        message: orderDescription.value.trim()
      })
    });
    const result = await response.json();

    if (!response.ok) {
      showOrderStatus(result.message || "Your order could not be sent. Please try again.", "is-error");
      return;
    }

    showOrderStatus(result.message, "is-success");
    orderForm.reset();
  } catch (error) {
    showOrderStatus("A connection error prevented your order from being sent. Please try again.", "is-error");
  } finally {
    cartCheckout.disabled = selectedPackage === null;
    cartCheckout.removeAttribute("aria-busy");
    cartCheckout.querySelector("span").textContent = "Send order request";
  }
});

function showOrderStatus(message, statusClass) {
  orderStatus.textContent = message;
  orderStatus.classList.remove("is-error", "is-success");
  orderStatus.classList.add(statusClass);
  orderStatus.hidden = false;
}

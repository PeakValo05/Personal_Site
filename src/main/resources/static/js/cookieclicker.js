// ---------- Game state ----------
let cookies = 0;
let clickPower = 1; // cookies earned per click

// Each upgrade adds `power` to clickPower. Its cost grows by COST_GROWTH per purchase.
const COST_GROWTH = 1.15;
const upgrades = [
    { name: "Better Clicks", baseCost: 25,       power: 1,    owned: 0 },
    { name: "Super Clicks",  baseCost: 100,      power: 5,    owned: 0 },
    { name: "Mega Clicks",   baseCost: 500,      power: 25,   owned: 0 },
    { name: "Ultra Clicks",  baseCost: 1000,     power: 60,   owned: 0 },
    { name: "Giga Clicks",   baseCost: 5000,     power: 300,  owned: 0 },
    { name: "Tera Clicks",   baseCost: 10000,    power: 700,  owned: 0 },
    { name: "Peta Clicks",   baseCost: 50000,    power: 4000, owned: 0 }
];

// ---------- Helpers ----------
function upgradeCost(upgrade) {
    return Math.ceil(upgrade.baseCost * Math.pow(COST_GROWTH, upgrade.owned));
}

function formatNumber(n) {
    return n.toLocaleString("en-US");
}

// ---------- Rendering ----------
// Updates the cookie counter and click-power labels.
function updateDisplay() {
    document.getElementById("cookie-count").innerHTML = "<b>" + formatNumber(cookies) + "</b>";
    document.getElementById("cookie-power").textContent = "+" + formatNumber(clickPower) + " per click";
}

// Enables or disables each Buy button depending on affordability.
function updateUpgradeButtons() {
    document.querySelectorAll(".upgrade-item").forEach((row, i) => {
        const upgrade = upgrades[i];
        row.querySelector(".upgrade-cost").textContent =
            formatNumber(upgradeCost(upgrade)) + " cookies · owned " + upgrade.owned;
        row.querySelector(".upgrade-buy").disabled = cookies < upgradeCost(upgrade);
    });
}

// Builds the upgrade rows inside the side panel.
function renderUpgrades() {
    const list = document.getElementById("upgradeList");
    upgrades.forEach((upgrade, i) => {
        const row = document.createElement("div");
        row.className = "upgrade-item";
        row.innerHTML =
            '<div class="upgrade-info">' +
                '<span class="upgrade-name">' + upgrade.name + ' (+' + formatNumber(upgrade.power) + ')</span>' +
                '<span class="upgrade-cost"></span>' +
            '</div>' +
            '<button class="upgrade-buy">Buy</button>';
        row.querySelector(".upgrade-buy").addEventListener("click", () => purchaseUpgrade(i));
        list.appendChild(row);
    });
    updateUpgradeButtons();
}

// Shows a "+N" label that floats up from where the cookie was clicked.
function spawnPopup(event) {
    const arena = document.getElementById("cookieArena");
    const popup = document.createElement("span");
    popup.className = "click-popup";
    popup.textContent = "+" + formatNumber(clickPower);

    // Keyboard activations have no pointer position, so fall back to the cookie centre.
    const rect = arena.getBoundingClientRect();
    const x = event.clientX || rect.left + rect.width / 2;
    const y = event.clientY || rect.top + rect.height / 2;
    popup.style.left = (x - rect.left) + "px";
    popup.style.top = (y - rect.top) + "px";

    arena.appendChild(popup);
    popup.addEventListener("animationend", () => popup.remove());
}

// ---------- Actions ----------
// Called when the cookie is clicked.
function clicker(event) {
    cookies += clickPower;
    updateDisplay();
    updateUpgradeButtons();
    spawnPopup(event);
}

// Opens or closes the upgrade side panel.
function toggleUpgrades() {
    document.getElementById("upgradePanel").classList.toggle("open");
}

// Buys one level of the upgrade at `index` if the player can afford it.
function purchaseUpgrade(index) {
    const upgrade = upgrades[index];
    const cost = upgradeCost(upgrade);
    if (cookies < cost) {
        return;
    }
    cookies -= cost;
    upgrade.owned++;
    clickPower += upgrade.power;
    updateDisplay();
    updateUpgradeButtons();
}

renderUpgrades();
updateDisplay();

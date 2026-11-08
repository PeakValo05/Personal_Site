const toggleBtn = document.getElementById('toggleBtn');
const closeBtn = document.getElementById('closeBtn');
const sidePanel = document.getElementById('sidePanel');
const panelOverlay = document.getElementById('panelOverlay');

const setPanelState = (isOpen) => {
    sidePanel.classList.toggle('open', isOpen);
    panelOverlay.classList.toggle('open', isOpen);
    sidePanel.setAttribute('aria-hidden', String(!isOpen));
    panelOverlay.setAttribute('aria-hidden', String(!isOpen));
    toggleBtn.setAttribute('aria-expanded', String(isOpen));
    document.body.classList.toggle('panel-is-open', isOpen);
};

toggleBtn.addEventListener('click', () => {
    setPanelState(true);
    closeBtn.focus();
});

closeBtn.addEventListener('click', () => {
    setPanelState(false);
    toggleBtn.focus();
});

panelOverlay.addEventListener('click', () => {
    setPanelState(false);
    toggleBtn.focus();
});

document.addEventListener('keydown', (event) => {
    if (event.key === 'Escape' && sidePanel.classList.contains('open')) {
        setPanelState(false);
        toggleBtn.focus();
    }
});

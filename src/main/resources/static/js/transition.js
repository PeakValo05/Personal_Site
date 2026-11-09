document.addEventListener("click", (event) => {
    const link = event.target.closest("a[href]");

    if (!link) return;

    const url = new URL(link.href);

    // Only animate internal page navigation
    if (url.origin !== window.location.origin) return;

    // Don't interfere with new tabs or special links
    if (link.target === "_blank" ||
        link.hasAttribute("download") ||
        event.ctrlKey ||
        event.metaKey ||
        event.shiftKey ||
        event.altKey) return;

    // Let same-page anchor links scroll normally
    if (url.pathname === window.location.pathname &&
        url.search === window.location.search &&
        url.hash) return;

    event.preventDefault();

    document.body.classList.add("page-exit");

    setTimeout(() => {
        window.location.assign(url.href);
    }, 250);
});
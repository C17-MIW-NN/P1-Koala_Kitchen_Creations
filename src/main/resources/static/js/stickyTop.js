const NAVBAR_ID = 'navbar'
function updateStickyTop() {
    const navbar = document.getElementById(NAVBAR_ID);
    const stickyElements = document.getElementsByClassName('dynamic-sticky-top');

    if (navbar && stickyElements.length > 0 ) {
        const navHeight = navbar.offsetHeight;
        for (let stickyElement of stickyElements)
        stickyElement.style.top = `${navHeight}px`;
    }
}

updateStickyTop();

window.addEventListener('resize', updateStickyTop);

if ('ResizeObserver' in window) {
    const observer = new ResizeObserver(updateStickyTop);
    observer.observe(document.getElementById(NAVBAR_ID));
}
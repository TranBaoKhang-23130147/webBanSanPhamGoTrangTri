
document.addEventListener('DOMContentLoaded', function () {
    const sections = document.querySelectorAll('.settings-card');
    const navItems = document.querySelectorAll('.settings-menu a');
    const headerElement = document.querySelector('.header');
    const dynamicHeaderHeight = headerElement ? headerElement.offsetHeight + 10 : 70;

    let isScrollingFrame;
    let updateTimer;
    function removeActiveClass() {
        navItems.forEach(item => {
            item.closest('li').classList.remove('active');
        });
    }
    function updateActiveSection() {
        const isScrolledToBottom = (window.innerHeight + window.scrollY) >= (document.documentElement.scrollHeight - 5);

        if (isScrolledToBottom) {
            removeActiveClass();
            if (navItems.length > 0) {
                navItems[navItems.length - 1].closest('li').classList.add('active');
            }
            return;
        }
        let currentActiveSectionId = sections[0].getAttribute('id');
        for (let i = sections.length - 1; i >= 0; i--) {
            const section = sections[i];
            const rect = section.getBoundingClientRect();
            if (rect.top <= dynamicHeaderHeight) {
                currentActiveSectionId = section.getAttribute('id');
                break;
            }
        }
        removeActiveClass();
        const activeLink = document.querySelector(`.settings-menu a[href="#${currentActiveSectionId}"]`);
        if (activeLink) {
            activeLink.closest('li').classList.add('active');
        }
    }
    updateActiveSection();

    window.addEventListener('scroll', function () {
        window.cancelAnimationFrame(isScrollingFrame);
        isScrollingFrame = window.requestAnimationFrame(updateActiveSection);

        clearTimeout(updateTimer);
        updateTimer = setTimeout(function () {
            updateActiveSection();
        }, 100);
    });

    navItems.forEach(item => {
        item.addEventListener('click', function (e) {
            e.preventDefault();
            const targetId = this.getAttribute('href').substring(1);
            const targetElement = document.getElementById(targetId);

            if (targetElement) {
                removeActiveClass();
                this.closest('li').classList.add('active');

                window.scrollTo({
                    top: targetElement.offsetTop - dynamicHeaderHeight,
                    behavior: 'smooth'
                });
                setTimeout(() => {
                    updateActiveSection();
                }, 500);
            }
        });
    });
});
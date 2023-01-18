
const loader = document.querySelector('.preloader');
document.addEventListener('DOMContentLoaded', (e) => {
    setTimeout(() => {
        //splash.classList.add('display-none');
        loader.style.display = "none"
    }, 3000);
})


function onApplyBtnClick(e) {
    window.location.href = "/login";
}
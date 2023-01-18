const loader = document.querySelector('.preloader');

document.addEventListener('DOMContentLoaded', (e) => {
    setTimeout(() => {
        //splash.classList.add('display-none');
        if (loader) {
            loader.style.display = "none"
        }
    }, 3000);
})

const btn = document.querySelector('.splash_iniciar_btn');
btn.addEventListener('click', function(event){
    console.log('Button Clicked');
});
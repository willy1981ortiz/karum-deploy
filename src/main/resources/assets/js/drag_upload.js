//selecting all required elements

const uploadBlockBtn = document.getElementById("uploadBlockBtn");
const continueBtn = document.getElementById("continue_btn");
const dropArea = document.querySelector(".drag-area"),
    input = dropArea.querySelector("input");
let file, frontFile, backFile; //this is a global variable and we'll use it inside multiple functions

var isSecondImage = false;

function onClickUploadImg() {
    input.click(); //if user click on the button then the input also clicked
}

function onClickUploadBlockBtn() {
    input.click(); //if user click on the button then the input also clicked
}

input.addEventListener("change", function () {
    //getting user select file and [0] this means if user select multiple files then we'll select only the first one
    file = this.files[0];
    dropArea.classList.add("active");
    showFile(); //calling function
});


//If user Drag File Over DropArea
dropArea.addEventListener("dragover", (event) => {
    event.preventDefault(); //preventing from default behaviour
    dropArea.classList.add("active");
});

//If user leave dragged File from DropArea
dropArea.addEventListener("dragleave", () => {
    dropArea.classList.remove("active");
});

//If user drop File on DropArea
dropArea.addEventListener("drop", (event) => {
    event.preventDefault(); //preventing from default behaviour
    //getting user select file and [0] this means if user select multiple files then we'll select only the first one
    file = event.dataTransfer.files[0];
    showFile(); //calling function
});

function showFile() {
    if (isSecondImage) {
        backFile = file;
    } else {
        frontFile = file;
    }
    let fileType = file.type; //getting selected file type
    let validExtensions = ["image/jpeg", "image/jpg", "image/png"]; //adding some valid image extensions in array
    if (validExtensions.includes(fileType)) { //if user selected file is an image file
        /*let fileReader = new FileReader(); //creating new FileReader object
        fileReader.onload = () => {
            let fileURL = fileReader.result; //passing user file source in fileURL variable
            // let imgTag = `<img src="${fileURL}" alt="">`; //creating an img tag and passing user selected file source inside src attribute
            $(dropArea).find("img").attr("src", fileURL);
            // $(dropArea).append(imgTag); //adding that created img tag inside dropArea container
        }
        fileReader.readAsDataURL(file);*/

        // uploadBlockBtn.style.visibility = "visible";
        // uploadTextBlock.style.visibility = "visible";
        /*if (isSecondImage) {
            // uploadBlockBtn.style.visibility = "visible";
            $(continueBtn).html("Subir");
            continueBtn.style.visibility = "visible";

        } else {
            continueBtn.style.visibility = "visible";
        }*/
        onDocumentSelectContinue();
    } else {
        alert("No es imagen. Adjunte una imagen");
        dropArea.classList.remove("active");
    }
}


function onDocumentSelectContinue() {
    if (isSecondImage || mType === "a" || mType === "p"|| mType === "i") {
        if (uploadTextBlock) {
            uploadTextBlock.style.visibility = "hidden";
        }
        if (uploadTextBlockOnMobile) {
            uploadTextBlockOnMobile.style.visibility = "hidden";
        }

        if ($(".uploadBtnMobile").is(":visible")) {
            $(".uploadBtnMobile").css("display", "none")
        }
        $(dropArea).hide();
        continueBtn.style.visibility = "hidden";
        backBtn.style.visibility = "hidden";
        uploadBlockBtn.style.visibility = "hidden";

        if (mType == "p") {
            loadFileToImagePreview(frontFile)
            backBtn.style.visibility = "visible";
            forwardBtn.style.visibility = "visible";
            uploadPassport = true;
        } else {
            uploadImages();
        }
    } else {
        $(dropArea).find("img").attr("src", "/assets/media/curp_back.png");
        isSecondImage = true;
        $("#pageIndicator").html("Paso 2 de 2");
        $("#pageIndicatorOnMobile").html("Paso 2 de 2");
        $("#documentTitle").html("Subir parte trasera del  INE / IFE");
        continueBtn.style.visibility = "hidden";
    }
}

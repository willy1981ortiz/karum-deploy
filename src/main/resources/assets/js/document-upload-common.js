window.history.forward();

/************ Precaching sample images with JavaScript *************/
//preloading Images with jQuery
function preloadImages(images) {
    $(images).each(function(){
        $('<img/>')[0].src = this;
    });
}

preloadImages([
    '/assets/media/curp_front.png',
    '/assets/media/curp_back.png',
    '/assets/media/address.png',
    '/assets/media/ine-upload.png',
    '/assets/media/camera.png',
]);


const imagesPreview = document.getElementById("images-preview");
const progressBar = document.getElementById("progressAreaId");
const uploadTextBlock = document.getElementById("uploadTextBlock");
const uploadTextBlockOnMobile = document.getElementById("uploadTextBlockOnMobile");
const forwardBtn = document.getElementById("forward-btn");
const backBtn = document.getElementById("back-btn");

$("#passport-number-preview").css("display", "none");
$("#images-preview").hide();
$("#summary-container").hide();
$("#crossAreaId").hide();
var uploadPassport = false;

function uploadImages() {

    const formData = new FormData();

    $(progressBar).show();
    backBtn.style.visibility = "hidden";
    forwardBtn.style.visibility = "hidden";

    // add assoc key values, this will be posts values
    formData.append("frontFile", frontFile, frontFile.name);
    if (mType !== "a" && mType !== "p" && mType !== "i") {
        formData.append("backFile", backFile, backFile.name);
    }
    let passport = $("#passportNumberId").val();
    formData.append("passport", passport);

    $.ajax({
        type: "POST",
        url: mGBaseUrl + '/api/ine/upload?type=' + mType,
        xhr: function () {
            /*if (myXhr.upload) {
                myXhr.upload.addEventListener('progress', that.progressHandling, false);
            }*/
            return $.ajaxSettings.xhr();
        },
        success: function (res) {
            console.log(res);
            // your callback here
            $(progressBar).hide();

            let msg = null;
            if (res.success == false) {
                msg = res.msg;
            }

            if (mType === "") {
                showIneImagesSummary(!res.success, msg);
            } else {
                showImagesPreview(!res.success, msg);
            }

            if (res.success) {
                $("#summaryIne").val(res.data.ine);
                $("#generatedCurpId").val(res.data.curp);
                $("#apellidoPaternoId").val(res.data.parentSurname);
                $("#apellidoMaternoId").val(res.data.motherSurname);
                $("#nombreId").val(res.data.name);
                $("#gender").val(res.data.gender);
                if (res.data.dob) {
                    $("#dateOfBirthId").val(res.data.dob.split("/").reverse().join("-"));
                } else {
                    $("#dateOfBirthId").val("");
                }
                $("#stateOfBirth").val(res.data.birthState);
                $("#registerYear").val(res.data.yearRegister);
                $("#address1").val(res.data.address1);
                $("#address2").val(res.data.address2);
                $("#address3").val(res.data.address3);
                $("#yearOfExpire").val(res.data.yearOfExpire);
                $("#ocrInput").val(res.data.ocr);
                $("#cicInput").val(res.data.cic);
            }

        },
        error: function (error) {
            // handle error
            $(progressBar).hide();

            if (mType === "") {
                showIneImagesSummary(false);
            } else {
                showImagesPreview(true);
            }

            if (mIsHandoff) {
                const userStatus = res.extra;
                if (!isNaN(userStatus) && userStatus >= 2) {
                    $("#forward-btn").hide();
                }
            }
        },
        async: true,
        data: formData,
        cache: false,
        contentType: false,
        processData: false,
        timeout: 60000
    });
}


function loadFileToImagePreview(file) {
    $("#images-preview").show();
    let fileReader = new FileReader(); //creating new FileReader object
    fileReader.onload = () => {
        let fileURL = fileReader.result; //passing user file source in fileURL variable
        let imgTag = `<img src="${fileURL}" alt="" style="max-width: 80%;" class="addressMobileImgPreview">`; //creating an img tag and passing user selected file source inside src attribute
        $("#images-preview").append(imgTag); //adding that created img tag inside dropArea container
        if (mType === "p") {
            $("#passport-number-preview").css("display", "flex") //adding that created img tag inside dropArea container
            $(".continueBtnBlock").css("display", "none") //adding that created img tag inside dropArea container
        }
    }
    fileReader.readAsDataURL(file);
}


function loadFileToImagePreviewSummary(file) {
    let fileReader = new FileReader(); //creating new FileReader object
    fileReader.onload = () => {
        let fileURL = fileReader.result; //passing user file source in fileURL variable
        let imgTag = `<img src="${fileURL}" alt="" style="max-width: 450px; width: 100%; display:block; margin-bottom:20px;" class="addressMobileImgPreview">`; //creating an img tag and passing user selected file source inside src attribute
        $("#summary-images-preview").append(imgTag); //adding that created img tag inside dropArea container
    }
    fileReader.readAsDataURL(file);
}

function onClickSubmit() {

    if (mIsHandoff) {
        if (mType === "i" || mType === "a") {
            if (mType === "a") {
                userStatusToPoa();
            }
            window.location = mGBaseUrl + "/goodByeMobile";
            return;
        } else if (mType === "p") {
            if (!uploadPassport) {
                if (mIsReturning) {
                    window.location = mGBaseUrl + "/goodByeMobile";
                } else {
                    window.location = mGBaseUrl + "/upload/document?type=a"
                }
                return;
            }
        } else {
            if (mIsReturning) {
                window.location = mGBaseUrl + "/goodByeMobile";
            } else {
                summaryDataAPI();
//                window.location = mGBaseUrl + "/upload/document?type=a"
            }
            return;
        }
    }

    if (mType === "i") {
        window.location = mGBaseUrl + "/document";
        return
    } else if (mType === "a") {
        if (mIsReturning) {
            window.location = mGBaseUrl + "/document"
        } else {
            protectBack();
            userStatusToPoa();
//            window.location = mGBaseUrl + "/person_info"
        }
    } else {
        if (mType === "p" && uploadPassport) {
            if (mType === "p") {
                let passport = $("#passportNumberId").val();
                if (passport.length === 12) {
                    $("#passportNumberError").css("display", "none")
                } else {
                    $("#passportNumberError").css("display", "block")
                    return
                }
            }

            $("#passport-number-preview").css("display", "none")
            $("#passportNumberId").prop("disabled", "true");
            $("#passportNumberId").css("border", "none");
            $("#passport_number_instructions").hide()
            $("#images-preview").html("");
            uploadPassport = false;
            uploadImages();
        } else {
            if (mIsReturning) {
                window.location = mGBaseUrl + "/document"
            } else {
                if (mType == "p") {
                   window.location = mGBaseUrl + "/upload/document?type=a";
                   return;
                }
                summaryDataAPI();
            }
        }
    }
}

function summaryDataAPI() {
    let form = $('#summary_data_form');
    form.validate({
        rules: {
            parent_surname: {required: true},
            mother_surname: {required: true},
            name: {required: true},
            gender: {required: true},
            stateOfBirth: {required: true},
            date_of_birth: {required: true},
        },
        messages: {
            parent_surname: { required: "El campo es obligatorio" },
            mother_surname: { required: "El campo es obligatorio" },
            name: { required: "El campo es obligatorio" },
            gender: { required: "El campo es obligatorio" },
            stateOfBirth: { required: "El campo es obligatorio" },
            date_of_birth: { required: "El campo es obligatorio" }
        }
    });
    if (!form.valid()) {
        return;
    }
    form.ajaxSubmit({
        url: mGBaseUrl + '/api/summary/set', success: function (response, status, xhr, $form) {
            console.log("response", response);
            if (response.success) {
                console.log(response.msg)
            }
            window.location = mGBaseUrl + "/upload/document?type=a"
        }, error: function (err, err1, err3) {
            console.log(err)
            alert('¡Algo salió mal, inténtalo de nuevo más tarde!');
        }
    });
}

$("#passportNumberId").keyup(function () {
    if ($("#passportNumberId").val().length === 12) {
        $("#passportNumberError").css("display", "none")
    }
});


function showImagesPreview(error, msg) {
    loadFileToImagePreview(frontFile);
    if (mType !== "a" && mType !== "p" && mType !== "i") {
        loadFileToImagePreview(backFile);
    }
    var errorMsg = "Errores encontrados en el INE";
    if (msg != null) {
        errorMsg = msg;
    } else if (mType === "p") {
        errorMsg = "Errores encontrados en el Pasaporte";
    } else if (mType === "a") {
        errorMsg = "Errores encontrados en el comprobante de domicilio";
    } else if (mType === "i") {
        errorMsg = "Errores encontrados en el comprobante de ingresos";
    }
    if (error) {
        backBtn.style.visibility = "visible";
        $("#bottom-message").html("<p style=\"font-size:1.5rem; padding-left:16px; padding-right:16px; color:red; text-align:center; margin-top:16px\">" + errorMsg + "</p>");
        forwardBtn.style.visibility = "hidden";
    } else {
        backBtn.style.visibility = "visible";
        $("#bottom-message").html("<p style=\"font-size:1.5rem; padding-left:16px; padding-right:16px; color:#ff6700; text-align:center; margin-top:16px\">Descarga completa</p>");
        forwardBtn.style.visibility = "visible";
    }
//    forwardBtn.style.visibility = "visible";
}

function showIneImagesSummary(error, msg) {
    $("#uploadContainer").hide();
    $("#summary-container").show();
    loadFileToImagePreviewSummary(frontFile);
    loadFileToImagePreviewSummary(backFile);

    var errorMsg = "Errores encontrados en el INE";
    if (msg != null) {
        errorMsg = msg;
    }

    if (error) {
        backBtn.style.visibility = "visible";
        $("#crossAreaId").show();
        $("#bottom-message").html("<p style=\"font-size:1.5rem; padding-left:16px; padding-right:16px; color:red; text-align:center; margin-top:16px\">" + errorMsg + "</p>");
    } else {
        backBtn.style.visibility = "visible";
        $("#bottom-message").html("<p style=\"font-size:1.5rem; padding-left:16px; padding-right:16px; color:#ff6700; text-align:center; margin-top:16px\">Descarga completa</p>");
        forwardBtn.style.visibility = "visible";
    }
//    forwardBtn.style.visibility = "visible";
    $("#containerForSummary").css("padding-top", "10px");
    $("#identificationText").css("margin", "0");
    $("#identificationText").css("display", "inline-block");
    $("#ineHeadText").css("margin", "0");
    $("#ineHeadText").css("display", "inline-block");
}

//For Passport Restrict Special Characters
$('.phoneValid').on('keypress', function (event) {
    var regex = new RegExp("^[a-zA-Z0-9]+$");
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
       event.preventDefault();
       return false;
    }
});

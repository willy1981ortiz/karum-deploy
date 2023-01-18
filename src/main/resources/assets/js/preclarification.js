window.history.forward();

/*  Disable copy paste functionality */
$('.block-copy-paste').on("cut copy paste", function (e) {
    e.preventDefault();
});

/*  Disable link and Enable after two minutes */
document.getElementById("reenviar_codigo_id").style.pointerEvents = "none";
document.getElementById("reenviar_codigo_id").style.cursor = "default";


const accept1ErrorSpan = document.getElementById('acceptError1Id');
const accept2ErrorSpan = document.getElementById('acceptError2Id');
const accept3ErrorSpan = document.getElementById('acceptError3Id');
const authCodeErrorSpan = $("#authCodeErrorId");
const accept1CheckBox = $("#acceptDocuments1");
const accept2CheckBox = $("#acceptDocuments2");
const accept3CheckBox = $("#acceptDocuments3");
const acceptLink1 = $("#acceptDocumentsLink1");
const acceptLink2 = $("#acceptDocumentsLink2");
const acceptLink3 = $("#acceptDocumentsLink3");

acceptLink1.click(function () {
    accept1ErrorSpan.style.display = "none"
    $("#authModal").modal("hide");
    $("#document1").show();
    $("#main-container").hide();
    document.getElementById("acceptDocuments1").checked = true;
});

accept1CheckBox.change(function () {
    if ($(this).prop("checked")) {
        accept1ErrorSpan.style.display = "none";
    } else {
        accept1ErrorSpan.style.display = "block"
    }
});

acceptLink2.click(function () {
    $("#authModal").modal("hide");
    $("#document2").show();
    $("#main-container").hide();
    accept2ErrorSpan.style.display = "none";
    document.getElementById("acceptDocuments2").checked = true;
});

accept2CheckBox.change(function () {
    if ($(this).prop("checked")) {
        accept2ErrorSpan.style.display = "none";
    } else {
        accept2ErrorSpan.style.display = "block";
    }
});

acceptLink3.click(function () {
    $("#authModal").modal("hide");
    $("#document3").show();
    $("#main-container").hide();
    accept3ErrorSpan.style.display = "none";
    document.getElementById("acceptDocuments3").checked = true;
});

accept3CheckBox.change(function () {
    if ($(this).prop("checked")) {
        accept3ErrorSpan.style.display = "none"
    } else {
        accept3ErrorSpan.style.display = "block"
    }
});

function onPrivacyForward() {
    $("#document1").hide();
    $("#document2").hide();
    $("#document3").hide();
    $("#main-container").show();
    $("#authModal").modal("show");
    enableOtpResend();
}

function onValidateOTP(el) {
    let form = $("#otp_form");

    /*    form.validate({
            rules: {
                auth_code: {
                    required: true,
                },
                messages: {
                    auth_code: {
                        required: "Codigo invalido o faltante",
                    }
                }
            }
        });

        if (!form.valid()) {
            return;
        }*/

    if (form[0].auth_code.value.length > 5) {
        authCodeErrorSpan.css("display", "none");
    } else {

        authCodeErrorSpan.text("Invalido, error");
        authCodeErrorSpan.css({"display": "block", "color": "red"});
    }

    var accept1CheckBoxStatus = false;
    var accept2CheckBoxStatus = false;
    var accept3CheckStatus = false;

    if (!accept1CheckBox.prop("checked")) {
        accept1ErrorSpan.style.display = "block"
        accept1CheckBoxStatus = true
    } else {
        accept1ErrorSpan.style.display = "none"
        accept1CheckBoxStatus = false
    }
    if (!accept2CheckBox.prop("checked")) {
        accept2ErrorSpan.style.display = "block"
        accept2CheckBoxStatus = true;
    } else {
        accept2ErrorSpan.style.display = "none"
        accept2CheckBoxStatus = false;
    }
    if (!accept3CheckBox.prop("checked")) {
        accept3ErrorSpan.style.display = "block"
        accept3CheckStatus = true;
    } else {
        accept3ErrorSpan.style.display = "none"
        accept3CheckStatus = false;
    }

    if (accept1CheckBoxStatus === true || accept2CheckBoxStatus === true || accept3CheckStatus === true) {
        return;
    }

    form.ajaxSubmit({
        url: mGBaseUrl + 'api/tc42/otp',
        success: function (response, status, xhr, $form) {
            console.log("response", response);
            if (response.success) {
                // showErrorMsg('Otp sent Successfully!');
                $("#authModal").modal("hide");
                $("#main-container").css("filter", "blur(5px)");
                // $("#authModal").css("filter", "blur(3px)");
                $('#revisionModal').modal({backdrop: 'static', keyboard: false});
                $("#revisionModal").modal("show");
            } else {
                if (response.msg == "Tramite declinado, ofrece al cliente las promociones vigentes.") {
                    $("#authModal").modal("hide");
                    $("#main-container").css("filter", "blur(5px)");
                    $('#authErrorModal').modal({backdrop: 'static', keyboard: false});
                    $("#authErrorModal").modal("show");
                    return;
                }
                authCodeErrorSpan.text(response.msg);
                authCodeErrorSpan.css({"display": "block", "color": "red"});
                //alert(response.msg);
            }
        }, error: function (err, err1, err3) {
            //alert('¡Algo salió mal, inténtalo de nuevo más tarde!');
            authCodeErrorSpan.text("Invalido, error");
            authCodeErrorSpan.css({"display": "block", "color": "red"});
        }
    });
}

function onDismissSuccessModel() {
    $("#main-container").css("filter", "blur(0px)");
    $("#authModal").css("filter", "blur(0px)");
    $("#revisionModal").modal("hide");
}

function onDismissDeclineModel() {
    $("#main-container").css("filter", "blur(0px)");
    $("#authErrorModal").modal("hide");
    window.location = mGBaseUrl + "goodBye"
}

function onClickResendOtp() {
    $.ajax({
        url: 'api/tc41/resubmit',
        type: "POST",
        success: function (response, status, xhr, $form) {
            if (response.success) {
                const spanElement = $("#authCodeErrorId");
                spanElement.css({"display": "block", "color": "#2EDB7C"})
                spanElement.text("Código Reenviado”");
            } else {
                const spanElement = $("#authCodeErrorId");
                spanElement.css({"display": "block", "color": "red"})
                spanElement.text(response.msg);
            }
        },
        error: function (err, err1, err3) {
            alert('¡Algo salió mal, inténtalo de nuevo más tarde!');
            console.log(err);
        }
    });
}

//$("#notAcceptError").hide();

function enableOtpResend(){
    document.getElementById("reenviar_codigo_id").style.pointerEvents = "none";
    document.getElementById("reenviar_codigo_id").style.cursor = "default";

    setTimeout(function () {
        document.getElementById("reenviar_codigo_id").style.pointerEvents="auto";
        document.getElementById("reenviar_codigo_id").style.cursor="pointer";

        document.getElementById("reenviar_codigo_id").style.color = "#0000ff";
    }, 60000 * 5);
}

function showAuthModal(userStatus) {

    var acceptRadioBtn = document.getElementsByClassName("acceptRadioBtn");
    for (let radio of acceptRadioBtn) {
        if (radio.type == "radio" && !radio.checked) {
//            $("#notAcceptError").show();
            $("#notAcceptError").css("display","block");
            return;
        }
    }

    $("#notAcceptError").hide();

    if (userStatus == TC41_API_COMPLETE) {
        $('#authModal').modal({backdrop: 'static', keyboard: false});
        $("#authModal").modal("show");
        enableOtpResend();
        return;
    }

    $("#loaderModal").modal("show");
    $.ajax({
        url: mGBaseUrl + 'api/tc41/otpSend',
        type: "POST",
        success: function (response, status, xhr, $form) {
            setTimeout(function () {
                $("#loaderModal").modal("hide");
            }, 1000);
            console.log("response", response);

            if (response.success) {
                $("#loaderModal").modal("hide");
                $('#authModal').modal({backdrop: 'static', keyboard: false});
                $("#authModal").modal("show");
                enableOtpResend();
            } else {
                alert(response.msg);
            }
        }, error: function (err, err1, err3) {
            setTimeout(function () {
                $("#loaderModal").modal("hide");
            }, 1000);

            alert('¡Algo salió mal, inténtalo de nuevo más tarde!');
        }
    });
}

function closeAuthModal() {
    $("#authModal").modal("hide");
}

$(".authInput").keyup(function () {
    if ($(".authInput").val().length === 6) {
        $("#authCodeErrorId").css("display", "none")
    }
})

//For Auth Code Restrict Special Characters
$('.authInput').on('keypress', function (event) {
    var regex = new RegExp("^[a-zA-Z0-9]+$");
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
        event.preventDefault();
        return false;
    }
});

//For Telephone Restrict Max Length
$('.authInput').on('input', function (event) {
    if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);
});

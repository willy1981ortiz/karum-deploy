"use strict";

document.addEventListener("DOMContentLoaded", function(event) {

    function OTPInput() {
        const inputs = document.querySelectorAll('#otp > *[id]');
        for (let i = 0; i < inputs.length; i++) {
            inputs[i].addEventListener('keydown', function(event) {
                if (event.key==="Backspace" ) {
                    inputs[i].value='' ;
                    if (i !==0) inputs[i - 1].focus();
                }
                else {
                    if (i===inputs.length - 1 && inputs[i].value !=='' ) {
                        return true;
                    }
                    else if (event.keyCode> 47 && event.keyCode < 58) {
                        inputs[i].value=event.key;
                        if (i !==inputs.length - 1)
                            inputs[i + 1].focus();
                        event.preventDefault();
                    }
                    else if (event.keyCode> 64 && event.keyCode < 91) {
                        inputs[i].value=String.fromCharCode(event.keyCode);
                        if (i !==inputs.length - 1) inputs[i + 1].focus();
                        event.preventDefault();
                    }
                }
                $("#errorMessageId").text("");
            });
        }
    }
    OTPInput(); });

$("#phone_number_id").keypress(function (event) {
    if (event.keyCode === 13) {
        $('#kt_login_signin_submit').click();
    }
});

$("#six").keypress(function (event) {
    if (event.keyCode === 13) {
        onOtpBtnClick();
    }
});

// Class Definition
let KTLoginGeneral = function () {
    let handleSignInFormSubmit = function () {
        $('#kt_login_signin_submit').click(function (e) {
            e.preventDefault();
            let btn = $(this);
            let form = $(this).closest('form');

            form.validate({
                rules: {
                    phone: {required: true}
                },
                messages: {
                    phone: {
                        required: "Por favor, inserte un número celular válido",
                        minlength: jQuery.validator.format("Por favor, inserte {0} números")
                    }
                }
            });

            if (!form.valid()) {
                return;
            }

            var isCheckBoxValid = true
            const acceptErrorSpan = document.getElementById('acceptErrorId');
            const receiveMsgErrorSpan = document.getElementById("receiveMsgErrorId");
            const acceptCheckBox = $("#acceptPrivacyCheckId");
            const readCheckBox = $("#readPrivacyCheckId");
            const receiveCheckBox = $("#receiveMessageCheckId");


            acceptCheckBox.change(function () {
                if ($(this).prop("checked")) {
                    acceptErrorSpan.style.display = "none"
//                    window.location.href = mGBaseUrl + '/documentPrivacy';
                } else {
                    acceptErrorSpan.style.display = "block"
                    acceptErrorSpan.innerHTML = "Por favor acepta documento";
                }
            });

            receiveCheckBox.change(function () {
                if ($(this).prop("checked")) {
                    receiveMsgErrorSpan.style.display = "none"
//                    window.location.href = mGBaseUrl + '/documentKarum';
                } else {
                    receiveMsgErrorSpan.style.display = "block"
                    receiveMsgErrorSpan.innerHTML = "Por favor acepta documento";
                }
            });

            if (!acceptCheckBox.prop("checked")) {
                acceptErrorSpan.style.display = "block"
                acceptErrorSpan.innerHTML = "Por favor acepta documento";
                isCheckBoxValid = false
            } else {
                acceptErrorSpan.style.display = "none"
            }

            if (!receiveCheckBox.prop("checked")) {
                receiveMsgErrorSpan.style.display = "block"
                receiveMsgErrorSpan.innerHTML = "Por favor acepta documento";
                isCheckBoxValid = false
            } else {
                receiveMsgErrorSpan.style.display = "none"
            }

            if (!isCheckBoxValid) {
                return;
            }

            btn.addClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', true);

            $("#country").val(iti.getSelectedCountryData().dialCode);

            form.ajaxSubmit({
                url: mGBaseUrl + 'api/otp', success: function (response, status, xhr, $form) {
                    console.log("response", response);
                    if (response.success) {
                        // showErrorMsg('Otp sent Successfully!');

                        window.location = mGBaseUrl + "/login/otp";
                    } else {
                        btn.removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
                        showErrorMsg(form, 'danger', response.msg);
                    }
                }, error: function (err, err1, err3) {

                    btn.removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
                    showErrorMsg(form, 'danger', '¡Algo salió mal, inténtalo de nuevo más tarde!');
                }
            });
        });
    };

    // Public Functions
    return {
        // public functions
        init: function () {
            handleSignInFormSubmit();
        }
    };
}();

KTLoginGeneral.init();

const loginContainer = document.getElementById('loginContainer');
const documentPrivacyContainer = document.getElementById('documentPrivacyContainer');
const documentKarumContainer = document.getElementById('documentKarumContainer');

$("#acceptPrivacyLinkId").click(function () {
    document.getElementById("acceptPrivacyCheckId").checked = true;
    loginContainer.style.display = "none";
    documentKarumContainer.style.display = "none";
    documentPrivacyContainer.style.display = "block";
});

$("#receiveMessageLinkId").click(function () {
    document.getElementById("receiveMessageCheckId").checked = true;
    loginContainer.style.display = "none";
    documentPrivacyContainer.style.display = "none";
    documentKarumContainer.style.display = "block";
});

$('#documentPrivacyBackBtn').click(function () {
    loginContainer.style.display = "block";
    documentPrivacyContainer.style.display = "none";
    documentKarumContainer.style.display = "none";
});

$('#documentKarumBackBtn').click(function () {
    loginContainer.style.display = "block";
    documentPrivacyContainer.style.display = "none";
    documentKarumContainer.style.display = "none";
});

let input = document.querySelector("#country_picker_id");
let iti = null;
if (input) {
    window.intlTelInput(input, {
        hiddenInput: "full", // separateDialCode: true,
        utilsScript: "https://cdnjs.cloudflare.com/ajax/libs/intl-tel-input/17.0.8/js/utils.js",
        allowDropdown: true,
        initialCountry: "MX",
        preferredCountries: ['mx'],
        autoHideDialCode: true,
    });
    iti = window.intlTelInputGlobals.getInstance(input);
    input.addEventListener("countrychange", function () {

        let name = iti.getSelectedCountryData().name.split(" ")[0];

        let code = iti.getSelectedCountryData().dialCode;
        $("#country_picker_id").val(name + "   " + code);
    });
}

function showErrorMsg(msg) {
    $("#errorMessageId").text(msg);
}

function onOtpBtnClick(e) {

    let first = document.getElementById("first");
    let second = document.getElementById("sec");
    let third = document.getElementById("third");
    let fourth = document.getElementById("fourth");
    let fifth = document.getElementById("fifth");
    let sixth = document.getElementById("six");


    if (first.value.length === 0 || second.value.length === 0 || third.value.length === 0 || fourth.value.length === 0 || fifth.value.length === 0 || sixth.value.length === 0) {
//        $("#errorMessageId").text("Codice non valido inserito");
        $("#errorMessageId").text("Favor de insertar los 6 dígitos enviados a su número celular");
    } else {
        //let btn = $(e);
        let form = $("#optFormId");

        form.radioBtn

        form.validate({
            rules: {
                first: {required: true},
                second: {required: true},
                third: {required: true},
                fourth: {required: true},
                fifth: {required: true},
                sixth: {required: true},
            }, messages: {
                first: {required: ""},
                second: {required: ""},
                third: {required: ""},
                fourth: {required: ""},
                fifth: {required: ""},
                sixth: {required: ""},
            }
        });

        if (!form.valid()) {
            return;
        }

        // btn.addClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', true);

        form.ajaxSubmit({
            url: mGBaseUrl + '/api/verify/otp', success: function (response, status, xhr, $form) {
                console.log("response", response);
                if (response.success) {
                    // showErrorMsg(form, 'success', 'Otp sent Successfully!');

//                    window.location.href = "/identification";
                    window.location.href = mGBaseUrl + "/welcome";
                } else {
                    /*if (response.msg == "isDeclined") {
                        $("#main-container").css("filter", "blur(5px)");
                        $('#authErrorModal').modal({backdrop: 'static', keyboard: false});
                        $("#authErrorModal").modal("show");
                    } else {
                        showErrorMsg("Favor de insertar los 6 dígitos enviados a su número celular");
                    }*/
                    showErrorMsg("Favor de insertar los 6 dígitos enviados a su número celular");
                    // btn.removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
//                    showErrorMsg("Codice non valido inserito");
                }
            }, error: function (err, err1, err3) {
                // btn.removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
                showErrorMsg('¡Algo salió mal, inténtalo de nuevo más tarde!');
            }
        });
    }

}

function onLoginDismissDeclineModel() {
    window.location.href = mGBaseUrl + "/goodBye";
}

//$(".iti--allow-dropdown").append("<i class='fa fa-chevron-down' style='position: absolute; right: 5px; color: red; padding:12px; cursor:pointer'></i>");
/*setTimeout(function () {
    $("#phone").on("click", function () {
        iti._a10();
    });
    /!* $(".fa-chevron-down").on("click", function (){
         iti._a10();
     });*!/
}, 1000);*/

$('.block-copy-paste').on("cut copy paste", function (e) {
    e.preventDefault();
});

$('#phone_number_id').on('keypress', function (event) {
    var regex = new RegExp("^[a-zA-Z0-9]+$");
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
        event.preventDefault();
        return false;
    }
});

$('#phone_number_id').on('input', function (event) {
    if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);
});

$('.text-center').on('keypress', function (event) {
    var regex = new RegExp("^[a-zA-Z0-9]+$");
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
        event.preventDefault();
        return false;
    }
});

$('.text-center').on('input', function (event) {
    if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);
});

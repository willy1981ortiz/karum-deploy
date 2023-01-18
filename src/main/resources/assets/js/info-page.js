window.history.forward();

/*window.onload = function () {
    window.history.forward();
}*/
//////////////////////////
function onClickGenerateCurp(el) {
    let form = $("#personal_info_form");

    form.validate({
        rules: {
            parent_surname: { required: true },
            mother_surname: { required: true },
            name: { required: true },
            street: { required: true },
            ext_no: { required: true },
            zip: { required: true },
            state: {required: true},
            municipality: {required: true},
            colony: {required: true},
            telephone: {required: true},
            confirm_telephone: { required: true },
            email: { required: true },
            company_name: { required: true },
            monthly_income: { required: true }
        },
        messages: {
            parent_surname: { required: "El campo es obligatorio" },
            mother_surname: { required: "El campo es obligatorio" },
            name: { required: "El campo es obligatorio" },
            email:{
                required: "El campo es obligatorio",
                email: "Por favor, ingresa un email valido"
            },
            street: { required: "El campo es obligatorio" },
            ext_no: { required: "El campo es obligatorio" },
            zip: {
                required: "El campo es obligatorio",
                minlength: "Por favor captura su código postal"
            },
            state: { required: "El campo es obligatorio" },
            municipality: { required: "El campo es obligatorio" },
            colony: { required: "El campo es obligatorio" },
            telephone: {
                required: "El campo es obligatorio",
                minlength: jQuery.validator.format("Por favor, inserte {0} caracteres")
            },
            confirm_telephone: {
                required: "El campo es obligatorio",
                minlength: jQuery.validator.format("Por favor, inserte {0} caracteres")
            },
            company_name: { required: "El campo es obligatorio" },
            monthly_income: { required: "El campo es obligatorio" },
        }
    });

    if (!form.valid()) {
        return;
    }

    paterno = $("#apellidoPaternoId").val();
    materno = $("#apellidoMaternoId").val();
    nombreValue = $("#nombreId").val();
    //estadoId = $("#estadoId").val();

    estadoId = $("#estadoId").find(":selected").attr("curp_code");
    gender = $("#gender").val();

    let year = $("#year").val();
    let month = $("#month").val();
    let day = $("#day").val();

    let generatedCurp = generaCurp({
        apellido_paterno: paterno,
        apellido_materno: materno,
        nombre: nombreValue,
        sexo: gender,
        estado: estadoId,
        fecha_nacimiento: [day, month, year]
    });


    curpInputValue = $("#curpInputId").text();
    console.log(generatedCurp);
    if (generatedCurp === curpInputValue) {
        // Check Telephone and confirm telephone both are same
        /*if (checkSameTelephoneValidity()) {
            return
        }*/

        onSubmitPersonInfoForm(this)
    } else {
        const errorElement = document.getElementById('curpErrorId');
        errorElement.style.display = "block"
        errorElement.innerHTML = "Please enter a valid curp value";
        setTimeout(() => {
            //splash.classList.add('display-none');
            errorElement.style.display = "none"
        }, 5000);
    }
}

$("#confirmTelephoneId").keyup(function () {
    checkSameTelephoneValidity()
});
// Disable copy past on confirm cell input field
$('.block-copy-paste').on("cut copy paste",function(e) {
    e.preventDefault();
});

function checkSameTelephoneValidity() {
    let telephoneValue = $("#telephoneId").val();
    let confirmTelephoneValue = $("#confirmTelephoneId").val();

    const confirmNumberErrorSpan = document.getElementById('confirmNumberErrorId');
    if (telephoneValue === confirmTelephoneValue) {
        confirmNumberErrorSpan.style.display = "none"
        return false
    } else {
        confirmNumberErrorSpan.style.display = "block"
        confirmNumberErrorSpan.innerHTML = "El Teléfono celular debe coincidir, por favor inserte el Teléfono celular correcto";

        return true
    }
}

$('#apellidoPaternoId').on('change', function () {
    checkAndGenerateCurp()
});

$('#apellidoMaternoId').on('change', function () {
    checkAndGenerateCurp()
});

$('#nombreId').on('change', function () {
    checkAndGenerateCurp()
});

$('#fechaNacimientoId').on('change', function () {
    checkAndGenerateCurp()
});

$('#gender').on('change', function () {
    checkAndGenerateCurp()
});

$('#estadoId').on('change', function () {
    checkAndGenerateCurp()
});

function checkAndGenerateCurp() {
    let paterno = $("#apellidoPaternoId").val();
    let materno = $("#apellidoMaternoId").val();
    let nombreValue = $("#nombreId").val();
    //let estadoId = $("#estadoId").val();
    let estadoId = $("#estadoId").find(":selected").attr("curp_code");
    let gender = $("#gender").val();


    let year = $("#year").val();
    let month = $("#month").val();
    let day = $("#day").val();
    if (paterno !== '' && materno !== '' && nombreValue !== '') {
        let generatedCurp = generaCurp({
            apellido_paterno: paterno,
            apellido_materno: materno,
            nombre: nombreValue,
            sexo: gender,
            estado: estadoId,
            fecha_nacimiento: [day, month, year]
        });
        if (generatedCurp === false) {
            $("#curpInputId").text("");
            $("#curpInputHidden").val("");
            $("#curpTickMark").css("visibility", "hidden");
        } else {
            $("#curpInputId").text(generatedCurp);
            $("#curpInputHidden").val(generatedCurp);
            $("#curpTickMark").css("visibility", "visible");
        }

    }

}

function isValidDate(txtDate) {
    var currVal = txtDate;
    if (currVal === '')
        return false;

    //Declare Regex
    //var rxDatePattern = /^(d{1,2})(|-)(d{1,2})(|-)(d{4})$/;
    var rxDatePattern = /^\d{4}-\d{2}-\d{2}$/;
    var dtArray = currVal.match(rxDatePattern); // is format OK?

    if (dtArray == null)
        return false;

    //Checks for mm/dd/yyyy format.
    dtMonth = dtArray[1];
    dtDay = dtArray[3];
    dtYear = dtArray[5];

    if (dtMonth < 1 || dtMonth > 12)
        return false;
    else if (dtDay < 1 || dtDay > 31)
        return false;
    else if ((dtMonth === 4 || dtMonth === 6 || dtMonth === 9 || dtMonth === 11) && dtDay === 31)
        return false;
    else if (dtMonth === 2) {
        var isleap = (dtYear % 4 === 0 && (dtYear % 100 !== 0 || dtYear % 400 === 0));
        if (dtDay > 29 || (dtDay === 29 && !isleap))
            return false;
    }
    return true;
}

/*///////////////// API CAll For Geo Data Fetch On Focus out of CP input field////////////*/
let estadoElement = $("#stateEl");
let municipioElement = $('#municipalityEl');
let coloniaElement = $('#colonyEl');
let ciudadElement = $("#cityEl");

municipioElement.on("change", function () {
    $("#municipalityCode").val($(this).find(":selected").attr("code"));
    if (municipioElement.val().length > 0) {
        municipioElement.css("border-color", "#2EDB7C");
    } else {
        municipioElement.css("border-color", "red");
    }
});
coloniaElement.on("change", function () {
    const colonyElement = $("#colonyCode");
    colonyElement.val($(this).find(":selected").attr("code"));
    if (coloniaElement.val().length > 0) {
        coloniaElement.css("border-color", "#2EDB7C");
    } else {
        coloniaElement.css("border-color", "red");
    }
    // Update CP Value on Change of colony
    let cp_code = $(this).find(":selected").attr("zip_code")
    if(cp_code !== undefined){
        $("#cpCodeId").val(cp_code);
    }
});
ciudadElement.on("change", function () {
    $("#cityCode").val($(this).find(":selected").attr("code"));
    if (ciudadElement.val().length > 0) {
        ciudadElement.css("border-color", "#2EDB7C");
    } else {
        ciudadElement.css("border-color", "red");
    }
});

$("#cpCodeId").on("input", function () {
    let cpCode = $("#cpCodeId").val();
    if (cpCode.length !== 5) {
        return
    }

    municipioElement.empty();
    coloniaElement.empty();
    ciudadElement.empty();

    municipioElement.css("border-color:red")
    coloniaElement.css("border-color:red")
    ciudadElement.css("border-color:red")

    $.ajax({
        data: jQuery.param({cp_code: cpCode}),
        url: mGBaseUrl + 'api/geoData/get',
        type: "POST",
        success: function (response, status, xhr, $form) {
            municipioElement.empty();
            coloniaElement.empty();
            ciudadElement.empty();
            console.log("response", response);
            if (response.success) {
                if (response.data) {
                    console.log(response.data)
                    //Select First Default Option
                    municipioElement.append(new Option("Seleccione uno", "", false, false)).trigger('change');
                    coloniaElement.append(new Option("Seleccione uno", "", false, false)).trigger('change');
                    ciudadElement.append(new Option("Seleccione uno", "", false, false)).trigger('change');
                    // Get File Response Data and loop through

                    const municipalityList = response.data.map(function (d) {
                        return d.municipio+"|||"+d.cmunicipio;
                    }).filter(unique);

                    const cityList = response.data.map(function (d) {
                        return d.ciudad+"|||"+d.c_cve_ciudad;
                    }).filter(unique);

                    const stateList = response.data.map(function (d) {
                        return d.estado+"|||"+d.Cestado;
                    }).filter(unique);

                    const colonyList = response.data.map(function (d) {
                        return d.asenta +"|||"+d.id_asenta_cpcons+"|||"+d.codigo;
                    }).sort();


                    stateList.forEach(function (e, index) {
                        // Update Value in estado Input field
                        let state = e.split("|||");
                        estadoElement.val(state[0]);
                        $("#stateCode").val(state[1]);
                    });

                    municipalityList.forEach(function (e, index) {
                        // Update Colonia List of options
                        let municipality = e.split("|||");
                        let municipioOption = new Option(municipality[0], municipality[0], false, false);
                        municipioOption.setAttribute("code", municipality[1]);
                        municipioElement.append(municipioOption).trigger('change');
                    });

                    colonyList.forEach(function (e, index) {
                        // Update Colonia List of options
                        let colony = e.split("|||");
                        let coloniaOption = new Option(colony[0], colony[0], false, false);
                        coloniaOption.setAttribute("code", colony[1]);
                        coloniaOption.setAttribute("zip_code", colony[2]);
                        coloniaElement.append(coloniaOption).trigger('change');
                    });

                    cityList.forEach(function (e, index) {
                        // Update Colonia List of options
                        let city = e.split("|||");

                        let cityOption = new Option(city[0], city[0], false, false);
                        cityOption.setAttribute("code", city[1]);
                        ciudadElement.append(cityOption).trigger('change');
                    });

                    $(".custom-info-form").removeClass("error");
                    $("#stateEl-error").text("");
                    $("#municipalityEl-error").text("");
                    $("#colonyEl-error").text("");
                    $("#cityEl-error").text("");
                }
            }
            // datatable.ajax.reload();
        },
        error: function (err, err1, err3) {
            console.log(err);
        }
    });
});
// Get Unique Element From Array with this method
const unique = (value, index, self) => {
    return self.indexOf(value) === index
}


$('#year').on('change', function () {
    validateAge();
    checkAndGenerateCurp()
});

$('#month').on('change', function () {
    validateAge();
    checkAndGenerateCurp()
});

$('#day').on('change', function () {
    validateAge();
    checkAndGenerateCurp()
});

function checkAgeValidation() {

}

//Create references to the dropdown's to select date of birth
const yearSelect = document.getElementById("year");
const monthSelect = document.getElementById("month");
const daySelect = document.getElementById("day");


const months = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];

//Months are always the same
(function populateMonths() {
    for (let i = 0; i < months.length; i++) {
        const option = document.createElement('option');
        option.textContent = months[i];
        option.value = (i + 1) + "";
        if (monthSelect) {
            monthSelect.appendChild(option);
        }
    }
})();

let previousDay;

function populateDays(month) {
    //Delete all of the children of the day dropdown
    //if they do exist
    const daySelectOldValue = daySelect.value;
    while (daySelect.firstChild) {
        daySelect.removeChild(daySelect.firstChild);
    }
    //Holds the number of days in the month
    let dayNum;
    //Get the current year
    let year = yearSelect.value;

//const months = [1'Enero', 2'Febrero', 3'Marzo', 4'Abril', 5'Mayo', 6'Junio', 7'Julio', 8'Agosto', 9'Septiembre', 10'Octubre', 11'Noviembre', 12'Diciembre'];

    if (month === '1' || month === '3' || month === '5' || month === '7' || month === '8'
        || month === '10' || month === '12') {
        dayNum = 31;
    } else if (month === '4' || month === '6' || month === '9' || month === '11') {
        dayNum = 30;
    } else {
        //Check for a leap year
        if (new Date(year, 1, 29).getMonth() === 1) {
            dayNum = 29;
        } else {
            dayNum = 28;
        }
    }
    //Insert the correct days into the day <select>
    for (let i = 1; i <= dayNum; i++) {
        const option = document.createElement("option");
        option.textContent = i;
        daySelect.appendChild(option);
    }
    if (previousDay) {
        daySelect.value = previousDay;
        if (daySelect.value === "") {
            daySelect.value = previousDay - 1;
        }
        if (daySelect.value === "") {
            daySelect.value = previousDay - 2;
        }
        if (daySelect.value === "") {
            daySelect.value = previousDay - 3;
        }
    }

    daySelect.value = daySelectOldValue;
}

function populateYears() {
    //Get the current year as a number
    let year = new Date().getFullYear();
    //Make the previous 100 years be an option
    for (let i = 0; i < 101; i++) {
        const option = document.createElement("option");
        option.textContent = year - i;
        if (yearSelect) {
            yearSelect.appendChild(option);
        }
    }
}

if (monthSelect) {
    populateDays(monthSelect.value);
    monthSelect.onchange = function () {
        populateDays(monthSelect.value);
    }
}
populateYears();

if (yearSelect) {
    yearSelect.onchange = function () {
        populateDays(monthSelect.value);
    }
}
if (daySelect) {
    daySelect.onchange = function () {
        previousDay = daySelect.value;
    }
}


const personInfoContainer = document.getElementById('personInfoContainer');
const documentConditionContainer = document.getElementById('documentConditionContainer');
const documentCreditContainer = document.getElementById('documentCreditContainer');
const documentMediaContainer = document.getElementById('documentMediaContainer');


$("#acceptConditionCheckId").change(function () {
    if ($(this).prop("checked")) {
        documentConditionContainer.style.display = "block";
        personInfoContainer.style.display = "none";
        documentCreditContainer.style.display = "none";
        documentMediaContainer.style.display = "none";
    }
});

$("#acceptCreditCheckId").change(function () {
    if ($(this).prop("checked")) {
        documentCreditContainer.style.display = "block";
        personInfoContainer.style.display = "none";
        documentConditionContainer.style.display = "none";
        documentMediaContainer.style.display = "none";
    }
});

$("#acceptMediaCheckId").change(function () {
    if ($(this).prop("checked")) {
        documentMediaContainer.style.display = "block";
        personInfoContainer.style.display = "none";
        documentConditionContainer.style.display = "none";
        documentCreditContainer.style.display = "none";
    }
});

$('#documentConditionBackBtn').click(function () {
    personInfoContainer.style.display = "block"
    documentConditionContainer.style.display = "none";
    documentCreditContainer.style.display = "none";
    documentMediaContainer.style.display = "none";
});

$('#documentCreditBackBtn').click(function () {
    personInfoContainer.style.display = "block"
    documentConditionContainer.style.display = "none";
    documentCreditContainer.style.display = "none";
    documentMediaContainer.style.display = "none";
});

$('#documentMediaBackBtn').click(function () {
    personInfoContainer.style.display = "block"
    documentConditionContainer.style.display = "none";
    documentCreditContainer.style.display = "none";
    documentMediaContainer.style.display = "none";
});


function onSubmitPersonInfoForm(el) {
    const form = $("#personal_info_form");

    var mobileEls = [form[0].company_phone];
    if (!validPhoneNumbers(mobileEls)) {
        return;
    }

    form.validate({
        rules: {
            parent_surname: {
                required: true
            },
            mother_surname: {
                required: true
            },
            state_birth: {
                required: true
            },
            dob: {
                required: true
            },
            gender: {
                required: true
            },
            curp: {required: true},
            street: {required: true},
            ext_no: {required: true},
            zip: {required: true},
            state: {required: true},
            // municipality: {required: true},
            colony: {required: true},
            /*city: {required: true},*/
            telephone: {required: true},
            // company_name: {required: true},
            // company_phone: {required: true},
            // monthly_income: {required: true},
        }
    });

    if (!form.valid() || !validateAge() || !form[0].checkValidity()) {
        return;
    }
    // btn.addClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', true);
    $("#loaderModal").modal("show");
    form.ajaxSubmit({
        url: mGBaseUrl + '/api/tc41/submit',
        success: function (response, status, xhr, $form) {
            console.log("response", response);
            setTimeout(function (){
                $("#loaderModal").modal("hide");
            }, 1000);
            if (response.success) {
                // showErrorMsg(form, 'success', 'Otp sent Successfully!');

                window.location.href = "/pre_clarification";
            } else {
                alert(response.msg);
                $("#loaderModal").modal("hide");
            }
        }, error: function (err, err1, err3) {
            setTimeout(function (){
                $("#loaderModal").modal("hide");
            }, 1000);
            alert('¡Algo salió mal, inténtalo de nuevo más tarde!');
        }
    });
}

if (typeof mGSelectedDob != "undefined") {
    let dateList = mGSelectedDob.split("-");
    $("#year").val(dateList[0]);
    $("#month").val(parseInt(dateList[1]));
    $("#day").val(parseInt(dateList[2]));
}


function validateAge() {
    let year = parseInt($("#year").val());
    let month = parseInt($("#month").val());
    let day = parseInt($("#day").val());
    let today = new Date();
    let currentMonth = today.getMonth() + 1;
    let currentDay = today.getDay();
//    let diff = today.getFullYear() - year;

    var age = getAge(year+"/"+month+"/"+day);
//    var maxAge = getAge(year+"/"+month+"/"+day, true);

    if (age > 74 || age < 21) {
        // $("#age_error").css("visibility", "visible");
        $("#age_error").show();
        return false;
    } else {
         // $("#age_error").css("visibility", "hidden");
         $("#age_error").hide();
         return true;
    }
}

function getAge(dateString) {
    var today = new Date();
    var birthDate = new Date(dateString);
    var age = today.getFullYear() - birthDate.getFullYear();
    var m = (today.getMonth() + 1) - (birthDate.getMonth() + 1);
    if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }
    return age;
}

//For Telephone Restrict Special Characters
$('.phoneValid').on('keypress', function (event) {
    var regex = new RegExp("^[a-zA-Z0-9]+$");
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
       event.preventDefault();
       return false;
    }
});

//For Telephone Restrict Max Length
$('.phoneValid').on('input', function (event) {
    if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);
});

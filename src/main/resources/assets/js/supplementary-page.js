/*if (mUserStatus >= TC44_API_COMPLETE) {
    window.location = mGBaseUrl + "/goodBye";
} else {
    window.history.forward();
}*/

if (window.performance && window.performance.navigation.type === window.performance.navigation.TYPE_BACK_FORWARD) {
    window.location.reload()
}

const zipCodeEl = $("#cpCodeId");

let estadoElement = $("#stateEl");
let municipioElement = $('#municipalityEl');
let coloniaElement = $('#colonyEl');
let ciudadElement = $("#cityEl");
let giroElement = $('#giroElementId');
let occupationElement = $('#occupationElementId');
let familyRelation1Element = $('#familyRelation1ElementId');
let familyRelation2Element = $('#familyRelation2ElementId');

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

giroElement.on("change", function () {

    if (giroElement.val().length > 0) {
        giroElement.css("border-color", "#2EDB7C");
    } else {
        giroElement.css("border-color", "red");
    }
});
occupationElement.on("change", function () {
    if (occupationElement.val().length > 0) {
        occupationElement.css("border-color", "#2EDB7C");
    } else {
        occupationElement.css("border-color", "red");
    }
});
familyRelation1Element.on("change", function () {
    if (familyRelation1Element.val().length > 0) {
        familyRelation1Element.css("border-color", "#2EDB7C");
    } else {
        familyRelation1Element.css("border-color", "red");
    }
});
familyRelation2Element.on("change", function () {
    if (familyRelation2Element.val().length > 0) {
        familyRelation2Element.css("border-color", "#2EDB7C");
    } else {
        familyRelation2Element.css("border-color", "red");
    }
});


function loadZipCodeData(callback) {
    let cpCode = zipCodeEl.val();
    if (cpCode.length !== 5) {
        if (callback) {
            callback();
        }
        return
    }

    municipioElement.empty();
    coloniaElement.empty();
    ciudadElement.empty();

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
            if (callback) {
                callback();
            }
            // datatable.ajax.reload();
        },
        error: function (err, err1, err3) {
            console.log(err);
            if (callback) {
                callback();
            }
        }
    });
// Get Unique Element From Array with this method
    const unique = (value, index, self) => {
        return self.indexOf(value) === index
    }
}

/*API CAll For Geo Data*/
zipCodeEl.on("input", function () {
    loadZipCodeData(null);
});

$("#load_from_home").on("change", function () {
    let form = $("form")[0];
    if ($("#load_from_home").prop("checked")) {
        if (typeof mTc41Data != "undefined") {
            form.street.value = mTc41Data.street;
            form.int_no.value = mTc41Data.intNo;
            form.ext_no.value = mTc41Data.extNo;
            form.zip.value = mTc41Data.zipCode;
            loadZipCodeData(function () {
                form.municipality.value = mTc41Data.municipality;
                form.colony.value = mTc41Data.colony;
                form.city.value = mTc41Data.city;
                form.state.value = mTc41Data.state;
                municipioElement.find("option:selected").siblings().prop("disabled", "true");
                coloniaElement.find("option:selected").siblings().prop("disabled", "true");
                ciudadElement.find("option:selected").siblings().prop("disabled", "true");
                municipioElement.trigger("change");
                coloniaElement.trigger("change");
                ciudadElement.trigger("change");
            });
            form.street.readOnly = true;
            form.int_no.readOnly = true;
            form.ext_no.readOnly = true;
            form.zip.readOnly = true;
            form.colony.readOnly = true;
            form.city.readOnly = true;
            form.state.readOnly = true;

            $(".custom-info-form").removeClass("error");
            $("#ext_no-error").text("");
            $("#street-error").text("");
            $("#zipCodInputId-error").text("");
            $("#stateEl-error").text("");
            $("#municipalityEl-error").text("");
            $("#colonyEl-error").text("");
            $("#cityEl-error").text("");
        }
    } else {
        form.street.value = "";
        form.int_no.value = "";
        form.ext_no.value = "";
        form.zip.value = "";
        form.municipality.value = "";
        form.colony.value = "";
        form.city.value = "";
        form.state.value = "";

        estadoElement.html("");
        municipioElement.html("");
        coloniaElement.html("");
        ciudadElement.html("");

        form.street.readOnly = false;
        form.int_no.readOnly = false;
        form.ext_no.readOnly = false;
        form.zip.readOnly = false;
        form.state.readOnly = false;
    }
});


function onSubmitForm(el) {
    const form = $("form");

    var mobileEls = [form[0].home_phone, form[0].telephone_reference_one, form[0].telephone_reference_two, form[0].personalReferencePhone];
    if (!validPhoneNumbers(mobileEls)) {
        return;
    }
    form.validate({
        rules: {
            profession: { required: true },
            home_phone: { required: true },
            supYear: { required: true },
            supMonth: { required: true },
            street: {required: true},
            ext_no: {required: true},
            zip: {required: true},
            state: {required: true},
            municipality: {required: true},
            colony: {required: true},
            /*city: {required: true},*/
            labYear: {required: true},
            labMonth: {required: true},
            name_reference_one: {required: true},
            telephone_reference_one: {required: true},
            name_reference_two: {required: true},
            telephone_reference_two: {required: true},
            familyRelation1: {required: true},
            familyRelation2: {required: true},
            giro: {required: true},
            occupation: {required: true},
            personalReferenceName: {required: true},
            personalReferencePhone: {required: true},
        },
        messages: {
            ext_no: { required: "Faltan datos" },
            zip: {
                required: "Faltan datos",
                minlength: "Por favor captura su código postal"
            },
            supYear: { required: "Faltan datos" },
            supMonth: {
                required: "Faltan datos",
                max: jQuery.validator.format("Por favor ingrese un valor menor o igual a {0}."),
                min: jQuery.validator.format("Por favor ingrese un valor mayor o igual a {0}.")
            },
            profession: { required: "El campo es obligatorio" },
            home_phone: {
                required: "El campo es obligatorio",
                minlength: jQuery.validator.format("Por favor, inserte {0} caracteres")
            },
            state: { required: "El campo es obligatorio" },
            street: { required: "El campo es obligatorio" },
            municipality: { required: "El campo es obligatorio" },
            colony: { required: "El campo es obligatorio" },
            /*city: { required: "El campo es obligatorio" },*/
            labYear: { required: "Faltan datos" },
            labMonth: {
                required: "Faltan datos",
                max: jQuery.validator.format("Por favor ingrese un valor menor o igual a {0}."),
                min: jQuery.validator.format("Por favor ingrese un valor mayor o igual a {0}.")
            },
            name_reference_one: { required: "El campo es obligatorio" },
            telephone_reference_one: {
                required: "El campo es obligatorio",
                minlength: jQuery.validator.format("Por favor, inserte {0} caracteres")
            },
            name_reference_two: { required: "El campo es obligatorio" },
            telephone_reference_two: {
                required: "El campo es obligatorio",
                minlength: jQuery.validator.format("Por favor, inserte {0} caracteres")
            },
            familyRelation1: { required: "El campo es obligatorio" },
            familyRelation2: { required: "El campo es obligatorio" },
            personalReferenceName: { required: "El campo es obligatorio" },
            personalReferencePhone: {
                required: "El campo es obligatorio",
                minlength: jQuery.validator.format("Por favor, inserte {0} caracteres")
            },
            giro: { required: "El campo es obligatorio" },
            occupation: { required: "El campo es obligatorio" },
        }
    });

    if (!form.valid() || !form[0].checkValidity()) {
        document.querySelector("#personal_reference_phone").reportValidity() && document.querySelector("#telephone_reference_one").reportValidity();
        return;
    }
    // btn.addClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', true);
    $("#loaderModal").modal("show");
    form.ajaxSubmit({
        url: mGBaseUrl + '/api/tc42/submit',
        success: function (response, status, xhr, $form) {
        setTimeout(function (){
            $("#loaderModal").modal("hide");
        }, 1000);

            console.log("response", response);
            if (response.success) {
                // showErrorMsg(form, 'success', 'Otp sent Successfully!');

                window.location.href = "/mobileData";
            } else {
                // btn.removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
                alert(response.msg);
            }
        }, error: function (err, err1, err3) {
            setTimeout(function (){
                $("#loaderModal").modal("hide");
            }, 1000);
            // btn.removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
            alert('¡Algo salió mal, inténtalo de nuevo más tarde!');
        }
    });
}

$('.block-copy-paste').on("cut copy paste",function(e) {
    e.preventDefault();
});

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

$(".meses").on('input', function(e) {
    if($(this).val() > 11 || $(this).val() < 1) {
        $(this).val('');
    }
});

//FOR VALIDATE DUPLICATE REFERENCE NUMBER
//validateDuplicateMain([document.querySelector('#telephone_reference_one'), document.querySelector('#telephone_reference_two'), document.querySelector('#personal_reference_phone')], ["123"]);
//FOR VALIDATE DUPLICATE REFERENCE NAME
//validateDuplicateMain([document.querySelector('#name_reference_one'), document.querySelector('#name_reference_two'), document.querySelector('#personal_reference_name')], ["abc"]);

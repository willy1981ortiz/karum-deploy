const zipCodeEl = $("#cpCodeId");

let estadoElement = $("#stateEl");
let municipioElement = $('#municipalityEl');
let coloniaElement = $('#colonyEl');
let ciudadElement = $("#cityEl");

/*municipioElement.on("change", function (){
    $("#municipalityCode").val($(this).find(":selected").attr("code"));
});
coloniaElement.on("change", function (){
    $("#colonyCode").val($(this).find(":selected").attr("code"));
});
ciudadElement.on("change", function (){
    $("#cityCode").val($(this).find(":selected").attr("code"));
});*/

municipioElement.on("change", function () {
    $("#municipalityCode").val($(this).find(":selected").attr("code"));
    if (municipioElement.val().length > 0) {
        municipioElement.css("border-color", "#2EDB7C");
    } else {
        municipioElement.css("border-color", "red");
    }
    // Update CP Value on Change of colony
    let cp_code = $(this).find(":selected").attr("zip_code")
    if(cp_code !== undefined){
        $("#cpCodeId").val(cp_code);
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

    const unique = (value, index, self) => {
        return self.indexOf(value) === index
    }
}

/*API CAll For Geo Data*/
zipCodeEl.on("input", function () {
    loadZipCodeData(null);
});

$("#load_from_home").on("change", function (){
    let form = $("form")[0];
    if ($("#load_from_home").prop("checked")) {
        $("#load_from_work").prop("checked", false);
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

$("#load_from_work").on("change", function (){
    let form = $("form")[0];
    if ($("#load_from_work").prop("checked")) {
        $("#load_from_home").prop("checked", false);
        if (typeof mTc42Data != "undefined") {
            form.street.value = mTc42Data.street;
            form.int_no.value = mTc42Data.intNo;
            form.ext_no.value = mTc42Data.extNo;
            form.zip.value = mTc42Data.zipCode;
            loadZipCodeData(function () {
                form.municipality.value = mTc42Data.municipality;
                form.colony.value = mTc42Data.colony;
                form.city.value = mTc42Data.city;
                form.state.value = mTc42Data.state;
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



function onSubmitMobileDataForm(el) {
    const form = $("form");

    form.validate({
        rules: {
            street: {required: true},
            ext_no: {required: true},
            zip: {required: true},
            state: {required: true},
            municipality: {required: true},
            colony: {required: true},
            city: {required: true}
        },
        messages: {
            ext_no: {required: "Faltan datos"},
            zip: {required: "Faltan datos"},
        }
    });

    if (!form.valid()) {
        return;
    }

    form.ajaxSubmit({
        url: mGBaseUrl + '/api/mobileData/submit',
        success: function (response, status, xhr, $form) {
            console.log("response", response);
            if (response.success) {
                window.location.href = mGBaseUrl + "/declaration";
            } else {
                alert(response.msg);
            }
        }, error: function (err, err1, err3) {
            alert('¡Algo salió mal, inténtalo de nuevo más tarde!');
        }
    });
}

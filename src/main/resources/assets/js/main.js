const NEW_USER = 0.0;
const INE_OR_PASS_UPLOADED = 1.0;
const DOCUMENT_COMPLETE = 2.0;
const SUMMARY_1_COMPLETE = 3.0;
const TC41_API_COMPLETE = 4.0;
const AUTH_CODE_TC42_ACCEPTED = 5.0;
const SUMMARY_2_COMPLETE = 6.0;
const MOBILE_DATA_COMPLETE = 7.0;
const TC43_API_COMPLETE = 8.0;
const TC44_API_COMPLETE = 9.0;
const SHIPPING_API_COMPLETE = 10.0;

function showFormMsg(form, type, msg) {
    const alert = $('<div class="alert alert-' + type + ' alert-dismissible" role="alert">\
			<div class="alert-text">' + msg + '</div>\
			<div class="alert-close">\
                <i class="flaticon2-cross kt-icon-sm" data-dismiss="alert"></i>\
            </div>\
		</div>');

    let alertEl = form.find('.alert');
    alertEl.remove();
    alert.prependTo(form);
    //alert.animateClass('fadeIn animated');
    KTUtil.animateClass(alert[0], 'fadeIn animated');
    alert.find('span').html(msg);

    try {
        form.closest(".modal").animate({
            scrollTop: $(form.find(".alert")).offset().top
        }, 500);
    } catch (e) {

    }
}

function removeFormMsg(form) {
    form.find('.alert').remove();
}

const NetworkHelper = function () {
    return {
        get: function (url, successCallback, errorCallback) {
            jQuery.ajax({
                url: url,
                cache: false,
                contentType: false,
                processData: false,
                type: 'GET',
                success: successCallback,
                error: errorCallback

            });
        }, post: function (url, formData, successCallback, errorCallback) {
            jQuery.ajax({
                url: url,
                data: formData,
                cache: false,
                contentType: false,
                processData: false,
                type: 'POST',
                success: successCallback,
                error: errorCallback

            });
        }
    }
}();

Date.prototype.toDateInputValue = (function () {
    const local = new Date(this);
    local.setMinutes(this.getMinutes() - this.getTimezoneOffset());
    return local.toJSON().slice(0, 10);
});


// phone number format
/*$(".phone-number-mask").inputmask("mask", {
    "mask": "0315-7499335"
});*/
$('.today-date').val(new Date().toDateInputValue());
if (typeof $.fn.selectpicker != "undefined") {
    $('.kt-selectpicker').selectpicker();
}


function getUrlVars() {
    let vars = {};
    let parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (m, key, value) {
        vars[key] = value;
    });
    return vars;
}

function encodeQueryData(data) {
    const ret = [];
    for (let d in data)
        ret.push(encodeURIComponent(d) + '=' + encodeURIComponent(data[d]));
    return ret.join('&');
}

$(".prevent-click").on("click", function (e) {
    e.preventDefault();
});



function cb(start, end) {
    ggStartDate = start;
    ggEndDate = end;

    $("#startDate").val(start.format("YYYY-MM-DD"));
    $("#endDate").val(end.format("YYYY-MM-DD"));
    $('.daterange-selector span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
}




//.dropdown.bootstrap-select{width:100% !important;}
jQuery(document).ready(function () {
    $(".dropdown.bootstrap-select").css("width", "100%");
});

$(document).ready(function () {
    if (typeof $.fn.imagepicker != "undefined") {
        $(".image-picker").imagepicker();
    }
    if (typeof $.fn.selectpicker != "undefined") {
        $('.kt-selectpicker').selectpicker();
    }

    if (typeof $.fn.select2 != "undefined") {
        $.fn.select2(window, $);
        var el = $('.kt-select2');
        el.each(function (i, e) {
            let ajaxCallback = $(e).data("ajax-callback");
            /*let url = typeof $(e).data("url") == "undefined" ? null : $(e).data("url");
            let tags = $(e).data("tags") == "true";
//            let liveSearch = $(e).data("live-search") == "true";
            let placeholder = typeof $(e).data("placeholder") == "undefined" ? null : $(e).data("placeholder");*/
            let templateResult = typeof $(e).data("template-parser") == "undefined" ? undefined : eval($(e).data("template-parser"));
            let templateSelection = typeof $(e).data("template-view") == "undefined" ? undefined : eval($(e).data("template-view"));

            $(e).select2(
                {
                    ajax: ajaxCallback != null ? {data: eval(ajaxCallback)} : null,


//                                               tags: tags,
//                                               ajax: (url != null && url.length > 1) ? { url: url, delay: 500, dataType: 'json'} : null,
//                                               placeholder: placeholder,
                    templateResult: templateResult,
                    templateSelection: templateSelection
                }
            );
            // $(e).val(null).trigger('change');

        });
    }
});

function showLoader(selector) {
    KTApp.block(selector);
}

function hideLoader(selector) {
    KTApp.unblock(selector);
}

$('#update-password-btn').click(function (e) {
    e.preventDefault();
    let btn = $(this);
    let form = $("#password-change-form");

    form.validate({
        rules: {
            old_password: {
                required: true
            },
            new_password: {
                required: true
            }
        }
    });

    if (!form.valid()) {
        return;
    }

    btn.addClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', true);

    form.ajaxSubmit({
        data: function () {
            return $(this).serialize();
        },
        url: '/api/password/update',
        success: function (response, status, xhr, $form) {
            console.log("response", response);
            if (response.success) {
                showFormMsg(form, 'success', 'Password Updated!');
                setTimeout(function () {
                    btn.removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
                    removeFormMsg(form);
                    $("#modal_change_password").modal("hide");
                    window.location = mGBaseUrl + "/logout";
                }, 500);
                form.reset();
            } else {
                btn.removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
                showFormMsg(form, 'danger', response.msg);
            }
        }, error: function (err, err1, err3) {
            btn.removeClass('kt-spinner kt-spinner--right kt-spinner--sm kt-spinner--light').attr('disabled', false);
            showFormMsg(form, 'danger', '¡Algo salió mal, inténtalo de nuevo más tarde!');
        }
    });
});


function decimalValue(value, decimal){
    if (typeof value != "undefined" && !isNaN(parseFloat(value))) {
        return parseFloat(value).toFixed(decimal)
    } else {
        return value;
    }
}

function showErrorMsg(msg){
    $("#errorMessageId").text(msg);
}


function protectBack(){
//    $("#backProtected").val("yes");
}

window.onload = function () {
    const e = document.getElementById("backProtected");
    if (e !== null) {
        if (e.value == "yes") {
            window.history.forward();
        }
    }
}


$('.nameValid').on('keypress', function (event) {
    var regex = new RegExp("^[a-zA-Z ]+$");
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
        event.preventDefault();
        return false;
    }
});

$('.alphanumeric').on('keypress', function (event) {
    var regex = new RegExp("^[a-zA-Z0-9]+$");
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
        event.preventDefault();
        return false;
    }
});

$('.alphanumericspace').on('keypress', function (event) {
    var regex = new RegExp("^[a-zA-Z0-9 ]+$");
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
        event.preventDefault();
        return false;
    }
});

$('.numeric').on('keypress', function (event) {
    var regex = new RegExp("^[0-9]+$");
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
        event.preventDefault();
        return false;
    }
});

$('.phoneNumber').on('keypress', function (event) {
    var regex = new RegExp("^[0-9]+$");
    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
    if (!regex.test(key)) {
        event.preventDefault();
        return false;
    }
});

//For Input Type Number Restrict Max Length
$('.numMaxLength').on('input', function (event) {
    if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);
});

function validateDuplicateMain(els, values, error, excludedItems) {
    $(els).keyup(function (e) {
	    var duplicateEls = new Set();
		els.forEach(function (elOuter){
		    if (values && values.includes(elOuter.value)) {
			    if(excludedItems && excludedItems.includes(elOuter.value) && excludedItems.includes(elOuter)) {
                			        // exclude these
                } else {
                    duplicateEls.add(elOuter);
                }

			}
			els.forEach(function (elInner){
			    if(excludedItems && excludedItems.includes(elInner) && excludedItems.includes(elOuter)) {
			        // exclude these
			    } else if (elOuter.id != elInner.id && elOuter.value == elInner.value && elOuter.value.length > 0) {
					duplicateEls.add(elInner);
					duplicateEls.add(elOuter);
				}
			});
		});

		els.forEach(function (el){
			var label = $(el).siblings("span");
			if (duplicateEls.has(el)) {
				label.show();
//				label.text("Referencia duplicada");
				el.setCustomValidity("número duplicado");
			} else {
				label.hide();
				el.setCustomValidity("");
			}
		});
//		console.log(duplicateEls);
	});

}

function validPhoneNumbers(els) {
    els.forEach(function (el) {
        if(el.value.startsWith("0") || el.value.startsWith("1234")) {
            el.setCustomValidity("Introduce un número válido");
            el.reportValidity();
            return false;
        } else {
            el.setCustomValidity("");
        }
    });

    return true;
}

function userStatusToPoa() {
    $.ajax({
        url: mGBaseUrl + '/api/user/statusUpdate',
        type: "POST",
        success: function (response) {
            console.log(response)
            window.location = mGBaseUrl + "/person_info"
        },
        error: function (error) {
            console.log(error)
        },
    });
}
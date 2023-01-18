var type = "";

$('input[type="checkbox"]').on('change', function () {
    $('input[type="checkbox"]').not(this).prop('checked', false);

    if (this.id === "addressCheckBoxId") {
        type = "a";
    } else if (this.id === "passportCheckBoxId") {
        type = "p";
    } else {

    }

    window.location = mGBaseUrl + "/uploadDocument?type=" + type
    // $("#forward-btn").attr("href", mGBaseUrl + "/uploadDocument?type=" + type);
});

//Forward Button Click Event
function onClickForwardBtn() {
    var addressUploaded = $("#addressCheckBoxId").prop("checked");
    var ineUploaded = $("#ineCheckBoxId").prop("checked");
    var passportUploaded = $("#passportCheckBoxId").prop("checked");

    console.log(mIsHandoff)

    if (mIsHandoff) {
        window.location = mGBaseUrl + "/goodByeMobile"
    } else if ((ineUploaded === true || passportUploaded === true) && addressUploaded === true) {
        document.getElementById("identificationErrorMsgId").style.display = "none";
        window.location = mGBaseUrl + "/person_info";
    } else {
        document.getElementById("identificationErrorMsgId").style.display = "block";
    }
}
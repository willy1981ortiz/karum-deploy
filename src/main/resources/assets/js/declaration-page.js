//window.history.forward();

if (mUserStatus >= TC44_API_COMPLETE) {
    window.location = mGBaseUrl + "/goodBye";
} else if (window.performance && window.performance.navigation.type === window.performance.navigation.TYPE_BACK_FORWARD) {
    window.location.reload()
}

function onAcceptBtnClick() {
    document.getElementById("declarationAcceptBtn").disabled = true;

    $("#loaderModal").modal("show");
    $.ajax({
        type: "POST",
        url: mGBaseUrl + 'api/data/complete',
        success: function (response, status, xhr, $form) {
            console.log("response", response);
            setTimeout(function () {
                $("#loaderModal").modal("hide");
            }, 1000);
            if (response.success) {
                if (response.data) {
                    console.log(response.data);
//                    document.getElementById("tc44Code").innerText = response.data;
                    $("#loaderModal").modal("hide");
                    $('#completeModal').modal({backdrop: 'static', keyboard: false});
                    $('#completeModal').modal('show');
                    mUserStatus = TC44_API_COMPLETE;
                }
            } else {
                $("#add_toast_id").text(response.msg)
                $('.toast').toast('show');
                //alert(response.msg);
            }
        },
        error: function (err, err1, err3) {
            console.log(err);
            $("#add_toast_id").text(response.msg)
            $('.toast').toast('show');
            //alert(response.msg);
            setTimeout(function () {
                $("#loaderModal").modal("hide");
            }, 1000);
        }
    });

}

var isBtnClicked = 0;

function onNotAgreeBtnClick() {
    if (isBtnClicked == 0) {
        $('#firstBtnClickText').css("display", "block");
        $('#secondBtnClickText').css("display", "none");
        isBtnClicked = 1;
    } else if (isBtnClicked == 1) {
        $('#secondBtnClickText').css("display", "block");
        $('#firstBtnClickText').css("display", "none");
        isBtnClicked = 2;
    } else if (isBtnClicked == 2) {
        window.location = mGBaseUrl + "/goodBye";
        return;
    }

    $('#notAgreeModal').modal({backdrop: 'static', keyboard: false});
    $('#notAgreeModal').modal('show');
//    $('#completeModal').modal('show');
}

function onCloseBtnClick() {
    $('#notAgreeModal').modal('hide');
}
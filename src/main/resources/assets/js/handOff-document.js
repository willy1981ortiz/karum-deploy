let qrcode = new QRCode("qrcode");

function onClickGenerateQRCode() {
    const qrModel = $("#modal-qrCode");
    qrModel.modal({backdrop: 'static', keyboard: false});
    qrModel.modal('show');
    $(".centro-container").css('filter', 'blur(5px)');

    //qrcode.makeCode("https://approva2.cardpaygroup.com/handoff?type=" + mType + "&token=" + mAuthToken);
    var baseUrl = window.location.origin;
    qrcode.makeCode(baseUrl + "/handoff?type=" + mType + "&token=" + mAuthToken);

    const handOffInterval = setInterval(function () {
        $.ajax({
            url: mGBaseUrl + '/api/mobile/flag/handoff',
            type: "GET",
            success: function (response) {
                if (response.success) {
                    let isMobileOpen = response.data;
                    console.log(isMobileOpen)
                    if (isMobileOpen === 1) {
                        $("#modal-qrCode").css('filter', 'blur(5px)');
                        $('#modal-scan-qr').modal({backdrop: 'static', keyboard: false});
                        $("#modal-scan-qr").modal('show');
                        //$(".centro-container").css('filter', 'blur(0px)');

                        clearInterval(handOffInterval)
                        startUserStatusCheckInterval();
                    }
                }
            },
            error: function (error) {
                console.log(error)
            },
        });

    }, 2000);
    /* let qrcode = new QRCode(document.getElementById("qrcode"), {
         text: "http://jindo.dev.naver.com/collie",
         width: 200,
         height: 200,
         colorDark: "#000000",

         correctLevel: QRCode.CorrectLevel.H
     });


     qrcode.clear(); // clear the code.
     qrcode.makeCode("http://naver.com");*/

    //let result = qrcode.makeCode("435345345");
}

function onClickDismissQR() {
    qrcode.clear();
    $(".centro-container").css('filter', 'blur(0px)');
    $('#modal-qrCode').modal('hide');
}

function startUserStatusCheckInterval() {
    const statusInterval = setInterval(function () {
        $.ajax({
            url: mGBaseUrl + '/api/mobile/flag/handoff',
            type: "GET",
            success: function (response) {
                if (response.success) {
                    let isMobileOpen = response.data;
                    if (isMobileOpen === 0) {
                        clearInterval(statusInterval);
                        window.location = mGBaseUrl + "/document"
                        /*if (mIsReturning) {
                            window.location = mGBaseUrl + "/document"
                        } else {
                            window.location = mGBaseUrl + "/identification"
                        }*/
                    }
                }
            },
            error: function (error) {
                console.log(error)
            },
        });
    }, 3000);
}

function onClickQRCodeInfo() {
    $(".centro-container").css('filter', 'blur(5px)');
    $('#modal-info-qr').modal({backdrop: 'static', keyboard: false});
    $("#modal-info-qr").modal('show');
}

function onClickDismissQRInfo(){
    $(".centro-container").css('filter', 'blur(0px)');
    $('#modal-info-qr').modal('hide');
}



function onClickDocumentForwardBtn(status){
    if (status >= TC44_API_COMPLETE) {
//        window.location = mGBaseUrl + "/info_view";
        window.location = mGBaseUrl + "/goodBye";
        /*$.ajax({
            url: mGBaseUrl + 'api/complements/submit',
            type: "POST",
            success: function (response, status, xhr, $form) {
                console.log("response", response);
                if (response.success) {
                    if (response.data) {
                        console.log(response.data)
                        window.location = mGBaseUrl + "/goodBye";
                    }
                }
            },
            error: function (err, err1, err3) {
                console.log(err);
            }
        });*/

    } else {
        window.location = mGBaseUrl + "/identification";
    }

}
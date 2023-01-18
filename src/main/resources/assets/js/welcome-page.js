

function onClickContinueBtn(status){
    if (status >= INE_OR_PASS_UPLOADED) {
        window.location = mGBaseUrl +"/document";
    } else {
        window.location = mGBaseUrl +"/identification";
    }

}
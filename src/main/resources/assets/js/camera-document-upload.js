let retryBtn = null;
let frontFile = null, backFile = null;
let pictureTaken = false;
let isFirstPicture = true;

const uploadBtnMobile = document.getElementsByClassName("uploadBtnMobile");
(function() {
    $("#images-preview").hide();

    // The width and height of the captured photo. We will set the
    // width to the value defined here, but the height will be
    // calculated based on the aspect ratio of the input stream.

    const width = 446;    // We will scale the photo width to this
    let height = 0;     // This will be computed based on the input stream

    // |streaming| indicates whether or not we're currently streaming
    // video from the camera. Obviously, we start at false.

    let streaming = false;

    // The various HTML elements we need to configure or control. These
    // will be set by the startup() function.

    let video = null;
    let canvas = null;
    // var photo = null;
    let startbutton = null;

    /*let front = false;
    document.getElementById("flipBtn").onclick = () => {
      front = !front;
      startup()
    };*/

    function startup() {
        /*var constraints = {
            audio: false,
            video: { facingMode: front ? "user" : "environment" }
        };*/

        video = document.getElementById('video');
        canvas = document.getElementById('canvas');
        // photo = document.getElementById('photo');
        startbutton = document.getElementById('continue_btn');
        retryBtn = document.getElementById('retry_btn');

        if (navigator.mediaDevices.getUserMedia) {
            navigator.mediaDevices.getUserMedia({video: { facingMode: "environment" }, audio: false})
                .then(function(stream) {
                    video.srcObject = stream;
                    video.play();
                })
                .catch(function(err) {
                    console.log("An error occurred: " + err);
                });
        }

        video.addEventListener('canplay', function(ev){
            if (!streaming) {
                height = video.videoHeight / (video.videoWidth/width);

                // Firefox currently has a bug where the height can't be read from
                // the video, so we will make assumptions if this happens.

                if (isNaN(height)) {
                    height = width / (4/3);
                }

                video.setAttribute('width', width);
                video.setAttribute('height', height);
                canvas.setAttribute('width', width);
                canvas.setAttribute('height', height);
                streaming = true;
            }
        }, false);

        startbutton.addEventListener('click', function(ev){
            ev.preventDefault();

            onDocumentSelectContinue();
        }, false);

        clearphoto();
    }

    // Fill the photo with an indication that none has been
    // captured.

    function clearphoto() {
        const context = canvas.getContext('2d');
        context.fillStyle = "#AAA";
        context.fillRect(0, 0, canvas.width, canvas.height);

        // var data = canvas.toDataURL('image/png');
        // photo.setAttribute('src', data);
    }

    // Capture a photo by fetching the current contents of the video
    // and drawing it into a canvas, then converting that to a PNG
    // format data URL. By drawing it on an offscreen canvas and then
    // drawing that to the screen, we can change its size and/or apply
    // other changes before drawing it.

    function takepicture() {
        const context = canvas.getContext('2d');
        if (width && height) {
            canvas.width = width;
            canvas.height = height;
            context.drawImage(video, 0, 0, width, height);

            const data = canvas.toDataURL('image/png');
            if (isFirstPicture) {
                frontFile = dataURLtoFile(data);
                frontFile.name = "front.png";
            } else {
                backFile = dataURLtoFile(data);
                backFile.name = "back.png";
            }
            // photo.setAttribute('src', data);
            $("#video").hide();
//            $("#flipBtn").hide();
            $("#camera-preview").show();
            retryBtn.style.visibility = "visible";
            $("#continue_btn").html("Si, continua");
            $("#capture-documents-title").html("Puede ver claramente el documento capturado?");
            pictureTaken = true;
        } else {
            clearphoto();
        }
    }

    // Set up our event listener to run the startup process
    // once loading is complete.
    window.addEventListener('load', startup, false);

    function onDocumentSelectContinue() {
        if (pictureTaken) {
            if (isFirstPicture && (mType != "a" && mType != "p" && mType != "i")) {
                isFirstPicture = false;
                clearForNewPicture();
            } else {
//                $("#flipBtn").hide();
                startbutton.style.visibility = "hidden";

                retryBtn.style.visibility = "hidden";
                if (uploadTextBlock) {
                    uploadTextBlock.style.visibility = "hidden";
                }
                if (uploadTextBlockOnMobile) {
                    uploadTextBlockOnMobile.style.visibility = "hidden";
                }
                if (uploadTextBlockOnMobile) {
                    uploadTextBlockOnMobile.style.visibility = "hidden";
                }
                backBtn.style.visibility = "hidden";
                $("#camera-preview").hide();
                $("#capture-documents-title").hide();

                if (mType == "p") {
                    loadFileToImagePreview(frontFile)
                    backBtn.style.visibility = "visible";
                    forwardBtn.style.visibility = "visible";
                    uploadPassport = true;
                } else {
                    uploadImages();
                }
            }
        } else {
            takepicture();
        }
    }
})();


function onRetry(){
    clearForNewPicture();
}

function clearForNewPicture(){
    pictureTaken = false;
    $("#video").show();
//    $("#flipBtn").show();
    $("#camera-preview").hide();
    retryBtn.style.visibility = "hidden";
    $("#continue_btn").html("Captura");
    $("#capture-documents-title").html("Alinear el documento dentro del marco, cuando se presiona el boton de captura");
    if (!isFirstPicture) {
        $("#documentTitle").html("Captura del INE (Volver)");
        $("#pageIndicator").html("Paso 2 de 2");
        $("#pageIndicatorOnMobile").html("Paso 2 de 2");
    }
}


function dataURLtoFile(dataurl, filename) {
    const arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
        bstr = atob(arr[1]);
    let n = bstr.length;
    const u8arr = new Uint8Array(n);
    while(n--){
        u8arr[n] = bstr.charCodeAt(n);
    }
    return new File([u8arr], filename, {type:mime});
}

function getMediaCast(castList) {
    let castTitle = [];
    castList.forEach((cast) => {
        castTitle.push(cast.character);
    });

return castTitle;
}

function mediaImageResult(state, t) {
    if (!state.id) {
        return state.text;
    }
    //  var baseUrl = "https://image.tmdb.org/t/p/w500";
    var baseUrl = "";
    var $state = $(
        '<span style="font-size:30px;"><img src="' + baseUrl + state.id + '" height="150px" style="margin-right:10px;" />' + state.text + '</span>'
    );
    return $state;
}

function mediaImageSelection(state, t) {
    if (!state.id) {
        return state.text;
    }
    //  var baseUrl = "https://image.tmdb.org/t/p/w500";
    var baseUrl = "";
    var $state = $(
        '<span style="font-size:20px;"><img src="' + baseUrl + state.id + '" height="90px" style="margin-right:10px;" />' + state.text + '</span>'
    );
    return $state;
}

function tmdbSearchImageSelection(state, t) {
    if (!state.id) {
        return state.text;
    }
    if (state.id == state.text) {
        $("#add-tmdb-media").val(null).trigger('change');
        return null;
    }
    //  var baseUrl = "https://image.tmdb.org/t/p/w500";
    var baseUrl = "";
    var $state = $(
        '<span style="font-size:20px;"><img src="' + baseUrl + state.poster + '" height="90px" style="margin-right:10px;" />' + state.text + '</span>'
    );
    return $state;
}

function onDeleteClick(id) {
    document.querySelector("#delete-media-form").media_id.value = id;
}
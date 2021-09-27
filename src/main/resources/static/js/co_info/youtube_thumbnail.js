let apikey = "AIzaSyBArW_oqXY3ItS2SwXTIu8qqmV-SF9y9Yk"
let keyword = "코로나"
let maxResults = 4
$(document).ready(function(){
    setInterval(
        YoutubeCall(),43200000)
});
function YoutubeCall() {
    $.ajax({
        type: "GET",
        dataType: "JSON",
        url: "https://www.googleapis.com/youtube/v3/search",
        data: {part: 'snippet', key: apikey, q: keyword, maxResults, type: 'video'},
        contentType: "application/json",
        success: function (data) {
            console.log(data)
            $.each(data.items, function (i, item) {
                url = "https://www.youtube.com/watch?v=" + item.id.videoId;
                thumbnail = item.snippet.thumbnails.medium.url;
                title = item.snippet.title;
                // $(".news-slide-container").append("<div class=news-slide><a href="+url+"><img class=video-img src="+thumbnail+"></a></div>");
                $(".news-slide-container").append("<div class=news-slide><span style='cursor:pointer;' onclick=window.open('" + url + "','','')><img class=video-img src=" + thumbnail + "></span></div>");
                console.log(thumbnail);
            });
            showSlides();
        }
    });
}
var num=0;
function showSlides(){

    var i;
    var x = document.getElementsByClassName("video-img");
    for(i=0;i<x.length;i++){
        x[i].style.display="none";
    }
    num++;
    if(num > x.length){num=1}
    x[num-1].style.display = "block";
    setTimeout(showSlides,2000);
}
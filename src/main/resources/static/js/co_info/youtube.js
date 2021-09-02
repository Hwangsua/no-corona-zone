let apikey = "AIzaSyCSs6Im2gt7TV1FymIdkzkYRgZ0LsiIfYg "
let keyword = "코로나"
let maxResults = 30
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
            console.log(data);
            $.each(data.items, function (i, item) {
                url = "https://www.youtube.com/watch?v=" + item.id.videoId;
                channelTitle = item.snippet.channelTitle;
                descriptionList = item.snippet.description;
                thumbnail = item.snippet.thumbnails.medium.url;
                title = item.snippet.title;
                $("#get_view").append("<div class=box>" + "<img class=thumbnail src=" + thumbnail + ">" +
                    "<div class=youtube-detail>" + "<a class=title-link href=" + url + ">" + "<div class=title>" +
                    title + "</a></div><div class=channel>" + channelTitle + "</div><div class=description>" + descriptionList + "</div></div>");

            });
        }
    });
}
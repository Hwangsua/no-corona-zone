let apikey = "AIzaSyAl3Cd1dxfvABLMjNJx6IcK1q9m9YY_zjA"
let keyword = "코로나"
let maxResults = 30

$.ajax({
    type : "GET",
    dataType : "JSON",
    url : "https://www.googleapis.com/youtube/v3/search",
    data:{part:'snippet',key:apikey,q:keyword,maxResults,type:'video'},
    contentType : "application/json",
    success : function(data){
        //console.log(data);
        $.each(data.items, function(i, item){
            url = "https://www.youtube.com/watch?v="+item.id.videoId;
            thumbnail = item.snippet.thumbnails.medium.url;
            $("#get_view").append("<p class=box>"+"<img class=thumbnail src="+thumbnail+">"+"<a href="+url+">"+"<br/>"+
                "<span class=title>"+item.snippet.title+"</span></a></p>");
            title = item.snippet.title;

            console.log(thumbnail);
            // console.log(videoId);
            console.log(title);
        });
    }
});
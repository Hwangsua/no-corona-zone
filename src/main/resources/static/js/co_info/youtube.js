// let apikey = "AIzaSyCdW0U4HuSoXPmWFbRy7Z-VYLxhtvtIcWM"
let apikey = "AIzaSyAl3Cd1dxfvABLMjNJx6IcK1q9m9YY_zjA" // 할당량 초과되면 위의 api키 주석 풀기!
let keyword = "코로나"
let maxResults = 30

$.ajax({
    type : "GET",
    dataType : "JSON",
    url : "https://www.googleapis.com/youtube/v3/search",
    data:{part:'snippet',key:apikey,q:keyword,maxResults,type:'video'},
    contentType : "application/json",
    success : function(data){
        console.log(data);
        $.each(data.items, function(i, item){
            url = "https://www.youtube.com/watch?v="+item.id.videoId;
            channelTitle=item.snippet.channelTitle;
            descriptionList=item.snippet.description;
            thumbnail = item.snippet.thumbnails.medium.url;
            title = item.snippet.title;
            $("#get_view").append("<div class=box>"+"<img class=thumbnail src="+thumbnail+">"+
                "<div class=youtube-detail>"+"<a class=title-link href="+url+">"+"<div class=title>"+
                title+"</a></div><div class=channel>"+channelTitle+"</div><div class=description>"+descriptionList+"</div></div>");
               // +"<div class=youtube-list>"+"<div class=channel>"+channelTitle+"</div>"+"<div class=description>"+descriptionList+"</div></div>"+"</div>");
            // $("#get_view").append("<p class=box>"+"<img class=thumbnail src="+thumbnail+">"+
            //     "<a href="+url+">"+"<span class=title>"+item.snippet.title+"</span></a></p>");
            // $("#video-detail").append("<a href="+url+">"+"<div class=title>"+title+"</div></a>"
            //     +"<div class=youtube-list>"+"<div class=channel>"+channelTitle+"</div>"+"<div class=description>"+descriptionList+"</div></div>");

            // console.log(thumbnail);
            // console.log(videoId);
            // console.log(title);
            // console.log(channelTitle);
            // console.log(descriptionList);
        });
    }
});
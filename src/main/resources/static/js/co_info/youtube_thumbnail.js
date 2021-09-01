let apikey = "AIzaSyCdW0U4HuSoXPmWFbRy7Z-VYLxhtvtIcWM"
// let apikey = "AIzaSyAl3Cd1dxfvABLMjNJx6IcK1q9m9YY_zjA" // 할당량 초과되면 위의 api키 주석 풀기!

let keyword = "코로나"
let maxResults = 4

$.ajax({
    type : "GET",
    dataType : "JSON",
    url : "https://www.googleapis.com/youtube/v3/search",
    data:{part:'snippet',key:apikey,q:keyword,maxResults,type:'video'},
    contentType : "application/json",
    success : function(data){
        console.log(data)
        $.each(data.items, function(i, item){
            url = "https://www.youtube.com/watch?v="+item.id.videoId;
            thumbnail = item.snippet.thumbnails.medium.url;
            title = item.snippet.title;
            $(".news-slide-container").append("<div class=news-slide><a href="+url+"><img class=video-img src="+thumbnail+"></a></div>");
            console.log(thumbnail);
        });
        showSlides();
    }
});
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
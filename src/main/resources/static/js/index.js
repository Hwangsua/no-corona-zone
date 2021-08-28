// 지도 선택 버튼 색 지정
if(window.location.href.indexOf("infection") > -1){ // 확진 지도일때
    document.getElementById("item-infection").style.background = "rgb(125, 204, 171)";
} else if(window.location.href.indexOf("density") > -1){ // 밀집 지도일때
    document.getElementById("item-density").style.background = "rgb(125, 204, 171)";
} else if(window.location.href.indexOf("distancing") > -1){ // 거리두기 지도일때
    document.getElementById("item-distancing").style.background = "rgb(125, 204, 171)";
} else { // 나머지. "/" 포함.
    document.getElementById("item-infection").style.background = "rgb(125, 204, 171)";
}
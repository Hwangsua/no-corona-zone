function cityKindChange(e) {
    var seoul = ["서울전체", "강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구",
        "노원구", "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구", "성동구", "성북구", "송파구",
        "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구"]; // 26
    var gyeonggi = ["경기전체", "가평군", "고양시", "과천시", "광명시", "광주시", "구리시", "군포시", "김포시",
        "남양주시", "동두천시", "부천시", "성남시", "수원시", "시흥시", "안산시", "안성시", "안양시", "양주시",
        "양평군", "여주시", "연천군", "오산시", "용인시", "의왕시", "의정부시", "이천시", "파주시", "평택시",
        "포천시", "하남시", "화성시"]; // 32
    var incheon = ["인천전체", "강화군", "계양구", "남동구", "동구", "미추홀구", "부평구", "서구", "연수구",
        "옹진군", "중구"]; // 11

    var target = document.getElementById('sub-city');

    if(e.value == "a") var b = seoul;
    else if(e.value == "b") var b = gyeonggi;
    else if(e.value == "c") var b = incheon;
    else if(e.value == "d") var b = busan;
    else if(e.value == "e") var b = daegu;
    else if(e.value == "f") var b = gwangju;
    else if(e.value == "g") var b = daejeon;
    else if(e.value == "h") var b = ulsan;
    else if(e.value == "i") var b = sejong;
    else if(e.value == "j") var b = gangwon;
    else if(e.value == "k") var b = gyeongnam;
    else if(e.value == "l") var b = gyeongbuk;
    else if(e.value == "m") var b = jeonnam;
    else if(e.value == "n") var b = jeonbuk;
    else if(e.value == "o") var b = chungnam;
    else if(e.value == "p") var b = chungbuk;
    else if(e.value == "q") var b = jeju;

    target.options.length = 0;

    for (x in b) {
        var opt = document.createElement("option");
        opt.value = b[x];
        opt.innerHTML = b[x];
        target.appendChild(opt);
    }

}

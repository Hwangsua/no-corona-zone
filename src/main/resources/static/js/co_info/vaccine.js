function getCurrentDate()
{
    var date = new Date();

    var month = date.getMonth() + 1;
    month = month < 10 ? '0' + month.toString() : month.toString();

    var day = date.getDate();
    day = day < 10 ? '0' + day.toString() : day.toString();

    console.log(month + "/" + day);
    return month + "/" + day ;

}
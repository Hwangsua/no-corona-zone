import csv

def savCsvFile(filePath, saveList):
    with open(filePath, 'a', encoding='utf-8', newline='') as f:
        writer = csv.writer(f)
        writer.writerows(saveList)


def naverArticlePattern(filePath, articles):
    mList = []

    for article in articles:

        try:
            press = article.find_element_by_class_name("info_group").find_element_by_tag_name("a")
            
            try:
                pressSub = press.find_element_by_tag_name("i").text
                pressName = press.text
                pressName = pressName.replace(pressSub, "")
            except:
                pressName = press.text

            pressImgUrl = press.find_element_by_tag_name("img").get_attribute("src")

            articleTitle = article.find_element_by_class_name("news_tit").text
            articleContent = article.find_element_by_class_name("api_txt_lines.dsc_txt_wrap").text
            articleLink = article.find_element_by_class_name("news_tit").get_attribute("href")
            articleImgUrl = article.find_element_by_class_name("thumb.api_get").get_attribute("src")
        except:
            img = ""
            pass

        mList.append([f'{pressName}|{pressImgUrl}|{articleTitle}|{articleContent}|{articleLink}|{articleImgUrl}'])

    savCsvFile(filePath, mList)

def naverSocialDistancing(filePath, information):

    mList = []
    localDoubleCheck = []

    for info in information:

        localName = info.find_element_by_class_name("local_name").text

        if localName in localDoubleCheck:
            continue

        population_number = info.find_element_by_class_name("population_number").text

        mList.append([f'{localName}|{population_number}'])
        localDoubleCheck.append(localName)

    savCsvFile(filePath, mList)

import ncz
import sys
import argparse
import chromedriver_binary
import re
from selenium.webdriver.chrome.options import Options
from selenium import webdriver


parser = argparse.ArgumentParser()
parser.add_argument('keyword1', help="filePath")

filePath = re.sub(r"[^./_a-z]","",parser.parse_args().keyword1)

chrome_options = Options()
# chrome_options.add_argument('--headless')  # 화면 안띄움
# chrome_options.add_argument('--start-maximized')  # F11 전체 화면 설정

driver = webdriver.Chrome(options=chrome_options)
url = "https://search.naver.com/search.naver?query=코로나+백신&where=news&ie=utf8&sm=nws_hty"

driver.get(url)

elem = driver.find_element_by_xpath('//*[@id="main_pack"]/section/div/div[2]/ul')
articles = elem.find_elements_by_class_name("news_wrap.api_ani_send") # 기사들

ncz.naverArticlePattern(filePath, articles)

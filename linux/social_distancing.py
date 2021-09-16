import time
import re
from selenium.webdriver.chrome.options import Options
from selenium import webdriver
import argparse
import ncz

import chromedriver_binary

parser = argparse.ArgumentParser()
parser.add_argument('keyword1', help="filePath")

filePath = re.sub(r"[^./_a-z]","",parser.parse_args().keyword1)

chrome_options = Options()
# chrome_options.add_argument('--headless')  # 화면 안띄움
# chrome_options.add_argument('--start-maximized')  # F11 전체 화면 설정

driver = webdriver.Chrome(options=chrome_options)
url = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=%EA%B1%B0%EB%A6%AC%EB%91%90%EA%B8%B0"

driver.get(url)

time.sleep(1.3);

elem = driver.find_element_by_xpath('//*[@id="_cs_production_type"]/div/div[6]/div/div/div/div[1]/div[1]/div')
information = elem.find_elements_by_class_name('local_info');

ncz.naverSocialDistancing(filePath, information);



#!bin/sh
echo 'start vaccine_article.sh training'
touch vaccine_article.csv | chmod -R 777 vaccine_article.csv
python3 vaccine_article.py $1

#!bin/sh

echo 'start local_article.sh training'
touch local_article.csv | chmod -R 777 local_article.csv
python3 local_article.py $1 $2 $3

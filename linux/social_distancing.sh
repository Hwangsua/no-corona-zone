#!bin/sh

echo 'start social_distancing.sh training'
touch social_distancing.csv | chmod -R 777 social_distancing.csv
python3 social_distancing.py $1

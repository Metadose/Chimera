import requests
import os
import datetime
import time


# Globals.
target_url = 'http://construction-cebedo.rhcloud.com'
home_backup = 'C:/Users/QR_User1/Documents/Vic 2/vcc/bak'
log_name = 'scrape'

# 5 Minutes.
sleep_time = 10

# Start the scraper.
while True:

    # Print start.
    now = datetime.datetime.now()
    print("Running: %s" % now)

    # Create the request.
    page = requests.get(target_url)

    # Construct file names and directory names.
    datetime_string = ('%s.%s.%s.%s.%s.%s' % (now.year, now.month, now.day, now.hour, now.minute, now.second))
    log_datetime_name = '%s.%s' % (datetime_string, log_name)

    # If the directory does not exist, create it.
    if not os.path.isdir(home_backup):
        os.makedirs(home_backup)

    # Store the scraped text to file.
    file = open(("%s/%s" % (home_backup, log_datetime_name)), "wb")
    file.write(page.content)

    # Close opened file
    file.close()

    # Delay for X seconds: time.sleep(X)
    print("Done: %s" % now)
    time.sleep(sleep_time)
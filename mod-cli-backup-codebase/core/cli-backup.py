import redis
import datetime
import shutil
import os
from cement.core.foundation import CementApp
from cement.core.controller import CementBaseController, expose


# Global variables.
application_label = 'cli-backup'
controller_label_base = 'base'
controller_label_redis = 'controller_redis'
controller_label_mysql = 'controller_mysql'

# Redis details.
redis_host = 'localhost'
redis_backup_name = 'dump.rdb'

# MySQL details.
mysql_host = 'localhost'
mysql_db = 'test'
mysql_backup_name = 'dump.sql'

# Home.
home_backup = 'C:/Users/QR_User1/Documents/Vic 2/vcc/bak'
home_mysql = 'C:/Program Files/MySQL/MySQL Server 5.6/bin'


# Base controller.
class BaseController(CementBaseController):
    class Meta:
        label = controller_label_base
        description = 'Backup tool for them nigguzz!'

    @expose(hide=True)
    def default(self):
        self.app.log.info('BaseController.default')


# Redis controller.
class RedisController(CementBaseController):

    # Setup the controller.
    class Meta:
        label = controller_label_redis
        stacked_on = controller_label_base

    # Backup Redis data.
    @expose(aliases=['redis-backup'], help='Backup Redis data')
    def backup_redis(self):

        self.app.log.info('RedisController.backup')

        # Connect to the Redis server.
        self.app.log.info('Connecting to Redis...')
        redis_server = redis.Redis(redis_host)
        print('Connected to Redis: %s' % redis_host)

        # Do a background save.
        self.app.log.info('Executing background save...')
        redis_server.bgsave()
        redis_backup_dir = redis_server.config_get('dir').get('dir')
        print('Saved RDB directory: %s' % redis_backup_dir)

        # Construct file names and directory names.
        self.app.log.info('Constructing file and directory names...')
        now = datetime.datetime.now()
        datetime_string = ('%s.%s.%s.%s.%s.%s' % (now.year, now.month, now.day, now.hour, now.minute, now.second))
        new_backup_name = '%s.%s' % (datetime_string, redis_backup_name)
        backup_source = '%s/%s' % (redis_backup_dir, redis_backup_name)
        backup_destination = '%s/%s' % (home_backup, new_backup_name)

        # If the directory does not exist, create it.
        if not os.path.isdir(home_backup):
            os.makedirs(home_backup)
            self.app.log.info('Backup home did not exist, created directory: %s' % home_backup)

        # Copy the RDB file to the backup location.
        self.app.log.info('Backing up RDB file...')
        shutil.copyfile(backup_source, backup_destination)
        print('Source file: %s' % backup_source)
        print('Destination file: %s' % backup_destination)
        print('Backup done.')


# MySQL controller.
class MySQLController(CementBaseController):

    # Setup the controller.
    class Meta:
        label = controller_label_mysql
        stacked_on = controller_label_base

    # Backup Redis data.
    @expose(aliases=['mysql-backup'], help='Backup MySQL data')
    def backup_mysql(self):

        self.app.log.info('MySQLController.backup')

        # Get user inputs.
        self.app.log.info('Getting MySQL credentials...')
        print("User:")
        user = input()
        print("Password:")
        password = input()

        # Construct the file name and destination.
        self.app.log.info('Constructing file name and destination...')
        now = datetime.datetime.now()
        datetime_string = ('%s.%s.%s.%s.%s.%s' % (now.year, now.month, now.day, now.hour, now.minute, now.second))
        new_backup_name = '%s.%s' % (datetime_string, mysql_backup_name)
        backup_destination = '%s/%s' % (home_backup, new_backup_name)
        print("Backup destination: %s" % backup_destination)

        # Construct the mysqldump command.
        self.app.log.info('Constructing mysqldump command...')
        mysqldump_template = '"%s/mysqldump" -u %s -p%s -h %s -e --opt -c "%s" > "%s"'
        mysql_command = mysqldump_template % (home_mysql, user, password, mysql_host, mysql_db, backup_destination)
        print("Executing: %s" % mysql_command)

        # Execute mysql dump.
        os.popen(mysql_command)
        print("Backup done.")


# Setup the application.
class CliBackup(CementApp):
    class Meta:
        label = application_label
        base_controller = controller_label_base
        handlers = [BaseController, RedisController, MySQLController]


# Run the application.
with CliBackup() as app:
    app.run()

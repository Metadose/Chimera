from cement.core.foundation import CementApp
from cement.core import hook
from cement.utils.misc import init_defaults


# Define our default configuration options.
app_name = "cli-backup"
defaults = init_defaults(app_name)
defaults[app_name]['debug'] = False


def cleanup_hook(app):
    print("Yellow!")
    pass


# define the application class
class CliBackup(CementApp):
    class Meta:
        label = app_name
        config_defaults = defaults


with CliBackup() as app:

    # Register framework or custom application hooks.
    hook.register('pre_close', cleanup_hook)

    # Add arguments to the parser.
    app.args.add_argument('-f', '--foo', action='store', metavar='STR', help='the notorious foo option')

    # Log stuff.
    app.log.debug("About to run my application!")

    # Run the application.
    app.run()

    # Continue with additional application logic.
    if app.pargs.foo:
        app.log.info("Received option: foo => %s" % app.pargs.foo)
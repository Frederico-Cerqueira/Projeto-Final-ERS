class Command:
    """A command that can be executed by the navigation system."""
    def __init__(self, name, args):
        self.name = name
        self.args = args

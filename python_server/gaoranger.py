class RAD():
    def __init__(self):
        self.states = {"led": False}
        self.actions = []
        self.choreography = []
        self.PROBE_SWITCH = {
            "states": self.probe_states,
            "actions": self.probe_actions,
            "choreography": self.probe_choreography
        }
        self.ACT_CPU = {
            "led": self.act_led
        }

    def probe(self, part):
        return self.PROBE_SWITCH[part]()

    def probe_states(self):
        return self.states

    def probe_actions(self):
        return self.actions

    def probe_choreography(self):
        return self.choreography

    def action(self, act, params):
        return self.ACT_CPU[act](params.split("$"))

    def act_led(self, args):
        if len(args) != 1:
            return False
        arg = args[0]
        if arg == "toggle":
            self.states["led"] = not self.states["led"]
        if arg == "on":
            self.states["led"] = True
        if arg == "off":
            self.states["led"] = False
        return True

function loadJsonProperties(attributes) {
    
    var _logger = SenchaLogManager.getLogger("load-json-properties"),
        prefix = (attributes.get("prefix") + '') || "json",
        jsonFile = attributes.get("file"),
        file = new java.io.File(jsonFile),
        req = attributes.get("required"),
        stack = [],
        getName = function() {
            return stack.join(".");
        },
        readProps = function(obj) {
            for(var name in obj) {
                stack.push(name);
                var val = obj[name];
                _logger.debug("Reading property {} as {}", getName(), val + '');
                if(isPrimitive(val)) {
                    _logger.debug("Setting property {} to {}", getName(), val + '');
                    project.setNewProperty(getName(), val + '');
                } else if(isArray(val)) {
                    for(var l = 0; l < val.length; l++) {
                        stack.push(l + '');
                        readProps(val[l]);
                        stack.pop();
                    }
                } else {
                    readProps(val);
                }
                stack.pop();
            }
        },
        config;
        
    req = req && (req + '').toLowerCase();
    req = (req === 'yes' || req === 'true' || req === '1');
    
    if (!req && !file.exists()) {
        self.log('Optional properties file not present (skipping) - ' + file.getAbsolutePath());
        return;
    }
    
    if(prefix) {
        stack.push(prefix);
    }
    
    config = readConfig(jsonFile);
    readProps(config);
}
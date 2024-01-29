import groovy.json.JsonParserType
import groovy.json.JsonSlurper


static void main(String[] args) {
//    def json = "https://character-service.dndbeyond.com/character/v5/character/115168153?includeCustomItems=true".toURL().text
    def json = readFileString("rudolf.json")
    def parser = new JsonSlurper().setType(JsonParserType.LAX)
    def jsonResp = parser.parseText(json)

    println """# $jsonResp.data.name's spell list"""

    // this only prints the 'class spells' (?) of the first class now
    printClassSpells(jsonResp)
}

private static void printClassSpells(jsonResp) {
    def spells = jsonResp.data.classSpells[0].spells as ArrayList

    // this works but gives a warning - works differently in Groovy vs java?
    spells.sort(Comparator.comparingInt(spell -> spell.definition.level).thenComparing(spell -> spell.definition.name))
    def lastLevel = -1

    spells.forEach { spell ->
        def d = spell.definition
        def name = """#### $d.name"""
        def level = parseLevel(d)

        def rangeProp = formatPropHB("Range", rangeParser(d.range))


        def castingTimeProp = formatPropHB("Casting Time", castingTimeParser(d.activation))
        def durationProp = formatPropHB("Duration", durationParser(d.duration))
        def componentsProp = formatPropHB("Components", componentsParser(d))

        if (d.level > lastLevel) {
            def header = d.level == 0 ? "## Cantrips" : """## Level $d.level"""
            println header
            lastLevel = d.level
        }
        println name
        println level
        println castingTimeProp
        println rangeProp
        println durationProp
        println componentsProp
        println ""
        println d.description // already formatted
        println "" // skip a line
    }
}

static String castingTimeParser(activation) {
    def result = activation.activationTime + " "
    switch (activation.activationType) {
        case 1 -> result + "action"
        case 3 -> result + "bonus action"
        default -> throw new Exception()
    }
}

static String durationParser(duration) {
    switch (duration.durationType) {
        case "Instantaneous" -> "Instantaneous"
        case "Time" -> """$duration.durationInterval $duration.durationUnit"""
        default -> throw new Exception()
    }
}

static String parseLevel(definition) {
    def level
    switch (definition.level) {
        case 0 -> level = "Cantrip"
        case 1 -> level = "1-st level"
        case 2 -> level = "2-nd level"
        case 3 -> level = "3-rd level"
        default -> level = definition.level + "th level"
    }

    return """*$level $definition.school*"""
}

static String formatPropHB(name, prop) {
    """**$name:** :: $prop"""
}

static String readFileString(String filePath) {
    File file = new File(filePath)
    String fileContent = file.text
    return fileContent
}

static String componentsParser(d) {
    def result = ""
    if (d.components.contains(1)) result += "S, "
    if (d.components.contains(2)) result += "V, "
    if (d.components.contains(3)) result += """M ($d.componentsDescription)"""
    if (result.endsWith(", ")) result = result.substring(0, result.length() - 2)
    return result
}

static String rangeParser(range) {
    def rangeProp
    boolean isRange = range.origin == "Ranged"
    if (isRange) {
        rangeProp = """$range.origin, $range.rangeValue ft"""
    } else {
        rangeProp = range.origin
    }

    if (range.aoeType != null) {
        rangeProp + """ ($range.aoeValue ft $range.aoeType)"""
    } else rangeProp
}

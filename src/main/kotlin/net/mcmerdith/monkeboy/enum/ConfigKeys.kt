package net.mcmerdith.monkeboy.enum

enum class ConfigKeys(val key: String) {
    PREF_FILLWARNING("%{0}.fillwarnings");

    fun pass(args: List<String>): String {
        var replaced = this.key
        args.forEachIndexed { i, it ->
            replaced = this.key.replace("%{$i}", it)
        }
        return replaced
    }
}
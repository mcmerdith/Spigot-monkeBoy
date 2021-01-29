package net.mcmerdith.monkeboy.commands

abstract class TNConditionalComplete(requirements: Map<Int, List<String>> = mapOf()) {
    val requirements = mutableMapOf<Int, List<String>>()

    fun permitArgAt(position: Int, arg: String) = modify(position, listOf(arg))
    fun permitArgsAt(position: Int, args: List<String>) = modify(position, args)

    private fun modify(target: Int, arg: List<String>) {
        val requirement = requirements[target]?.toMutableList() ?: mutableListOf()
        requirement.addAll(arg)
        requirements[target] = requirement
    }
}
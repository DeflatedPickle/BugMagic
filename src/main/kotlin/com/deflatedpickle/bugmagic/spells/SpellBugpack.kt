package com.deflatedpickle.bugmagic.spells

class SpellBugpack : SpellBase() {
    init {
        name = "Bugpack"
        id = 2
        cost = 15
        drain = 4
        drainWait = 60

        addToMap()
    }

    override fun cast() { println("Bugpack") }
}
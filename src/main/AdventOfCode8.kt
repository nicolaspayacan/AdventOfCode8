package main

data class Machine(
    var pc: Int,
    var ac: Int,
    var history: List<Int> = listOf()
)

data class Instruction(
    val name: String,
    val value: Int
)

fun Machine.nop() = this.apply {
    history += pc
    pc++
}

fun Machine.acc(amount: Int) = this.apply {
    history += pc
    pc++
    ac += amount
}

fun Machine.jmp(value: Int) = this.apply {
    history += pc
    pc += value
}

fun Machine.execute(instructions: Map<Int, Instruction>): Machine {
    if (history.contains(pc)) return this
    val instruction = instructions[pc] ?: return this
    when (instruction.name) {
        ":acc" -> acc(instruction.value)
        ":jmp" -> jmp(instruction.value)
        else -> nop()
    }
    return execute(instructions)
}

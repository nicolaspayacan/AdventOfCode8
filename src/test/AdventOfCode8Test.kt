package test

import main.*
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import kotlin.test.assertEquals

class AdventOfCode8Test {
    private lateinit var machine: Machine

    @BeforeMethod
    fun setUp() {
        machine = Machine(pc = 0, ac = 0)
    }

    @Test
    fun shouldExecuteANop() {
        machine.nop()
        assertEquals(1, machine.pc)
        assertEquals(0, machine.ac)
    }

    @Test
    fun shouldExecuteAnAcc() {
        machine.acc(2)
        assertEquals(1, machine.pc)
        assertEquals(2, machine.ac)
    }

    @Test
    fun shouldExecuteAJmp() {
        machine.jmp(-3)
        assertEquals(-3, machine.pc)
        assertEquals(0, machine.ac)
    }

    @Test
    fun shouldExecuteInstructions() {
        val program = mapOf(
            0 to Instruction(":acc", 1),
            1 to Instruction(":jmp", 2),
            2 to Instruction(":acc", -1),
            3 to Instruction(":nop", 0)
        )
        machine.execute(instructions = program)
        assertEquals(4, machine.pc)
        assertEquals(1, machine.ac)
        assertEquals(listOf(0, 1, 3), machine.history)
    }

    @Test
    fun shouldHaltWhenAskedToRepeatAnInstruction() {
        val program = mapOf(
            0 to Instruction(":acc", 1),
            1 to Instruction(":jmp", -1),
            2 to Instruction(":nop", 0)
        )
        machine.execute(instructions = program)
        assertEquals(0, machine.pc)
        assertEquals(1, machine.ac)
        assertEquals(listOf(0, 1), machine.history)
    }


    @Test
    fun shouldParseOneInstruction() {
        val expected = Instruction(
            name = ":jmp",
            value = 1
        )
        assertEquals(expected, "jmp +1".parseInstruction())
    }

    @Test
    fun shouldParseManyInstruction() {
        val instructions = mapOf(
            0 to Instruction(
                name = ":jmp",
                value = -1
            ),
            1 to Instruction(
                name = ":nop",
                value = 4
            )
        )
        assertEquals(instructions, "jmp -1\nnop +4".parseInstructions())
    }

    @Test
    fun shouldPassFirstAcceptanceTest() {
        val rawInstructions = "nop +0\nacc +1\njmp +4\nacc +3\njmp -3\nacc -99\nacc +1\njmp -4\nacc +6"
        val instructions = rawInstructions.parseInstructions()
        println(instructions)
        assertEquals(5, machine.execute(instructions).ac)
    }

    private fun String.parseInstructions(): Map<Int, Instruction> =
        split('\n')
            .mapIndexed { index, s ->
                index to s.parseInstruction()
            }
            .toMap()

    private fun String.parseInstruction(): Instruction {
        val pattern = "([a-z]{3}) (.\\d+)"
        val s1 = Regex(pattern).find(this, 0)?.value ?: ""
        return Instruction(
            name = ":${s1.split(' ')[0]}",
            value = s1.split(' ')[1].toInt()
        )
    }
}

import chisel3._
import chiseltest._
import org.scalatest._

import flatspec._
import matchers._

class ProgramCounterSpec extends AnyFlatSpec with ChiselScalatestTester with should.Matchers {
  "ProgramCounter" should "initialize to 0" in {
    test(new ProgramCounter()) { c =>
      c.io.dataOut.expect(0.U)
    }
  }
  it should "walk 4 bytes" in {
    test(new ProgramCounter()) { c =>
      c.io.countEnable.poke(true.B)
      c.io.dataOut.peek().litValue() should be(0)
      c.clock.step()
      c.io.dataOut.peek().litValue() should be(4)
    }
  }
  it should "jump to 0xbaddcafe (write)" in {
    test(new ProgramCounter()) { c =>
      c.io.writeEnable.poke(true.B)
      c.io.dataIn.poke("h_baddcafe".U)
      c.clock.step()
      c.io.dataOut.peek().litValue() should be(BigInt("baddcafe", 16))
    }
  }
  it should "add 32 to PC" in {
    test(new ProgramCounter()) { c =>
      c.io.writeEnable.poke(true.B)
      c.io.writeAdd.poke(true.B)
      c.io.dataIn.poke(32.U)
      c.clock.step()
      c.io.dataOut.peek().litValue() should be(28)
    }
  }
}

import com.stephenn.scalatest.jsonassert.JsonMatchers
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import service.response.{Primes, PrimesProtocol}
import spray.json._


class PrimeResponseTest extends AnyWordSpec with Matchers with JsonMatchers with PrimesProtocol {

  "The service" should {

    "provide a spray json format for unmarshalling" in {

      val expectedJson =
        s"""{"primeNumbers":[2,3,5,7]}""".stripMargin

      val parsedJson = new Primes(Seq(2,3,5,7)).toJson.prettyPrint

      expectedJson should matchJson(parsedJson)

    }

  }

}

package service.response

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

/**
  * Default case class serialization
  */
trait PrimesProtocol extends DefaultJsonProtocol {
  implicit val primesFormat: RootJsonFormat[Primes] = jsonFormat1(Primes)
}

final case class Primes(primeNumbers: Seq[Int])

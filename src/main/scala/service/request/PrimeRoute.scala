package service.request

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.util.Timeout
import proxyservice.grpc.{PrimeNumber, PrimeNumberRequest}
import service.response.{Primes, PrimesProtocol}

import scala.concurrent.duration._

/**
  * Contains the route definition in conjunction with some useful
  * custom rejections
  */
class PrimeRoute(client: PrimeNumber) extends SprayJsonSupport with PrimesProtocol {

  //only one service available - so want to respond with not found to anything else
  implicit def rejectionHandler: RejectionHandler =
    RejectionHandler.newBuilder()
      .handleNotFound {
        extractUnmatchedPath { p =>
          complete(StatusCodes.NotFound, s"The requested path [$p] could not be found")
        }
      }
      //only supports GET - explicit response
      .handleAll[MethodRejection] { methodRejections =>
      val names = methodRejections.map(_.supported.name)
      complete(StatusCodes.MethodNotAllowed,
        s"Unsupported request method type for this resource. Supported options include: ${names mkString " or "}")
    }
      .result()

  //set the route and seal it to include the rejection handler
  val primesRoute: Route = Route.seal(
    path("prime" / IntNumber) { n =>
      get {
        implicit val timeout: Timeout = 3.seconds
        rejectEmptyResponse {
          onSuccess(client.listPrimeNumbers(PrimeNumberRequest(n))) { response =>
            complete(Primes(response.primeNumbers))
          }
        }
      }
    }
  )

}

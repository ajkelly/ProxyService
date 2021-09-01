import akka.grpc.GrpcClientSettings
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import proxyservice.grpc.{PrimeNumber, PrimeNumberClient}
import service.request.PrimeRoute

/**
  * Contains the tests for the route entry point
  */
class RouteTest extends AnyWordSpec with Matchers with ScalatestRouteTest {

  //configure the client
  val clientSettings: GrpcClientSettings = GrpcClientSettings.connectToServiceAt("testonly", 8081).withTls(false)

  //create a client-side stub for the service
  val client: PrimeNumber = PrimeNumberClient(clientSettings)

  //route with implicit rejections
  val primeRoute: Route = new PrimeRoute(client).primesRoute

  "The service" should {

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put("/prime/5") ~> Route.seal(primeRoute) ~> check {
        status shouldEqual StatusCodes.MethodNotAllowed
        responseAs[String] shouldEqual "Unsupported request method type for this resource. Supported options include: GET"
      }
    }
    "return a MethodNotAllowed error for POST requests to the root path" in {
      Post("/prime/5") ~> Route.seal(primeRoute) ~> check {
        status shouldEqual StatusCodes.MethodNotAllowed
        responseAs[String] shouldEqual "Unsupported request method type for this resource. Supported options include: GET"
      }
    }
    "return a MethodNotAllowed error for DELETE requests to the root path" in {
      Delete("/prime/5") ~> Route.seal(primeRoute) ~> check {
        status shouldEqual StatusCodes.MethodNotAllowed
        responseAs[String] shouldEqual "Unsupported request method type for this resource. Supported options include: GET"
      }
    }

    "handle GET requests to any other paths with a 404 (not leave them)" in {
      Get("/another/12") ~> primeRoute ~> check {
        handled shouldBe true
        response.status shouldEqual StatusCodes.NotFound
        responseAs[String] shouldEqual "The requested path [/another/12] could not be found"
      }
    }

    "handle GET requests with param of the wrong type should result in NotFound" in {
      Get("/prime/1hey2") ~> primeRoute ~> check {
        response.status shouldEqual StatusCodes.NotFound
        responseAs[String] shouldEqual "The requested path [/prime/1hey2] could not be found"
      }
    }

    "handle GET requests to the desired path" in {
      Get("/prime/3") ~> primeRoute ~> check {
        handled shouldBe true
      }
    }

  }

}

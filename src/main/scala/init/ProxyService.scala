package init

import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings

import scala.util.{Failure, Random, Success}
import akka.http.scaladsl.Http
import service.response.PrimesProtocol
import proxyservice.grpc.{PrimeNumber, PrimeNumberClient}
import service.request.PrimeRoute

import scala.concurrent.ExecutionContextExecutor

object ProxyService extends PrimesProtocol {

  def main(args: Array[String]): Unit = {

    //boot akka
    implicit val system: ActorSystem = ActorSystem("ProxyService")
    implicit val ec: ExecutionContextExecutor = system.dispatcher

    //configure the client to connect to the prime-number-service
    val clientSettings = GrpcClientSettings.connectToServiceAt("127.0.0.1", 8081).withTls(false)
    //create a client-side stub for the service
    val client: PrimeNumber = PrimeNumberClient(clientSettings)

    //service handler route for outside world requests
    val primeRoute: PrimeRoute = new PrimeRoute(client)

    //bind service handler servers to localhost 8080
    val futureBinding = Http().newServerAt("127.0.0.1", 8080).bind(primeRoute.primesRoute)

    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        val random = new Random
        val number = random.nextInt(100)
        println(s"ProxyService Server is online! Need inspiration for your first call? Try this...\n" +
          s"http://${address.getHostString}:${address.getPort}/prime/$number")
      case Failure(ex) =>
        println("Failed to bind HTTP endpoint -- terminating system", ex)
        system.terminate()
    }
  }

}

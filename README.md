# ProxyService

Acts as an entry point to the outside world. Exposes a HTTP endpoint over REST responding to GET /prime/<number>. Requests are validated appropriately and passed along to a second microservice - prime-number-server - via Grpc, to perform the necessary calculations.

To run:
- Clone the repository (git clone https://github.com/ajkelly/proxy-service.git)
- Compile the project from the sbt shell (compile) - this will generate the Grpc interfaces and stubs
- Once the target folder containing the Grpc is present, simply run the project from the main method inside ProxyService.scala
- See the console for a suggested first call! Alternatively try: http://127.0.0.1:8080/primes/15

To run tests:
- From the sbt shell (test)

Notable implementation choices:
- Language choice = Scala as this was more challenging and a good opportunity for me to practise/learn (I am a little more confident in Java!)
- Used an implicit RejectionHandler on the Route to reject anything that does not match /prime/[0-9], responding with NotFound, as this is the only service offered here

Additional:
- See https://github.com/ajkelly/prime-number-service.git
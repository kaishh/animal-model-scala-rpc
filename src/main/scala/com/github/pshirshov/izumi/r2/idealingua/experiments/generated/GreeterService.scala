package com.github.pshirshov.izumi.r2.idealingua.experiments.generated

import com.github.pshirshov.izumi.r2.idealingua.experiments.runtime._
import io.circe._
import io.circe.generic.semiauto._

import scala.language.{higherKinds, implicitConversions}
//--------------------------------------------------------------------------
// Generated part
trait GreeterService[R[_]] extends WithResultType[R] {
  def greet(name: String, surname: String): Result[String]
}


trait GreeterServiceWrapped[R[_]] extends WithResultType[R] {

  import GreeterServiceWrapped._

  def greet(input: GreetInput): Result[GreetOutput]
}

object GreeterServiceWrapped {

  sealed trait GreeterServiceInput

  case class GreetInput(name: String, surname: String) extends GreeterServiceInput

  sealed trait GreeterServiceOutput

  case class GreetOutput(value: String) extends GreeterServiceOutput


  object GreetInput {
    implicit val encodeTestPayload: Encoder[GreetInput] = deriveEncoder
    implicit val decodeTestPayload: Decoder[GreetInput] = deriveDecoder
  }

  object GreetOutput {
    implicit val encodeTestPayload: Encoder[GreetOutput] = deriveEncoder
    implicit val decodeTestPayload: Decoder[GreetOutput] = deriveDecoder
  }

  object GreeterServiceInput {
    implicit val encodeTestPayload: Encoder[GreeterServiceInput] = deriveEncoder
    implicit val decodeTestPayload: Decoder[GreeterServiceInput] = deriveDecoder
  }

  object GreeterServiceOutput {
    implicit val encodeTestPayload: Encoder[GreeterServiceOutput] = deriveEncoder
    implicit val decodeTestPayload: Decoder[GreeterServiceOutput] = deriveDecoder
  }

  trait GreeterServiceDispatcherPacking[R[_]] extends GreeterService[R] with WithResult[R] {
    def dispatcher: Dispatcher[GreeterServiceInput, GreeterServiceOutput, R]

    def greet(name: String, surname: String): Result[String] = {
      val packed = GreetInput(name, surname)
      val dispatched = dispatcher.dispatch(packed)
      _ServiceResult.map(dispatched) {
        case o: GreetOutput =>
          o.value
        case o =>
          throw new TypeMismatchException(s"Unexpected input in GreeterServiceDispatcherPacking.greet: $o", o)
      }
    }
  }

  object GreeterServiceDispatcherPacking {

    class Impl[R[_] : ServiceResult](val dispatcher: Dispatcher[GreeterServiceInput, GreeterServiceOutput, R]) extends GreeterServiceDispatcherPacking[R] {
      override protected def _ServiceResult: ServiceResult[R] = implicitly
    }

  }


  trait GreeterServiceDispatcherUnpacking[R[_]] extends GreeterServiceWrapped[R] with Dispatcher[GreeterServiceInput, GreeterServiceOutput, R] with WithResult[R] {
    def service: GreeterService[R]


    override def greet(input: GreetInput): Result[GreetOutput] = {
      val result = service.greet(input.name, input.surname)
      _ServiceResult.map(result)(GreetOutput.apply)
    }

    def dispatch(input: GreeterServiceInput): Result[GreeterServiceOutput] = {
      input match {
        case v: GreetInput =>
          _ServiceResult.map(greet(v))(v => v) // upcast
      }
    }
  }

  object GreeterServiceDispatcherUnpacking {

    class Impl[R[_] : ServiceResult](val service: GreeterService[R]) extends GreeterServiceDispatcherUnpacking[R] {
      override protected def _ServiceResult: ServiceResult[R] = implicitly
    }

  }

  type GreeterServiceStringMarshaller = TransportMarshallers[String, GreeterServiceInput, String, GreeterServiceOutput]

  class GreeterServiceStringMarshallerCirceImpl extends GreeterServiceStringMarshaller {
    import io.circe.parser._
    import io.circe.syntax._

    override val requestUnmarshaller: Unmarshaller[String, GreeterServiceInput] = (v: String) => {
      parse(v).flatMap(_.as[GreeterServiceInput]) match {
        case Right(value) =>
          value
        case Left(problem) =>
          throw new UnparseableDataException(s"Cannot parse $problem into GreeterServiceInput")
      }
    }

    override val responseUnmarshaller: Unmarshaller[String, GreeterServiceOutput] = (v: String) => {
      parse(v).flatMap(_.as[GreeterServiceOutput]).right.get
    }

    override val requestMarshaller: Marshaller[GreeterServiceInput, String] = (v: GreeterServiceInput) => v.asJson.noSpaces
    override val responseMarshaller: Marshaller[GreeterServiceOutput, String] = (v: GreeterServiceOutput) => v.asJson.noSpaces
  }
}

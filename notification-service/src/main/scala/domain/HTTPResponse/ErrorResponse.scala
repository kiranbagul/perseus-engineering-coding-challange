package domain.HTTPResponse

case class ErrorResponse(error : Error)
case class Error(reason : String)

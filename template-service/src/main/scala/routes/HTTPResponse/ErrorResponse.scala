package routes.HTTPResponse

case class ErrorResponse(error : Error)
case class Error(reason : String)

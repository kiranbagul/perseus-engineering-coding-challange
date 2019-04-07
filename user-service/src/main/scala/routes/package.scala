import routes.HTTPResponse.{Error, ErrorResponse}

package object routes {

  def toErrorResponse(error: String): ErrorResponse = ErrorResponse(Error(error))

}

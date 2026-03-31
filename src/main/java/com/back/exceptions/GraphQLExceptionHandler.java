package com.back.exceptions;


import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;


@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {

        if (ex instanceof RuntimeException) {
            return GraphqlErrorBuilder.newError()
                    .message(ex.getMessage()) // mensaje
                    .errorType(ErrorType.DataFetchingException)
                    .build();
        }
        if (ex instanceof InvalidCredentialsException) {
            return GraphqlErrorBuilder.newError()
                    .message("Credenciales incorrectas")
                    .errorType(ErrorType.DataFetchingException)
                    .build();
        }

        if (ex instanceof UserNotFoundException) {
            return GraphqlErrorBuilder.newError()
                    .message("Usuario no encontrado")
                    .errorType(ErrorType.DataFetchingException)
                    .build();
        }

        if (ex instanceof RuntimeException) {
            return GraphqlErrorBuilder.newError()
                    .message(ex.getMessage())
                    .errorType(ErrorType.DataFetchingException)
                    .build();
        }

        return null;
    }

}

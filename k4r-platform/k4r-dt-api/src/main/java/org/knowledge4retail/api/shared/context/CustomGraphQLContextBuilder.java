package org.knowledge4retail.api.shared.context;

import graphql.kickstart.execution.context.GraphQLKickstartContext;
import graphql.kickstart.servlet.context.DefaultGraphQLServletContextBuilder;
import graphql.kickstart.servlet.context.GraphQLServletContextBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.shared.context.dataloader.DataLoaderRegistryFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomGraphQLContextBuilder implements GraphQLServletContextBuilder {

    private final DataLoaderRegistryFactory dataLoaderRegistryFactory;

    @Override
    public GraphQLKickstartContext build(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        var defaultGraphQLServletContextBuilder = new DefaultGraphQLServletContextBuilder();
        GraphQLKickstartContext context = defaultGraphQLServletContextBuilder.build(httpServletRequest, httpServletResponse);

        return GraphQLKickstartContext.of(dataLoaderRegistryFactory.create(), context.getMapOfContext());
   }

    @Override
    public GraphQLKickstartContext build(Session session, HandshakeRequest handshakeRequest) {

        throw new IllegalStateException("Unsupported");
    }

    @Override
    public GraphQLKickstartContext build() {

        throw new IllegalStateException("Unsupported");
    }
}

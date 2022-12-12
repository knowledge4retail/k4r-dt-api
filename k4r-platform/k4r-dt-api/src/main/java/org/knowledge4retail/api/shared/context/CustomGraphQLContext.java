package org.knowledge4retail.api.shared.context;

import graphql.kickstart.execution.context.GraphQLKickstartContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoaderRegistry;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CustomGraphQLContext implements GraphQLKickstartContext {

    private final GraphQLKickstartContext context;

    @Override
    public @NonNull DataLoaderRegistry getDataLoaderRegistry() {
        return context.getDataLoaderRegistry();
    }

    @Override
    public Map<Object, Object> getMapOfContext() {
        return context.getMapOfContext();
    }
}

package com.simeon.commands.output;

import com.simeon.CLI;
import com.simeon.CollectionInfo;
import com.simeon.Response;

import java.io.Serializable;

public class CollectionInfoOutputCommand implements OutputCommand {
    @Override
    public Class<? extends Serializable> getOutputType() {
        return CollectionInfo.class;
    }

    @Override
    public void show(Response response, CLI cli) throws ClassCastException {
        CollectionInfo collectionInfo = (CollectionInfo) response.getData();
        cli.print(String.format("%d elements in the collection\n", collectionInfo.getSize()));
    }
}

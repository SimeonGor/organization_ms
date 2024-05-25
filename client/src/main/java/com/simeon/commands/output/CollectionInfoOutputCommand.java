package com.simeon.commands.output;

import com.simeon.CLI;
import com.simeon.CollectionInfo;
import com.simeon.CommandInfo;

import java.io.Serializable;

public class CollectionInfoOutputCommand implements OutputCommand {
    @Override
    public Class<? extends Serializable> getOutputType() {
        return CollectionInfo.class;
    }

    @Override
    public void show(Serializable message, CLI cli) throws ClassCastException {
        CollectionInfo collectionInfo = (CollectionInfo) message;
        cli.print(String.valueOf(collectionInfo.getSize()));
    }
}

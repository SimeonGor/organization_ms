package com.simeon;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class CollectionInfo implements Serializable {
    private long size;
}

package net.consensys.linea.zktracer.module.hub.transients;

import lombok.Getter;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata;

import java.util.HashMap;
import java.util.Map;

public class StateManagerMetadata
{
    @Getter
    Map<Transients.AddrBlockPair, TransactionProcessingMetadata.TransactAccountFirstAndLast> txnAccountFirstLastBlockMap = new HashMap<>();
}
